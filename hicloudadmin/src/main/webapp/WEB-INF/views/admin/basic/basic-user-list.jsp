<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/admin/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>用户列表</title>
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
      	<button class="am-btn am-btn-primary am-btn-sm" onclick="window.location.href='${ctx}/admin/user/save?isPage=true'">
			  <i class="am-icon-plus"></i>
			   添加用户
		  </button>
		  <button type="button" class="am-btn am-btn-primary am-btn-sm"
			  data-am-modal="{target: '#import-dialog', closeViaDimmer: 0, width: 500, height: 180}">
			  <i class="am-icon-upload"></i>
			 	批量导入
			</button>
      </div>
    </div>
    <div class="am-g">
      <div class="am-u-sm-12">
        <table class="am-table am-table-striped am-table-bordered am-table-compact am-table-centered" id="datatable">
		  <thead>
		  <tr>
		    <th>账号</th>
		    <th>真实姓名</th>
		    <th>创建时间</th>
		    <th>状态</th>
			<th>已使用空间</th>
			<th>总空间</th>
		    <th>操作</th>
		  </tr>
		  </thead>
		  <tbody>
		  <c:forEach items="${requestScope.result}" var="item" varStatus="status">
			
			<c:choose>
		  		<c:when test="${status.index%2==0}">
		  			<tr  class="odd gradeX" id="${item.id}">
		  		</c:when>
		  		<c:otherwise>
		  			<tr class="even gradeC" id="${item.id}">
		  		</c:otherwise>
		  	</c:choose>
		  		<td>${item.userName }</td>
		  		<td>${item.name }</td>
		  		<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		  		<td>
		  			${item.status==0?"禁用":"有效"}
		  		</td>
				<td><tags:fileSize fileSize="${item.usedSpace }"/></td>
				<td><tags:fileSize fileSize="${not empty item.space.spaceSize?item.space.spaceSize:0 }"/></td>
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
											<td>账号</td>
											<td>${item.userName}</td>
										</tr>
										<tr>
											<td>姓名</td>
											<td>${item.name}</td>
										</tr>
										<tr>
											<td>性别</td>
											<td>${item.sex==0?"女":"男"}</td>
										</tr>
										<tr>
											<td>出生日期</td>
											<td><fmt:formatDate value="${item.birthDay }" pattern="yyyy-MM-dd" /></td>
										</tr>
										<tr>
											<td>电子邮件</td>
											<td>${item.email}</td>
										</tr>
										<tr>
											<td>状态</td>
											<td>${item.status==0?"禁用":"有效"}</td>
										</tr>
										<tr>
											<td>创建时间</td>
											<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
										</tr>
										
										<tr>
											<td>已使用空间</td>
											<td><tags:fileSize fileSize="${item.usedSpace }"/></td>
										</tr>
										<tr>
											<td>总空间</td>
											<td><tags:fileSize fileSize="${not empty item.space.spaceSize?item.space.spaceSize:0 }"/></td>
										</tr>
									</table>
									</div>
							    </div>
							  </div>
							</div>
		                    <button class="am-btn am-btn-default am-btn-xs am-text-secondary" onclick="window.location.href='${ctx}/admin/user/edit?id=${item.id}'"><span class="am-icon-pencil-square-o"></span> 编辑</button>
		                    <c:if test="${item.status==1}">
		                    	<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only" onclick="changeStatus('${item.id}','0')"><span class="am-icon-trash-o"></span> 禁用</button>
		                    </c:if>
			                <c:if test="${item.status==0}">
			                	<button class="am-btn am-btn-default am-btn-xs am-text-danger am-hide-sm-only" onclick="changeStatus('${item.id}','1')"><span class="am-icon-trash-o"></span> 激活
			                	</button>
			                </c:if>
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

<div class="am-modal am-modal-no-btn" tabindex="-1" id="import-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">导入用户报表
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
     	<form action="${ctx}/admin/user/import" method="post" class="am-form" enctype="multipart/form-data">
     		<fieldset class="am-form-set am-center">
	        	<input type="file" name="file" class="am-center">
	     	</fieldset>
	     	<fieldset class="am-form-set am-center">
	     	<button type="submit" class="am-btn am-btn-primary am-btn-sm am-fr">确定</button>&nbsp;
	     	<button type="button" class="am-btn am-btn-default am-btn-sm am-fr" onclick="$('#import-dialog').modal('close');">取消</button>
	     	</fieldset>
	     	
     		<fieldset class="am-form-set ">
     			<a href="${ctx}/admin/user/download">下载用户报表模板</a>
     		</fieldset>
     	</form>
    </div>
  </div>
</div>


<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/admin/includes/footer.jsp" %>
</body>

<script type="text/javascript">
  $(function() {
	//初始化datatable
    $('#datatable').DataTable();	
  });

  function changeStatus(id,status){
	  $.ajax({
			type : "post",
			url : "${ctx}/admin/user/changeStatus",
			data :{
				'id':id,
				'status':status
			},
			error : function(request) {
				warning("操作失败");
			},
			dataType: "json", 
			success : function(data) {
				switch(data.status){
				case "success":
					success(data.message);
					break;
				case "error":
					error(data.errInfo);
					break;
				default:
					error("操作失败");
					break;
				}
			}
		});
  }
</script>
</html>
