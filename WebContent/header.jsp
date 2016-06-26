<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<meta http-equiv="X-UA-Compatible" content="chrome=1">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>">
<title>音遇 MusicMeet</title>
<head>
<jsp:include page="source.jsp"></jsp:include>
<script type="text/javascript">
	var ws = null;
	var user = "${sGlobal.username}";
	var user_avatar = "avatar/${pageContext.session.id}?size=small";
</script>
</head>
<body>
	<div id="header" class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<c:choose>
					<c:when test="${not empty sGlobal}">
						<a class="brand" href="#index" style="padding: 0px 10px 0px 20px;"><img src="public/image/pic_logo.png"></a>
					</c:when>
					<c:otherwise>
						<a class="brand" href="" style="padding: 0px 10px 0px 20px;"><img src="public/image/pic_logo.png"></a>
					</c:otherwise>
				</c:choose>
				<div class="nav-collapse collapse" style="height: 0px;">
					<ul class="nav">
						<c:choose>
							<c:when test="${not empty sGlobal}">
								<li name="index"><a href="javascript:get_url('index')">首页</a></li>
								<li name="chat"><a href="javascript:get_url('chat')">聊天室</a></li>
							</c:when>
							<c:otherwise>
								<li name="index"><a href="">首页</a></li>
								<li name="chat"><a href="chat">聊天室</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
					<ul class="nav a_login_register pull-right">
						<c:choose>
							<c:when test="${not empty sGlobal}">
								<li><img src="avatar/${sGlobal.avatar_id}?size=small" class="img-polaroid avatar" width="30px" height="30px" /></li>
								<li class="dropdown"><a class="nav dropdown-toggle a_name" data-toggle="dropdown" href="javascript:;">${sGlobal.username} <b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li><a href="javascript:get_url('setting-profile')">个人设置</a></li>
										<li><a href="javascript:get_url('setting-email')">帐号设置</a></li>
										<li class="divider"></li>
										<li><a href="logout" class="a_logout">退出</a></li>
									</ul></li>
								<li>
							</c:when>
							<c:otherwise>
								<li><a class="nav a_login" href="login">登录</a></li>
								<li class="black_dot">•</li>
								<li><a class="nav a_register" href="register">注册</a></li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>
		</div>
	</div>