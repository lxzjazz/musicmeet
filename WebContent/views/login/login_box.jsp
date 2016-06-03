<%@ page language="java" pageEncoding="UTF-8"%>

<style>
#re-login-modal {
	width: 580px;
}

#re-login-modal .modal-header {
	background-color: #f6f6f6;
	border-bottom: 1px solid #f4f4f4;
	padding: 0 15px 0;
	height: 55px;
	line-height: 55px;
}

#re-login-modal .modal-header h3 {
	color: #00b38a;
	text-transform: uppercase;
	margin: 0;
	line-height: 52px;
	height: 60px;
}

#re-login-modal .reg_nav {
	margin-top: -60px;
	float: right;
	font-size: 13px;
}

#re-login-modal .reg_nav .backward {
	background: url("public/image/login-return.png") no-repeat;
	padding-left: 18px;
}

#re-login-modal .reg_nav, .reg_nav a {
	color: #00b38a;
}

#re-login-modal .reg_nav a:hover {
	color: #00a57f;
}

#re-login-modal form {
	margin-top: 50px;
	padding-bottom: 2px;
}

#re-login-modal .section-content .help-block, #re-login-modal .section-content .help-inline
	{
	visibility: hidden;
}

#re-login-modal .checkbox {
	display: inline;
	padding-left: 8px;
}

#re-login-modal .checkbox input {
	margin-left: 0;
}

#re-login-modal .submit-result {
	padding: 5px 8px;
	font-size: 14px;
	margin-left: 20px;
	visibility: hidden;
}
</style>
<!--登录弹出框 -->
<div class="modal hide fade" id="re-login-modal">
	<div class="modal-header">
		<h3>登录</h3>
		<p class="reg_nav">
			<a class="backward" href="<%=application.getContextPath()%>/">返回首页</a>&nbsp;•&nbsp;<a href="register">注册</a>
		</p>
	</div>
	<div class="modal-body">
		<div class="section-content">
			<form class="form-horizontal" id="box-login-form">
				<fieldset>
					<div class="control-group">
						<label class="control-label" for="box-email">帐号</label>
						<div class="controls">
							<div class="input-prepend">
								<span class="add-on"><i class="icon-envelope"></i></span><input class="input-large" name="box-email" id="box-email" placeholder="请输入邮箱帐号" maxlength="32" type="text">
							</div>
							<p class="help-block email-tip">帐号不存在</p>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="box-password">密码</label>
						<div class="controls">
							<div class="input-prepend">
								<span class="add-on"><i class="icon-lock"></i></span><input class="input-large" name="box-password" id="box-password" placeholder="请输入密码" maxlength="20" type="password">
							</div>
							<p class="help-block password-tip">密码不正确</p>
						</div>
					</div>
					<div class="control-group">
						<div class="controls">
							<label class="checkbox"> <input name="cookiesaved" id="box-cookiesaved" type="checkbox">记住登录
							</label> <a href="" style="margin-left: 110px;">忘记密码?</a>
						</div>
					</div>
					<div class="control-group" style="padding-top: 30px;">
						<div class="controls">
							<input class="btn btn-large btn-primary" value="立即登录" id="box-submit" name="box-submit" type="submit" data-loading-text="登录中..." /><span class="label label-success submit-result">恭喜您,登录成功</span>
						</div>
					</div>
				</fieldset>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	var user = getCookie("user_email");
	if (user != null) {
		$("#box-email").val(user);
		$("#box-cookiesaved").attr("checked", "checked");
	}
	// 提交表单
	$("#box-login-form").submit(function() {
		if ($("#box-submit").hasClass("disabled")) {
			return false;
		}
		$("#box-submit").button('loading');
		$.post('login', {
			email : $("#box-email").val(),
			password : $("#box-password").val(),
			cookieSaved : $("#box-cookiesaved").attr("checked") == "checked"
		}, function(data) {
			if (data.error) {
				$(".submit-result").attr("class", "label label-important submit-result");
				$(".submit-result").html(data.error);
				$(".submit-result").css("visibility", "visible");
				$("#box-submit").button('reset');
			} else {
				if ($("#box-cookiesaved").attr("checked") == "checked") {
					setCookie("user_email", $("#box-email").val());
				} else {
					delCookie("user_email");
				}
				window.location.reload();
			}
		}, 'json');
		return false;
	});
</script>