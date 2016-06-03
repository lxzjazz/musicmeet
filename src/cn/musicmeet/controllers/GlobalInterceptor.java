package cn.musicmeet.controllers;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.paoding.rose.web.ControllerInterceptorAdapter;
import net.paoding.rose.web.Invocation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;
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

		LoginUser loginUser = null;
		String currentSessionID = request.getSession().getId();
		// 通过session中的uid读取用户信息
		String uid = (String) request.getSession().getAttribute("uid");
		loginUser = JedisUtil.getLoginUserByUid(pool, uid);
		// 读取用户信息失败或者用户信息中的sessionID已过期
		if (!checkLoginUser(loginUser) || !loginUser.getSessionID().equals(currentSessionID)) {
			// 通过cookie读取用户信息
			loginUser = JedisUtil.getLoginUserByCookie(pool, request.getCookies());
			// 读取用户信息失败(或者用户信息sessionID已过期且用户未选择自动登录),则删除cookie并跳出拦截器
			if (!checkLoginUser(loginUser) || (!loginUser.getSessionID().equals(currentSessionID) && !loginUser.isCookieSaved())) {
				Cookie delete_cookie = new Cookie("user_token", null);
				delete_cookie.setMaxAge(0);
				delete_cookie.setPath(request.getContextPath() + "/");
				response.addCookie(delete_cookie);
				return true;
			}
			// 读取用户信息成功
			// 1.将内存数据库中的sessionID更新
			JedisUtil.resetUser(pool, loginUser.getUid(), LoginUser.SESSION_ID, currentSessionID);
			// 2.将loginUser中的sessionID更新
			loginUser.setSessionID(currentSessionID);
			// 3.将uid存入session
			request.getSession().setAttribute("uid", loginUser.getUid());
		}
		Map<String, Object> sGlobal = new HashMap<String, Object>();
		sGlobal.put(LoginUser.UID, loginUser.getUid());
		sGlobal.put(LoginUser.USERNAME, loginUser.getUsername());
		sGlobal.put(LoginUser.EMAIL, loginUser.getEmail());
		sGlobal.put(LoginUser.ACCOUNT_STATUS, loginUser.getAccountStatus());
		sGlobal.put(LoginUser.AVATAR_ID, loginUser.getAvatarID());
		sGlobal.put(LoginUser.SESSION_ID, loginUser.getSessionID());
		request.setAttribute(LoginUser.SGLOBAL, sGlobal);
		return true;
	}

	@Override
	protected Class<? extends Annotation> getDenyAnnotationClass() {
		return GlobalDeny.class;
	}

	private boolean checkLoginUser(LoginUser loginUser) {
		if (loginUser == null || StringUtils.isNoneBlank(loginUser.getUid(), loginUser.getUsername(), loginUser.getEmail(), loginUser.getAccountStatus(), loginUser.getAvatarID(), loginUser.getSessionID())) {
			return false;
		}
		return true;
	}
}
