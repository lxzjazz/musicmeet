<%@ page language="java" pageEncoding="UTF-8"%>

<div class="modal hide fade" id="browser_tip">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3>温馨提示</h3>
	</div>
	<div class="modal-body">
		<p class="tip_msg"></p>
	</div>
	<div class="modal-footer">
		<label class="checkbox pull-left"> <input id="browser_tip_cookie" name="browser_tip_cookie" type="checkbox">下次不再提醒我
		</label> <a class="btn btn-primary" data-dismiss="modal" onclick="no_browser_tip()">我知道了</a>
	</div>
	<footer style="text-align: center; border-top: 1px solid #eeebe7; padding: 30px 0; margin-top: 30px; color: #777;">
		<span>Copyright ? 2016 MusicMeet&nbsp;|&nbsp;</span><a href="http://www.miitbeian.gov.cn" style="color: #777;" target="_blank">粤ICP备16016217号-1</a>
	</footer>
</div>
</body>
</html>