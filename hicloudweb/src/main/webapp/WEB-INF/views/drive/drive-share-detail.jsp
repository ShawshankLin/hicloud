<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>
<jsp:useBean id="fileUtil" class="cn.edu.cylg.cis.hicloud.utils.FileUtil" scope="page"/>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>分享文件${share.file.name}</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/styles/grid.css">
  <style>
    .toolbar{
    	width:350px;
    	height:50px;
    	margin:0 auto;
    }
    .my-toolbar{
    	width:250px;
    	height:50px;
    	margin:0 auto;
    }
    .img{
    	background: url(${ctx}/resources/images/drive_80x80/${share.file.suffix}_80x80.png) no-repeat;
    	width:80px;
    	height:80px;
    	margin:0 auto;
    }
    .text{
    	width:300px;
    	height:30px;
    	margin:0 auto;
    	font-size:11px;
    	text-align:center;
    	margin-top:20px;
    }
  </style>
</head>
<body>
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-cf">
		<div class="am-g am-margin-top-xl">
			<div class="am-u-lg-4 am-u-md-6 am-u-sm-centered">
				<c:choose>
          			<c:when test="${share.file.isDir==1}"><!-- 文件夹 -->
          						<div id="${share.file.id}" file-info='{id:"${share.file.id}",isDir:"${share.file.isDir}",type:"${fileUtil.getFileTypeBySuffix(share.file.suffix)}",link:"${ctx}/myfile/download?fileId=${share.file.id}",path:"${ctx}/myfile/grid?path=${fn:trim(path)}/${share.file.fileName }"}'>
          							<div class="img"></div>
          							<div class="text">
          								${share.file.fileName }
          							</div>
          						</div>
        					</c:when>
        					<c:otherwise><!-- 文件 -->
        						<div id="${share.file.id}" file-info='{id:"${share.file.id}",isDir:"${share.file.isDir}",type:"${fileUtil.getFileTypeBySuffix(share.file.suffix)}",link:"${ctx}/myfile/download?fileId=${share.file.id}"}'>
        							<div class="img"></div>
        							<div class="text">
        								${share.file.name}.${share.file.suffix }
        							</div>
        						</div>
        					</c:otherwise>
        		</c:choose>
        		<c:if test="${LOGINUSER.id!=share.file.createId }">
        		<div class="am-btn-toolbar toolbar">
				  <div class="am-btn-group">
				  	  <button type="button" class="am-btn am-btn-default" onclick="openFile()">在线预览</button>
					  <button type="button" class="am-btn am-btn-default" id="save-drive-bt">保存至网盘
					  <button class="am-btn am-btn-default" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId=${share.file.id}'">下载</button>
					  <div class="am-dropdown" data-am-dropdown>
					    <button class="am-btn am-btn-default am-dropdown-toggle" data-am-dropdown-toggle> <span class="am-icon-caret-down"></span></button>
					    <ul class="am-dropdown-content">
					      <li><span id="qrcode"></span></li>
					    </ul>
					  </div>
					</div>
				</div>
        		</c:if>
				<c:if test="${LOGINUSER.id==share.file.createId }">
					<div class="am-btn-toolbar my-toolbar">
					<div class="am-btn-group">
						<button type="button" class="am-btn am-btn-default" onclick="openFile()">在线预览</button>
						<button class="am-btn am-btn-default" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId=${share.file.id}'">下载</button>
					  <div class="am-dropdown" data-am-dropdown>
					    <button class="am-btn am-btn-default am-dropdown-toggle" data-am-dropdown-toggle> <span class="am-icon-caret-down"></span></button>
					    <ul class="am-dropdown-content">
					      <li><span id="qrcode"></span></li>
					    </ul>
					  </div>
					</div>
					</div>

				</c:if>
			</div>
		  </div>
		<div class="am-g am-margin-top-xl">
		  <div class="am-u-lg-10 am-u-md-10 am-u-sm-centered">
				<table class="am-table am-table-centered ">
			    <thead>
			        <tr>
			            <td>创建人</td>
			            <td>文件类型</td>
			            <td>文件大小</td>
			            <td>创建时间</td>
			        </tr>
			    </thead>
			    <tbody>
			        <tr>
			            <td>${share.file.createName }</td>
			            <td>${share.file.suffix }</td>
			            <td><tags:fileSize fileSize="${share.file.fileSize}"/></td>
			            <td><fmt:formatDate value="${share.file.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			        </tr>

			    </tbody>
			</table>
		  </div>
		</div>
		
		<div class="am-g am-margin-top-xl">
		  <div class="am-u-lg-10 am-u-md-10 am-u-sm-centered">
		  		<!-- 多说评论框 start -->
				<div class="ds-thread" data-thread-key="${share.id }" data-title="${share.file.createName }" data-url="请替换成文章的网址"></div>
				<!-- 多说评论框 end -->
				<!-- 多说公共JS代码 start (一个网页只需插入一次) -->
				<script type="text/javascript">
				var duoshuoQuery = {short_name:"hicloud"};
					(function() {
						var ds = document.createElement('script');
						ds.type = 'text/javascript';ds.async = true;
						ds.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') + '//static.duoshuo.com/embed.js';
						ds.charset = 'UTF-8';
						(document.getElementsByTagName('head')[0] 
						 || document.getElementsByTagName('body')[0]).appendChild(ds);
					})();
					</script>
				<!-- 多说公共JS代码 end -->
		  
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
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<script type="text/javascript" src="${ctx}/resources/scripts/jquery.qrcode.min.js"></script>
<script>
	$(function(){		
		//保存网盘
		$('#save-drive-bt').click(function(){
			$('#my-modal-loading').modal('open');
			  $.ajax({
					type : 'get',
					url : '${ctx}/share/saveMyDrive',
					data : {
						'fileId':'${share.file.id}'
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
		
	});

	var name=utf16to8('${share.file.name}');
	$('#qrcode').qrcode("{\"id\":\"${share.file.id}\",\"name\":\""+name+"\",\"suffix\":\"${share.file.suffix}\",\"link\":\"/api/myfile/myfile/download?fileId=${share.file.id}\"}");	
	
	function openFile(){
		switch('${fileUtil.getFileTypeBySuffix(share.file.suffix)}'){
		case '<%=FileType.DOCUMENT%>'://文档	
			$('body').append('<a href="${ctx}/myfile/preview?fileId=${share.file.id}" id="goto" target="_blank"></a>');
			$('#goto').get(0).click();
			$('#goto').remove();
			break;
		case '<%=FileType.VEDIO%>'://视频
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
  </script>		
</body>
</html>
