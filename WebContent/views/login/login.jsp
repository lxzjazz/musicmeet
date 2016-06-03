<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../../unlogin-header.jsp"></jsp:include>
<script type="text/javascript" src="public/javascript/login/login.js"></script>
</head>
<body style="padding-top: 140px;">
	<div class="container">
		<div class="tabbable" style="margin-bottom: 9px;">
			<div class="span6 section section-large">
				<div class="section-header">
					<h3>登录</h3>
					<p class="reg_nav">
						<a class="backward" href="<%=application.getContextPath()%>/">返回首页</a>&nbsp;•&nbsp;<a href="register">注册</a>
					</p>
				</div>
				<div class="section-content">
					<form class="form-horizontal" id="form-login">
						<fieldset>
							<div class="control-group">
								<label class="control-label" for="email">帐号</label>
								<div class="controls">
									<div class="input-prepend">
										<span class="add-on"><i class="icon-envelope"></i></span><input class="input-large" id="email" placeholder="请输入邮箱帐号" maxlength="32" type="text">
									</div>
									<p class="help-block email-tip">帐号不存在</p>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="password">密码</label>
								<div class="controls">
									<div class="input-prepend">
										<span class="add-on"><i class="icon-lock"></i></span><input class="input-large" id="password" placeholder="请输入密码" maxlength="20" type="password">
									</div>
									<p class="help-block password-tip">密码不正确</p>
								</div>
							</div>
							<div class="control-group">
								<div class="controls">
									<label class="checkbox"> <input id="cookiesaved" type="checkbox">记住登录
									</label> <a href="" style="margin-left: 110px;">忘记密码?</a>
								</div>
							</div>
							<div class="control-group" style="padding-top: 30px;">
								<div class="controls">
									<input class="btn btn-large btn-primary" value="立即登录" name="submit" id="submit" type="submit" data-loading-text="登录中..." /><span class="label label-success submit-result">恭喜您,登录成功</span>
								</div>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="../../footer.jsp"></jsp:include>