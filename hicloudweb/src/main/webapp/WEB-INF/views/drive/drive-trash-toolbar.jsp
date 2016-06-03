<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<input type="hidden" id="fileId"/>

<div class="am-g am-padding-top-sm" style="padding-bottom:0.2rem">
      <div class="am-u-sm-12 am-u-md-9">
      	 <div class="am-btn-toolbar">
      	 	<div class="am-btn-group am-inline am-disabled" id="dir-toolbar">
      	 		<button class="am-btn am-btn-primary am-btn-sm" disabled="disabled" id="restore-bt" onclick="restoreFile()">
				  <i class="am-icon-history"></i>
				  还原文件
			  </button>
			  <button class="am-btn am-btn-primary am-btn-sm" disabled="disabled"  id="del-complete-bt" onclick="delComplete()">
				  <i class="am-icon-trash"></i>
				  删除
			  </button>
      	 	</div>
      	 	<button class="am-btn am-btn-primary am-btn-sm" onclick="emptyTrash()">
		  <i class="am-icon-trash"></i>
		  	清空回收站
	  		</button>
		 </div>      	
      </div>
      <div class="am-u-sm-12 am-u-md-3">
      </div>
</div>
    
