package cn.musicmeet.controllers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.beans.User;
import cn.musicmeet.util.JedisUtil;

public class GlobalInterceptor extends ControllerInterceptorAdapter {
	@Autowired
	private ShardedJedisPool pool;

	public GlobalInterceptor() {
		setPriority(20000);
	}

	@Override
	protected Object before(Invocation inv) throws Exception {
		HttpServletRequest request = inv.getRequest();
		HttpServletResponse response = inv.getResponse();

		String current_sessionID = request.getSession().getId();
		request.setAttribute("sessionID", current_sessionID);
		LoginUser loginUser = null;
		// 读session
		loginUser = JedisUtil.getUserBySession(pool, request.getSession());
		// 如果session读取失败
		if (loginUser == null || !loginUser.getSessionID().equals(current_sessionID)) {
			// 通过cookie读取
			loginUser = JedisUtil.getUserByCookie(pool, request.getCookies());
			// 如果cookie读取失败,或者(用户未选择自动登录,并且session已过期),则删除cookie并跳出拦截器
			if (loginUser == null || (loginUser.getCookiesaved().equals("false") && !loginUser.getSessionID().equals(current_sessionID))) {
				Cookie delete_cookie = new Cookie("user_token", null);
				delete_cookie.setMaxAge(0);
				delete_cookie.setPath(request.getContextPath() + "/");
				response.addCookie(delete_cookie);
				return true;
			}
			// 如果cookie读取成功
			// 1.将member中的sessionID更新
			loginUser.setSessionID(current_sessionID);
			// 2.将cookie中的sessionID更新
			JedisUtil.resetUser(pool, loginUser.getUid(), "sessionID", loginUser.getSessionID());
			// 3.将uid存入session
			request.getSession().setAttribute("uid", loginUser.getUid());
		}
		if (checkLoginUser(loginUser)) {
			Map<String, Object> sGlobal = new HashMap<String, Object>();
			sGlobal.put(User.UID, loginUser.getUid());
			sGlobal.put(User.USERNAME, loginUser.getUsername());
			sGlobal.put(User.ACCOUNT_STATUS, loginUser.getAccountStatus());
			sGlobal.put(User.AVATAR_ID, loginUser.getAvatarID());
			request.setAttribute(User.SGLOBAL, sGlobal);
		}
		return true;
	}

	@Override
	protected Class<? extends Annotation> getDenyAnnotationClass() {
		return GlobalDeny.class;
	}

	private boolean checkLoginUser(LoginUser loginUser) {
		if (loginUser == null || loginUser.getUid() == null || loginUser.getUsername() == null || loginUser.getAccountStatus() == null || loginUser.getAvatarID() == null) {
			return false;
		}
		return true;
	}
}
