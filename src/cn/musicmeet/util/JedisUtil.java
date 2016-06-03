package cn.musicmeet.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import cn.musicmeet.beans.LoginUser;

public class JedisUtil {

	private static Logger logger = Logger.getLogger(JedisUtil.class);

	public static boolean saveUser(ShardedJedisPool pool, LoginUser loginUser, String cookie) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();

			// userCookie
			String userCookiekey = JedisKeyGenerator.getUserCookieKeyByUid(loginUser.getUid());
			// 清除旧数据
			if (jedis.exists(userCookiekey)) {
				// 清除cookieMap旧数据
				jedis.del(jedis.get(userCookiekey));
				// 清除userCookie旧数据
				jedis.del(userCookiekey);
			}

			// 组装数据
			Map<String, String> mapData = new HashMap<String, String>();
			mapData.put(LoginUser.UID, loginUser.getUid());
			mapData.put(LoginUser.USERNAME, loginUser.getUsername());
			mapData.put(LoginUser.EMAIL, loginUser.getEmail());
			mapData.put(LoginUser.ACCOUNT_STATUS, loginUser.getAccountStatus());
			mapData.put(LoginUser.SESSION_ID, loginUser.getSessionID());
			mapData.put(LoginUser.COOKIE_SAVED, Boolean.toString(loginUser.isCookieSaved()));

			// 新cookieMapKey
			String cookieMapKey = JedisKeyGenerator.getCookieMapKeyByCookie(cookie);
			// 更新或者存入cookie-map(默认会话保持保持30天)
			jedis.hmset(cookieMapKey, mapData);
			// FIXME 是否有必要
			jedis.expire(cookieMapKey, 60 * 60 * 24 * 30);

			// 更新或存入user-cookie
			jedis.set(userCookiekey, cookieMapKey);
			// FIXME 是否有必要
			jedis.expire(userCookiekey, 60 * 60 * 24 * 30);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return false;
	}

	public static LoginUser getLoginUserByUid(ShardedJedisPool pool, String uid) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			// 获取userCookieKey
			String userCookiekey = JedisKeyGenerator.getUserCookieKeyByUid(uid);
			if (StringUtils.isNotBlank(userCookiekey) && jedis.exists(userCookiekey)) {
				return getLoginUserByCookieMapKey(jedis, jedis.get(userCookiekey));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return null;
	}

	public static LoginUser getLoginUserByCookie(ShardedJedisPool pool, Cookie[] cookie) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			if (cookie != null) {
				for (int i = 0; i < cookie.length; i++) {
					if (cookie[i].getName().equals("user_token")) {
						return getLoginUserByCookieMapKey(jedis, JedisKeyGenerator.getCookieMapKeyByCookie(cookie[i].getValue()));
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return null;
	}

	private static LoginUser getLoginUserByCookieMapKey(ShardedJedis jedis, String cookieMapKey) {
		if (StringUtils.isNotBlank(cookieMapKey) && jedis.exists(cookieMapKey)) {
			LoginUser loginUser = new LoginUser();
			loginUser.setUid(jedis.hget(cookieMapKey, LoginUser.UID));
			loginUser.setUsername(jedis.hget(cookieMapKey, LoginUser.USERNAME));
			loginUser.setEmail(jedis.hget(cookieMapKey, LoginUser.EMAIL));
			loginUser.setAccountStatus(jedis.hget(cookieMapKey, LoginUser.ACCOUNT_STATUS));
			loginUser.setSessionID(jedis.hget(cookieMapKey, LoginUser.SESSION_ID));
			loginUser.setCookieSaved(Boolean.valueOf(jedis.hget(cookieMapKey, LoginUser.COOKIE_SAVED)));
			return loginUser;
		}
		return null;
	}

	public static boolean resetUser(ShardedJedisPool pool, String uid, String key, String value) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			// 获取userCookieKey
			String userCookiekey = JedisKeyGenerator.getUserCookieKeyByUid(uid);
			if (StringUtils.isNotBlank(userCookiekey) && jedis.exists(userCookiekey)) {
				// 通过userCookieKey获取cookieMapKey
				String cookieMapKey = jedis.get(userCookiekey);
				if (StringUtils.isNotBlank(cookieMapKey) && jedis.exists(cookieMapKey)) {
					// 更新cookie-map
					jedis.hset(cookieMapKey, key, value);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
		return false;
	}

	public static void deleteUser(ShardedJedisPool pool, String uid) {
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
			// 获取userCookieKey
			String userCookiekey = JedisKeyGenerator.getUserCookieKeyByUid(uid);
			if (StringUtils.isNotBlank(userCookiekey) && jedis.exists(userCookiekey)) {
				// 通过userCookieKey获取cookieMapKey
				String cookieMapKey = jedis.get(userCookiekey);
				if (StringUtils.isNotBlank(cookieMapKey) && jedis.exists(cookieMapKey)) {
					// 更新cookie-map
					jedis.del(cookieMapKey);
				}
				jedis.del(userCookiekey);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			jedis.close();
		}
	}
}
