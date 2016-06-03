package cn.musicmeet.util;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;

public class JedisUtil {

	private static Logger logger = Logger.getLogger(JedisUtil.class);

	public static void saveUser(ShardedJedisPool pool, String userCookie, LoginUser loginUser) throws NoSuchAlgorithmException {
		Map<String, String> userMap = new HashMap<String, String>();
		String uid = loginUser.getUid();
		userMap.put("uid", uid);
		userMap.put("username", loginUser.getUsername());
		userMap.put("accountStatus", loginUser.getAccountStatus());
		userMap.put("cookiesaved", loginUser.getCookiesaved());
		userMap.put("sessionID", loginUser.getSessionID());
		userMap.put("avatarID", CommonUtil.md5(loginUser.getEmail()));
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			if (!jedis.exists(JedisKeyGenerator.getCookieKey(uid))) {
				// 存cookie-map
				jedis.hmset(userCookie, userMap);
				// 存user-map
				userMap.put("cookie", userCookie);
				jedis.hmset(JedisKeyGenerator.getCookieKey(uid), userMap);
			} else {
				String last_cookie = jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "cookie").get(0);
				if (last_cookie != null) {
					// 删除cookie-map
					jedis.del(last_cookie);
				}
				// 存cookie-map
				jedis.hmset(userCookie, userMap);
				// 删除user-map
				jedis.del(JedisKeyGenerator.getCookieKey(uid));
				// 存user-map
				userMap.put("cookie", userCookie);
				jedis.hmset(JedisKeyGenerator.getCookieKey(uid), userMap);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
	}

	public static void deleteUser(ShardedJedisPool pool, String uid) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			String last_cookie = jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "cookie").get(0);
			if (last_cookie != null) {
				// 删除cookie-map
				jedis.del(last_cookie);
			}
			// 删除user-map
			jedis.del(JedisKeyGenerator.getCookieKey(uid));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
	}

	public static LoginUser getUserBySession(ShardedJedisPool pool, HttpSession session) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			String uid = (String) session.getAttribute("uid");
			if (uid != null && jedis.exists(JedisKeyGenerator.getCookieKey(uid))) {
				LoginUser member = new LoginUser();
				member.setUid(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "uid").get(0));
				member.setUsername(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "username").get(0));
				member.setAccountStatus(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "accountStatus").get(0));
				member.setCookiesaved(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "cookiesaved").get(0));
				member.setSessionID(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "sessionID").get(0));
				member.setAvatarID(jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "avatarID").get(0));
				return member;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return null;
	}

	public static LoginUser getUserByCookie(ShardedJedisPool pool, Cookie[] cookie) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			if (cookie != null) {
				String userCookie = null;
				for (int i = 0; i < cookie.length; i++) {
					if (cookie[i].getName().equals("user_token")) {
						userCookie = cookie[i].getValue();
						break;
					}
				}
				if (userCookie != null && jedis.exists(userCookie)) {
					LoginUser loginUser = new LoginUser();
					loginUser.setUid(jedis.hmget(userCookie, "uid").get(0));
					loginUser.setUsername(jedis.hmget(userCookie, "username").get(0));
					loginUser.setAccountStatus(jedis.hmget(userCookie, "accountStatus").get(0));
					loginUser.setCookiesaved(jedis.hmget(userCookie, "cookiesaved").get(0));
					loginUser.setSessionID(jedis.hmget(userCookie, "sessionID").get(0));
					loginUser.setAvatarID(jedis.hmget(userCookie, "avatarID").get(0));
					return loginUser;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return null;
	}

	public static boolean resetUser(ShardedJedisPool pool, String uid, String key, String value) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			if (jedis.exists(JedisKeyGenerator.getCookieKey(uid))) {
				String last_cookie = jedis.hmget(JedisKeyGenerator.getCookieKey(uid), "cookie").get(0);
				if (last_cookie != null) {
					// 更新cookie-map
					jedis.hset(last_cookie, key, value);
				}
				// 更新user-map
				jedis.hset(JedisKeyGenerator.getCookieKey(uid), key, value);
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return false;
	}
}
