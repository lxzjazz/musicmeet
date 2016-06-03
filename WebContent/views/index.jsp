<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:if test="${empty sGlobal}">
	<jsp:include page="../header.jsp"></jsp:include>
</c:if>
<script type="text/javascript">
	$("#header .nav li.active").removeClass("active");
	$("#header .nav").find("li[name='index']").addClass("active");
</script>
<p style="width: 100%; text-align: center;">
	<img src="public/image/banner.png" style="display: inline-block; margin: 140px auto;">
</p>
<c:if test="${empty sGlobal}">
	<jsp:include page="../footer.jsp"></jsp:include>
</c:if>