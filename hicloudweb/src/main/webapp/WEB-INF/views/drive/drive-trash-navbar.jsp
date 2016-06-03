<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="am-u-sm-10 am-text-xs am-margin-top-sm">
	回收站 （温馨提示：回收站文件不占用您云盘空间，保留30天后将自动删除。）
</div>
<div class="am-u-sm-2">
	<div class="am-btn-toolbar am-fr am-margin-top-xs">
		<div class="am-btn-group">
			<a href="javascript:void(0)"  onclick="changeStyle('list','${ctx}/myfile/trash')" class="am-btn am-btn-default am-icon-list-ul am-btn-xs"></a> 
			<a href="javascript:void(0)" onclick="changeStyle('grid','${ctx}/myfile/trash')" class="am-btn am-btn-default am-icon-th-large am-btn-xs"></a>
		</div>
	</div>
</div>
