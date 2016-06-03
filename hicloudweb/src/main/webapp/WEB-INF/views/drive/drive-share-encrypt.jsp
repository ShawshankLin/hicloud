<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>hicloud云盘分享</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
</head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>
<div class="am-g">
  <div class="am-u-lg-5 am-u-md-7 am-u-sm-centered" style="margin-top:100px">	
    <div class="am-panel am-panel-default">
    	<div class="am-panel-hd">${share.createName } 给您分享了加密文件。
    		<form method="post" class="am-form" action="${ctx }/share/${share.id}" onsubmit="return checkForm()">
		      <br>
		      <input type="text" name="extractionCode" id="extractionCode" placeholder="请输入提取码" >
		      <br>
			  <button type="submit" class="am-btn am-btn-primary am-fr">提取文件</button>
			  <hr>
		    </form>  
    	</div>
	</div>
  </div>
</div>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<script type="text/javascript">
function checkForm(){
	if($('#extractionCode').val()==''){
		warning("提取码不允许为空");
		return false;
	}
	return true;
}
</script>		
</body>
</html>
