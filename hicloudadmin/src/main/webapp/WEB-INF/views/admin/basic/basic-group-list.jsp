<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/admin/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>群组列表</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/admin/includes/link.jsp" %>

</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/admin/includes/header.jsp" %>

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/admin/includes/sidebar.jsp" %>

  <!-- content start -->
  <div class="admin-content">
    <div class="am-g am-padding-top am-padding-bottom">
      <div class="am-u-sm-12 am-u-md-3">
      	<%-- <button class="am-btn am-btn-primary am-btn-sm" onclick="window.location.href='${ctx}/admin/user/save?isPage=true'">
			  <i class="am-icon-cloud-upload"></i>
			   添加用户
		  </button>
		  <button type="button" class="am-btn am-btn-primary am-btn-sm"
			  data-am-modal="{target: '#createDirDialog', closeViaDimmer: 0, width: 300, height: 160}">
			  <i class="am-icon-cloud-download"></i>
			 	批量导入
			</button> --%>
      </div>
    </div>
    <div class="am-g">
      <div class="am-u-sm-12">
        <table class="am-table am-table-striped am-table-bordered am-table-compact am-table-centered" id="datatable">
		  <thead>
		  <tr>
		    <th>群组编号</th>
		    <th>群组名</th>
		    <th>创建时间</th>
		    <th>群主</th>
		    <th>已使用空间</th>
			<th>总空间</th>
		    <th>操作</th>
		  </tr>
		  </thead>
		  <tbody>
		  <c:forEach items="${requestScope.groups}" var="item" varStatus="status">
			
			<c:choose>
		  		<c:when test="${status.index%2==0}">
		  			<tr  class="odd gradeX" id="${item.id}">
		  		</c:when>
		  		<c:otherwise>
		  			<tr class="even gradeC" id="${item.id}">
		  		</c:otherwise>
		  	</c:choose>
		  		<td>${item.groupNo }</td>
		  		<td>${item.groupName }</td>
				<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		  		<td>${item.createName }</td>
		  		<td><tags:fileSize fileSize="${item.usedSpace }"/></td>
				<td><tags:fileSize fileSize="${item.space.spaceSize }"/></td>
		  		<td >
		  			<div class="am-btn-toolbar">
	                  <div class="am-btn-group am-btn-group-xs">
	                  	<button type="button" class="am-btn am-btn-default am-text-success am-hide-sm-only" data-am-modal="{target: '#show${item.id}', closeViaDimmer: 0, width: 600, height: 425}">
							<span class="am-icon-search-plus"></span> 查看
						</button>
						<!-- 查看窗口 -->
						<div class="am-modal am-modal-no-btn" tabindex="-1" id="show${item.id}">
							  <div class="am-modal-dialog">
							    <div class="am-modal-hd">用户基本信息
							      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
							    </div>
							    <div class="am-modal-bd">
							    	<div style="overflow:auto;width:100%;height:300px">
							     	<table class="am-table am-table-striped am-table-hover">
										<tr>
											<td>群组编号</td>
											<td>${item.groupNo}</td>
										</tr>
										<tr>
											<td>群组名</td>
											<td>${item.groupName}</td>
										</tr>
										<tr>
											<td>群主</td>
											<td>${item.createName}</td>
										</tr>
										<tr>
											<td>创建时间</td>
											<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd" /></td>
										</tr>
										<tr>
											<td>已使用空间</td>
											<td><tags:fileSize fileSize="${item.usedSpace }"/></td>
										</tr>
										<tr>
											<td>总空间</td>
											<td><tags:fileSize fileSize="${item.space.spaceSize }"/></td>
										</tr>											
									</table>
									</div>
							    </div>
							  </div>
							</div>
		                    <button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only" onclick="del('${item.id}')"><span class="am-icon-trash-o"></span> 删除</button>
	                  </div>
	                </div>
		  		</td>
		  	</tr>
		  </c:forEach>
		</table>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/admin/includes/footer.jsp" %>
</body>

<script type="text/javascript">
  $(function() {
	//初始化datatable
    $('#datatable').DataTable();	
  });
</script>
</html>
