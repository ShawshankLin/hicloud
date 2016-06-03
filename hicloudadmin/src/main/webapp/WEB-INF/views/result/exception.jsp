<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.include file="../includes/top.jsp" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String errMsg = (String) request.getAttribute("exp");
	if (errMsg == null)
		errMsg = "未知错误";
	else
		errMsg = errMsg.replace("\r", "\\r").replace("\n", "\\n")
				.replace("\"", "\\\"");
%>
<html xmlns="http://www.w3.org/1999/xhtml" style="overflow-y: hidden">
<head>
<title>错误提示页面</title>
<link rel="shortcut icon" href="${basePath }images/favicon.ico">
</head>
<body bgcolor="#fffff"></body>
<script type="text/javascript" src="${basePath }lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }lib/layer/1.9.3/layer.js"></script>
<script type="text/javascript" src="${basePath }lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript" src="${basePath }lib/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${basePath }js/H-ui.js"></script>
<script type="text/javascript" src="${basePath }js/H-ui.admin.js"></script>
<script type="text/javascript">
	var url = '${responseURL}';
	var message = "<%=errMsg%>";
	layer.msg(message, {
		icon : 2,
		time : 1000
	}, function() {
		if (url == '') {
			history.back();
		} else {
			document.location.href = '${basePath}' + url;
		}
	});
</script>
</html>