<%@page contentType="text/html" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.CustomSessionListener"  %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.cache.DataConverter"  %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<%
	Map<String,String> fileTypeMap=DataConverter.getFileType();
	request.setAttribute("fileTypeMap",fileTypeMap);
	Map<String, HttpSession> sessionList = CustomSessionListener.getSessionList();
	request.setAttribute("sessionList",sessionList);
%>
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
  <link rel="stylesheet" href="${ctx}/resources/styles/friend.css"> 
  <link rel="stylesheet" href="${ctx}/resources/plugins/contextjs/css/context.standalone.css"> 
  <style type="text/css">
  .material-design {
  	position: relative;
   }
  .material-design canvas {
	  opacity: 0.25;
	  position: absolute;
	  top: 0;
	  left: 0;
	}
  </style>
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-g am-container">
  	  <div class="am-u-lg-12 am-margin-top-sm">
  	  	  <div class="am-u-lg-10 am-u-sm-9">
  	  	  	<p class="am-text-lg">我的好友</p>
  	  	  </div>
  	  	  <div class="am-u-lg-2 am-u-sm-3">
  	  	  	<button type="button" class="am-btn am-btn-primary am-btn-sm" data-am-modal="{target: '#friend-dialog', closeViaDimmer: 0, width: 400, height: 100}">
			  <i class="am-icon-user-plus"></i>
			 	添加好友
			</button>
  	  	  </div>
			
  	  </div>
  	  <div class="am-u-lg-12 am-margin-top-sm">
  	  	<div class="am-u-lg-3 am-u-sm-12">
		  		<c:if test="${not empty friends}">
		  		<div class="sidebar-content">
		  			<c:forEach items="${friends}" var="item">
		  				<div id="${item.id}" class="contact" contact-info='{"id":"${item.id}","userId":"${item.friend.id}","name":"${item.friend.name }","photo":"${ctx}${item.friend.photo}","isOnline":"${not empty sessionList[item.friend.id]}"}'>
						  <img src="${ctx}${item.friend.photo}" alt="${item.friend.name }" class="contact__photo" />
						  <span class="contact__name">${item.friend.name }</span>
						  <c:choose>
						  	 <c:when test="${not empty sessionList[item.friend.id]}">
						  		<span class="contact__status online"></span>
						  	</c:when>
						  	<c:otherwise>
						  		<span class="contact__status"></span>
						  	</c:otherwise>
						  </c:choose>
						</div>
		  			</c:forEach>
			  		</div>
		  		</c:if>
		  	</div>
		  	<div class="am-u-lg-9 am-u-sm-12">
			  	<div class="chat" style="display:none">
					<div class="chat__person">
					  <span class="chat__online active"></span>
					  <span class="chat__name"></span>
					</div>
					<div class="chat__messages">
					</div>
					<div class="chat__input">
					<button type="button" class="am-btn am-btn-primary am-btn-block" data-am-modal="{target: '#myfile-dialog', closeViaDimmer: 0, width: 600, height: 500}">发送文件</button>
					</div>
					<img src="${ctx}/resources/images/user/2.png" alt="" id="contact-photo" class="contact__photo cloned" style="top: 1.8rem;right:0.5rem;">
			  </div>
		  	
		  </div>
  	  </div>
  	  		  	

</div>

<!-- 添加好友 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="friend-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	添加好友
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd"> 	
	     <form class="am-form-inline" role="form" id="friend-form">
		      <div class="am-form-group">
			    <input type="text" class="am-form-field" placeholder="输入好友帐号" name="friendName" id="friendName" onkeydown="if(event.keyCode==13) return false;" autofocus>
			  </div>
		      <button type="button" class="am-btn am-btn-primary" id="friend-bt">添加</button>
	    </form>
    </div>

  </div>
</div>
<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<!-- 文件弹出框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="myfile-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	选择从云盘添加到群里的文件
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
		  <table class="am-table am-table-striped am-table-hover table-main " id="myfile-table" cellspacing="0" width="100%">
          
          </table>
    </div>
    <div class="am-modal-foot">
    	<button type="button" class="am-btn am-btn-primary am-fr am-margin-right-xl" id="toshare-bt">确定</button>
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


<input type="hidden" name="toId" id="toId">
<input type="hidden" name="fileIds" id="fileIds">

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>

<!-- contextjs -->
<script type="text/javascript" src="${ctx}/resources/plugins/contextjs/js/context.js"></script>
<%-- <script src="${ctx}/resources/plugins/shuibo/js/prefixfree.min.js"></script>
<script src="${ctx}/resources/plugins/shuibo/js/index.js"></script> --%>
<script type="text/javascript" src="https://cdn.datatables.net/plug-ins/1.10.11/api/fnReloadAjax.js"></script>
<script type="text/javascript">
	$(function(){
		$('#friend-bt').click(function(){
			$('#friend-dialog').modal('close');
			$('#my-modal-loading').modal('open');
			$.ajax({
				type : 'post',
				url : '${ctx}/friend/sendFriendRQ',
				data : {
					"friendName" : $('#friendName').val()
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
		
		
		$('.contact').each(function(){
			var friend=$(this).attr("contact-info");
			friend=eval('(' + friend + ')');
			attachFriendContext(friend);
	    }).click(function(){
			var contact=$(this).attr("contact-info");
			contact=eval('(' + contact + ')');
			$('#toId').val(contact.userId);
			$('.chat__name').text(contact.name);
			if(contact.isOnline=='true'){
				$('.chat__online').show();
			}else{
				$('.chat__online').hide();
			}
			$('#contact-photo').attr('src',contact.photo);
			$('.chat').addClass('am-animation-slide-right').show();
			getShareHistory();
		});
		
		
		initDataTable();
		
		$('#myfile-table').on('click', '.itembox', function () {
			var fileIds="";
			$('.itembox').each(function(i,item){
				if($(item).is(':checked')){
					fileIds=fileIds+$(item).val()+","
				}
		   	});
			$('#fileIds').val(fileIds)
		});
		
		$('#toshare-bt').click(function(){
			$('#myfile-dialog').modal('close');
			var fileIds=$('#fileIds').val();
			if(fileIds==""){
				warning('请选择发送文件');
				return;
			}
			$('#my-modal-loading').modal('open');
			  $.ajax({
					type : 'get',
					url : '${ctx}/share/toshare',
					data : {
						"fileId" : fileIds,
						"toId":$('#toId').val(),
						"type": 3
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
								getShareHistory();
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

	//初始化datatable
	function initDataTable(){
		$.ajax({
		    url: '${ctx}/myfile/getFileJsonData',
		    type: 'GET',
		    dataType: 'json',
		    success: function (data) {
		        assignToEventsColumns(data);
		    }
		});
	}

	//重载datatable
	function reloadDataTable(link){
		table.fnReloadAjax(link);	
	}
	
	function assignToEventsColumns(data) {
		table = $('#myfile-table').dataTable({
		    "bAutoWidth": false,
		    "aaData": data,	
		    "sScrollY": 320,
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
	

	
	//获取分享历史
	function getShareHistory(){
		$.ajax({
			type : 'get',
			url : '${ctx}/share/getShareHistory',
			data : {
				"toId":$('#toId').val(),
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
						var history=data.data;
						$('.chat__messages').empty();
						for(var i=0;i<history.length;i++){
							if(history[i].createId=="${LOGINUSER.id}"){
								$('.chat__messages').append("<div class=\"chat__msgRow\"><div class=\"chat__message mine\"><div><img src=\"${ctx }/resources/images/drive_80x80/"+history[i].file.suffix+"_80x80.png\" style=\"float:left;\"></img></div>"+history[i].file.name+"<br><a href='${ctx}/myfile/download?fileId="+history[i].file.id+"'>下载</a>&nbsp;&nbsp;<a href='${ctx}/myfile/preview?fileId="+history[i].file.id +"' target='_black'>预览</a></div></div>");
							}else{
								$('.chat__messages').append("<div class=\"chat__msgRow\"><div class=\"chat__message notMine\"><div><img src=\"${ctx }/resources/images/drive_80x80/"+history[i].file.suffix+"_80x80.png\" style=\"float:right;\"></img></div>"+history[i].file.name+"<br><a href='${ctx}/myfile/download?fileId="+history[i].file.id+"'>下载</a>&nbsp;&nbsp;<a href='${ctx}/myfile/preview?fileId="+history[i].file.id +"' target='_black'>预览</a></div></div>");
							}
						}
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
	
	
	function attachFriendContext(friend) {
		context.attach("#"+friend.id, [
  				{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
  					delFriend(friend.id);
  				}},
  				{text: '<span class="am-icon-edit">&nbsp;备注</span>', href: 'javascript:void(0)',action:function(e){
  					alert('功能开发中');
  				}}
      	]);
	}
	
	
	function destroyContext(selector){
		context.destroy("#"+selector);
	}
	
	
	function delFriend(id){
		$.ajax({
			type : 'get',
			url : '${ctx}/friend/removeFriend',
			data : {
				"id":id,
			},
			dataType:'json',
			error : function(request) {
				$('#my-modal-loading').modal('close');
				warning('连接失败');
			},
			success : function(data) {
				$('#my-modal-loading').modal('close');
				console.log(data);
				try {
					switch(data.status){
					case "success":
						success(data.message);
						window.location.href = data.target;
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
	function openFile(fileId,isDir,suffix,link,path){
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

</script>
</body>
</html>
