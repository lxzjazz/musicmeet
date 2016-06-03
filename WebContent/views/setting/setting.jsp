<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link type="text/css" href="public/lib/avatar/css/jquery.jcrop.css" rel="stylesheet">
<link type="text/css" href="public/stylesheet/setting.css" rel="stylesheet">
<script type="text/javascript" src="public/lib/avatar/js/jquery.jcrop.min.js"></script>
<script type="text/javascript" src="public/lib/avatar/js/jquery.imgCutter.min.js"></script>
<script type="text/javascript" src="public/lib/avatar/js/jquery.ajaxfileupload.js"></script>
<div id="setting">
	<div class="sider-content">
		<div class="container">
			<div class="row">
				<div class="span3 sidebar">
					<ul class="nav nav-list sidenav">
						<li class="nav-header">&nbsp;&nbsp;&nbsp;<i class="icon-list-alt"></i> 个人设置
						</li>
						<li class="nav-item active" id="setting-profile_li"><a href="javascript:post_url('setting-profile');"><i class="icon-chevron-right"></i>基本资料</a></li>
						<li class="nav-item" id="setting-avatar_li"><a href="javascript:post_url('setting-avatar');"><i class="icon-chevron-right"></i>我的头像</a></li>
						<li class="nav-header" style="border-top: solid 1px #e5e5e5;">&nbsp;&nbsp;&nbsp;<i class="icon-cog"></i> 账号设置
						</li>
						<li class="nav-item" id="setting-email_li"><a href="javascript:post_url('setting-email');"><i class="icon-chevron-right"></i>认证邮箱</a></li>
						<li class="nav-item" id="setting-password_li"><a href="javascript:post_url('setting-password');"><i class="icon-chevron-right"></i>修改密码</a></li>
						<li class="nav-item" id="setting-other_li"><a href="javascript:post_url('setting-other');"><i class="icon-chevron-right"></i>其他设置</a></li>
					</ul>
				</div>
				<div id="page" class="span9"></div>
			</div>
		</div>
	</div>
</div>