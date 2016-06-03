<%@ page language="java" pageEncoding="UTF-8"%>

<div id="avatar_area" avatar_id="${sGlobal.avatar_id}">
	<a href="javascript:reset_avatar()" id="reset_avatar">重置为默认头像</a>
	<h1>头像设置</h1>
	<div class="dotted_line"></div>
	<div class="avatar_info">
		<form method="POST" enctype="multipart/form-data" id="form-avatar">
			<input type="file" name="file" id="file" accept="image/*"/>
			<div class="pull-left">
				<div class="btn_area">
					<button id="selectBtn" class="btn btn-primary pull-left">
						<i class="icon-picture icon-white"></i> 本地照片
					</button>
					<button id="saveBtn" class="btn disabled pull-right" disabled="disabled">保存</button>
				</div>
				<div id="picture_index">
					<img src="public/image/avatar.png" width="300px" height="300px" />
				</div>
				<div id="picture_original">
					<img src="public/image/avatar.png" width="300px" height="300px" />
				</div>
			</div>
			<div class="pull-right">
				<div>
					<div id="picture_200" class="temp_img"></div>
					<img class="saved_img big_img" src="avatar/${sessionScope.avatar_id}?size=big" /> <span class="big_pic">大尺寸头像,200x200像素</span>
				</div>
				<div>
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
		</form>
	</div>
</div>
<script type="text/javascript" src="public/lib/avatar/js/jquery.avatar2.js"></script>