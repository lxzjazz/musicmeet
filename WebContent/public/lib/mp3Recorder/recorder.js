Wami.Setup = function(options) {
	options = options || {};
	Wami.swfobject = Wami.swfobject || swfobject;
	// flash版本
	var version = '10.0.0';
	// flash参数
	var flashVars = {
		visible : false,
		console : true,
		recordMp3 : true,
		loadedCallback : Wami.nameCallback(delegateWamiAPI),
		onError : Wami.nameCallback(onError)
	};
	// 其他参数
	var params = {
		allowScriptAccess : "always",
		wmode : "transparent"
	};

	// 安装flash
	$("#recorder")[0].innerHtml = 'WAMI requires Flash " + version + " or greater<br />https://get.adobe.com/flashplayer/';
	Wami.swfobject.embedSWF("public/lib/mp3Recorder/Wami.swf", "recorder", 214, 138, version, null, flashVars, params);
	Wami.swfobject.createCSS("#recorder", "outline:none");

	// 界面事件绑定
	Wami.GUI();

	// 错误的回调方法
	function onError() {
		if (options.onError) {
			options.onError();
		}
	}

	// 事件绑定
	function delegateWamiAPI() {
		var recorder = $("#recorder")[0];
		function delegate(name) {
			Wami[name] = function() {
				return recorder[name].apply(recorder, arguments);
			};
		}
		delegate('startRecording');
		delegate('stopRecording');
		delegate('startPlaying');
		delegate('pausePlaying');
		delegate('resumePlaying');
		delegate('stopPlaying');
		delegate('posting');
		delegate('startListening');
		delegate('stopListening');
		delegate('getRecordingLevel');
		delegate('getSettings');
		delegate('showSecurity');
		Wami.show = function() {
			$(recorder).css('visibility', 'visible');
		};
		Wami.hide = function() {
			$(recorder).css('visibility', 'hidden');
		};
		// 检查权限,如果没有权限,就提示用户需要获取权限
		Wami.checkSecurity = function(callback) {
			var settings = Wami.getSettings();
			// 已经获取权限
			if (settings.microphone.granted) {
				if (callback && typeof (callback) == "function") {
					callback();
				}
			} else {
				// 显示用户设置面板
				Wami.showSecurity("privacy", "Wami.show", Wami.nameCallback(checkSecurityCallBack), Wami.nameCallback(onError));
			}
		};
		if (options.onReady) {
			options.onReady();
		}
	}

	// 用户设置是否可以使用麦克风的回调方法
	function checkSecurityCallBack() {
		var settings = Wami.getSettings();
		Wami.hide();
		if (settings.microphone.granted) {
			// 已经获取权限,可以显示录音界面
			$("#recorder_box").addClass("visible");
		}
	}
};
// 创建一个随机数
Wami.createID = function() {
	return "wid" + ("" + 1e10).replace(/[018]/g, function(a) {
		return (a ^ Math.random() * 16 >> a / 4).toString(16);
	});
};
// 每次回调都生成一个新的方法
Wami.nameCallback = function(cb, cleanup) {
	Wami._callbacks = Wami._callbacks || {};
	var id = Wami.createID();
	Wami._callbacks[id] = function() {
		if (cleanup) {
			Wami._callbacks[id] = null;
		}
		cb.apply(null, arguments);
	};
	return "Wami._callbacks['" + id + "']";
};