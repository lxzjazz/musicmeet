package cn.musicmeet.controllers.setting;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import cn.musicmeet.beans.User;
import cn.musicmeet.controllers.LoginRequired;
import cn.musicmeet.dao.UserDAO;

@LoginRequired
@Path("")
public class SettingController {
	@Autowired
	private UserDAO userDAO;

	@Post
	public String post_setting(Invocation inv) {
		return "setting";
	}

	@Post("profile")
	public String post_setting_profile(Invocation inv) {
		return "setting-profile";
	}

	@Post("avatar")
	public String post_setting_avatar(Invocation inv) {
		return "setting-avatar";
	}

	@SuppressWarnings("unchecked")
	@Post("email")
	public String post_setting_email(Invocation inv) {
		HttpServletRequest request = inv.getRequest();
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute("sGlobal");
		inv.addModel("emailInfo", userDAO.getEmailInfoByUid((String) sGlobal.get(User.UID)));
		return "setting-email";
	}

	@Post("password")
	public String post_setting_password(Invocation inv) {
		return "setting-password";
	}

	@Post("other")
	public String post_setting_other(Invocation inv) {
		return "setting-other";
	}
}