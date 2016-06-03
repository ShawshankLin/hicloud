<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>首页</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-g am-container am-margin-top-sm">
	<div class="js-masonry am-animation-scale-up" data-masonry-options='{ "itemSelector": ".item", "columnWidth": 20 ,"isAnimated":true}'>
	  <c:forEach items="${shares}" var="item">
	  	<div class="item">
	  	<div class="am-panel am-panel-default" >
		  <div class="am-panel-hd am-text-truncate"><b>${item.file.name }</b></div>
		  <div class="am-panel-bd">
		  		创建日期：<fmt:formatDate value="${item.file.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/> <br> 
				文件类型：${item.file.suffix}<br>
				文件大小：<tags:fileSize fileSize="${item.file.fileSize}"/><br>
				<a href="${ctx}/myfile/preview?fileId=${item.file.id}" class="am-text-sm" target="_black">在线预览 </a>
				<a href="${ctx}/myfile/download?fileId=${item.file.id}" class="am-text-sm">文件下载 </a>
				<a href="javascript:void(0)" onclick="saveDrive('${item.file.id}')" class="am-text-sm">保存至网盘 </a>
		  </div>
		</div>
	  </div>
	  </c:forEach>
	</div>
	  		
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<script type="text/javascript" src="${ctx}/resources/plugins/masonry/masonry.pkgd.min.js"></script>

<script type="text/javascript">
//保存网盘
function saveDrive(fileId){
	$('#my-modal-loading').modal('open');
	  $.ajax({
			type : 'get',
			url : '${ctx}/share/saveMyDrive',
			data : {
				'fileId':fileId
			},
			dataType:'json',
			error : function(request) {
				$('#my-modal-loading').modal('close');
				warning('连接失败');
			},
			success : function(data) {
				$('#my-modal-loading').modal('close');
				try {
					switch(data.status){
					case "success":
						success(data.message);
						break;
					case "error":
						error(data.message);
						break;
					}
				} catch (e) {
					warning('操作失败'+e);
				}
				
			}
		});
}



</script>


</body>

</html>
