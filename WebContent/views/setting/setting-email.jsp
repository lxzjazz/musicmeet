<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	function change_email() {
		$(".old_email").hide();
		$(".change_email").show();
	}
	function cancel_email() {
		$(".change_email").hide();
		$(".old_email").show();
	}
</script>
<div id="email_area">
	<a href="javascript:change_email()" id="reset_eamil">修改邮箱</a>
	<h1>邮箱设置</h1>
	<div class="dotted_line"></div>
	<table class="email_info">
		<tr class="old_email">
			<td>常用邮箱:<br /> <c:choose>
					<c:when test="${requestScope.emailInfo.emailStatus==1}">
						<span style="font-size: 12px; color: green;">&nbsp;(已验证)</span>
					</c:when>
					<c:otherwise>
						<span style="font-size: 12px; color: orange;">&nbsp;(未验证)</span>
					</c:otherwise>
				</c:choose>
			</td>
			<td><input type="text" style="width: 180px;" value="${requestScope.emailInfo.email}" disabled="disabled" /> <c:if test="${requestScope.emailInfo.emailStatus==0}">
					<a href='javascript:send_email()' class="send_email" style="margin-left: 10px;">验证</a>
					<span class="send_msg" style="margin-left: 10px; display: none;"></span>
				</c:if></td>
		</tr>
		<tr class="change_email">
			<td>新邮箱:</td>
			<td><input type="text" id="newemail" name="newemail" style="width: 180px;"><span class="msg">邮箱格式不正确</span></td>
		</tr>
		<tr class="change_email">
			<td colspan="2"><input type="submit" value="确认修改" id="update_email" data-loading-text="处理中..." class="update_email btn btn-primary"><input type="submit" value="取消" class="btn offset1" onclick="cancel_email()"></td>
		</tr>
	</table>
</div>