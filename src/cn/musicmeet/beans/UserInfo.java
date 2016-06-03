package cn.musicmeet.beans;

import java.security.NoSuchAlgorithmException;

import cn.musicmeet.util.CommonUtil;

public class UserInfo {
	
	private Long uid;
	private String username;
	private String email;
	private String location;
	private String sign;
	private String createTime;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() throws NoSuchAlgorithmException {
		return CommonUtil.md5(email);
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
