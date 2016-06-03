package cn.musicmeet.util;

import org.apache.commons.lang3.StringUtils;

public class JedisKeyGenerator {

	public static String getUserCookieKeyByUid(String uid) {
		if (StringUtils.isNotBlank(uid)) {
			return "MUSICMEET:USER_COOKIE_".concat(uid);
		}
		return "";
	}

	public static String getCookieMapKeyByCookie(String cookie) {
		if (StringUtils.isNotBlank(cookie)) {
			return "MUSICMEET:COOKIE_MAP_".concat(cookie);
		}
		return "";
	}
}
