<%@ page language="java" pageEncoding="UTF-8"%>

<script type="text/javascript" src="public/lib/province_city/province_city.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$(".section-content textarea").focus(function() {
			$(this).parent().addClass("focus");
		}).blur(function() {
			$(this).parent().removeClass("focus");
		}).on('input', (function() {
			var str = $(".section-content textarea").val();
			var num = Math.ceil((zh_count(str) + str.length) / 2);
			var _parent = $(this).parent();
			if (num < 71) {
				_parent.find("span").html("<i>" + num + "</i>/70");
				_parent.removeClass("error");
			} else {
				_parent.find("span").html("<i style='color:red;'>" + num + "</i>/70");
				_parent.addClass("error");
			}
		}));
		// 兼容ie浏览器
		$(".section-content textarea").each(function() {
			var _this = this;
			if (this.attachEvent) {
				this.attachEvent('onpropertychange', function(e) {
					if (e.propertyName != 'value') {
						return;
					}
					$(_this).trigger('input');
				});
			}
		});
	});
</script>
<div id="profile_area">
	<h1>资料设置</h1>
	<input type="submit" value="保存" class="save_info btn btn-primary pull-right" style="margin-top: -36px; margin-right: 6px;">
	<div class="dotted_line"></div>
	<div class="section-content">
		<form class="form-horizontal" style="margin-top: 20px;">
			<fieldset>
				<div class="control-group">
					<label class="control-label" for="username">常用昵称</label>
					<div class="controls">
						<input style="width: 120px; padding-left: 16px;" class="input-xlarge" id="username" type="text" value="${sGlobal.username}" disabled="disabled" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="username">恋爱状态</label>
					<div class="controls">
						<div class="default_select">
							<div class="select_text">
								<span>单身</span>
							</div>
							<div class="select_menu">
								<ul>
									<li class="select"><a>单身</a></li>
									<li><a>热恋</a></li>
									<li><a>订婚</a></li>
									<li><a>已婚</a></li>
									<li><a>暗恋中</a></li>
									<li><a>暧昧中</a></li>
									<li><a>求交往</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="username">所在行业</label>
					<div class="controls">
						<div class="default_select">
							<div class="select_text">
								<span>创业</span>
							</div>
							<div class="select_menu">
								<ul>
									<li class="select"><a>创业</a></li>
									<li><a>程序</a></li>
									<li><a>设计</a></li>
									<li><a>产品</a></li>
									<li><a>运营</a></li>
									<li><a>文化</a></li>
									<li><a>媒体</a></li>
									<li><a>金融</a></li>
									<li><a>教育</a></li>
									<li><a>商务</a></li>
									<li><a>管理</a></li>
									<li><a>公关</a></li>
									<li><a>科研</a></li>
									<li><a>生物</a></li>
									<li><a>健康</a></li>
									<li><a>法律</a></li>
									<li><a>政府</a></li>
									<li><a>建筑</a></li>
									<li><a>制造</a></li>
									<li><a>学生</a></li>
									<li><a>其他</a></li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="location">现居城市</label>
					<div class="controls">
						<div style="width: 348px;">
							<div id="province_select_area" class="default_select pull-left">
								<div class="select_text">
									<span>选择省份</span>
								</div>
								<div id="province_select" class="select_menu">
									<script type="text/javascript">
										PCA.showprovince('province', 'city', '湖北', '武汉');
									</script>
								</div>
							</div>
							<div id="city_select_area" class="default_select pull-right">
								<div class="select_text">
									<span>选择城市</span>
								</div>
								<div id="city_select" class="select_menu"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="textarea">个性签名</label>
					<div class="controls">
						<div class="text">
							<textarea class="input-xlarge" id="sign" rows="4"></textarea>
							<div style="text-align: right; width: 100%">
								<span><i>0</i>/70</span>
							</div>
						</div>
					</div>
				</div>
			</fieldset>
		</form>
	</div>
</div>
<script type="text/javascript">
	$(".default_select .select_text").click(function(e) {
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			window.event.cancelBubble = true;
		}
		var _this_menu = $(this).parent().find(".select_menu");
		if (_this_menu.is(":visible")) {
			$(".default_select .select_menu").hide();
		} else {
			$(".default_select .select_menu").hide();
			$(this).parent().find(".select_menu").toggle();
		}
	});
	$("body").click(function() {
		$(".default_select .select_menu").hide();
	});
</script>
