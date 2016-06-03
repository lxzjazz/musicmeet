package cn.musicmeet.util;

public class JedisKeyGenerator {
	
	public static String getCookieKey(String uid) {
		return "key:".concat(uid).concat(":cookie");
	}

	public static String getSessionKey(String uid) {
		return "key:".concat(uid).concat(":session");
	}
}
