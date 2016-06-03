package cn.musicmeet.beans;

import org.apache.commons.lang3.StringUtils;

import cn.musicmeet.util.CommonUtil;

public class LoginUser {

	public static final String SGLOBAL = "sGlobal";
	public static final String UID = "uid";
	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String ACCOUNT_STATUS = "account_status";
	public static final String SESSION_ID = "session_id";
	public static final String AVATAR_ID = "avatar_id";
	public static final String COOKIE_SAVED = "cookie_saved";

	private String uid;
	private String email;
	private String username;
	private String password;
	private String salt;
	private String accountStatus;
	private String sessionID;
	private boolean cookieSaved;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getAvatarID() {
		if (StringUtils.isNotBlank(email)) {
			return CommonUtil.md5(email);
		}
		return "";
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isCookieSaved() {
		return cookieSaved;
	}

	public void setCookieSaved(boolean cookieSaved) {
		this.cookieSaved = cookieSaved;
	}
}
