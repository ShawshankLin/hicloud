<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"%>

<!-- 分享对话框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="share-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	分享文件（夹）
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
      	<button type="button" class="am-btn am-btn-primary" onclick="shareFile('1')">创建公开链接</button>
	    <button type="button" class="am-btn am-btn-primary" onclick="shareFile('0')">创建私密链接</button>
    </div>
  </div>
</div>
<!-- 分享链接 -->
<div class="am-modal am-modal-confirm" tabindex="-1" id="share-link-dialog">
  <div class="am-modal-dialog">
  	<div id="share-link">  		
  	</div>
    <div class="am-modal-footer">
      <span class="am-modal-btn" data-am-modal-cancel>取消</span>
      <span class="am-modal-btn" id="copy-link-bt" data-clipboard-target="share-link-input" onclick="success('链接已复制到剪切板');">复制链接</span>
    </div>
  </div>
</div>

<!-- drivejs -->
<script type="text/javascript" src="${ctx}/resources/scripts/drive.js"></script>
<!-- contextjs -->
<script type="text/javascript" src="${ctx}/resources/plugins/contextjs/js/context.js"></script>

<!-- baidu media -->
<script type="text/javascript" src="${ctx}/resources/plugins/Baidu-T5Player-SDK-Web-v2.0.2/player/cyberplayer.js"></script>

<!-- zeroclipboard -->
<script type="text/javascript" src="${ctx}/resources/plugins/zeroclipboard/ZeroClipboard.min.js"></script>

<script type="text/javascript">
//删除文件
function delFile(){
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'post',
		url : '${ctx}/myfile/delete',
		data : {
			'fileId':$('#fileId').val(),
			'path':'${path}'
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



//分享
function shareFile(isPublic){
	$('#share-dialog').modal('close');
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'post',
		url : '${ctx}/share/toshare',
		data : {
			'fileId':$('#fileId').val(),
			'isPublic':isPublic
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
					$('#share-link').text("").prepend(data.data);
					$('#share-link-dialog').modal('open');
					break;
				case "error":
					warning(data.message);
					break;
				default:
					warning('请重新请求');
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
			//var file='${ctx}/myfile/download?fileId='+fileId;
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
			warning("不支持文件类型");
			return;
	}
	}
}


</script>
