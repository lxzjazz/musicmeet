package cn.musicmeet.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import cn.musicmeet.beans.EmailInfo;
import cn.musicmeet.beans.LoginUser;
import cn.musicmeet.beans.User;

@DAO
public interface UserDAO {
	@SQL("SELECT uid,username,email,password,salt,account_status FROM t_user WHERE email=:1")
	public LoginUser getUserByEmail(String email);

	@SQL("SELECT uid,username,email,password,salt,account_status FROM t_user WHERE uid=:1")
	public LoginUser getUserByUid(long uid);

	@ReturnGeneratedKeys
	@SQL("INSERT INTO t_user(username,email,password,salt,location,account_status,create_time) VALUES (:1.username,:1.email,:1.password,:1.salt,:1.location,:1.accountStatus,:1.createTime)")
	public long saveUser(User user);

	@SQL("SELECT COUNT(*) FROM t_user WHERE email=:1")
	public int getEmailCount(String email);

	@SQL("SELECT COUNT(*) FROM t_user WHERE username=:1")
	public int getUsernameCount(String username);
	
	@SQL("SELECT email,email_status FROM t_user WHERE uid= :1")
	public EmailInfo getEmailInfoByUid(String uid);
}
