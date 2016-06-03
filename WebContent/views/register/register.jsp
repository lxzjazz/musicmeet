<%@ page language="java" pageEncoding="UTF-8"%>

<jsp:include page="../../unlogin-header.jsp"></jsp:include>
<link type="text/css" href="public/lib/chosen/chosen.css" rel="stylesheet" />
<link type="text/css" href="public/lib/avatar/css/jquery.jcrop.css" rel="stylesheet">
<link type="text/css" href="public/stylesheet/avatar-modal.css" rel="stylesheet" />
<script src="public/lib/chosen/chosen.jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="public/lib/avatar/js/jquery.jcrop.min.js"></script>
<script type="text/javascript" src="public/lib/avatar/js/jquery.imgCutter.min.js"></script>
<script type="text/javascript" src="public/lib/avatar/js/jquery.ajaxfileupload.js"></script>
<script type="text/javascript" src="public/javascript/register/register.js"></script>
</head>
<body style="padding-top: 40px;">
	<div class="container">
		<div class="span10 section section-large">
			<div class="section-header">
				<h3>注册新帐号</h3>
				<p class="reg_nav">
					<a class="backward" href="<%=application.getContextPath()%>/" style="background-position-y: 1px;">返回首页</a>&nbsp;•&nbsp;<a href="login">登录</a>
				</p>
			</div>
			<div class="section-content">
				<form class="form-horizontal" style="margin-top: 0;" ng-app="register-form" id="register-form">
					<fieldset>
						<div class="page-header">
							<h3>帐号信息</h3>
						</div>
						<div class="control-group">
							<label class="control-label" for="username">常用昵称</label>
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-user"></i></span><input class="input-xlarge" id="username" onblur="checkUsername(this)" type="text" ng-model="user.username" username>
								</div>
								<p class="help-block username-tip">昵称 {{user.username}} 已被占用</p>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="email">电子邮箱</label>
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-envelope"></i></span><input class="input-xlarge" id="email" type="text" maxlength="32" onblur="checkEmail(this)" ng-model="user.email" email>
								</div>
								<p class="help-block email-tip">邮箱 {{user.email}} 已被占用</p>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="password1">创建密码</label>
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-lock"></i></span><input class="input-xlarge" id="password1" type="password" maxlength="20" onblur="checkPW1(this)" ng-model="user.password1" password1>
								</div>
								<p class="help-block password1-tip">密码长度需为6-20个字符</p>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="password2">确认密码</label>
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-lock"></i></span><input class="input-xlarge" id="password2" type="password" maxlength="20" onblur="checkPW2(this)" ng-model="user.password2" password2>
								</div>
								<p class="help-block password2-tip">两次密码输入不一致</p>
							</div>
						</div>
						<div class="control-group" style="margin-bottom: 0px;">
							<label class="control-label" for="code" style="width: 146px;">邀请码</label>
							<div class="controls">
								<div class="input-prepend">
									<span class="add-on"><i class="icon-hand-right"></i></span><input class="input-xlarge" id="code" type="text" value="sdh6+$%#jadhdF!DSc3#$jkFRG78Hd!Af2">
								</div>
								<p class="help-block code-tip">邀请码输入错误</p>
							</div>
						</div>
						<div class="page-header" style="margin-top: 0;">
							<h3>
								个人信息<span style="font-size: 13px; font-weight: normal;">(选填)</span>
							</h3>
						</div>
						<div class="control-group" style="margin-bottom: 30px;">
							<label class="control-label" for="location">现居城市</label>
							<div class="controls">
								<select data-placeholder="请选择一个城市..." class="chzn-select" id="location" style="width: 325px;">
									<option value=""></option>
								</select>
							</div>
						</div>
						<div class="control-group" style="margin-bottom: 30px;">
							<label class="control-label" for="reg_avatar">个性头像</label>
							<div class="controls">
								<div id="reg_avatar">
									<img src="avatar/${sessionScope.avatar_id}?size=middle" class="img-polaroid" onclick='get_avatarModal()' style="cursor: pointer;">
								</div>
							</div>
						</div>
						<div class="control-group" style="padding-top: 20px;">
							<div class="controls">
								<input class="btn btn-large disabled" value="注册新用户" onclick="reg_submit(this)" type="submit" data-loading-text="注册中..." disabled="disabled" /> <span class="label label-success submit-result">恭喜您,帐号注册成功</span>
							</div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
	<div class="modal hide fade" id="avatarModal"></div>
	<jsp:include page="../../footer.jsp"></jsp:include>