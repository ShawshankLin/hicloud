<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.cylg.cis.hicloud.core.cache.DataConverter"  %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>

<%
	Map<String,String> fileTypeMap=DataConverter.getFileType();
	request.setAttribute("fileTypeMap",fileTypeMap);
%>
<!-- 文件弹出框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="myfile-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
		<!-- <a href="javascript: void(0)" class="am-text-sm am-fl">《返回上一级</a> -->选择从云盘添加到群里的文件
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd" id="myfile-panel">
		  <table class="am-table am-table-striped am-table-hover table-main " id="myfile-table" cellspacing="0" width="100%">
          </table>
    </div>
    <div class="am-modal-foot">
    	<button type="button" class="am-btn am-btn-primary am-fr am-margin-right-xl" id="upload-bt">确定</button>
    </div>
  </div>
</div>


<!-- 新建文件夹 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="mkdir-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	新建文件夹
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
      <form class="am-form" id="mkdir-form">
      	  <fieldset class="am-form-set">
      	  	<input type="hidden" name="groupId" value="${group.id }"/>
	        <input type="hidden" name="path" value="${path}"/>
	      </fieldset>
	      <fieldset class="am-form-set">
	        <input type="text" placeholder="新建文件夹 " name="dirName" onkeydown="if(event.keyCode==13) return false;" autofocus>
	      </fieldset>
	      <div style="float:right">
	      	<button type="button" class="am-btn am-btn-default" onclick="$('#mkdir-dialog').modal('close');">取消</button>
	      	<button type="button" class="am-btn am-btn-primary" id="mkdir-bt">创建</button>
	      </div>
    	</form>
    </div>
  </div>
</div>

<!-- 添加好友 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="friend-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	邀请好友
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd"> 	
	     <form class="am-form-inline" role="form" id="friend-form">
		      <div class="am-form-group">
		      	<input type="hidden" name="groupId" value="${group.id }"/>
			    <input type="text" class="am-form-field" placeholder="输入好友帐号" name="friendName" id="friendName" onkeydown="if(event.keyCode==13) return false;" autofocus>
			  </div>
		      <button type="button" class="am-btn am-btn-primary" id="friend-bt">添加</button>
	    </form>
    </div>

  </div>
</div>

<!-- 发布群公告-->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="notice-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	发布群公告
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd"> 	
	     <form class="am-form-inline" role="form" id="notice-form">
		      <div class="am-form-group">
		      	<input type="hidden" name="groupId" value="${group.id }"/>
			  	<textarea rows="5" cols="40" name="notice" id="notice" onkeydown="if(event.keyCode==13) return false;" autofocus></textarea>
			  </div>
			 <div class="am-form-group am-margin-top-sm am-fr">
			 	<button type="button" class="am-btn am-btn-primary am-btn-sm " id="notice-bt">发布</button>
			 	<button type="button" class="am-btn am-btn-default am-btn-sm" data-am-modal-close>取消</button>
			 </div>
	    </form>
    </div>
  </div>
</div>


<!-- 多媒体播放器 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="media-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	播放器
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd" id="playercontainer">
		
    </div>
  </div>
</div>

<!-- 重新命名 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="rename-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	重新命名
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
      <form class="am-form" id="rename-form">
	      <fieldset class="am-form-set">
	        <input type="text" placeholder="无标题文件夹 " name="newname" id="newname"  onkeydown="if(event.keyCode==13) return false;" autofocus>
	      </fieldset>
	      <div style="float:right">
	      	<button type="button" class="am-btn am-btn-default" onclick="$('#rename-dialog').modal('close');">取消</button>
	      	<button type="button" class="am-btn am-btn-primary" id="rename-bt">保存</button>
	      </div>
    	</form>
    </div>
  </div>
</div>

<input type="hidden" name="fileIds" id="fileIds">

<!-- contextjs -->
<script type="text/javascript" src="${ctx}/resources/plugins/contextjs/js/context.js"></script>
<!-- groupjs -->
<script type="text/javascript" src="${ctx}/resources/scripts/group.js"></script>
<!-- <script type="text/javascript" src="https://cdn.datatables.net/plug-ins/1.10.11/api/fnReloadAjax.js"></script> -->
<!-- baidu media -->
<script type="text/javascript" src="${ctx}/resources/plugins/Baidu-T5Player-SDK-Web-v2.0.2/player/cyberplayer.js"></script>

<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.metadata.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.messages_cn.js" ></script>
<link rel="stylesheet" href="${ctx}/resources/plugins/jqueryValidate/validate.css" />
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/IDCord.js" ></script>

<script type="text/javascript">
<!--

	$(function(){
		
		initDataTable();
		
		//创建文件夹
		$('#mkdir-bt').click(function(){
			$('#mkdir-dialog').modal('close');
			$('#my-modal-loading').modal('open');
			$.ajax({
				type : 'post',
				url : '${ctx}/group/mkdir',
				data : $('#mkdir-form').serialize(),
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
							location.reload();
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
		
		
		$('#friend-bt').click(function(){
			$('#friend-dialog').modal('close');
			$('#my-modal-loading').modal('open');
			$.ajax({
				type : 'post',
				url : '${ctx}/group/sendGroupRQ',
				data : $('#friend-form').serialize(),
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
		});
		
		
		
	    $("#notice-form").validate({
			rules: {
				'notice': {
					required: true,
				}
			},
			messages: {
				'notice': {
					required:'*请输入公告内容'
				}
			},
			
			errorPlacement: function(error, element) { //指定错误信息位置 
				if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox 
				var eid = element.attr('name'); //获取元素的name属性 
				error.appendTo(element.parent().parent()); //将错误信息添加当前元素的父结点后面 
				} else { 
				error.appendTo(element.parent().next()); 
				} 
			},
			errorElement: "check"
			
		 });
		
		
		$('#notice-bt').click(function(){
			if($("#notice-form").valid()){
				$('#notice-dialog').modal('close');
				$('#my-modal-loading').modal('open');
				$.ajax({
					type : 'post',
					url : '${ctx}/group/publishNotice',
					data : $('#notice-form').serialize(),
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
								window.location.href=data.target;
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
			return false;
		});
		
		
		
		$('#upload-bt').click(function(){
			$('#myfile-dialog').modal('close');
			var fileIds=$('#fileIds').val();
			if(fileIds==""){
				warning('请选择上传文件');
				return;
			}
			$('#my-modal-loading').modal('open');
			$.ajax({
					type : 'get',
					url : '${ctx}/group/uploadFile',
					data : {
						"groupId":'${group.id}',
						"fileId" : fileIds,
						"path":'${path}',
						"currentPath":'${currentPath}'
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
								window.location.href=data.target;
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
		
		
		//重命名操作
		$('#rename-bt').click(function(){
			$('#rename-dialog').modal('close');
			$('#my-modal-loading').modal('open');
			$.ajax({
				type : 'post',
				url : '${ctx}/group/renameFile',
				data : {
					'fileId':$('#fileId').val(),
					'newname':$('#newname').val(),
					'currentPath':'${currentPath}'
				},
				dataType: 'json', 
				error : function(response) {
					$('#my-modal-loading').modal('close');
					warning(response.statusText);
				},
				success : function(data) {
					$('#my-modal-loading').modal('close');
					try {
						switch(data.status){
						case "success":
							success(data.message);
							window.location.href=data.target;
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
		
	});

var table;

function bindTableClickEvent(){
	$('#myfile-table').on('click', '.itembox', function () {
		var fileIds="";
		$('.itembox').each(function(i,item){
			if($(item).is(':checked')){
				fileIds=fileIds+$(item).val()+","
			}
	   	});
		$('#fileIds').val(fileIds)
	});
}

function initDataTable(){
	$.ajax({
	    url: '${ctx}/myfile/getFileJsonData',
	    type: 'GET',
	    dataType: 'json',
	    success: function (data) {
	        assignToEventsColumns(data);
	        bindTableClickEvent();
	    }
	});
	
}



function reloadDataTable(link){
	$('#myfile-panel').empty();
	$('#myfile-panel').append("<table class=\"am-table am-table-striped am-table-hover table-main \" id=\"myfile-table\" cellspacing=\"0\" width=\"100%\"></table>");
	$.ajax({
	    url: link,
	    type: 'GET',
	    dataType: 'json',
	    success: function (data) {
	        assignToEventsColumns(data);
	        bindTableClickEvent();
	    }
	});
	
}
function assignToEventsColumns(data) {
	table = $('#myfile-table').DataTable({
	    "bAutoWidth": false,
	    "aaData": data,	
	    "sScrollY": 350,
    	"dom": 'ti',
    	"bSort":false,
    	 //"bFilter": true, //搜索栏
    	 "bPaginate":false,
	    "aoColumnDefs": [
	       {
	           "aTargets": [0],
	           "bSearchable": false,
	           "bSortable": false,
	           "bSort": false,
	           "mData": "id",
	           "mRender": function (event) {
	        	   return '<input class="itembox" type="checkbox"  value="' + event + '">';
	           }
	       },
	       {
	           "aTargets": [1],
	           "bSearchable": false,
	           "bSortable": false,
	           "bSort": false,
	           "mData": "suffix",
	           "mRender": function (data, type, full) {
	        	   if(full.isDir=='1'){
	        		   return '<img alt="floder" src="${ctx}/resources/images/drive_30x30/folder_30x30.png">';
	        	   }else{
	        		   return '<img alt="file" src="${ctx}/resources/images/drive_30x30/'+data+'_30x30.png">';
	        	   }
	           }
	       },
	       {
	           "aTargets": [2], 
	           "mData": "name",
	           "mRender": function (data, type, full) {
	        	  if(full.isDir=='1'){
	        		  var path=full.parentPath+full.fileName;
	        		  return "<a href=\"javascript:void(0)\" onclick=\"reloadDataTable('${ctx}/myfile/getFileJsonData?path=/"+path+"')\">"+full.name+"</a>";
	        	  }else{
	        		  return data;
	        	  }
		       	}
	           
	       },
	       {
	           "aTargets": [3], 
	           "mData": "fileSize",
	           "mRender": function (event) {
	        	  return getFileSize(event);
	           }
	       },
	       {
	           "aTargets": [4], 
	           "mData": "createDate",
	           "mRender": function (event) {
	        	   var newTime = new Date(event);
		           return  newTime.toLocaleDateString();
		        }
	       }
		],


	});

}

//删除文件
function delFile(){
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'post',
		url : '${ctx}/group/deleteFile',
		data : {
			'groupId':'${group.id}',
			'fileId':$('#fileId').val(),
			'path':'${path}',
			'currentPath':'${currentPath}'
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
					window.location.href=data.target;
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



//打开文件
function openFile(id,fileId,isDir,suffix,link,path){
	console.log(suffix);
	if(isDir==1){
		window.location.href=path;
	}else{
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
			    file:file,
			    stretching: "uniform",
			    autostart: true,
			    repeat: false,
			    volume: 100,
			    controls: true,
			    ak: '799E296BC90ca6afdf95cc32ce3083b6' // 公有云平台注册即可获得accessKey
			});
			
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
}


//-->
</script>
