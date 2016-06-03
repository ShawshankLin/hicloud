<%@page contentType="text/html" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>hicloud群共享</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
  <style type="text/css">
  	@media only screen and (min-width: 1200px) {
      .blog-g-fixed {
        max-width: 1200px;
      }
    }

    @media only screen and (min-width: 641px) {
      .blog-sidebar {
        font-size: 1.4rem;
      }
    }

    .blog-main {
      padding: 20px 0;
    }

    .blog-title {
      margin: 10px 0 20px 0;
    }

    .blog-meta {
      font-size: 14px;
      margin: 10px 0 20px 0;
      color: #222;
    }

    .blog-meta a {
      color: #27ae60;
    }

    .blog-pagination a {
      font-size: 1.4rem;
    }

    .blog-team li {
      padding: 4px;
    }

    .blog-team img {
      margin-bottom: 0;
    }

    .blog-content img,
    .blog-team img {
      max-width: 100%;
      height: auto;
    }

    .blog-footer {
      padding: 10px 0;
      text-align: center;
    }
  
  </style>
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-g">
  <div class="am-u-md-9">
  <!-- content start -->
  <div class="admin-content">
    <%@ include file="/WEB-INF/views/group/group-info-toolbar.jsp" %>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-0">
     	  <%@ include file="/WEB-INF/views/group/group-info-navbar.jsp" %>
     	  <div class="am-margin-left-sm am-margin-right-sm">
     	  	<table class="am-table am-table-striped am-table-hover table-main" id="data_table">
            <thead>
              <tr>
				<th class="table-check" ><input type="checkbox" onclick="checkAllBox(this)"/></th>
				<th class="table-title">文件名</th>
				<th class="table-date am-hide-sm-only">大小</th>
				<th class="table-date am-hide-sm-only">分享者</th>
				<th class="table-date am-hide-sm-only">日期</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${requestScope.groupFiles}" var="item" varStatus="status">
           		<c:choose>
   					<c:when test="${item.isDir==1}">
   						<tr id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",link:"${ctx}/group/downloadFile?groupId=${group.id}&fileId=${item.id}",path:"${ctx}/group/content?groupId=${group.id}&path=${fn:trim(path)}/${item.fileName }&currentPath=${fn:trim(currentPath)}/${item.name }"}'>
		          			<td>
		          				<span class="am-inline-block am-margin-right-sm"><input type="checkbox" name="item"/></span>
		          				<span class="am-inline-block">
		          				<img alt="floder" src="${ctx}/resources/images/drive_30x30/folder_30x30.png">
		          				</span>
		          			</td>
		          			<td class="am-text-middle">
		          				<a href="${ctx}/group/content?groupId=${group.id}&path=${fn:trim(path)}/${item.fileName }&currentPath=${fn:trim(currentPath)}/${item.name }">${item.name }</a>
		          			</td>
		          			<td class="am-text-middle">
		          				-
		          			</td>
		          			<td>${item.createName }</td>
		          			<td class="am-text-middle"><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
		          		</tr>
   					</c:when>
   					<c:otherwise>
   						<tr id="${item.id}" file-info='{id:"${item.id}",fileId:"${item.ref.id }",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.ref.suffix)}",link:"${ctx}/group/downloadFile?groupId=${group.id}&fileId=${item.id}"}' class="fileDiv" data-am-popover="{content: '${item.name }.${item.ref.suffix } <br/> 创建时间：<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>', trigger: 'hover focus'}">
		          			<td>
		          				<span class="am-inline-block am-margin-right-sm"><input type="checkbox" name="item"/></span>
		          				<span class="am-inline-block">
		          				<img alt="file" src="${ctx}/resources/images/drive_30x30/${item.ref.suffix }_30x30.png" onerror="this.src='${ctx}/resources/images/drive_30x30/file_30x30.png'">
		          				</span>
		          			</td>
		          			<td class="am-text-middle">
		          				${item.name}.${item.ref.suffix }
		          			</td>
		          			<td class="am-text-middle">
		          				<tags:fileSize fileSize="${item.ref.fileSize}"/>
		          			</td>
		          			<td>${item.ref.createName }</td>
		          			<td class="am-text-middle"><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
		          		</tr>
   					</c:otherwise>
   				</c:choose>
          	</c:forEach>
          </tbody>
        </table>
     	  
     	  </div>
      </div>

    </div>
  </div>
  <!-- content end -->
  </div>

  <%@ include file="/WEB-INF/views/group/group-info-sidebar.jsp" %>

</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>

<%@ include file="/WEB-INF/views/group/group-info-deal.jsp" %>


<script type="text/javascript">
$(function(){
	//初始化datatable
    $('#data_table').DataTable({
    	  "responsive": true,
    	  "dom": 'ti',
    	  //"bFilter": true, //搜索栏
    	  "bPaginate":false,
    	  "aoColumnDefs": [ 
    	     { "bSortable": false, "aTargets": [ 0]}
    	  ],//设置那列不排序
    	  "aaSorting": [[ 1, "desc" ]],//设置默认排序列
		  "sScrollY": 430,
		  "bAutoWidth":false,
		  "aoColumns": [
		                { "sWidth": "8%"},
		                null,
		                { "sWidth": "15%" },
		                { "sWidth": "20%" },
		                null,
		              ],
		   "bInfo":false,
   });
	
	
});



</script>

</body>

</html>
