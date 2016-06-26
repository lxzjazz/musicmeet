<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${empty sGlobal}">
	<jsp:include page="../../header.jsp"></jsp:include>
</c:if>
<link type="text/css" href="public/lib/chat/chat.css" rel="stylesheet" />
<script type="text/javascript">
	$("#header .nav").find("li[name='chat']").addClass("active");
</script>
<script type="text/javascript" src="public/lib/chat/chat.js"></script>
<script type="text/javascript" src="public/lib/base64/Base64.js"></script>
</head>

<div class="container">
	<div id="player_box" class="popover bottom">
		<div class="arrow"></div>
		<div class="popover-content">
			<div class="player pull-left"></div>
		</div>
	</div>
	<audio id="chatAudio" src="data/audio/chat.mp3"></audio>
	<div class="webox_chat" onmouseover="moveObj(this)">
		<div class="inside">
			<h1 class="locked">
				<i class="icon-comment"></i>&nbsp;聊天室&nbsp;-&nbsp;<span id="status"></span>&nbsp;&nbsp;(在线人数:<span id="num" style="color: green"></span>) <a class="span_min" onclick="chat_min(this)" href="javascript:void(0);"></a><a class="span_close" onclick="chat_close(this)" href="javascript:void(0);"></a>
			</h1>
			<div class="chat_msg_panle">
				<div class="chat_msg_panle_inner clearfix"></div>
			</div>
			<div class="chat-tools">
				<form method="POST" enctype="multipart/form-data" id="form-image" style="display: none;">
					<input type="file" name="file" id="file" multiple="multiple" accept="image/*" />
				</form>
				<a class="btn btn-small btn-tool" id="image-btn" style="color: #fff;"><i class="icon-picture icon-white"></i> 图片</a> <a class="btn btn-small btn-tool recorder-btn" id="audio-btn" style="margin-left: 5px;color:#fff;" data-loading-text="加载中" onclick="record(this)"><i class="icon-bullhorn icon-white"></i> 语音</a>
			</div>
			<div class="chat_text clearfix">
				<div class="pull-left" style="width: 556px;">
					<textarea id="textMsg"></textarea>
					<p class="num">
						<i>0</i>/100
					</p>
				</div>
				<button class="btn btn-large btn-primary pull-right" id="send-msg-btn" style="width: 144px; height: 110px;" onclick="sendMsg(this)">发送</button>
			</div>
		</div>
	</div>
</div>
<c:if test="${empty sGlobal}">
	<jsp:include page="../../footer.jsp"></jsp:include>
</c:if>