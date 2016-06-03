//消息提示框
function msg(msg,fnt){
	var html="<div class=\"am-modal am-modal-alert\" tabindex=\"-1\" id=\"msg\"><div class=\"am-modal-dialog\">"
		+"<div class=\"am-modal-hd\">消息</div><div class=\"am-modal-bd\">"+msg+"</div><div class=\"am-modal-footer\">"
	    +"<span class=\"am-modal-btn\">确定</span></div></div></div>";
	$(document.body).append(html);
	$('#msg').modal('open');
};

// 消息确认框
function confirm(msg){
	var html="<div class=\"am-modal am-modal-confirm\" tabindex=\"-1\" id=\"confirm\">"+
			"<div class=\"am-modal-dialog\">"+
			"<div class=\"am-modal-hd\">提示</div>"+
			"<div class=\"am-modal-bd\">"+
				msg +
			"</div>"+
			"<div class=\"am-modal-footer\">"+
			"<span class=\"am-modal-btn\" data-am-modal-cancel>取消</span>"+
			"<span class=\"am-modal-btn\" data-am-modal-confirm>确定</span>"+
			"</div>"+
			"</div>"+
			"</div>";
	$(document.body).append(html);
}

// 消息输入框
function prompt(msg){
	var html="<div class=\"am-modal am-modal-prompt\" tabindex=\"-1\" id=\"prompt\">"+
				"<div class=\"am-modal-dialog\">"+
				"<div class=\"am-modal-hd\">提示</div>"+
				"<div class=\"am-modal-bd\">"
					+msg+
				"<input type=\"text\" class=\"am-modal-prompt-input\">"+
				"</div>"+
				"<div class=\"am-modal-footer\">"+
				"<span class=\"am-modal-btn\" data-am-modal-cancel>取消</span>"+
				"<span class=\"am-modal-btn\" data-am-modal-confirm>提交</span>"+
				"</div>"+
				"</div>"+
				"</div>";
	$(document.body).append(html);
}

// 成功消息
function success(msg,time){
	var html="<div class=\"am-alert am-alert-success msg\" data-am-alert id=\"success\"><button type=\"button\" class=\"am-close\">&times;</button>"
		  +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#success').alert();
	if(time==undefined){
		time=1000;
	}
	setInterval("$('#success').alert('close')", time );
}

// 警告消息
function warning(msg,time){
	var html="<div class=\"am-alert am-alert-warning msg\" data-am-alert id=\"warning\"><button type=\"button\" class=\"am-close\">&times;</button>"
		 +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#warning').alert();
	if(time==undefined){
		time=1000;
	}
	setInterval("$('#warning').alert('close')", time );
}

// 危险消息
function error(msg,time){
	var html="<div class=\"am-alert am-alert-danger msg\" data-am-alert id=\"danger\"><button type=\"button\" class=\"am-close\">&times;</button>"
		 +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#danger').alert();
	if(time==undefined){
		time=1000;
	}
	setInterval("$('#danger').alert('close')", time );
}


// 次要消息
function secondary(msg,time){
	var html="<div class=\"am-alert am-alert-secondary msg\" data-am-alert id=\"secondary\"><button type=\"button\" class=\"am-close\">&times;</button>"
		  +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#secondary').alert();
	if(time==undefined){
		time=1000;
	}
	setInterval("$('#secondary').alert('close')", time );
}


function getUrlParam(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  // 匹配目标参数
    if (r!=null) return unescape(r[2]); return null; // 返回参数值
} 


function getFileSize(fileSize){
	var showSize="";  
	if (fileSize == 0) {
	    showSize = "-";
	  } else if (fileSize > 0 && fileSize < 1024) {
	    showSize = fileSize.toFixed(2) + "B";
	  } else if (fileSize >= 1024 && fileSize < (1024 * 1024)) {
	    showSize = (fileSize / 1024).toFixed(2) + "KB";
	  } else if (fileSize >= (1024 * 1024) && fileSize < (1024 * 1024 * 1024)) {
	    showSize = (fileSize / (1024 * 1024)).toFixed(2) + "MB";
	  } else if (fileSize >= (1024 * 1024 * 1024)) {
	    showSize = (fileSize / (1024 * 1024 * 1024)).toFixed(2) + "GB";
	  }
	return showSize;
}

//设置为7天之后，date格式的时间
function cookieTime(){
	end = new Date();
	end = new Date(end.valueOf() + 7*24*60*60*1000);
	return end;
}

//二维码中文
function utf16to8(str) {  
    var out, i, len, c;  
    out = "";  
    len = str.length;  
    for(i = 0; i < len; i++) {  
    c = str.charCodeAt(i);  
    if ((c >= 0x0001) && (c <= 0x007F)) {  
        out += str.charAt(i);  
    } else if (c > 0x07FF) {  
        out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));  
        out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));  
        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));  
    } else {  
        out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));  
        out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));  
    }  
    }  
    return out;  
} 