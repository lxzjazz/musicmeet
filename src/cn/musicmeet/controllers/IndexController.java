package cn.musicmeet.controllers;

import javax.servlet.http.HttpServletRequest;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import cn.musicmeet.beans.LoginUser;

@Path("")
public class IndexController {
	@Get
	public String get_index(Invocation inv, HttpServletRequest request) {
		if (request.getAttribute(LoginUser.SGLOBAL) != null) {
			return "user-view-body";
		}
		return "index";
	}

	@Post("index")
	public String post_index() {
		return "index";
	}
}
