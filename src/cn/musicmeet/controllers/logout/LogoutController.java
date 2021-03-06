package cn.musicmeet.controllers.logout;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.musicmeet.controllers.GlobalDeny;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

@GlobalDeny
@Path("")
public class LogoutController {

	private final static Logger logger = Logger.getLogger(LogoutController.class);

	@Get
	public String getLogout(Invocation inv, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 清空session中的值
			request.getSession().invalidate();
			// 删除用户信息cookie
			Cookie cookie = new Cookie("user_token", null);
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath() + "/");
			response.addCookie(cookie);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "r:" + request.getContextPath() + "/";
	}

	@Post
	public JSONObject postLogout(JSONObject json, Invocation inv, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 清空session中的值
			request.getSession().invalidate();
			// 删除用户信息cookie
			Cookie cookie = new Cookie("user_token", null);
			cookie.setMaxAge(0);
			cookie.setPath(request.getContextPath() + "/");
			response.addCookie(cookie);
			json.put("success", "退出成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.put("error", "对不起,服务器异常");
		}
		return json;
	}
}
