<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.cache.DataConverter"  %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>

<%
	Map<String,String> fileTypeMap=DataConverter.getFileType();
	request.setAttribute("fileTypeMap",fileTypeMap);
%>
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
    <div class="am-g am-padding-top-sm" style="padding-bottom:0.3rem">
    	<div class="am-u-sm-12 am-u-md-6">
		<div class="am-btn-toolbar">
          	<div class="am-btn-group">
          	<button type="button" class="am-btn am-btn-primary am-btn-sm" disabled="disabled" id="history_bt"><span class="am-icon-history"></span> 穿越到选中的时光号</button>
            <button type="button" class="am-btn am-btn-primary am-btn-sm" id="look-bt"><span class="am-icon-search"></span> 在线查看</button>
            <button type="button" class="am-btn am-btn-primary am-btn-sm" id="download-bt"><span class="am-icon-download"></span> 下载</button>
			</div>
        </div>
		</div>
    </div>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>	
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-right-0">
     	  <span class="am-text-sm">所有文件 > "${file.name }.${file.suffix}"的文件时光机（可恢复7天内任意历史版本）</span>
          <table class="am-table am-table-striped am-table-hover table-main am-margin-right-0" id="data_table">
            <thead>
              <tr>
				<th></th>
				<th class="table-title">时光号</th>
				<th class="table-author am-hide-sm-only">大小</th>
				<th class="table-date am-hide-sm-only">修改日期</th>
              </tr>
          </thead>
          <tbody>
          	<c:forEach items="${requestScope.result}" var="item" varStatus="status">
				<c:choose>
			  		<c:when test="${status.index%2==0}">
			  	<tr  class="odd gradeX" id="${item.id}" file-info='{id:"${item.id}",status:"${item.status }"}'>
			  		</c:when>
			  		<c:otherwise>
			  			<tr class="even gradeC" id="${item.id}" file-info='{id:"${item.id}",status:"${item.status }"}'>
			  		</c:otherwise>
			  	</c:choose>
			  		<td>       
			  			<label class="am-radio">
					      <input type="radio" name="item" value="${item.id}" data-am-ucheck ${item.status=='active'?'checked':'' }>
					    </label>
			  		</td>
			  		<td>${item.fileVersion}${item.status=="active"?"<span class='am-text-danger'>(当前)</span>":""}</td>
			  		<td><tags:fileSize fileSize="${item.fileSize}"/></td>
					<td><fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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

</body>
<script type="text/javascript">
$(function(){
	//初始化datatable
    $('#data_table').DataTable({
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
						{ "sWidth": "5%"},
		                { "sWidth": "60%"},
		                { "sWidth": "15%" },
		                { "sWidth": "20%" },
		              ],
		   "bInfo":false,
    });
	
	
  	//单选框点击
	$('#data_table tbody tr > td:first-child input:radio').click(function(event){
		event.stopPropagation();
		if($(this).is(":checked")){
			//$(this).closest('tr').addClass('am-primary');
			var file=$(this).closest('tr').attr("file-info");
			file=eval('(' + file + ')');
			if(file.status=='active'){
				$('#history_bt').attr('disabled',true);
			}else{
				$('#history_bt').attr('disabled',false);
			}
		}
	});
  	
  	$('#download-bt').click(function(){
  		var fileId=$("input[name='item']:checked").val();
  		window.location.href="${ctx}/myfile/download?fileId="+fileId;
  	});
	
	$('#history_bt').click(function(){
		var fileId=$("input[name='item']:checked").val();
		confirm("确定要穿越到此时光号（还原到选中版本）");
		$('#confirm').modal({
	        relatedTarget: this,
	        onConfirm: function(options) {
	        	$.ajax({
	    			type : "get",
	    			url : "${ctx}/myfile/history",
	    			data : {
	    				fileId:fileId
	    			},
	    			error : function(request) {
	    				warning("连接错误");
	    			},
	    			success : function(data){
	    				var json = $.parseJSON(data);
	    				switch(json.status){
	    				case "success":
	    					success(json.message);
	    					window.location.href=json.target;
	    					break;
	    				case "error":
	    					danger(json.message);
	    					break;
	    				default:
	    					danger("操作失败");
	    					break;
	    				}
	    			}
	    		});
	        },
	        onCancel: function() {
	        	
	        }
	     });
	});
	
	
	$('#look-bt').click(function(){
		var fileId=$("input[name='item']:checked").val();
		var suffix='${fileUtil.getFileTypeBySuffix(file.suffix)}';
		openFile(fileId,suffix);
	});

	
});


//打开文件
function openFile(fileId,suffix){
	switch(suffix){
	case '<%=FileType.DOCUMENT%>'://文档	
		$('body').append('<a href="${ctx}/myfile/preview?fileId='+fileId+'" id="goto" target="_blank"></a>');
		$('#goto').get(0).click();
		$('#goto').remove();
		break;
	case '<%=FileType.VEDIO%>'://视频	
		var file='http://115.159.29.214:18080/test.mp4';
		var player = cyberplayer("playercontainer").setup({
            width: 854,
            height: 480,
            stretching: "uniform",
            file: file,
            autostart: true,
            repeat: false,
            volume: 100,
            controls: true,
            ak: '799E296BC90ca6afdf95cc32ce3083b6' // 公有云平台注册即可获得accessKey
        });
        /* jwplayer("playercontainer").setup({
            file: "test.mp4",
            image: "/uploads/myPoster.jpg"
        });
		 */
		$('#media-dialog').modal({
		      relatedTarget: this,
		      closeViaDimmer:false,
			  width:854,
			  height:480
		}).on('open.modal.amui', function(){
			player.play();
		}).on('closed.modal.amui', function(){
			player.stop();
			player.remove();
		});
		break;
	case '<%=FileType.MUSCI%>'://音乐
		
		break;
	default: 
		return;
	}
}


</script>

</html>

