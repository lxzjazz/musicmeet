package cn.musicmeet.controllers.register;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.beans.User;
import cn.musicmeet.dao.UserDAO;
import cn.musicmeet.util.CommonUtil;
import cn.musicmeet.util.JedisUtil;

@Path("")
public class RegisterController {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private ShardedJedisPool pool;

	private Logger logger = Logger.getLogger(RegisterController.class);

	@Get
	public String get_register_user(Invocation inv) {
		inv.addModel("title", "注册");
		return "register";
	}

	@Get("avatar_modal")
	public String get_register_avatar_modal() {
		return "avatar-modal";
	}

	@Post("check_email")
	public JSONObject post_register_user_check_email(JSONObject json, @Param("email") String email) {
		try {
			if (userDAO.getEmailCount(email) != 0) {
				json.element("check_email", "existed");
			} else {
				json.element("check_email", "unexisted");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "对不起，服务器异常");
		}
		return json;
	}

	@Post("check_username")
	public JSONObject post_register_user_check_username(JSONObject json, @Param("username") String username) {
		try {
			if (userDAO.getUsernameCount(username) != 0) {
				json.element("check_username", "existed");
			} else {
				json.element("check_username", "unexisted");
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "对不起，服务器异常");
		}
		return json;
	}

	@Post
	public JSONObject post_register_user(JSONObject json, Invocation inv, HttpServletRequest request, HttpServletResponse response, HttpSession session, User user) throws NoSuchAlgorithmException, NoSuchProviderException {
		String avatar_id = (String) session.getAttribute("avatar_id");
		// 检查输入信息是否合法
		if (!checkIsUnexisted(user.getUsername(), user.getEmail(), json)) {
			return json;
		}
		String code = user.getCode();
		if (StringUtils.isBlank(code) || !"sdh6+$%#jadhdF!DSc3#$jkFRG78Hd!Af2".equals(code)) {
			json.element("error", "对不起，邀请码输入错误");
			return json;
		}
		// 处理有关信息(生成salt,加密密码,用户名去掉前后空格,邮箱去掉前后空格并转为小写,修改帐号状态,记录注册时间)
		String salt = CommonUtil.getRandSalt(128);
		user.setSalt(salt);
		user.setPassword(CommonUtil.sha512(salt + user.getPassword()));
		user.setUsername(user.getUsername().trim());
		user.setEmail(user.getEmail().trim().toLowerCase());
		user.setAccountStatus(1);
		user.setCreateTime(Long.toString(System.currentTimeMillis()));
		// 存数据库
		try {
			if (avatar_id != null) {
				if (!reNameAvatar(inv, avatar_id, CommonUtil.md5(user.getEmail()))) {
					json.element("error", "对不起,服务器异常");
					return json;
				}
				session.setAttribute("avatar_id", null);
			}
			long key = userDAO.saveUser(user);
			LoginUser loginUser = userDAO.getUserByUid(key);
			// 登录缓存信息
			loginUser.setSessionID(request.getSession().getId());
			// 存cookie
			String user_cookie = UUID.randomUUID().toString().replace("-", "");
			Cookie cookie = new Cookie("user_token", user_cookie);
			JedisUtil.saveUser(pool, user_cookie, loginUser);
			cookie.setPath(request.getContextPath() + "/");
			cookie.setMaxAge(31536000);
			response.addCookie(cookie);
			// 存session
			session.setAttribute("uid", loginUser.getUid());
			json.element("url", request.getContextPath() + "/#index");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.element("error", "对不起，服务器异常");
		}
		return json;
	}

	private boolean checkIsUnexisted(String username, String email, JSONObject json) {
		try {
			if (userDAO.getUsernameCount(username) != 0) {
				json.element("error", "昵称 " + username + " 已被占用");
				return false;
			}
			if (userDAO.getEmailCount(email) != 0) {
				json.element("error", "邮箱 " + email + " 已被占用");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "对不起，服务器异常");
			return false;
		}
		return true;
	}

	private String getrealPath(Invocation inv, String avatar_id, String size) {
		return (inv.getServletContext().getRealPath("data") + "/avatar/" + avatar_id + "_" + size + ".jpeg").replace("/", System.getProperties().getProperty("file.separator"));
	}

	private boolean reNameFile(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		File newFile = new File(newPath);
		if (newFile.exists()) {
			newFile.delete();
		}
		oldFile.renameTo(newFile);
		return false;
	}

	private boolean reNameAvatar(Invocation inv, String avatar_id, String newAvatar_id) {
		return reNameFile(getrealPath(inv, avatar_id, "small"), getrealPath(inv, newAvatar_id, "small")) && reNameFile(getrealPath(inv, avatar_id, "middle"), getrealPath(inv, newAvatar_id, "middle")) && reNameFile(getrealPath(inv, avatar_id, "big"), getrealPath(inv, newAvatar_id, "big"));
	}
}