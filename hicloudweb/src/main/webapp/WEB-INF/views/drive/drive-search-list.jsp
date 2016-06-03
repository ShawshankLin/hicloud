<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
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
           <table class="am-table am-table-striped am-table-hover table-main am-margin-right-0" id="data_table">
            <thead>
              <tr>
				<th class="table-check" ><input type="checkbox" /></th>
				<th class="table-title">文件名</th>
				<th class="table-author am-hide-sm-only">大小</th>
				<th class="table-date am-hide-sm-only">修改日期</th>
				<th class="table-date am-hide-sm-only">所在目录</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${requestScope.files}" var="item" varStatus="status">
          		<tr id="${item.id}" type="${item.isDir}">
          			<td>
          				<span class="am-inline-block am-margin-right-sm"><input type="checkbox" /></span>
          				<span class="am-inline-block">
          				<c:choose>
          					<c:when test="${item.isDir==1}">
          						<img alt="floder" src="${ctx}/resources/images/drive_30x30/folder_30x30.png">
          					</c:when>
          					<c:otherwise>
          						<img alt="file" src="${ctx}/resources/images/drive_30x30/${item.suffix }_30x30.png" onerror="this.src='${ctx}/resources/images/drive_30x30/file_30x30.png'">
          					</c:otherwise>
          				</c:choose>
          				</span>
          			</td>
          			<td class="am-text-middle">
          				
          				<c:choose>
          					<c:when test="${item.isDir==1}">
          						<c:choose>
       								<c:when test="${fn:trim(path)=='/'}">
       									<a href="${ctx}/myfile?path=${fn:trim(path)}${item.fileName }">${item.fileName }</a>
       								</c:when>
       								<c:otherwise>
       									<a href="${ctx}/myfile?path=${fn:trim(path)}/${item.fileName }">${item.fileName }</a>
       								</c:otherwise>
       							</c:choose>
          						
          					</c:when>
          					<c:otherwise>
          						${item.name}.${item.suffix }
          					</c:otherwise>
          				</c:choose>
          			</td>
          			<td class="am-text-middle">
          				<tags:fileSize fileSize="${item.fileSize}"/>
          			</td>
          			<td class="am-text-middle"><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
          			<td>
          				<c:choose>
          					<c:when test="${item.parentPath=='/'}">
          						根目录
          					</c:when>
          					<c:otherwise>
          						${item.parentPath }
          					</c:otherwise>
          				</c:choose>
          			</td>
          		</tr>
          	</c:forEach>
          </tbody>
        </table>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<%@ include file="/WEB-INF/views/drive/drive-info-deal.jsp" %>

</body>

<script type="text/javascript">

//Convert divs to queue widgets when the DOM is ready

$(function () {
	//初始化datatable
    $('#data_table').DataTable({
    	  "responsive": true,
    	  "dom": 'ti',
    	  "bFilter": true, //搜索栏
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
		                { "sWidth": "20%" },
		              ],
		   "bInfo":false,
    	});
	
    context.init({
	    fadeSpeed: 100,
	    filter: function ($obj){},
	    above: 'auto',
	    preventDoubleContext: true,
	    compress: false
	});
	
	context.attach('#data_table tbody tr[type="true"]', [
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#'},
		{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: '#'},
		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: '#'},
		{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: '#'},

	]);
	
	context.attach('#data_table tbody tr[type="false"]', [
		{text: '<span class="am-icon-eye">&nbsp;&nbsp;在线查看</span>', href: '#'},
		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>', href: '#'},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#'},
		{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: '#'},
		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: '#'},
		{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: '#'},
		{text: '<span class="am-icon-history">&nbsp;&nbsp;文件时光机</span>', href: '#'},
	]);                                   		
	
	//表格行单击
	$('#data_table tbody tr').click(function(){
		var checkBox=$(this).find('td:first-child input:checkbox');
		if(checkBox.is(":checked")==false){
			checkBox.prop('checked',true);
			$(this).addClass('am-primary');
			if($(this).attr('isDir')==1){
				showToolBar('dir-toolbar');
			}else{
				showToolBar('file-toolbar');
			}
		}else{
			checkBox.prop('checked',false);
			$(this).removeClass('am-primary');
			hideToolBar();
		}
		$(this).siblings().find('td:first-child input:checkbox').prop('checked',false);
		$(this).siblings().removeClass('am-primary');
		$('#fileId').val($(this).attr('id'));
	});
	
	//单选框点击
	$('#data_table tbody tr > td:first-child input:checkbox').click(function(event){
		event.stopPropagation();//阻止冒泡
		if($(this).is(":checked")){
			$(this).closest('tr').addClass('am-primary');
			if($(this).closest('tr').attr('isDir')==1){
				showToolBar('dir-toolbar');
			}else{
				showToolBar('file-toolbar');
			}
		}else{
			$(this).closest('tr').removeClass('am-primary');
			hideToolBar();
		}
		
	});
	
	//全选框单击
    $(document).on('click', 'th input:checkbox' , function(){
          var that = this;
          $('#data_table' ).find('tr > td:first-child input:checkbox').each( function(){
               this.checked = that.checked;
               $( this).closest('tr' ).toggleClass('selected');
               if(that.checked){
 	        	 $( this).closest('tr' ).addClass('am-primary');
 	        	 showToolBar('dir-toolbar');
 	          }else{
 	        	  $( this).closest('tr' ).removeClass('am-primary');
 	        	 hideToolBar();
 	          }
          });
     });

    
});
</script>
</html>
