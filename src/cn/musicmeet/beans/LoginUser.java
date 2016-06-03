package cn.musicmeet.beans;

public class LoginUser {
	private String uid;
	private String email;
	private String username;
	private String password;
	private String salt;
	private String accountStatus;
	private String sessionID;
	private String avatarID;
	private String cookiesaved = "false";

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

	public String getCookiesaved() {
		return cookiesaved;
	}

	public void setCookiesaved(String cookiesaved) {
		this.cookiesaved = cookiesaved;
	}

	public String getAvatarID() {
		return avatarID;
	}

	public void setAvatarID(String avatarID) {
		this.avatarID = avatarID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
