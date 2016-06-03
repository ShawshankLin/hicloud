<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp"%>
<!doctype html>
<html class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>消息盒子</title>
<meta name="description" content="这是一个 table 页面">
<meta name="keywords" content="table">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="renderer" content="webkit">
<meta http-equiv="Cache-Control" content="no-siteapp" />
<%@ include file="/WEB-INF/views/includes/link.jsp"%>
<link rel="stylesheet"
	href="${ctx}/resources/plugins/contextjs/css/context.standalone.css">
<link rel="stylesheet" type="text/css"
	href="${ctx}/resources/styles/floatbox.css">
</head>
<body>
	<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

	<%@ include file="/WEB-INF/views/includes/header.jsp"%>

	<div class="am-g am-container">

		<!-- content start -->
		<div class="admin-content">
			<div class="am-g">
				<div class="am-u-sm-12 am-padding-right-0">
					<table
						class="am-table am-table-striped am-table-hover am-margin-right-0"
						id="data_table">
						<thead>
							<tr>
								<th class="table-title">内容</th>
								<th>时间</th>
								<th class="table-author am-hide-sm-only">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${notices }" var="item">
								<tr>
									<td>${item.content}</td>
									<td><fmt:formatDate value="${item.createDate }"
											pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td>${item.target}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>

			</div>
		</div>
		<!-- content end -->
	</div>

	<a href="#"
		class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu"
		data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

	<%@ include file="/WEB-INF/views/includes/footer.jsp"%>
</body>

<script type="text/javascript">
    $(function () {
    	//初始化datatable
	    $('#data_table').DataTable({
	    	  "responsive": true,
	    	  "dom": 'ti',
	    	  //"bFilter": true, //搜索栏
	    	  "bPaginate":false,
			  "sScrollY": 430,
			  "bAutoWidth":false,
			  "aoColumns": [
			                { "sWidth": "60%"},
			                { "sWidth": "20%" },
			                { "sWidth": "20%" }
			              ],
			   "bInfo":false,
	   });
    });	

</script>
</html>
