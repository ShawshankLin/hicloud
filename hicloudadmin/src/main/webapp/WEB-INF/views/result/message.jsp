<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<jsp:directive.include file="/WEB-INF/views/includes/top.jsp" />
<head>
<title>提示信息</title>
<script type="text/javascript">
	back = function(befpage)
	{
		<c:if test="${target==null}">
		history.back();
		</c:if>
		<c:if test="${target!=null}">
		<c:choose> 
		 <c:when test="${empty searparam}">
		 	location.href='${basePath }${target}';
		 </c:when>
		 <c:otherwise>
		 	location.href='${basePath }${target}?searparam=${searparam}';
		 </c:otherwise>
		</c:choose>
		</c:if>
	}
	</script>
</head>
<body>
	<div class="pd-20">
		<div style="background-color: #f5fafe; line-height: 45px;">
			<i class="Hui-iconfont" style="margin-left: 40%;">&#xe647;</i><%=request.getAttribute("message")%>
		</div>
	</div>
</body>
<script type="text/javascript" src="${basePath }lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }lib/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="${basePath }lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="${basePath }lib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${basePath }js/H-ui.js"></script>
<script type="text/javascript" src="${basePath }js/H-ui.admin.js"></script>
</html>
