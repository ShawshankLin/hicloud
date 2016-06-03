<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.edu.cylg.cis.hicloud.core.cache.DataConverter"  %>
<%@ page import="cn.edu.cylg.cis.hicloud.core.constants.FileType"  %>

<%
	Map<String,String> fileTypeMap=DataConverter.getFileType();
	request.setAttribute("fileTypeMap",fileTypeMap);
%>

<!-- 文件上传框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="upload-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	上传文件到个人网盘
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd" id="uploader" style="text-align:left;overflow-y:scroll;height:300px;">      
             <div class="am-panel am-panel-default">
			    <div class="am-panel-bd">
			    	 <ul class="am-list admin-content-file" id="theList">
		            </ul> 
			    </div>
			</div>
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

<!-- 移动对话框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="move-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	移动文件（夹）
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
		<div class="zTreeDemoBackground" style="margin-left: 30px;margin-top: 20px;">
			<ul id="file-tree" class="ztree"></ul>
		</div>
		<input type="hidden" name="dirId" id="dirId">
    </div>
    <div class="am-modal-foot">
     	<button type="button" class="am-btn am-btn-default" onclick="$('#move-dialog').modal('close');">取消</button>
	    <button type="button" class="am-btn am-btn-primary" id="move-bt">确定移动</button>
    </div>
  </div>
</div>

<!-- 分享对话框 -->
<div class="am-modal am-modal-no-btn" tabindex="-1" id="share-dialog">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">
     	分享文件（夹）
      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
    </div>
    <div class="am-modal-bd">
      	<button type="button" class="am-btn am-btn-primary" onclick="shareFile('1')">创建公开链接</button>
	    <button type="button" class="am-btn am-btn-primary" onclick="shareFile('2')">创建私密链接</button>
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
<script src="${ctx}/resources/plugins/webuploader/md5.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/resources/plugins/webuploader/webuploader.js" type="text/javascript" charset="utf-8"></script>

<!-- contextjs -->
<script type="text/javascript" src="${ctx}/resources/plugins/contextjs/js/context.js"></script>
<!-- drivejs -->
<script type="text/javascript" src="${ctx}/resources/scripts/drive.js"></script>
<!-- baidu media -->
<script type="text/javascript" src="${ctx}/resources/plugins/Baidu-T5Player-SDK-Web-v2.0.2/player/cyberplayer.js"></script>
<!-- amazeui tree -->
<script type="text/javascript" src="${ctx}/resources/AmazeUI-2.5.2/assets/js/amazeui.tree.min.js"></script>
<!-- zeroclipboard -->
<script type="text/javascript" src="${ctx}/resources/plugins/zeroclipboard/ZeroClipboard.min.js"></script>
<!-- ztree -->
<script type="text/javascript" src="${ctx}/resources/plugins/ztree/js/jquery.ztree.core-3.5.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/ztree/js/jquery.ztree.excheck-3.5.min.js" ></script>	
<%-- <script type="text/javascript" src="${ctx}/resources/plugins/jwplayer/jwplayer.js"></script>
<script type="text/javascript">jwplayer.key="ABCDEFGHIJKLMOPQ";</script> --%>

<script type="text/javascript">
            var userInfo = {userId:'${LOGINUSER.id}', md5:""};//用户会话信息
            var chunkSize = 5000 * 1024;        //分块大小
            var uniqueFileName = null;          //文件唯一标识符
            var md5Mark = null;

            var backEndUrl = "${ctx}/myfile/upload";

            WebUploader.Uploader.register({
            	"add-file":"addFile",
                "before-send-file": "beforeSendFile"
                , "before-send": "beforeSend"
                , "after-send-file": "afterSendFile"
            }, {
            	addFile:function(files){
    				$('#upload-dialog').modal({
    			        relatedTarget: this,
    			        closeViaDimmer: 0, 
    			        width: 600, 
    			        height: 380,
    			        closeViaDimmer:false
    			      });
            	},
                beforeSendFile: function(file){                	
                    //秒传验证
                    var task = new $.Deferred();
                    var start = new Date().getTime();
                    (new WebUploader.Uploader()).md5File(file, 0, 10*1024*1024).progress(function(percentage){
                        //console.log(percentage);
                    }).then(function(val){
                        //console.log("总耗时: "+((new Date().getTime()) - start)/1000);
                        md5Mark = val;
                        userInfo.md5 = val;
                        $.ajax({
                            type: "POST",
                            url: backEndUrl,
                            data: {
                            	opt: "md5Check",
                            	path:'${path}',
                            	md5: val
                            },
                            cache: false,
                            timeout: 1000,//todo 超时的话，只能认为该文件不曾上传过,
                            dataType: "json"
                        }).then(function(data, textStatus, jqXHR){
                            if(data.ifExist){   //若存在，这返回失败给WebUploader，表明该文件不需要上传
                                task.reject();
                                uploader.skipFile(file);
                                file.path = data.path;
                                UploadComlate(file);
                            }else{
                                task.resolve();
                                //拿到上传文件的唯一名称，用于断点续传
                                uniqueFileName = md5(''+userInfo.userId+file.name+file.type+file.lastModifiedDate+file.size);
                            }
                        }, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
                            task.resolve();
                            //拿到上传文件的唯一名称，用于断点续传
                            uniqueFileName = md5(''+userInfo.userId+file.name+file.type+file.lastModifiedDate+file.size);
                        });
                    });
                    return $.when(task);
                }
                , beforeSend: function(block){
                    //分片验证是否已传过，用于断点续传
                    var task = new $.Deferred();
                    $.ajax({
                        type: "POST",
                        url: backEndUrl,
                        data: {
                        	opt: "chunkCheck",
                        	path:'${path}',
                        	name: uniqueFileName,
                        	chunkIndex: block.chunk,
                        	size: block.end - block.start
                        }
                        , cache: false
                        , timeout: 1000 //todo 超时的话，只能认为该分片未上传过
                        , dataType: "json"
                    }).then(function(data, textStatus, jqXHR){
                        if(data.ifExist){   //若存在，返回失败给WebUploader，表明该分块不需要上传
                            task.reject();
                        }else{
                            task.resolve();
                        }
                    }, function(jqXHR, textStatus, errorThrown){    //任何形式的验证失败，都触发重新上传
                        task.resolve();
                    });

                    return $.when(task);
                }
                , afterSendFile: function(file){
                	var uniqueFileName = md5(''+userInfo.userId+file.name+file.type+file.lastModifiedDate+file.size);
                	var chunksTotal = 0;
                    if((chunksTotal = Math.ceil(file.size/chunkSize)) > 1){
                        //合并请求
                        var task = new $.Deferred();
                        $.ajax({
                            type: "POST",
                            url: backEndUrl,
                            data: {
                            	opt: "chunksMerge",
                            	path:'${path}',
                            	name: uniqueFileName,
                            	chunks: chunksTotal,
                            	ext: file.ext,
                            	md5: md5Mark,
                            	size:file.size,
                            	realName:file.name
                            }
                            , cache: false
                            , dataType: "json"
                        }).then(function(data, textStatus, jqXHR){

                            //todo 检查响应是否正常

                            task.resolve();
                            file.path = data.path;
                            UploadComlate(file);

                        }, function(jqXHR, textStatus, errorThrown){
                            task.reject();
                        });

                        return $.when(task);
                    }else{
                        UploadComlate(file);
                    }
                }
            });

			var uploader = WebUploader.create({
				swf: "Uploader.swf"
				, server: backEndUrl
				, pick: "#picker"
				, resize: false
				, dnd: "#theList"
				, paste: document.body
				, disableGlobalDnd: true
				, thumb: {
					width: 100
					, height: 100
					, quality: 70
					, allowMagnify: true
					, crop: true
					//, type: "image/jpeg"
				}
//				, compress: {
//					quality: 90
//					, allowMagnify: false
//					, crop: false
//					, preserveHeaders: true
//					, noCompressIfLarger: true
//					,compressSize: 100000
//				}
                , compress: false
				, prepareNextFile: true
				, chunked: true
				, chunkSize: chunkSize
				, threads: true
				, formData: function(){return $.extend(true, {}, userInfo);}
				, fileNumLimit: 5
				, fileSingleSizeLimit: 1000 * 1024 * 1024
				, duplicate: true,
				formData:{
					'path':'${path}',
                	'userId':'${LOGINUSER.id}',
                	'md5': userInfo.md5,
				}
			});

			uploader.on("fileQueued", function(file){	
				$("#theList").append('<li id="'+file.id+'">' +
						'<img /><strong><span class=\"am-icon-upload icon\"></span>'+file.name+'</strong><p><span class="hasUpload"></span> of '+getFileSize(file.size)+'- <span class="time"></span> - <span class="speed"></span>MB/Sec</p><div class=\"am-progress am-progress-striped am-progress-sm am-active\">'+
			                  '<div class=\"am-progress-bar am-progress-bar-success percentage\" style=\"width: 0%\">0%</div>'+
			                '</div><span class=\"itemUpload\">上传</span><span class=\"itemStop\">暂停</span><span class=\"itemDel\">删除</span>' +
						'</li>');
				
				var $img = $("#" + file.id).find("img");
				uploader.makeThumb(file, function(error, src){
					if(error){
						//$img.replaceWith("<span>不能预览</span>");
					}

					$img.attr("src", src);
				});
								
				uploader.upload();
			});
			
			$("#theList").on("click", ".itemUpload", function(){
				uploader.upload();
                //"上传"-->"暂停"
                $(this).hide();
                $(".itemStop").show();
			});

            $("#theList").on("click", ".itemStop", function(){
                uploader.stop(true);
                //"暂停"-->"上传"
                $(this).hide();
                $(".itemUpload").show();
            });

            //todo 如果要删除的文件正在上传（包括暂停），则需要发送给后端一个请求用来清除服务器端的缓存文件
			$("#theList").on("click", ".itemDel", function(){
				uploader.removeFile($(this).parent().attr("id"));	//从上传文件列表中删除

				$(this).parent().remove();	//从上传列表dom中删除
			});
			
			var start = new Date().getTime();
			uploader.on("uploadProgress", function(file, percentage){
				var time=((new Date().getTime()) - start)/1000;
				var int=setInterval(speed(file,percentage),1000);
				$("#" + file.id+" .time").text(Math.round(time)+"Sec");
				$("#" + file.id+" .hasUpload").text(getFileSize(file.size*percentage));
				$("#" + file.id + " .percentage").css("width",Math.round(percentage * 100) + "%");
				$("#" + file.id + " .percentage").text(Math.round(percentage * 100) + "%");
				if(percentage * 100==100){
					clearInterval(int);
				}
			});
			
			function speed(file,percentage){
				//var secsize=file.size*percentage-size;
				var speed=Math.round(file.size/1024/1024*percentage);
				$("#"+file.id+" .speed").text(speed);
			}

            function UploadComlate(file){
            	$("#" + file.id+" .hasUpload").text(getFileSize(file.size));
                $("#" + file.id + " .percentage").css("width","100%");
                $("#" + file.id + " .percentage").text("上传完毕");
                $("#" + file.id + " .icon").removeClass("am-icon-upload").addClass("am-icon-check");
                $(".itemStop").hide();
                $(".itemUpload").hide();
                $(".itemDel").hide();
            }
		</script>


<script type="text/javascript">
var setting = {  
        check: {  
            enable: true  
        },  
        data: {  
            simpleData: {  
                enable: true  
            }  
        },
        callback: {
    		onClick: zTreeOnClick
    	}
};

var setting = {
		async: {
			enable: true,
			url : '${ctx}/myfile/getDirTree',
			autoParam:["id=rootId"],
			dataFilter: filter,
			dataType: 'json'
		},
		callback: {//回调函数，在这里可做一些回调处理 
			onClick: zTreeOnClick
		},
		//获取数据时节点Id和父id对应json的属性名,rootPId 为根节点的id
		data: { 
			simpleData: { 
				enable : true, 
				idKey : "id", 
				pIdKey : "pId",
				rootPId: 0
		},
		//显示节点名称时对应的json数据里面的属性
		key: {
			name: "name"
		} 
	} 
};

//异步请求返回数据的处理
function filter(treeId, parentNode, childNodes) {
	if (!childNodes) return null;
	return childNodes;
}

//点击事件
function zTreeOnClick(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("file-tree");
	zTree.reAsyncChildNodes(treeNode,"refresh",true);
	$('#dirId').val(treeNode.id);
} 

$(function(){
	
	$.fn.zTree.init($("#file-tree"), setting);
	
	//初始化
    context.init({
	    fadeSpeed: 100,
	    filter: function ($obj){},
	    above: 'auto',
	    preventDoubleContext: true,
	    compress: false
	});
	

	
  	//创建文件夹
	$('#mkdir-bt').click(function(){
		$('#mkdir-dialog').modal('close');
		$('#my-modal-loading').modal('open');
		$.ajax({
			type : 'post',
			url : '${ctx}/myfile/mkdir',
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
						success(data.message,3000);
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
	
	//重命名操作
	$('#rename-bt').click(function(){
		$('#rename-dialog').modal('close');
		$('#my-modal-loading').modal('open');
		$.ajax({
			type : 'post',
			url : '${ctx}/myfile/rename',
			data : {
				'fileId':$('#fileId').val(),
				'newname':$('#newname').val(),
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
						success(data.message,3000);
						window.location.href=data.target;
						break;
					case "error":
						error(data.message,3000);
						break;
					}
				} catch (e) {
					warning('操作失败'+e);
				}
				
			}
		});
	});
	
	
	$('#move-bt').click(function(){
		$('#move-dialog').modal('close');
		$('#my-modal-loading').modal('open');
		$.ajax({
			type : 'post',
			url : '${ctx}/myfile/move',
			data : {
				'fileId':$('#fileId').val(),
				'dirId':$('#dirId').val(),
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
						success(data.message,3000);
						window.location.href=data.target;
						break;
					case "error":
						error(data.message,3000);
						break;
					}
				} catch (e) {
					warning('操作失败'+e);
				}
				
			}
		});
		
	});
	
	//定义一个新的复制对象
	var clip = new ZeroClipboard($('#copy-link-bt'), {
	  moviePath: "${ctx}/resources/plugins/zeroclipboard/ZeroClipboard.swf"
	});
	
	//关闭上传dialog,刷新页面
	$('#upload-dialog').on('closed.modal.amui', function(){
		location.reload();
	});
	
});


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
					success(data.message,3000);
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

//文件时光机
function historyFile(fileId){
	if($('#fileId').val()==""){
		$('#fileId').val(fileId);
	}
	window.location.href="${ctx}/myfile/history?isPage=true&fileId="+$('#fileId').val();
}

//分享
function shareFile(type){
	$('#share-dialog').modal('close');
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'post',
		url : '${ctx}/share/toshare',
		data : {
			'fileId':$('#fileId').val(),
			'type':type
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
}


function initMove(){
	$('#move-dialog').modal({
		closeViaDimmer: 0,
		width: 506, 
		height: 361
	});
}



</script>
