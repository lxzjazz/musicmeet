package cn.musicmeet.controllers.logout;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.sf.json.JSONObject;
import cn.musicmeet.controllers.GlobalDeny;

@GlobalDeny
@Path("")
public class LogoutController {
	@Get
	public String get_logout(Invocation inv, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 清空session中的值
			request.getSession().invalidate();
			// 删除用户信息cookie
			Cookie cookie = new Cookie("user_token", null);
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath() + "/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "r:" + request.getContextPath() + "/";
	}

	@Post
	public JSONObject post_logout(JSONObject json, Invocation inv, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 清空session中的值
			request.getSession().invalidate();
			// 删除用户信息cookie
			Cookie cookie = new Cookie("user_token", null);
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath() + "/");
			response.addCookie(cookie);
			json.element("success", "退出成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "对不起,服务器异常");
		}
		return json;
	}
}
