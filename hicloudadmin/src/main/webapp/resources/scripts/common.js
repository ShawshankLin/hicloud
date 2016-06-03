//消息提示框
function msg(msg,fnt){
	var html="<div class=\"am-modal am-modal-alert\" tabindex=\"-1\" id=\"msg\"><div class=\"am-modal-dialog\">"
		+"<div class=\"am-modal-hd\">消息</div><div class=\"am-modal-bd\">"+msg+"</div><div class=\"am-modal-footer\">"
	    +"<span class=\"am-modal-btn\">确定</span></div></div></div>";
	$(document.body).append(html);
	$('#msg').modal('open');
};

//成功消息
function success(msg,time){
	var html="<div class=\"am-alert am-alert-success msg\" data-am-alert id=\"success\"><button type=\"button\" class=\"am-close\">&times;</button>"
		  +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#success').alert();
	setTimeout("$('#success').alert('close')", time );
}

//警告消息
function warning(msg,time){
	var html="<div class=\"am-alert am-alert-warning msg\" data-am-alert id=\"warning\"><button type=\"button\" class=\"am-close\">&times;</button>"
		 +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#warning').alert();
	setTimeout("$('#warning').alert('close')", time );
}

//危险消息
function danger(msg,time){
	var html="<div class=\"am-alert am-alert-danger msg\" data-am-alert id=\"danger\"><button type=\"button\" class=\"am-close\">&times;</button>"
		 +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#danger').alert();
	setTimeout("$('#danger').alert('close')", time );
}


//次要消息
function secondary(msg,time){
	var html="<div class=\"am-alert am-alert-secondary msg\" data-am-alert id=\"secondary\"><button type=\"button\" class=\"am-close\">&times;</button>"
		  +"<p>"+msg+"</p></div>";
	$(document.body).prepend(html);
	$('#secondary').alert();
	setTimeout("$('#secondary').alert('close')", time );
}





