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

function attachFileContext(selector) {
	context.attach(selector, [
			{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#',action:function(e){
				console.log(selector);
			}},
			{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: '#'},
			{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: '#'},
			{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: '#'},

	]);
}

function attachDirContext(selector) {
	context.attach(selector, [
		{text: '<span class="am-icon-eye">&nbsp;&nbsp;在线查看</span>', href: '#',action:function(e){
			console.log(selector);
		}},
		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>', href: '#'},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#'},
		{text: '<span class="am-icon-pencil-square-o">&nbsp;&nbsp;重命名</span>', href: '#'},
		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: '#'},
		{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: '#'},
		{text: '<span class="am-icon-history">&nbsp;&nbsp;文件时光机</span>', href: '#'},

	]);
}

function attachMultipleContext(selector){
	context.attach(selector, [
      		{text: '<span class="am-icon-download">&nbsp;&nbsp;下载</span>', href: '#'},
      		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#'},
      		{text: '<span class="am-icon-exchange">&nbsp;&nbsp;移动</span>', href: '#'},
      		{text: '<span class="am-icon-share-alt">&nbsp;&nbsp;分享</span>', href: '#'},
      	]);
}

function attachTrashContext(selector){
	context.attach(selector, [
		{text: '<span class="am-icon-download">&nbsp;&nbsp;还原</span>', href: '#'},
		{text: '<span class="am-icon-trash">&nbsp;&nbsp;删除</span>', href: '#'}
	]);
}

function destroyContext(selector){
	context.destroy(selector);
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