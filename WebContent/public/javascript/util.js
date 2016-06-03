//时间格式化
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

$.fn.hasAttr = function(name) {
	return this.attr(name) !== undefined;
};

$.ajaxSetup({
	"error" : function(XMLHttpRequest, textStatus, errorThrown) {
		if (XMLHttpRequest.status == 500) {
			load500();
		} else if (XMLHttpRequest.status == 408) {
			login();
		} else {
			load404();
		}
	}
});

function load404() {
	window.history.replaceState(null, "", window.location.href.replace(/\#.*$/, '#404'));
	var div = '<div style="border: 1px solid #DDD;"><div class="error_area"><div class="error_img"><img src="public/image/404.png"></div><div class="error_info"><br><p><span class="red" style="font-size: 20px;">非常抱歉, 服务器404错误。。。</span></p><br><p>404错误小贴士:</p><br><p><span class="h-box">&nbsp;&nbsp;&nbsp;&nbsp;一般是因为您请求的URL资源在服务器上并不存在,请检查您的URL拼写是否正确。</span></p><br><p><span class="h-box">&nbsp;&nbsp;&nbsp;&nbsp;如果是由于非用户因素而造成的服务器错误，并给您的使用造成不便，敬请谅解。感谢您长期以来对 HitCode 的支持，并请继续关注、支持 HitCode !</span></p></div></div></div>';
	$("#view").html(div);
}

function login() {
	get_login_box(function() {
		$('#re-login-modal').modal({
			backdrop : true,
			keyboard : false,
			show : true
		});
	});
}

function get_login_box(callback) {
	if ($("#loginModal")[0]) {
		if (callback || typeof (callback) == "function") {
			callback();
		}
	} else {
		$.get("login/box").done(function(data) {
			$("body").append(data);
			if (callback || typeof (callback) == "function") {
				callback();
			}
		});
	}
}
// 存cookie
function setCookie(name, value) {
	var exp = new Date();
	exp.setTime(exp.getTime() + 365 * 24 * 60 * 60 * 1000);
	document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";
}
// 取cookies
function getCookie(name) {
	var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
	if (arr != null)
		return unescape(arr[2]);
	return null;
}
// 删除cookie
function delCookie(name) {
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval = getCookie(name);
	if (cval != null)
		document.cookie = name + "=" + escape(cval) + ";expires=" + exp.toGMTString() + ";path=/";
}
// 刷新图片
function reflush_avatar_img(avatar_id) {
	if ($("#header img.avatar")[0]) {
		$("#header img.avatar").attr("src", $("#header img.avatar").attr("src").split('&')[0] + "&r=" + Math.random());
	}
	if (avatar_id != undefined) {
		$('img.big_img').attr('src', 'avatar/' + avatar_id + '?size=big&r=' + Math.random());
		$('img.middle_img').attr('src', 'avatar/' + avatar_id + '?size=middle&r=' + Math.random());
		$('img.small_img').attr('src', 'avatar/' + avatar_id + '?size=small&r=' + Math.random());
	}
}

// 浏览器提示
function check_browser(msg, type) {
	if (getCookie("no_browser_tip") != null) {
		return false;
	}
	if (type == 'ie') {
		$("#browser_tip .tip_msg").html('&nbsp;&nbsp;&nbsp;&nbsp;您当前使用的浏览器为   <strong>IE: ' + msg + '</strong>，本站站长已经对IE系浏览器不报任何希望，如果您想继续正常地浏览，使用本站，强烈建议您下载安装最新版 <a href="http://www.google.cn/intl/zh-CN/chrome/browser/">Chrome</a> 浏览器。');
	} else {
		$("#browser_tip .tip_msg").html('&nbsp;&nbsp;&nbsp;&nbsp;您当前使用的浏览器为  <strong>' + msg + '</strong> ，网站中极有可能存在一些优秀的功能(如 WebRTC )将不被此浏览器支持，如果您想继续正常地浏览，使用本站，强烈建议您下载安装最新版 <a href="http://www.google.cn/intl/zh-CN/chrome/browser/">Chrome</a> 浏览器。');
	}
	$('#browser_tip').modal({
		backdrop : true,
		keyboard : false,
		show : true
	});
}

// 不再显示浏览器提示
function no_browser_tip() {
	if ($("#browser_tip_cookie").attr("checked") == "checked") {
		setCookie("no_browser_tip", "true");
	}
}

// 计算中文字数
function zh_count(str) {
	if (str == undefined || str == '') {
		return 0;
	}
	var pattern = /[\u4e00-\u9fa5]/g;
	if (pattern.test(str)) {
		return str.match(pattern).length;
	}
	return 0;
}

$(document).ready(function() {
	var ua = navigator.userAgent.toLowerCase();
	if ($.browser.msie) {
		check_browser(ua.match(/msie ([\d.]+)/)[1], "ie");
	} else if ($.browser.opera) {
		check_browser("Opera: " + ua.match(/opera.([\d.]+)/)[1]);
	} else if ($.browser.mozilla) {
		if ($.browser.version == '11.0') {
			check_browser("IE: 11");
		} else {
			check_browser("Firefox: " + ua.match(/firefox\/([\d.]+)/)[1]);
		}
	} else if ($.browser.safari) {
		check_browser("Safari: " + ua.match(/version\/([\d.]+)/)[1]);
	} else if ($.browser.chrome) {
		if (!window.webkitRTCPeerConnection) {
			check_browser("Chromium内核: " + ua.match(/chrome\/([\d.]+)/)[1]);
		}
	}
	$("#back-top").hide();
	$(function() {
		$(window).scroll(function() {
			if ($(this).scrollTop() > 100) {
				$('#back-top').fadeIn();
			} else {
				$('#back-top').fadeOut();
			}
		});
		$('#back-top span.top').click(function() {
			$('body,html').animate({
				scrollTop : 0
			}, 800);
			return false;
		});
	});
});