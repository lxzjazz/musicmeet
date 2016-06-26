$(document).ready(function() {
	resize_webox();
	if (ws) {
		ws.close();
	}
	var url = "ws://" + document.location.hostname + ":8088";
	if ('WebSocket' in window) {
		ws = new WebSocket(url);
	} else if ('MozWebSocket' in window) {
		ws = new MozWebSocket(url);
	} else {
		$('#status').html('<span style="color:red;">你的浏览器过时了, 竟然不支持WebSocket!</span>');
		return;
	}
	ws.onopen = function() {
		$('#status').html('<span style="color:green;">已正常连接, 欢迎光临!</span>');
	};
	ws.onmessage = function(event) {
		var data = event.data;
		showMsg(data, false);
	};
	ws.onclose = function() {
		$('#status').html('<span style="color:red;">连接已断开, 再见!</span>');
		$('#num').text(0);
	};

	$(".webox_chat textarea").focus(function() {
		$(this).parent().parent().addClass("focus");
	}).blur(function() {
		$(this).parent().parent().removeClass("focus");
	}).on('input', (function() {
		var str = $(".webox_chat textarea").val();
		var num = Math.ceil((zh_count(str) + str.length) / 2);
		var _parent = $(this).parent();
		var btn = $('#send-msg-btn');
		if (num < 101) {
			_parent.find(".num").html("<i>" + num + "</i>/100");
			btn.attr("class", "btn btn-large btn-primary");
		} else {
			_parent.find(".num").html("<i style='color:red;'>" + num + "</i>/100");
			btn.attr("class", "btn btn-large disabled");
		}
	}));
	// 兼容ie浏览器
	$(".webox_chat textarea").each(function() {
		var _this = this;
		if (this.attachEvent) {
			this.attachEvent('onpropertychange', function(e) {
				if (e.propertyName != 'value')
					return;
				$(_this).trigger('input');
			});
		}
	});
	// 绑定图片上传点击事件
	$('#image-btn').click(function() {
		$("#file").trigger("click");
		return false;
	});
	// 选中图片后
	$("#file").change(function() {
		if ($.browser.msie) {
			alert("对不起,IE浏览器不支持图片上传");
		} else {
			var file = $("#file")[0];
			var images = file.files;
			for ( var i in images) {
				var image = images[i];
				if (/image\/\w+/.test(image.type)) {
					var reader = new FileReader();
					reader.readAsDataURL(image);
					reader.onload = function(e) {
						var data = {
							userName : user,
							userAvatar : user_avatar,
							src : this.result
						};
						data = JSON.stringify(data);
						showMsg('[img]:' + data, true);
					};
				}
			}
		}
	});
});

// 计算中文字数
var zh_count = function(str) {
	if (str == undefined || str == '') {
		return 0;
	}
	var pattern = /[\u4e00-\u9fa5]/g;
	if (pattern.test(str)) {
		return str.match(pattern).length;
	}
	return 0;
}

function showMsg(data, isSelf) {
	if (data.substring(0, 6) == '[num]:') {
		data = data.substring(6);
		$('#num').text(data);
	} else if (data.substring(0, 6) == '[msg]:') {
		data = data.substring(6);
		if (!isSelf) {
			data = BASE64.decoder2(data);
		}
		try {
			data = JSON.parse(data);
		} catch (e) {
			return false;
		}
		var type = (isSelf ? 'chat_msg_own' : 'chat_msg_opposite');
		var color = (isSelf ? '#F17F2F' : '#38ABFC');
		var panel = $('.chat_msg_panle_inner');
		var dd = $('<dd class="' + type + '"></dd>');
		dd.appendTo(panel);
		if (!isSelf) {
			dd.append('<div class="chat_msg_time"><span style="color:' + color + '"></span>&nbsp;&nbsp;' + new Date().format('hh:mm:ss') + '</div>');
		} else {
			dd.append('<div class="chat_msg_time">' + new Date().format('hh:mm:ss') + '&nbsp;&nbsp;<span style="color:' + color + '"></span></div>');
		}
		dd.find('.chat_msg_time>span').text(data.userName);
		var dd_bottom = $('<div class="dd_bottom"></div>');
		dd_bottom.append('<div class="chat_msg_box"></div>');
		var chat_msg_box = dd_bottom.find('.chat_msg_box');
		var avatar = $('<img src="' + data.userAvatar + '" class="img-polaroid avatar" width="30px" height="30px">');
		if (!isSelf) {
			avatar.addClass('pull-left');
			chat_msg_box.addClass('pull-right');
			avatar.css({
				'margin-right' : '10px'
			});
			dd_bottom.prepend(avatar);
		} else {
			chat_msg_box.addClass('pull-left');
			avatar.addClass('pull-right');
			avatar.css({
				'margin-left' : '10px'
			});
			dd_bottom.append(avatar);
		}
		dd.append(dd_bottom);
		if (data.text) {
			var p = $('<p style="min-height: 24px; line-height: 24px; margin-bottom: 0px; max-width: 280px; overflow: hidden;word-break:break-all;"></p>');
			p.text(BASE64.decoder2(data.text));
			p.appendTo(chat_msg_box);
		} else if (data.audio) {
			chat_msg_box.addClass('audio_msg');
			var btn = $('<a class="btn btn-small player-btn" onclick="player_audio(this)" audio_id="' + data.audio.audioId + '"><i class="icon-volume-up"></i> ' + data.audio.audioTime + '</a>');
			if (!isSelf) {
				btn.addClass('playbox-right');
			}
			btn.appendTo(chat_msg_box);
		}
		panel.parent()[0].scrollTop = panel[0].scrollHeight;
		if (!isSelf) {
			$('#chatAudio')[0].play();
		}
	} else if (data.substring(0, 6) == '[img]:') {
		data = data.substring(6);
		if (!isSelf) {
			data = BASE64.decoder2(data);
		}
		try {
			data = JSON.parse(data);
		} catch (e) {
			return false;
		}
		var type = (isSelf ? 'chat_msg_own' : 'chat_msg_opposite');
		var color = (isSelf ? '#F17F2F' : '#38ABFC');
		var panel = $('.chat_msg_panle_inner');
		var dd = $('<dd class="' + type + '"></dd>');
		dd.appendTo(panel);
		if (!isSelf) {
			dd.append('<div class="chat_msg_time"><span style="color:' + color + '"></span>&nbsp;&nbsp;' + new Date().format('hh:mm:ss') + '</div>');
		} else {
			dd.append('<div class="chat_msg_time">' + new Date().format('hh:mm:ss') + '&nbsp;&nbsp;<span style="color:' + color + '"></span></div>');
		}
		dd.find('.chat_msg_time>span').text(data.userName);
		var dd_bottom = $('<div class="dd_bottom"></div>');
		dd_bottom.append('<div class="chat_msg_box"></div>');
		var chat_msg_box = dd_bottom.find('.chat_msg_box');
		var avatar = $('<img src="' + data.userAvatar + '" class="img-polaroid avatar" width="30px" height="30px">');
		if (!isSelf) {
			avatar.addClass('pull-left');
			chat_msg_box.addClass('pull-right');
			avatar.css({
				'margin-right' : '10px'
			});
			dd_bottom.prepend(avatar);
		} else {
			chat_msg_box.addClass('pull-left');
			avatar.addClass('pull-right');
			avatar.css({
				'margin-left' : '10px'
			});
			dd_bottom.append(avatar);
		}
		dd.append(dd_bottom);
		var p = $('<img width="30px" height="30px"/>');
		p.attr('src', data.src);
		var a = $('<a href="' + data.src + '" style="cursor: url(public/image/big.cur), auto !important;"></a>');
		a.imgbox({
			'speedIn' : 500,
			'speedOut' : 300,
			'alignment' : 'center',
			'overlayShow' : false,
			'allowMultiple' : false
		});
		p.appendTo(a);
		a.appendTo(chat_msg_box);
		panel.parent()[0].scrollTop = panel[0].scrollHeight;
		if (!isSelf) {
			$('#chatAudio')[0].play();
		}
	}
}

function sendMsg(obj) {
	var _this = $(obj);
	if (_this.hasClass('disabled')) {
		return;
	}
	var textMsg = $('#textMsg');
	if (textMsg.val() == '') {
		setTimeout(function() {
			textMsg.val('');
		}, 1);
		return;
	}
	user = user != '' ? user : '匿名用户';
	if (ws != null && user != '') {
		var data = {
			userName : user,
			userAvatar : user_avatar,
			text : BASE64.encoder(textMsg.val())
		};
		data = JSON.stringify(data);
		ws.send(BASE64.encoder(data));
		setTimeout(function() {
			showMsg('[msg]:' + data, true);
			textMsg.val('');
			$('.chat_text .num').html('<i>0</i>/100');
		}, 0);
	}
}

$(".webox_chat .chat_text").live("keydown", function(e) {
	var text = $(this).parent().find("textarea");
	var k = e || event;
	var currKey = k.keyCode || k.which || k.charCode;
	if (k.ctrlKey && currKey == 13) {
		text.val(text.val() + "\n");
	} else if (currKey == 13) {
		sendMsg($('#send-msg-btn'));
	}
});

function resize_webox() {
	$('.webox_chat').css({
		top : ($(window).height() - $('.webox_chat').height()) / 2 + 'px',
		left : ($(window).width() - $('.webox_chat').width()) / 2 + 'px'
	});
}

$(window).resize(function() {
	resize_webox();
});

function record(obj) {
	var _this = $(obj);
	var item = _this.parent().attr("item");
	if (!$("#recorder_box")[0]) {
		_this.button('loading');
		$.get('record.html', function(data) {
			var div = document.createElement("div");
			div.innerHTML = data;
			$(".chat-tools")[0].appendChild(div);
			Wami.Setup({
				onReady : function() {
					var recorder_box = $("#recorder_box");
					recorder_box.css({
						top : _this.offset().top + 20 - $('.webox_chat').offset().top,
						left : _this.offset().left - 56 - $('.webox_chat').offset().left
					});
					_this.addClass("on");
					_this.button('reset');
					recorder_box.attr("item", item);
					Wami.checkSecurity(function() {
						Wami.hide();
						recorder_box.addClass("visible");
					});
				},
				onError : function() {
					_this.button('reset');
					alert('对不起，您的麦克风连接不正常，请在确保麦克风连接良好的情况下，刷新本页面后重试');
				}
			});
		});
	} else {
		var recorder_box = $("#recorder_box");
		var status = recorder_box.attr("status");
		if (status == undefined || status != "listening") {
			alert("正在录音中,无法关闭");
			return false;
		}
		if (_this.hasClass("on") && _this.hasClass("recorder-btn")) {
			if (!recorder_box.hasClass("visible")) {
				reRecord();
			} else {
				recorder_box.removeClass("visible");
				return;
			}
		} else {
			if (recorder_box.hasClass("visible")) {
				recorder_box.removeClass("visible");
			}
			recorder_box.css({
				top : _this.offset().top + 20,
				left : _this.offset().left - 56
			});
			recorder_box.attr("item", item);
			$("#audio-btn.on").removeClass("on");
			if (_this.hasClass("recorder-btn")) {
				_this.addClass("on");
			}
			reRecord();
		}
		// 获取权限后显示录音插件
		Wami.checkSecurity(function() {
			recorder_box.addClass("visible");
		});
	}
}

function player_audio(obj) {
	var _this = $(obj);
	var audioId = _this.attr('audio_id');
	var player_box = $('#player_box');
	if (!_this.hasClass("active")) {
		var isRight = _this.hasClass('playbox-right');
		// 移动到当前位置
		player_box.css({
			top : _this.offset().top + 31,
			left : isRight ? (_this.offset().left - 1) : (_this.offset().left - 138)
		});
		if (isRight) {
			player_box.addClass('playbox-right');
		} else {
			player_box.removeClass('playbox-right');
		}
		// 安装播放器
		var url = BASE64.encoder("chat/download/audio?audioId=" + audioId);
		player_box.find('.player').html('<object type="application/x-shockwave-flash" style="outline: none" data="public/lib/mp3Player/player.swf" width="200" height="24"><param name="bgcolor" value="#FFFFFF"><param name="wmode" value="transparent"><param name="menu" value="false"><param name="flashvars" value="animation=no&amp;encode=yes&amp;initialvolume=60&amp;remaining=no&amp;noinfo=yes&amp;buffer=5&amp;checkpolicy=no&amp;autostart=yes&amp;soundFile=' + url + '"></object>');
		// 添加当前标记
		player_box.css('visibility', 'visible');
		$(".player-btn.active").removeClass("active");
		_this.addClass('active');
	} else {
		player_box.css('visibility', 'hidden');
		player_box.find('.player').empty();
		_this.removeClass('active');
	}
}