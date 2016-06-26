var se, m = 0, s = 0;
var Wami = window.Wami || {};
Wami.GUI = function() {
	var recordInterval;
	var recordButton = new recordButton();
	recordButton.onstart = startRecording;
	recordButton.onstop = stopRecording;
	recordButton.setEnabled(true);

	var playButton = new playButton();
	playButton.onpause = pausePlaying;
	playButton.onresume = resumePlaying;
	playButton.setEnabled(true);
	playButton.played = false;

	this.setRecordEnabled = function(val) {
		recordButton.setEnabled(val);
	};
	function startRecording() {
		recordButton.setActivity(0);
		Wami.startRecording(Wami.nameCallback(onRecordStart), Wami.nameCallback(onRecordFinish), Wami.nameCallback(onError));
	}

	function pausePlaying() {
		playButton.setEnabled(true);
		playButton.active = false;
		$("#recorder_box .recorder-tip").html('<span class="success">播放已暂停</span>');
		$("#recorder_box").attr("status", "listening");
		clearInterval(se);
		Wami.pausePlaying();
	}

	function resumePlaying() {
		playButton.setActivity(0);
		playButton.active = true;
		$("#recorder_box .recorder-tip").html('<span class="success">播放录音中</span>');
		$("#recorder_box").attr("status", "playing");
		if (!playButton.played) {
			Wami.startPlaying(Wami.nameCallback(onPlayStart), Wami.nameCallback(onPlayFinish), Wami.nameCallback(onError));
			playButton.played = true;
			m = s = 0;
		} else {
			Wami.resumePlaying();
			se = setInterval(function() {
				second('play');
			}, 1000);
		}
	}

	function stopRecording() {
		recordButton.setEnabled(true);
		Wami.stopRecording();
	}
	function onError(e) {
		alert(e);
	}
	function onPlayStart() {
		se = setInterval(function() {
			second('play');
		}, 1000);
	}
	function onPlayFinish() {
		playButton.setEnabled(true);
		playButton.played = false;
		$("#recorder_box .recorder-tip").html('<span class="success">播放已完成</span>');
		$("#recorder_box").attr("status", "listening");
		clearInterval(se);
		m = s = 0;
		$("#player-time .timer").text('00:00');
	}

	function onRecordStart() {
		$("#recorder_box .recorder-tip").html('点击麦克风 ' + '<span class="important">结束</span>' + ' 录音');
		$("#recorder-status").html('<span class="label label-success">正在录音中...</span>');
		se = setInterval(function() {
			second();
			if (s == 30) {
				if (recordButton.active && recordButton.onstop) {
					clearInterval(se);
					m = s = 0;
					$("#recorder_box .recorder-tip").html('<span class="success">录音处理中,请稍候</span>');
					$("#recorder-status").html('');
					$("#recorder_box").attr("status", "handing");
					recordButton.onstop();
					recordButton.setEnabled(false);
				}
			}
		}, 1000);
		$("#recorder_box").attr("status", "recording");
		recordInterval = setInterval(function() {
			if (recordButton.isActive()) {
				var level = Wami.getRecordingLevel();
				recordButton.setActivity(level);
			}
		}, 200);
	}
	function second(type) {
		s++;
		if (s > 0 && (s % 60) == 0) {
			m += 1;
			s = 0;
		}
		if (type != undefined && type == 'play') {
			$("#player-time .timer").text((m < 10 ? ('0' + m) : m) + ':' + (s < 10 ? ('0' + s) : s));
		} else {
			$("#recorder-time").text((m < 10 ? ('0' + m) : m) + ':' + (s < 10 ? ('0' + s) : s));
		}
	}
	function onRecordFinish() {
		clearInterval(recordInterval);
		// 录音完成的方法
		$("#recorder-area").hide();
		$("#recorder_box .recorder-tip").html('<span class="success">录音已完成</span>');
		$("#recorder_box").attr("status", "listening");
		recordButton.setEnabled(true);
		// 启动播放状态
		$("#player-time .timer").text('00:00');
		$("#player-time .total").text('/' + $("#recorder-time").text());
		$("#recorder-time").text('00:00');
		playButton.setEnabled(true);
		playButton.played = false;
		$("#post-btn").attr({
			"class" : "btn btn-primary pull-right"
		});
		$("#post-btn").html('<i class="icon-ok icon-white"></i> 发送');
		$("#player-area").show();
	}
	function recordButton() {
		var self = this;
		self.guidiv = $("#recorder-btn .guidiv")[0];
		self.meterDiv = $("#recorder-btn .meterDiv")[0];
		self.coverDiv = $("#recorder-btn .coverDiv")[0];
		self.guidiv.onmousedown = mouseHandler;
		self.active = false;
		function background(index) {
			if (index == 1)
				return "-56px 0px";
			if (index == 2)
				return "0px 0px";
			if (index == 3)
				return "-112px 0";
		}
		function meter(index, offset) {
			var top = 5;
			if (offset)
				top += offset;
			if (index == 1)
				return "-169px " + top + "px";
			if (index == 2)
				return "-189px " + top + "px";
			if (index == 3)
				return "-249px " + top + "px";
		}
		function mouseHandler(e) {
			var rightclick;
			if (!e)
				var e = window.event;
			if (e.which)
				rightclick = (e.which == 3);
			else if (e.button)
				rightclick = (e.button == 2);
			if (!rightclick) {
				if (self.active && self.onstop) {
					clearInterval(se);
					m = s = 0;
					$("#recorder_box .recorder-tip").html('<span class="success">录音处理中,请稍候</span>');
					$("#recorder-status").html('');
					$("#recorder_box").attr("status", "handing");
					self.onstop();
					self.setEnabled(false);
				} else if (!self.active && self.onstart) {
					self.active = true;
					self.onstart();
				}
			}
		}
		self.isActive = function() {
			return self.active;
		};
		self.setActivity = function(level) {
			self.guidiv.onmouseout = function() {
			};
			self.guidiv.onmouseover = function() {
			};
			self.guidiv.style.backgroundPosition = background(2);
			self.coverDiv.style.backgroundPosition = meter(1, 5);
			self.meterDiv.style.backgroundPosition = meter(2, 5);
			var totalHeight = 31;
			var maxHeight = 9;
			var height = (maxHeight + totalHeight - Math.floor(level / 100 * totalHeight));
			self.coverDiv.style.height = height + "px";
		};
		self.setEnabled = function(enable) {
			self.active = false;
			if (enable) {
				self.coverDiv.style.backgroundPosition = meter(1);
				self.meterDiv.style.backgroundPosition = meter(1);
				self.guidiv.style.backgroundPosition = background(1);
				self.guidiv.onmousedown = mouseHandler;
				self.guidiv.onmouseover = function() {
					self.guidiv.style.backgroundPosition = background(3);
				};
				self.guidiv.onmouseout = function() {
					self.guidiv.style.backgroundPosition = background(1);
				};
			} else {
				self.coverDiv.style.backgroundPosition = meter(3);
				self.meterDiv.style.backgroundPosition = meter(3);
				self.guidiv.style.backgroundPosition = background(1);
				self.guidiv.onmousedown = null;
				self.guidiv.onmouseout = function() {
				};
				self.guidiv.onmouseover = function() {
				};
			}
		};
	}
	function playButton() {
		var self = this;
		self.guidiv = $("#player-btn .guidiv")[0];
		self.meterDiv = $("#player-btn .meterDiv")[0];
		self.coverDiv = $("#player-btn .coverDiv")[0];
		self.guidiv.onmousedown = mouseHandler;
		self.active = false;
		function background(index) {
			if (index == 1)
				return "-56px 0px";
			if (index == 2)
				return "0px 0px";
			if (index == 3)
				return "-112px 0";
		}
		function meter(index, offset) {
			var top = 5;
			if (offset)
				top += offset;
			if (index == 1)
				return "-269px " + top + "px";
			if (index == 2)
				return "-298px " + top + "px";
			if (index == 3)
				return "-327px " + top + "px";
		}
		function mouseHandler(e) {
			var rightclick;
			if (!e)
				var e = window.event;
			if (e.which)
				rightclick = (e.which == 3);
			else if (e.button)
				rightclick = (e.button == 2);
			if (!rightclick) {
				if (self.active) {
					self.onpause();
				} else if (!self.active) {
					self.onresume();
				}
			}
		}
		self.isActive = function() {
			return self.active;
		};
		self.setActivity = function(level) {
			self.guidiv.onmouseout = function() {
			};
			self.guidiv.onmouseover = function() {
			};
			self.guidiv.style.backgroundPosition = background(2);
			self.coverDiv.style.backgroundPosition = meter(1, 5);
			self.meterDiv.style.backgroundPosition = meter(2, 5);
			var totalHeight = 31;
			var maxHeight = 14;
			var height = (maxHeight + totalHeight - Math.floor(level / 100 * totalHeight));
			self.coverDiv.style.height = height + "px";
		};
		self.setEnabled = function(enable) {
			self.active = false;
			if (enable) {
				self.coverDiv.style.backgroundPosition = meter(1);
				self.meterDiv.style.backgroundPosition = meter(1);
				self.guidiv.style.backgroundPosition = background(1);
				self.guidiv.onmousedown = mouseHandler;
				self.guidiv.onmouseover = function() {
					self.guidiv.style.backgroundPosition = background(3);
				};
				self.guidiv.onmouseout = function() {
					self.guidiv.style.backgroundPosition = background(1);
				};
			} else {
				self.coverDiv.style.backgroundPosition = meter(3);
				self.meterDiv.style.backgroundPosition = meter(3);
				self.guidiv.style.backgroundPosition = background(1);
				self.guidiv.onmousedown = null;
				self.guidiv.onmouseout = function() {
				};
				self.guidiv.onmouseover = function() {
				};
			}
		};
	}
};
function reRecord() {
	Wami.stopPlaying();
	if (se) {
		clearInterval(se);
	}
	m = s = 0;
	$("#player-area").hide();
	$("#recorder_box .recorder-tip").html('点击麦克风 ' + '<span class="success">开始</span>' + ' 录音');
	$("#recorder_box").attr("status", "listening");
	$("#recorder-area").show();
}

function closeRecord() {
	var recorder = $('#recorder_box');
	var status = recorder.attr('status') || '';
	if (status == 'recording' || status == 'handing' || status == 'playing') {
		alert("正在录音中,无法关闭");
		return false;
	}
	recorder.removeClass('visible');
}

function postAudio(obj) {
	var _this = $(obj);
	var audioId = sessionID + '_' + (new Date()).getTime();
	if (_this == undefined || _this.hasClass("disabled") || audioId == undefined) {
		return false;
	}
	_this.attr({
		"class" : "btn disabled pull-right"
	});
	_this.html("发送中");
	$("#recorder_box").attr("status", "posting");
	$('#audio-btn').attr('audioId', audioId);
	Wami.posting('chat/upload/audio?audioId=' + audioId, Wami.nameCallback(onPostStart), Wami.nameCallback(onPostFinish), Wami.nameCallback(onError));
}

function onPostStart() {
	$("#recorder_box .recorder-tip").html('<span class="success">发送中...</span>');
}

function onPostFinish() {
	// 显示已完成界面
	$("#recorder_box .recorder-tip").html('<span class="success">录音已保存</span>');
	$("#recorder_box").attr("status", "listening");
	$("#post-btn").text('已发送');
	// 隐藏录音工具
	$('#recorder_box').removeClass("visible");
	// 删除缓存
	$(".player-btn.on").removeClass("on");
	// 还原界面
	reRecord();
	// 发送录音
	user = user != '' ? user : '匿名用户';
	if (ws != null && user != '') {
		var data = {
			userName : user,
			userAvatar : user_avatar,
			audio : {
				audioId : $('#audio-btn').attr('audioId'),
				audioTime : $("#player-time .total").text().substring(1)
			}
		};
		data = JSON.stringify(data);
		ws.send(BASE64.encoder(data));
		setTimeout(function() {
			showMsg('[msg]:' + data, true);
		}, 0);
		$('#audio-btn').removeAtrr('audioId');
	}
}

function onError(e) {
	alert(e);
}