<%@ page language="java" pageEncoding="utf-8"%>

<div class="modal-header">
	<a class="close" data-dismiss="modal">×</a>
	<h3>上传头像</h3>
</div>
<div class="modal-body" id="avatar_area" avatar_id="${pageContext.session.id}">
	<div class="avatar_info">
		<form method="POST" enctype="multipart/form-data" id="form-avatar">
			<input type="file" name="file" id="file" accept="image/*" />
			<div class="pull-left" style="margin-top: 50px;">
				<div id="picture_index">
					<img src="public/image/avatar.png" width="300px" height="300px" />
				</div>
				<div id="picture_original">
					<img src="public/image/avatar.png" width="300px" height="300px" />
				</div>
			</div>
		</form>
		<div class="pull-right">
			<div id="picture_200" class="temp_img"></div>
			<img class="saved_img big_img" src="avatar/${sessionScope.avatar_id}?size=big" /> <span class="big_pic">大尺寸头像,200x200像素</span>
		</div>
		<div class="pull-right">
			<div class="pull-left" style="padding-top: 60px;">
				<div id="picture_30" class="temp_img" style="margin-left: 34px;"></div>
				<img class="saved_img small_img" style="margin-left: 34px;" src="avatar/${sessionScope.avatar_id}?size=small" />
				<p class="small_pic">小尺寸头像</p>
				<p class="pic_size">30x30像素</p>
			</div>
			<div class="pull-right">
				<div id="picture_90" class="temp_img"></div>
				<img class="saved_img middle_img" src="avatar/${sessionScope.avatar_id}?size=middle" />
				<p class="small_pic">中尺寸头像</p>
				<p class="pic_size">90x90像素</p>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<div class="btn_area">
		<button id="selectBtn" class="btn btn-primary pull-left">
			<i class="icon-picture icon-white"></i> 本地照片
		</button>
		<button id="saveBtn" class="btn disabled pull-right" disabled="disabled">保存</button>
	</div>
</div>
<script type="text/javascript" src="public/lib/avatar/js/jquery.avatar.js"></script>