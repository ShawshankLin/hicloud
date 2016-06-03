<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<input type="hidden" id="fileId"/>

<div class="am-g am-padding-top-sm" style="padding-bottom:0.2rem">
      <div class="am-u-sm-12 am-u-md-9">
        <div class="am-btn-toolbar">
			<div class="am-btn-group" id="search-toolbar" style="display:none;">
				<button type="button" class="am-btn am-btn-default am-btn-sm" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId='+$('#fileId').val()"><span class="am-icon-download"></span> 下载</button>
	            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="delFile()"><span class="am-icon-trash"></span> 删除</button>
	            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="shareFile()"><span class="am-icon-share-alt"></span> 分享</button>
			 </div>
        </div>
      </div>
      <div class="am-u-sm-12 am-u-md-3">
      	<form action="${ctx}/myfile/search" method="post" class="am-form">
      		<div class="am-input-group am-input-group-sm">
        		<input type="text" class="am-form-field" placeholder="搜索我的文件" name="fileName">
	          <span class="am-input-group-btn">
	            <button class="am-btn am-btn-default" type="submit">搜索</button>
	          </span>
        	</div>
      	</form>
      </div>
</div>
    