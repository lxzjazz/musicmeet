var tip_msg = {
	username : "<div style='width:220px;'>允许由汉字,字母,数字以及下划线组成,字符长度限制为4-20个,必须以汉字或字母开头(汉字相当于2个字符)</div>",
	email : "<div style='width:220px;'>本邮箱将作为本网站的登录帐号,绑定邮箱后可以快速修改和找回密码,请认真填写并牢记</div>",
	password1 : "<div style='width:220px;'>允许由6-20个任意字符组成(为了您的帐号安全,请尽量混合使用字母大小写,字符和数字)</div>"
};
var China_Citys = [ "bj_北京", "tj_天津", "hrb_哈尔滨", "wlmq_乌鲁木齐", "xn_西宁", "lz_兰州", "yc_银川", "hhht_呼和浩特", "sjz_石家庄", "ty_太原", "sy_沈阳", "cc_长春", "jl_济南", "ls_拉萨", "cd_成都", "km_昆明", "xa_西安", "zz_郑州", "cq_重庆", "wh_武汉", "cs_长沙", "gy_贵阳", "nj_南京", "hf_合肥", "sh_上海", "hz_杭州", "nc_南昌", "fz_福州", "gz_广州", "sm_厦门", "qd_青岛", "dl_大连", "wx_无锡", "gl_桂林", "nn_南宁", "hk_海口", "xg_香港", "am_澳门", "tw_台湾", "dyd_钓鱼岛" ];
var add_p1_error = function() {
	$("#password1").addClass("error");
	$(".section-content .password1-tip").css("visibility", "visible");
};
var add_p2_error = function() {
	$("#password2").addClass("error");
	$(".section-content .password2-tip").css("visibility", "visible");
};
var rem_p1_error = function() {
	$("#password1").removeClass("error");
	$(".section-content .password1-tip").css("visibility", "hidden");
};
var rem_p2_error = function() {
	$("#password2").removeClass("error");
	$(".section-content .password2-tip").css("visibility", "hidden");
};
var add_e_error = function() {
	$(".section-content .email-tip").css({
		display : "none",
		visibility : "hidden"
	});
	$('<p class="help-block email-error">邮箱格式不正确</p>').insertBefore(".section-content .email-tip");
};
var rem_e_error = function() {
	$("#email").removeClass("my-invalid").removeClass("error");
	$(".section-content .email-error").remove();
	$(".section-content .email-tip").css({
		display : "block",
		visibility : "hidden"
	});
};
var add_u_error = function() {
	$(".section-content .username-tip").css({
		display : "none",
		visibility : "hidden"
	});
	$('<p class="help-block username-error">昵称格式不正确</p>').insertBefore(".section-content .username-tip");
};
var rem_u_error = function() {
	$("#username").removeClass("my-invalid").removeClass("error");
	$(".section-content .username-error").remove();
	$(".section-content .username-tip").css({
		display : "block",
		visibility : "hidden"
	});
};
var isPass = function(input) {
	if ($(input).val() == "" || $(input).hasClass("ng-invalid") || $(input).hasClass("error") || $(input).hasClass("my-invalid") || !$(input).hasAttr("blured")) {
		return false;
	}
	return true;
};
var activateRegBtn = function() {
	var success = true;
	$("#register-form input").each(function(i, e) {
		if ($(".text").hasClass("error") || !$("#username").hasAttr("tested") || (!$(e).attr("autocomplete") && $(e).attr("type") != "submit" && $(e).attr("id") != "code" && !isPass(e))) {
			success = false;
		}
	});
	var submitBtn = $("#register-form input[type='submit']");
	if (success) {
		submitBtn.removeAttr("disabled");
		submitBtn.attr("class", "btn btn-large btn-primary");
		return true;
	}
	submitBtn.attr("class", "btn btn-large disabled");
	submitBtn.attr("disabled", "disabled");
	return false;
};
var get_avatarModal = function() {
	if ($('#avatarModal .modal-body')[0]) {
		$('#avatarModal').modal('show');
	} else {
		$.get('register/avatar_modal').done(function(data) {
			$('#avatarModal').html(data);
			$('#avatarModal').modal({
				backdrop : "static",
				keyboard : true,
				show : true
			});
		});
	}
};
$(document).ready(function() {
	// 初始化tip提示插件
	for ( var i in tip_msg) {
		$("#" + i).tips({
			showOn : "focus",
			content : tip_msg[i],
			alignY : "center",
			alignX : "right",
			offsetX : 10,
			offsetY : 18,
			style : "blue",
			bubble : false
		});
	}
	// 初始化select插件
	for ( var i in China_Citys) {
		$("<option></option>").val(China_Citys[i].split('_')[0]).text(China_Citys[i].split('_')[1]).appendTo($(".chzn-select"));
	}
	$(".chzn-select").chosen();
});
// angular初始化
var app = angular.module('register-form', []);
// 昵称检测
var Username_REGEXP = /^([\u4e00-\u9fa5a-zA-Z][\u4e00-\u9fa5\w]{1,19})$/;
app.directive('username', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if ($("#username").hasAttr("blured")) {
					$("#username").removeAttr("tested");
					rem_u_error();
					var str = $("#username").val();
					if (!Username_REGEXP.test(viewValue) || str.length + zh_count(str) > 20 || str.length + zh_count(str) < 4) {
						$("#username").attr("tested", "");
						ctrl.$setValidity('username', false);
						add_u_error();
						activateRegBtn();
						return viewValue;
					}
				}
				ctrl.$setValidity('username', true);
				activateRegBtn();
				return viewValue;
			});
		}
	};
});
function checkUsername(input) {
	var username = input.value;
	if (username != "") {
		if (!$(input).hasAttr("blured") && (!Username_REGEXP.test(username) || username.length + zh_count(username) > 20 || username.length + zh_count(username) < 4)) {
			$(input).addClass("error");
			add_u_error();
			activateRegBtn();
		} else if (!$(input).hasAttr("tested")) {
			$.post("register/check/username", {
				username : username
			}, function(data) {
				if (data.error) {
					$(input).addClass("error");
					$(".section-content .username-tip").html(data.error);
					$(".section-content .username-tip").css("visibility", "visible");
				} else if (data.existed) {
					$(input).addClass("my-invalid");
					$(".section-content .username-tip").css("visibility", "visible");
				}
				$(input).attr("tested", "");
				activateRegBtn();
			}, "json");
		}
		$(input).attr("blured", "");
	}
}
// 邮箱检测
var Email_REGEXP = /^[_a-zA-Z\d\-\.]+@[_a-zA-Z\d\-]+(\.[_a-zA-Z\d\-]+)+$/;
app.directive('email', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if ($("#email").hasAttr("blured")) {
					$("#email").removeAttr("tested");
					rem_e_error();
					if (!Email_REGEXP.test(viewValue)) {
						$("#email").attr("tested", "");
						ctrl.$setValidity('email', false);
						add_e_error();
						activateRegBtn();
						return viewValue;
					}
				}
				ctrl.$setValidity('email', true);
				activateRegBtn();
				return viewValue;
			});
		}
	};
});
function checkEmail(input) {
	var email = input.value;
	if (email != "") {
		if (!$(input).hasAttr("blured") && !Email_REGEXP.test(email)) {
			$(input).addClass("error");
			add_e_error();
			activateRegBtn();
		} else if (!$(input).hasAttr("tested")) {
			$.post("register/check/email", {
				email : email
			}, function(data) {
				if (data.error) {
					$(input).addClass("error");
					$(".section-content .email-tip").html(data.error);
					$(".section-content .email-tip").css("visibility", "visible");
				} else if (data.existed) {
					$(input).addClass("my-invalid");
					$(".section-content .email-tip").css("visibility", "visible");
				}
				$(input).attr("tested", "");
				activateRegBtn();
			}, "json");
		}
		$(input).attr("blured", "");
	}
}
// 密码框1检测
app.directive('password1', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				rem_p1_error();
				rem_p2_error();
				if ($("#password1").hasAttr("blured") || $("#password2").hasAttr("blured")) {
					var p1 = $("#password1").val();
					if (p1.length < 6) {
						add_p1_error();
						activateRegBtn();
						return viewValue;
					}
					var p2 = $("#password2").val();
					if ($("#password2").hasAttr("blured") && p1 != p2) {
						add_p2_error();
					}
				}
				activateRegBtn();
				return viewValue;
			});
		}
	};
});
function checkPW1(input) {
	var p1 = input.value;
	if (p1 != "" && !$(input).hasAttr("blured")) {
		if (p1.length < 6) {
			add_p1_error();
		}
		$(input).attr("blured", "");
	}
	activateRegBtn();
}
// 密码框2检测
app.directive('password2', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				if (!$("#password1").hasClass("error") && $("#password1").hasAttr("blured")) {
					var p1 = $("#password1").val();
					var p2 = $("#password2").val();
					if ($("#password2").hasAttr("blured")) {
						if (p1 == p2) {
							rem_p2_error();
						} else {
							add_p2_error();
						}
					} else {
						if (!(p1.length < p2.length) && p1.substring(0, p2.length) == p2) {
							rem_p2_error();
							if (p1 == p2 && !$("#password2").hasAttr("blured")) {
								$("#password2").attr("blured", "");
							}
						} else {
							add_p2_error();
						}
					}
				}
				activateRegBtn();
				return viewValue;
			});
		}
	};
});
function checkPW2(input) {
	var p1 = $("#password1").val();
	var p2 = input.value;
	if (p2 != "" && !$(input).hasAttr("blured")) {
		if (p1 != p2) {
			add_p2_error();
		}
		$(input).attr("blured", "");
	}
	activateRegBtn();
}
// 点击注册按钮
function reg_submit(obj) {
	if (!activateRegBtn() || $(obj).hasClass("disabled")) {
		return false;
	}
	$(".submit-result").css("visibility", "hidden");
	$(obj).button('loading');
	$.post('register', {
		email : $("#email").val(),
		password : $("#password1").val(),
		username : $("#username").val(),
		code : $("#code").val()
	}, function(data) {
		if (data.error) {
			$(".submit-result").attr("class", "label label-important submit-result");
			$(".submit-result").html(data.error);
			$(".submit-result").css("visibility", "visible");
			$(obj).button('reset');
		} else {
			setCookie("user_email", $("#email").val());
			window.location.href = data.url;
		}
	}, 'json');
}