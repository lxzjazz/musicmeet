<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="password_area">
	<h1>修改密码</h1>
	<div class="dotted_line"></div>
	<table class="password_info">
		<tr>
			<td>原密码:</td>
			<td><input type="password" id="oldpassword" name="oldpassword"><span class="msg">请输入4-20位的密码</span></td>
		</tr>
		<tr>
			<td>新密码:</td>
			<td><input type="password" id="newpassword" name="newpassword"><span class="msg">请输入4-20位的密码</span></td>
		</tr>
		<tr>
			<td>重复新密码:</td>
			<td><input type="password" id="newpasswordRepeat" name="newpasswordRepeat"><span class="msg">两次密码输入不一致</span></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" id="reset_pwd" value="确认修改" class="update_password btn btn-primary" data-loading-text="处理中..."></td>
		</tr>
	</table>
</div>