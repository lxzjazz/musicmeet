$(document).ready(function() {
	var user = getCookie("user_email");
	if (user != null) {
		$("#email").val(user);
		$("#cookiesaved").attr("checked", "checked");
	}
	// 提交表单
	$("#form-login").submit(function() {
		if ($("#submit").hasClass("disabled")) {
			return false;
		}
		$("#submit").button('loading');
		$.post('login', {
			email : $("#email").val(),
			password : $("#password").val(),
			cookieSaved : $("#cookiesaved").attr("checked") == "checked"
		}, function(data) {
			if (data.error) {
				$(".submit-result").attr("class", "label label-important submit-result");
				$(".submit-result").html(data.error);
				$(".submit-result").css("visibility", "visible");
				$("#submit").button('reset');
			} else {
				if ($("#cookiesaved").attr("checked") == "checked") {
					setCookie("user_email", $("#email").val());
				} else {
					delCookie("user_email");
				}
				window.location.href = "";
			}
		}, 'json');
		return false;
	});
});