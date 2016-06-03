<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>
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
  <link rel="stylesheet" href="${ctx}/resources/plugins/contextjs/css/context.standalone.css">
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/styles/floatbox.css">
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
    <%@ include file="/WEB-INF/views/drive/drive-trash-toolbar.jsp" %>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-right-0">
     	  <%@ include file="/WEB-INF/views/drive/drive-trash-navbar.jsp" %>
          <table class="am-table am-table-striped am-table-hover table-main am-margin-right-0" id="data_table">
            <thead>
              <tr>
				<th class="table-check" ><input type="checkbox" onclick="checkAllBox(this)"/></th>
				<th class="table-title">文件名</th>
				<th class="table-author am-hide-sm-only">大小</th>
				<th class="table-date am-hide-sm-only">删除日期</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${requestScope.files}" var="item" varStatus="status">
          		<c:choose>
   					<c:when test="${item.isDir==1}">
   						<tr id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}",link:"${ctx}/myfile/grid?path=${fn:trim(path)}/${item.fileName }"}'>
		          			<td>
		          				<span class="am-inline-block am-margin-right-sm"><input type="checkbox" name="item"/></span>
		          				<span class="am-inline-block">
		          				<img alt="floder" src="${ctx}/resources/images/drive_30x30/folder_30x30.png">
		          				</span>
		          			</td>
		          			<td class="am-text-middle">
		          				<a href="${ctx}/myfile?path=${fn:trim(path)}/${item.fileName }">${item.fileName }</a>
		          			</td>
		          			<td class="am-text-middle">
		          				-
		          			</td>
		          			<td class="am-text-middle"><fmt:formatDate value="${item.modifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
		          		</tr>
   					</c:when>
   					<c:otherwise>
   						<tr id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}"}'>
		          			<td>
		          				<span class="am-inline-block am-margin-right-sm"><input type="checkbox" name="item"/></span>
		          				<span class="am-inline-block">
		          				<img alt="file" src="${ctx}/resources/images/drive_30x30/${item.suffix }_30x30.png" onerror="this.src='${ctx}/resources/images/drive_30x30/file_30x30.png'">
		          				</span>
		          			</td>
		          			<td class="am-text-middle">
		          				${item.name}.${item.suffix }
		          			</td>
		          			<td class="am-text-middle">
		          				<tags:fileSize fileSize="${item.fileSize}"/>
		          			</td>
		          			<td class="am-text-middle"><fmt:formatDate value="${item.modifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
		          		</tr>
   					</c:otherwise>
   				</c:choose>
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
<%@ include file="/WEB-INF/views/drive/drive-trash-deal.jsp" %>
</body>

<script type="text/javascript">
    $(function () {
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
			              ],
			   "bInfo":false,
	    	});
    	
	    
	    //绑定contextjs
	    $('#data_table>tbody>tr').each(function(){
	    	var file=$(this).attr("file-info");
	    	if(file!=undefined){
	    		file=eval('(' + file + ')');
		    	attachTrashContext(file);
	    	}
	    }).click(function(){
	    	var checkBox=$(this).find('td:first-child input:checkbox');
			if(checkBox.is(":checked")==false){
				checkBox.prop('checked',true);
				$(this).addClass('am-primary');
				$('#restore-bt').attr('disabled',false);
				$('#del-complete-bt').attr('disabled',false);
				$('#fileId').val($(this).attr('id'));
			}else{
				checkBox.prop('checked',false);
				$(this).removeClass('am-primary');
				$('#restore-bt').attr('disabled',true);
				$('#del-complete-bt').attr('disabled',true);
			}
			$(this).siblings().find('td:first-child input:checkbox').prop('checked',false);
			$(this).siblings().removeClass('am-primary');
		});
		
		//单选框点击
		$('#data_table tbody tr > td:first-child input:checkbox').click(function(event){
			event.stopPropagation();
			if($(this).is(":checked")){
				$(this).closest('tr').addClass('am-primary');
				var file=$(this).closest('tr').attr("file-info");
				file=eval('(' + file + ')');
				$('#fileId').val(file.id);
				$('#restore-bt').attr('disabled',false);
				$('#del-complete-bt').attr('disabled',false);
			}else{
				$(this).closest('tr').removeClass('am-primary');
				$('#restore-bt').attr('disabled',true);
				$('#del-complete-bt').attr('disabled',true);
			}
		});
});
   
   //全选框
   function checkAllBox(obj) {
	   	var items = document.getElementsByName("item");
	   	var fileId;
	   	for (i = 0; i < items.length; i++) {
   			items[i].checked = obj.checked;
   			if(i==0){
   				fileId=$(items[i]).closest('tr').attr('id');
   			}else{
   				fileId=$(items[i]).closest('tr').attr('id')+","+fileId;
   			}
   		}
	   	if(obj.checked){
       	 	$('#data_table>tbody>tr').addClass('am-primary');
       	 	$('#fileId').val(fileId);
       	 	$('#restore-bt').attr('disabled',false);
			$('#del-complete-bt').attr('disabled',false);
        }else{
        	$('#fileId').val("");
        	$('#data_table>tbody>tr').removeClass('am-primary');
       	 	$('#restore-bt').attr('disabled',true);
			$('#del-complete-bt').attr('disabled',true);
        }
   }

</script>
</html>
