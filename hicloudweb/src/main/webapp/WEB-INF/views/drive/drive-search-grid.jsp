<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>我的云端硬盘</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
   <%@ include file="/WEB-INF/views/includes/link.jsp" %>
   <link rel="stylesheet" type="text/css" href="${ctx}/resources/styles/grid.css">
   <link rel="stylesheet" href="${ctx}/resources/plugins/contextjs/css/context.standalone.css"> 
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/includes/sidebar.jsp" %>

  <!-- content start -->
  <div class="admin-content">
    <%@ include file="/WEB-INF/views/drive/drive-search-toolbar.jsp" %>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-0">
     	  	<%@ include file="/WEB-INF/views/drive/drive-search-navbar.jsp" %>
			<div  class="grid" id="grid">
				<ul data-am-widget="gallery" class="am-gallery am-avg-sm-8 am-gallery-imgbordered" data-am-gallery="{pureview:{target: '.img a'}}">
				<c:forEach items="${requestScope.files }" var="item" varStatus="status">					
			        	<c:choose>
          					<c:when test="${item.isDir==1}"><!-- 文件夹 -->
          						<li id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}",link:"${ctx}/myfile/download?fileId=${item.id}",path:"${ctx}/myfile?path=${fn:trim(path)}/${item.fileName }"}' class="fileDiv" data-am-popover="{content: '${item.name } <br/> 创建时间：<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>', trigger: 'hover focus'}">
          							<div class="img" style="background: url(${ctx}/resources/images/drive_80x80/folder_80x80.png) no-repeat;"></div>
          							<span class="filename am-text-truncate">
          								<a href="${ctx}/myfile?path=${fn:trim(path)}/${item.fileName }">${item.fileName }</a>
          							</span>
          						</li>
          					</c:when>
          					<c:otherwise><!-- 文件 -->
          						<li id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}",link:"${ctx}/myfile/download?fileId=${item.id}"}' class="fileDiv" data-am-popover="{content: '${item.name }.${item.suffix } <br/> 创建时间：<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>', trigger: 'hover focus'}">
          							<c:choose>
          								<c:when test="${fileUtil.getFileTypeBySuffix(item.suffix)=='img'}">
		          							<div class="img">
		          							  <a href="${ctx}/dynimage/download?location=${item.ref}" title="${item.name }.${item.suffix}">
										      	<img src="${ctx}/resources/images/drive_80x80/${item.suffix}_80x80.png" onerror="this.src='${ctx}/resources/images/drive_30x30/file_80x80.png'" data-rel="${ctx}/dynimage/download?location=${item.ref}"/>
										      </a>
										    </div>
          								</c:when>
          								<c:otherwise>
          									<div class="img" style="background: url(${ctx}/resources/images/drive_80x80/${item.suffix}_80x80.png) no-repeat;"></div>
          								</c:otherwise>
          							</c:choose>
          							<span class="filename am-text-truncate">
          								<a href="javascript:void(0)" onclick="openFile('${item.id}','${item.isDir }','${fileUtil.getFileTypeBySuffix(item.suffix)}')">${item.name}.${item.suffix }</a>
          							</span>
          						</li>
          					</c:otherwise>
          				</c:choose>			        	
				</c:forEach>
			    </ul>			    
			</div>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<%@ include file="/WEB-INF/views/drive/drive-search-deal.jsp" %>

<script type="text/javascript">
$(document).ready(function(){
	
	//添加右键菜单
	$('.grid ul li').click(function(){
    	var file=$(this).attr("file-info");
		file=eval('(' + file + ')');
		showToolBar('search-toolbar');
	    $(this).addClass("seled").siblings().removeClass("seled");
		$('#fileId').val(file.id);
	}).dblclick(function(){
		var file=$(this).attr("file-info");
		file=eval('(' + file + ')');
		if(file.isDir==1){
			openFile(file.id,file.isDir,file.type,file.link,file.path);
		}else{
			openFile(file.id,file.isDir,file.type);
		}
	});
	

	
});


</script>
</body>
</html>
