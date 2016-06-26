var cutter = new jQuery.ImgCutter({
	// 主图片所在容器ID
	content : "picture_original",

	// 缩略图配置,ID:所在容器ID;width,height:缩略图大小
	purviews : [ {
		id : "picture_200",
		width : 200,
		height : 200
	}, {
		id : "picture_90",
		width : 90,
		height : 90
	}, {
		id : "picture_30",
		width : 30,
		height : 30
	} ],

	// 选择器默认大小
	selector : {
		width : 120,
		height : 120
	}
});

cutter.init();
// 刷新图片
reflush_avatar_img($("#avatar_area").attr("avatar_id"));

$("#selectBtn").click(function() {
	$("#file").trigger("click");
	return false;
});

$("#saveBtn").click(function() {
	var self = $(this);
	self.attr("class", "btn disabled pull-right");
	self.attr("disabled", "disabled");
	if ($("#file").val() == '') {
		alert("未选择上传图片");
	} else {
		var data = cutter.submit();
		$.ajaxFileUpload({
			url : 'avatar/upload',// 服务器端程序
			secureuri : false,
			fileElementId : 'file',// input框的ID
			dataType : 'json',// 返回数据类型
			beforeSend : function() {// 上传前需要处理的工作，如显示Loading...
			},
			data : {
				"x" : data.x,
				"y" : data.y,
				"width" : data.w,
				"height" : data.h
			},
			success : function(data, status) {// 上传成功
				if (data.error) {
					alert(data.error);
				}
				// 刷新页面
				post_url('setting-avatar', true);
			}
		});
	}
	return false;
});

$("#file").change(function() {
	var file = $("#file")[0];
	if ($.browser.msie) {
		alert("对不起,IE浏览器不支持头像上传");
	} else {
		var img_file = file.files[0];
		if (img_file && /image\/\w+/.test(img_file.type)) {
			var reader = new FileReader();
			reader.readAsDataURL(img_file);
			reader.onload = function(e) {
				cutter.reload(this.result);
				$("#picture_index").hide();
				$("#picture_original").show();
				$(".saved_img").hide();
				$(".temp_img").show();
				$("#saveBtn").removeAttr("disabled");
				$("#saveBtn").attr("class", "btn btn-primary pull-right");
			};
		} else {
			post_url('setting-avatar', true);
		}
	}
});

function reset_avatar() {
	$.post('avatar/reset', {}, function(data) {
		if (data.error) {
			alert(data.error);
		}
		// 刷新页面
		post_url('setting-avatar', true);
	}, 'json');
}