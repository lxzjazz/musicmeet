$(document).ready(function() {
	if ($.browser.msie || $.browser.version == '11.0') {
		$(window).bind("hashchange", URLRoute.route);
	} else {
		$(window).bind("popstate", URLRoute.route);
	}
	URLRoute.route();
});

var URLRoute = {
	route : function() {
		$("#header .nav li.active").removeClass("active");
		var _hash = window.location.hash;
		var _url = _hash.replace(/[\?]+.*/, '').match(/[\w]+/gi);
		if (_url != null) {
			switch (_url.length) {
			case 1:
				URLRoute.load_view(_url[0], URLRoute.get_url_params(_hash));
				break;
			case 2:
				URLRoute.load_view(_url[0], URLRoute.get_url_params(_hash), _url[1]);
				break;
			default:
				URLRoute.gotoIndex();
				break;
			}
		} else {
			URLRoute.gotoIndex();
		}
	},
	gotoIndex : function() {
		var _index = '#index';
		window.location.href = window.location.href.split("#")[0] + _index;
	},
	load_view : function(view, args, page) {
		$.post(view, args).done(function(data) {
			$("#view").html(data);
			if (page) {
				URLRoute.load_page(view, view, args, page);
			}
		});
	},
	load_page : function(view_url, view, args, page) {
		$(".sidenav li").removeClass("active");
		$("#" + view + "-" + page + "_li").addClass("active");
		$.post(view_url + '/' + page, args).done(function(data) {
			$("#page").html(data);
			if ($("#" + page)[0]) {
				angular.bootstrap($("#page")[0], [ page ]);
			}
		});
	},
	get_url_params : function(url) {
		var params = {};
		url.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(str, key, value) {
			params[key] = value;
		});
		return params;
	}
};

function get_url(url) {
	window.location.hash = "#" + url;
};

function post_url(str, force_refresh) {
	var _hash = "#" + str;
	var _url = _hash.replace(/[\?]+.*/, '').match(/[\w]+/gi);
	if (_url != null && _url.length == 2) {
		if ($.browser.msie) {
			URLRoute.get_url(str);
		} else {
			if (_hash != window.location.hash || force_refresh) {
				window.history.pushState(null, "", window.location.href.split("#")[0] + _hash);
				var view_url = _url[0];
				URLRoute.load_page(view_url, _url[0], URLRoute.get_url_params(_hash), _url[1]);
			}
		}
	}
}
