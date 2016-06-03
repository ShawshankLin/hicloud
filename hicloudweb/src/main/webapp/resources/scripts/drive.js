function extbkboxnar(){
	var narrowmen = document.getElementById("extbkboxnar");
	var narrowbox = document.getElementById("extbkboxb");
	if (narrowbox.style.display == "block"){
		narrowbox.style.display = "none";
		narrowmen.className = "extbkboxnarove";
		}
		else{
		narrowbox.style.display = "block";
		narrowmen.className = "extbkboxnar";
		}		
	}
	
function extbkboxove(str){document.getElementById("extbkbox").style.display = "none";}

function attachDirContext(file) {
	context.attach("#"+file.id, [
			{text: '<span class="am-icon-download">&nbsp;&nbsp;打包下载</span>', href: 'javascript:void(0)',action:function(e){
				window.location.href=file.link;
			}},
			{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(file.id);
				delFile();
			}},
			{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(file.id);
				$('#rename-dialog').modal({
					closeViaDimmer: 0,
					width:400,
					height:150,
				});
			}},
			{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(file.id);
				initMove();
			}},
			/*{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: 'javascript:void(0)',action:function(){
				$('#fileId').val(file.id);
				$('#share-dialog').modal('open');
			}}*/

	]);
}

function attachFileContext(file) {
	context.attach("#"+file.id, [
		{text: '<span class="am-icon-eye">&nbsp;&nbsp;在线查看</span>', href: 'javascript:void(0)',action:function(e){
			openFile(file.id,file.isDir,file.type);
		}},
		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>', href: 'javascript:void(0)',action:function(e){
			window.location.href=file.link;
		}},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(file.id);
			delFile();
		}},
		{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(file.id);
			$('#rename-dialog').modal({
				closeViaDimmer: 0,
				width:400,
				height:150,
			});
		}},
		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>',href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(file.id);
			initMove();
		}},
		{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: 'javascript:void(0)',action:function(){
			$('#fileId').val(file.id);
			$('#share-dialog').modal('open');
		}},
		{text: '<span class="am-icon-history">&nbsp;&nbsp;文件时光机</span>', href: 'javascript:void(0)',action:function(e){
			historyFile(file.id);
		}}

	]);
}

function attachMultipleContext(file){
	context.attach("#"+file.id, [
      		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>',href: 'javascript:void(0)',action:function(e){
      			window.location.href=file.link.substring(0,file.link.indexOf("=")+1).concat($('#fileId').val());
    		}},
      		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
      			$('#fileId').val(file.id);
      			delFile();
    		}},
      		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: 'javascript:void(0)',action:function(e){
				$('#fileId').val(file.id);
				initMove();
			}},
      		/*{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: 'javascript:void(0)',action:function(){
      			$('#fileId').val(file.id);
      			$('#share-dialog').modal('open');
    		}}*/
      	]);
}

function attachTrashContext(file){
	context.attach("#"+file.id, [
		{text: '<span class="am-icon-download">&nbsp;&nbsp;还原</span>', href: 'javascript:void(0)',action:function(){
			$('#fileId').val(file.id);
			restoreFile();
		}},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: 'javascript:void(0)',action:function(e){
			$('#fileId').val(file.id);
			delComplete();
		}},
	]);
}

function destroyContext(selector){
	context.destroy("#"+selector);
}

function showToolBar(toolbar){
	var selector="#"+toolbar;
	$(selector).show().siblings().hide();
}

function hideToolBar(){
	$('#file-toolbar').hide();
	$('#dir-toolbar').hide();
	$('#multiple-toolbar').hide();
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

