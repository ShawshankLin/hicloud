<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<input type="hidden" id="fileId" name="fileId"/>

<div class="am-g" style="padding-top:0.8rem">
      <div class="am-u-sm-12 am-u-md-3">
	  <div id="picker" class="am-fl"><i class="am-icon-cloud-upload"></i>&nbsp;上传</div>
	  <div class="am-fl am-margin-left-xs">
	  	<button type="button" class="am-btn am-btn-primary am-btn-sm"
		  data-am-modal="{target: '#mkdir-dialog', closeViaDimmer: 0, width: 400, height: 170,closeViaDimmer:false}">
		  <i class="am-icon-folder"></i>
		 新建文件夹
		</button>
	  </div>
      </div>
      <div class="am-u-sm-12 am-u-md-6">
        <div class="am-btn-toolbar">
          	<div class="am-btn-group" id="dir-toolbar" style="display:none;"><!-- 目录工具条 -->
			<button type="button" class="am-btn am-btn-default am-btn-sm" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId='+$('#fileId').val()"><span class="am-icon-download"></span> 打包下载</button>
            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="delFile()"><span class="am-icon-trash"></span> 删除</button>
            <!-- <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="shareFile()"><span class="am-icon-share-alt"></span> 分享</button> -->
			<button type="button" class="am-btn am-btn-default am-btn-sm" data-am-modal="{target: '#rename-dialog', closeViaDimmer: 0, width: 400, height: 150}"><span class="am-icon-pencil-square-o"> 重命名</span></button>
            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="initMove();"><span class="am-icon-exchange"> 移动</span></button>
			  <!-- <div class="am-dropdown" data-am-dropdown>
			    <button class="am-btn am-btn-default am-btn-sm am-dropdown-toggle" data-am-dropdown-toggle>更多  <span class="am-icon-caret-down"></span></button>
			    <ul class="am-dropdown-content">
			      <li><a href="javascript:void(0)" data-am-modal="{target: '#rename-dialog', closeViaDimmer: 0, width: 400, height: 150}"><span class="am-icon-pencil-square-o"> 重命名</span></a></li>
			      <li><a href="javascript:void(0)" onclick="initMove();"> <span class="am-icon-exchange"> 移动</span></a></li>
			    </ul>
			  </div> -->
			</div>
		   <div class="am-btn-group" id="file-toolbar" style="display:none;"><!-- 文件工具条 -->
			<button type="button" class="am-btn am-btn-default am-btn-sm" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId='+$('#fileId').val()"><span class="am-icon-download"></span> 下载</button>
            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="delFile()"><span class="am-icon-trash"></span> 删除</button>
            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="javascript:$('#share-dialog').modal('open');"><span class="am-icon-share-alt"></span> 分享</button>
			  <div class="am-dropdown" data-am-dropdown>
			    <button class="am-btn am-btn-default am-btn-sm am-dropdown-toggle" data-am-dropdown-toggle>更多  <span class="am-icon-caret-down"></span></button>
			    <ul class="am-dropdown-content">
			      <li><a href="javascript:void(0)" data-am-modal="{target: '#rename-dialog', closeViaDimmer: 0, width: 400, height: 150}"><span class="am-icon-pencil-square-o"> 重命名</span></a></li>
			      <li><a href="javascript:void(0)" onclick="initMove();"> <span class="am-icon-exchange"> 移动</span></a></li>
			      <li><a href="javascript:void(0)" onclick="historyFile()"> <span class="am-icon-history"> 文件时光机</span></a></li>
			    </ul>
			  </div>
		   </div>
		   <div class="am-btn-group" id="multiple-toolbar" style="display:none;">
			<button type="button" class="am-btn am-btn-default am-btn-sm" onclick="javascript:window.location.href='${ctx}/myfile/download?fileId='+$('#fileId').val()"><span class="am-icon-download"></span> 打包下载</button>
            <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="delFile()"><span class="am-icon-trash"></span> 删除</button>
            <!-- <button type="button" class="am-btn am-btn-default am-btn-sm" onclick="shareFile()"><span class="am-icon-share-alt"></span> 分享</button> -->
             <button type="button" onclick="initMove();" class="am-btn am-btn-default am-btn-sm"><span class="am-icon-exchange"></span> 移动</button>
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
    
