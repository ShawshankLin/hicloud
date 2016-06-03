function attachDirContext(groupfile) {
	context.attach("#"+groupfile.id, [
			/*{text: '<span class="am-icon-download">&nbsp;&nbsp;打包下载</span>', href: 'javascript:void(0)',action:function(e){
				window.location.href=groupfile.link;
			}},*/
			{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(groupfile.id);
				delFile();
			}},
			{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(groupfile.id);
				$('#rename-dialog').modal({
					closeViaDimmer: 0,
					width:400,
					height:150,
				});
			}},/*
			{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;保存到云盘</span>', href: 'javascript:void(0)',action:function(){
				
			}}*/
	]);
}

function attachFileContext(groupfile) {
	context.attach("#"+groupfile.id, [
		{text: '<span class="am-icon-eye">&nbsp;&nbsp;在线查看</span>', href: 'javascript:void(0)',action:function(e){
			openFile(groupfile.id,groupfile.fileId,groupfile.isDir,groupfile.type);
		}},
		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>', href: 'javascript:void(0)',action:function(e){
			window.location.href=groupfile.link;
		}},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(groupfile.id);
			delFile();
		}},
		{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(groupfile.id);
			$('#rename-dialog').modal({
				closeViaDimmer: 0,
				width:400,
				height:150,
			});
		}},
		{text: '<span class="am-icon-history">&nbsp;&nbsp;保存到云盘</span>', href: '/hicloud/group/saveMyDrive?id='+groupfile.id,action:function(e){
		
		}}

	]);
}

function destroyContext(selector){
	context.destroy("#"+selector);
}


function changeStyle(style,url){
	var cookie = $.AMUI.utils.cookie; // 获取cookie
	if(cookie.get("STYLE")!=""){
		cookie.unset("STYLE");
	}
	cookie.set("STYLE",style,cookieTime(),"/hicloud");
	window.location.href=url;
}

//设置为7天之后，date格式的时间
function cookieTime(){
	end = new Date();
	end = new Date(end.valueOf() + 7*24*60*60*1000);
	return end;
}

