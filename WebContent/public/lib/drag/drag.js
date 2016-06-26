function moveObj(obj) {
	$(obj).find(".locked").css("cursor", "move");
	$(obj).find(".locked").mousedown(function(e) {
		var top = obj.offsetTop;
		var left = obj.offsetLeft;
		var drag_x = document.all ? event.clientX : e.pageX;
		var drag_y = document.all ? event.clientY : e.pageY;
		var selection = disableSelection(document.body);
		var move = function(e) {
			obj.style.left = left + (document.all ? event.clientX : e.pageX) - drag_x + "px";
			obj.style.top = top + (document.all ? event.clientY : e.pageY) - drag_y + "px";
			if (parseInt(obj.style.left) < 0) {
				obj.style.left = 0 + "px";
			}
			if (parseInt(obj.style.left) > $(window).width() - $(obj).width()) {
				obj.style.left = $(window).width() - $(obj).width() + "px";
			}
			if (parseInt(obj.style.top) > $(window).height() - $(obj).height()) {
				obj.style.top = $(window).height() - $(obj).height() + "px";
			}
			if (parseInt(obj.style.top) < 51) {
				obj.style.top = 51 + "px";
			}
		};
		var stop = function(e) {
			removeEvent(document, "mousemove", move);
			removeEvent(document, "mouseup", stop);
			if (selection) {
				selection();
				delete selection;
			}
		};
		addEvent(document, "mousemove", move);
		addEvent(document, "mouseup", stop);
		selection();
	});
};

function addEvent(target, type, listener, capture) {
	return eventHandler(target, type, listener, true, capture);
};

function removeEvent(target, type, listener, capture) {
	return eventHandler(target, type, listener, false, capture);
};

function eventHandler(target, type, listener, add, capture) {
	type = type.substring(0, 2) === "on" ? type.substring(2) : type;
	if (document.all) {
		add ? target.attachEvent("on" + type, listener) : target.detachEvent("on" + type, listener);
	} else {
		capture = (typeof capture === "undefined" ? true : (capture === "false" ? false : ((capture === "true") ? true : (capture ? true : false))));
		add ? target.addEventListener(type, listener, capture) : target.removeEventListener(type, listener, capture);
	}
};

function disableSelection(target) {
	var disable = true;
	var oCursor = document.all ? target.currentStyle["cursor"] : window.getComputedStyle(target, null).getPropertyValue("cursor");
	var returnFalse = function(e) {
		e.stopPropagation();
		e.preventDefault();
		return false;
	};
	var returnFalseIE = function() {
		return false;
	};
	var selection = function() {
		if (document.all) {
			disable ? addEvent(target, "selectstart", returnFalseIE) : removeEvent(target, "selectstart", returnFalseIE);
		} else {
			disable ? addEvent(target, "mousedown", returnFalse, false) : removeEvent(target, "mousedown", returnFalse, false);
		}
		target.style.cursor = disable ? "default" : oCursor;
		disable = !disable;
	};
	return selection;
};