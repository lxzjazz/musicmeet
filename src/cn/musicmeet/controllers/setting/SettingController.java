package cn.musicmeet.controllers.setting;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Post;

import org.springframework.beans.factory.annotation.Autowired;

import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.controllers.LoginRequired;
import cn.musicmeet.dao.UserDAO;

@LoginRequired
@Path("")
public class SettingController {
	@Autowired
	private UserDAO userDAO;

	@Post
	public String postSetting(Invocation inv) {
		return "setting";
	}

	@Post("profile")
	public String postSettingProfile(Invocation inv) {
		return "setting-profile";
	}

	@Post("avatar")
	public String postSettingAvatar(Invocation inv) {
		return "setting-avatar";
	}

	@SuppressWarnings("unchecked")
	@Post("email")
	public String postSettingEmail(Invocation inv, HttpServletRequest request) {
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute("sGlobal");
		inv.addModel("emailInfo", userDAO.getEmailInfoByUid((String) sGlobal.get(LoginUser.UID)));
		return "setting-email";
	}

	@Post("password")
	public String postSettingPassword(Invocation inv) {
		return "setting-password";
	}

	@Post("other")
	public String postSettingOther(Invocation inv) {
		return "setting-other";
	}
}