package cn.musicmeet.controllers.login;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.controllers.GlobalDeny;
import cn.musicmeet.dao.UserDAO;
import cn.musicmeet.util.CommonUtil;
import cn.musicmeet.util.JedisUtil;

@GlobalDeny
@Path("")
public class LoginController {
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ShardedJedisPool pool;

	@Get
	public String getLogin(Invocation inv) {
		inv.addModel("title", "登录");
		return "login";
	}

	@Get("box")
	public String getLoginBox(Invocation inv) {
		return "login_box";
	}

	@Post
	public JSONObject postLogin(JSONObject json, Invocation inv, @Param("email") String email, @Param("password") String password, @Param("cookieSaved") boolean cookieSaved) {
		HttpServletRequest request = inv.getRequest();
		HttpServletResponse response = inv.getResponse();
		if (email == null || email.trim().isEmpty()) {
			json.put("error", "请输入用户名");
			return json;
		}
		if (password == null || password.isEmpty()) {
			json.put("error", "请输入密码");
			return json;
		}
		try {
			LoginUser loginUser = userDAO.getUserByEmail(email.toLowerCase());
			if (loginUser == null) {
				json.put("error", "帐号或者密码错误");
				return json;
			}

			if (!CommonUtil.slowEquals(loginUser.getPassword(), CommonUtil.sha512(loginUser.getSalt().concat(password)))) {
				json.put("error", "帐号或者密码错误");
				return json;
			}

			if (loginUser.getAccountStatus().equals("0")) {
				json.put("error", "帐号已被锁定,请联系管理员");
				return json;
			}

			// 用户是否允许自动登录
			loginUser.setCookieSaved(cookieSaved);
			// 用户当前的sessionID
			loginUser.setSessionID(request.getSession().getId());

			// 存cookie, 默认保存一个月
			String userCookie = UUID.randomUUID().toString().replace("-", "");
			Cookie cookie = new Cookie("user_token", userCookie);
			cookie.setPath(request.getContextPath() + "/");
			cookie.setMaxAge(60 * 60 * 24 * 30);
			response.addCookie(cookie);
			// 加入缓存数据库
			JedisUtil.saveUser(pool, loginUser, userCookie);
			// 存session
			request.getSession().setAttribute("uid", loginUser.getUid());
			json.put("success", "恭喜您,登录成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.put("error", "对不起,服务器异常");
		}
		return json;
	}
}
