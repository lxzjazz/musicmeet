package cn.musicmeet.beans;

public class EmailInfo {
	
	private String email;
	private Integer emailStatus;
	private String shortEmail;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getShortEmail() {
		return shortEmail;
	}

	public void setShortEmail(String shortEmail) {
		String email_prefix = shortEmail.substring(0, shortEmail.indexOf("@"));
		String email_postfix = shortEmail.substring(shortEmail.indexOf("@"));
		if (email_prefix.length() > 3) {
			String d = email_prefix.substring(3);
			int l = d.length();
			StringBuffer b = new StringBuffer();
			if (l > 2) {
				l--;
				email_postfix = email_prefix.substring(email_prefix.length() - 1, email_prefix.length()) + email_postfix;
			}
			for (int i = 0; i < l; i++) {
				b.append("*");
			}
			shortEmail = email_prefix.substring(0, 3) + b.toString() + email_postfix;
		} else {
			shortEmail = email_prefix.substring(0, email_prefix.length() - 1) + "*" + email_postfix;
		}
		this.shortEmail = shortEmail;
	}
}
