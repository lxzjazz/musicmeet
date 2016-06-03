package cn.musicmeet.controllers;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;
import cn.musicmeet.beans.LoginUser;

public class LoginInterceptor extends ControllerInterceptorAdapter {

	public LoginInterceptor() {
		setPriority(1000);
	}

	protected Class<? extends Annotation> getRequiredAnnotationClass() {
		return LoginRequired.class;
	}

	@Override
	protected Object before(Invocation inv) throws Exception {
		HttpServletRequest request = inv.getRequest();
		HttpServletResponse response = inv.getResponse();
		// 判断用户是否登录成功
		if (inv.getRequest().getAttribute(LoginUser.SGLOBAL) == null) {
			// 清空session中的值并退回主页
			request.getSession().invalidate();
			if (request.getMethod().equalsIgnoreCase("POST")) {
				response.setStatus(408);
				return false;
			}
			return "r:" + request.getContextPath() + "/login";
		}
		return true;
	}
}