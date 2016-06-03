package cn.musicmeet.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import cn.musicmeet.beans.User;

@Path("")
public class IndexController {
	@Get
	public String get_index(Invocation inv, HttpServletRequest request) {
		if (isLogin(request)) {
			return "user-view-body";
		}
		return "index";
	}

	@Post("index")
	public String post_index() {
		return "index";
	}

	@SuppressWarnings("unchecked")
	private boolean isLogin(HttpServletRequest request) {
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute(User.SGLOBAL);
		if (sGlobal == null || sGlobal.get(User.UID) == null || sGlobal.get(User.USERNAME) == null || sGlobal.get(User.ACCOUNT_STATUS) == null || sGlobal.get(User.AVATAR_ID) == null || !sGlobal.get(User.ACCOUNT_STATUS).equals("1")) {
			return false;
		}
		return true;
	}
}
