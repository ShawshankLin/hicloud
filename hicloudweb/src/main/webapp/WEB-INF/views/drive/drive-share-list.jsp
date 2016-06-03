<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>hicloud云盘</title>
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

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/includes/sidebar.jsp" %>

  <!-- content start -->
  <div class="admin-content">
    <div class="am-g am-padding-top-sm" style="padding-bottom:0.3rem">
    	<div class="am-u-sm-12 am-u-md-6">
		<div class="am-btn-toolbar" id="share-toolbar">
          	<div class="am-btn-group">
          	<a class="am-btn am-btn-primary am-btn-sm" href="javascript:void(0)" target="_blank"" disabled="disabled" id="look-share-bt"><span class="am-icon-search"></span> 查看分享链接</a>
            <button type="button" class="am-btn am-btn-primary am-btn-sm" id="cancel-share-bt" disabled="disabled"><span class="am-icon-share-alt"></span> 取消分享</button>
            <button type="button" class="am-btn am-btn-primary am-btn-sm" disabled="disabled" id="copy-share-bt"  data-clipboard-target onclick="success('链接已复制到剪切板')"><span class="am-icon-copy" ></span> 复制分享链接</button>
			</div>
        </div>
		</div>
    </div>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>	
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-right-0">
     	  <span class="am-text-sm">我分享的文件</span>
          <table class="am-table am-table-striped am-table-hover table-main am-margin-right-0 " id="data_table">
            <thead>
              <tr>
				<th class="table-check" ><input type="checkbox" onclick="checkAllBox(this)"/></th>
				<th class="table-title">文件名</th>
				<th class="am-hide-sm-only">分享类型</th>
				<th class="am-hide-sm-only">提取码</th>
				<th class="am-hide-sm-only">下载次数</th>
				<th class="am-hide-sm-only">大小</th>
				<th class="table-date am-hide-sm-only">日期</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${requestScope.result}" var="item" varStatus="status">
				<c:choose>
			  		<c:when test="${status.index%2==0}">
			  			<tr  class="odd gradeX" id="${item.id }">
			  		</c:when>
			  		<c:otherwise>
			  			<tr class="even gradeC" id="${item.id }">
			  		</c:otherwise>
			  	</c:choose>
			  		<td>       
			  			<input type="checkbox" name="item"/>
			  		</td>
			  		<td>${item.name }.${item.suffix }</td>
			  		<td>
			  			<c:choose>
			  				<c:when test="${item.type==1}">
			  					公开
			  				</c:when>
			  				<c:when test="${item.type ==2}">
			  					私有
			  				</c:when>
			  				<c:when test="${item.type==3}">
			  					个人
			  				</c:when>
			  				<c:otherwise>
			  					-
			  				</c:otherwise>
			  			</c:choose>
			  		
			  		</td>
			  		<td>${item.extractionCode}</td>
			  		<td>${item.hot }</td>
			  		<td><tags:fileSize fileSize="${item.fileSize}"/></td>
					<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<input type="hidden" id="share-link-${item.id}" value="${base}/share/${item.id}"/>
			  	</tr>
		  	</c:forEach>
          </tbody>
        </table>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<input type="hidden" name="shareId" id="shareId"/>


<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>

</body>
<script type="text/javascript" src="${ctx}/resources/plugins/zeroclipboard/ZeroClipboard.min.js"></script>
<script type="text/javascript">
$(function(){
	//初始化datatable
    var datatable=$('#data_table').DataTable({
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
		  "bInfo":false,
   });
	
  $('#cancel-share-bt').click(function(){
	  var shareId=$('#shareId').val();
	  if(shareId==""){
		  warning('请选择文件');
		  return;
	  }
	  $('#my-modal-loading').modal('open');
	  $.ajax({
			type : 'get',
			url : '${ctx}/share/cancel',
			data : {
				'shareId':shareId
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
						var shareIds=shareId.split(',');
						for(var i=0;i<shareIds.length;i++){
							$('#'+shareIds[i]).remove();
						}
						$('#look-share-bt').attr('disabled',true);
						$('#copy-share-bt').attr('disabled',true);
						$('#cancel-share-bt').attr('disabled',true);
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
  });

  //单选框点击
  $('#data_table tbody tr > td:first-child input:checkbox').click(function(event){
		var shareId;		
		$('input:checked[name="item"]').each(function(i,item){
			if(i==0){
				$('#look-share-bt').attr('disabled',false);
				$('#copy-share-bt').attr('disabled',false);
				shareId=$(this).closest('tr').attr('id');
				$('#look-share-bt').attr('href','${ctx }/share/'+shareId);
				$('#copy-share-bt').attr('data-clipboard-target','share-link-'+shareId);
			}else{
				$('#look-share-bt').attr('disabled',true);
				$('#copy-share-bt').attr('disabled',true);
				shareId=$(this).closest('tr').attr('id')+","+shareId;
			}
			$('#cancel-share-bt').attr('disabled',false);
		});
		if($('input:checked[name="item"]').length==0){//无选择全部disabled
			$('#look-share-bt').attr('disabled',true);
			$('#copy-share-bt').attr('disabled',true);
			$('#cancel-share-bt').attr('disabled',true);
		}
		$('#shareId').val(shareId);
	});

	//定义一个新的复制对象
	var clip = new ZeroClipboard($('#copy-share-bt'), {
	  moviePath: "${ctx}/resources/plugins/zeroclipboard/ZeroClipboard.swf"
	});
});

//全选框
function checkAllBox(obj) {
   	var items = document.getElementsByName("item");
   	var shareId;
   	for (i = 0; i < items.length; i++) {
		items[i].checked = obj.checked;
		if(i==0){
			shareId=$(items[i]).closest('tr').attr('id');
		}else{
			shareId=$(items[i]).closest('tr').attr('id')+","+shareId;
		}
	}
   	if(obj.checked){
   		$('#cancel-share-bt').attr('disabled',false);
   		$('#shareId').val(shareId);
   	}else{
   		$('#cancel-share-bt').attr('disabled',true);
   		shareId="";
   	}
}
</script>

</html>

