package cn.musicmeet.controllers.register;

import java.io.File;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.beans.User;
import cn.musicmeet.dao.UserDAO;
import cn.musicmeet.util.CommonUtil;
import cn.musicmeet.util.JedisUtil;

@Path("")
public class RegisterController {

	private final static Logger logger = Logger.getLogger(RegisterController.class);

	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ShardedJedisPool pool;

	@Get
	public String getRegister(Invocation inv) {
		inv.addModel("title", "注册");
		return "register";
	}

	@Get("avatar/modal")
	public String getAvatarModal() {
		return "avatar-modal";
	}

	@Post("check/email")
	public JSONObject postRegisterCheckEmail(JSONObject json, @Param("email") String email) {
		try {
			json.put("existed", userDAO.getEmailCount(email) != 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.put("error", "对不起,服务器异常");
		}
		return json;
	}

	@Post("check/username")
	public JSONObject postRegisterCheckUsername(JSONObject json, @Param("username") String username) {
		try {
			json.put("existed", userDAO.getUsernameCount(username) != 0);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.put("error", "对不起,服务器异常");
		}
		return json;
	}

	@Post
	public JSONObject postRegister(JSONObject json, Invocation inv, HttpServletRequest request, HttpServletResponse response, HttpSession session, User user, @Param("code") String code) {
		// 检查输入信息是否合法
		if (!checkIsUnexisted(user.getUsername(), user.getEmail(), json)) {
			return json;
		}
		if (StringUtils.isBlank(code) || !"sdh6+$%#jadhdF!DSc3#$jkFRG78Hd!Af2".equals(code)) {
			json.put("error", "对不起,邀请码输入错误");
			return json;
		}
		try {
			// 处理有关信息(生成salt,加密密码,用户名去掉前后空格,邮箱去掉前后空格并转为小写,修改帐号状态,记录注册时间)
			String salt = CommonUtil.getRandString(128);
			user.setSalt(salt);
			user.setPassword(CommonUtil.sha512(salt + user.getPassword()));
			user.setUsername(user.getUsername().trim());
			user.setEmail(user.getEmail().trim().toLowerCase());
			user.setAccountStatus(1);
			user.setCreateTime(Long.toString(System.currentTimeMillis()));
			// 如果用户上传了头像,需要转移文件
			reNameAvatar(inv, session.getId(), CommonUtil.md5(user.getEmail()));
			// 存数据库
			long uid = userDAO.saveUser(user);
			// 用户登录
			LoginUser loginUser = userDAO.getUserByUid(uid);
			// 用户是否允许自动登录
			loginUser.setCookieSaved(false);
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
			// 跳转到首页
			json.put("url", request.getContextPath() + "/#index");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.put("error", "对不起,服务器异常");
		}
		return json;
	}

	private boolean checkIsUnexisted(String username, String email, JSONObject json) {
		try {
			if (userDAO.getUsernameCount(username) != 0) {
				json.put("error", "昵称 " + username + " 已被占用");
				return false;
			}
			if (userDAO.getEmailCount(email) != 0) {
				json.put("error", "邮箱 " + email + " 已被占用");
				return false;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.put("error", "对不起,服务器异常");
			return false;
		}
		return true;
	}

	private void reNameAvatar(Invocation inv, String avatarID, String newAvatarID) {
		reNameFile(getRealPath(inv, avatarID, "small"), getRealPath(inv, newAvatarID, "small"));
		reNameFile(getRealPath(inv, avatarID, "middle"), getRealPath(inv, newAvatarID, "middle"));
		reNameFile(getRealPath(inv, avatarID, "big"), getRealPath(inv, newAvatarID, "big"));
	}

	private void reNameFile(String oldPath, String newPath) {
		File oldFile = new File(oldPath);
		if (oldFile.exists()) {
			File newFile = new File(newPath);
			if (newFile.exists()) {
				newFile.delete();
			}
			oldFile.renameTo(newFile);
		}
	}

	private String getRealPath(Invocation inv, String avatarID, String size) {
		return (inv.getServletContext().getRealPath("data") + "/avatar/" + avatarID + "_" + size + ".jpeg").replace("/", System.getProperties().getProperty("file.separator"));
	}
}