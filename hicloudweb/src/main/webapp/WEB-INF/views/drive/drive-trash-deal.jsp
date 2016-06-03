<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!-- contextjs -->
<script type="text/javascript" src="${ctx}/resources/plugins/contextjs/js/context.js"></script>
<script type="text/javascript" src="${ctx}/resources/scripts/drive.js"></script>
<script type="text/javascript">
function restoreFile(){
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'get',
		url : '${ctx}/myfile/restore',
		data : {
			'fileId':$('#fileId').val(),
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

function delComplete(){
	$('#my-modal-loading').modal('open');
	$.ajax({
		type : 'get',
		url : '${ctx}/myfile/emptyTrash',
		data : {
			'fileId':$('#fileId').val(),
		},
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

function emptyTrash(){
	$('#fileId').val('all');
	delComplete();
}

</script>