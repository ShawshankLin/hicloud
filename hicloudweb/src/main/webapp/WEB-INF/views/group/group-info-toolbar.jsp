<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<input type="hidden" id="fileId" name="fileId"/>

<div class="am-g am-padding-top-sm" style="padding-bottom:0.2rem">
	<div class="am-u-sm-12 am-u-md-3">
		<img src="${ctx }${group.cover}" alt="${group.groupName }" width="40px" height="40px">
		<span class="am-margin-left-sm am-text-truncate">${group.groupName }</span>
	</div>
      <div class="am-u-sm-12 am-u-md-6">   	
		<button type="button" class="am-btn am-btn-primary am-btn-sm" onclick="window.location.href='${ctx}/group'">
		  <i class="am-icon-angle-double-left"></i>
		 	返回群首页
		</button>
		<div class="am-dropdown" data-am-dropdown>
		  <button class="am-btn am-btn-primary am-btn-sm am-dropdown-toggle" data-am-dropdown-toggle>上传&nbsp;&nbsp;<span class="am-icon-caret-down"></span></button>
		  <ul class="am-dropdown-content">
		    <li><a href="javascript:void(0)" id="yun-import" data-am-modal="{target: '#myfile-dialog', closeViaDimmer: 0, width: 600, height: 520}">从网盘导入</a></li>
		    <!-- <li><a href="javascript:void(0)" id="local-import" onclick="alert('okk')">从本地上传</a></li> -->
		  </ul>
		</div>
		<button type="button" class="am-btn am-btn-primary am-btn-sm"
		  data-am-modal="{target: '#mkdir-dialog', closeViaDimmer: 0, width: 400, height: 180,closeViaDimmer:false}">
		  <i class="am-icon-folder"></i>
		 新建文件夹
		</button>
		<div class="am-dropdown" data-am-dropdown>
		  <button class="am-btn am-btn-primary am-btn-sm am-dropdown-toggle" data-am-dropdown-toggle>群内管理&nbsp;&nbsp;<span class="am-icon-caret-down"></span></button>
		  <ul class="am-dropdown-content">
		      <li><a href="javascript:void(0)" data-am-modal="{target: '#friend-dialog', closeViaDimmer: 0, width: 400, height: 100}">邀请好友入群</a></li>
		      <c:if test="${LOGINUSER.id=group.createId }">
		      	<li><a href="javascript:void(0)" data-am-modal="{target: '#notice-dialog', closeViaDimmer: 0, width: 400, height: 230}">发布群公告</a></li>
		      <li><a href="${ctx}/group/editGroup?id=${group.id}">更改群信息</a></li>
		      </c:if>
		      <li><a href="${ctx}/group/delGroup?id=${group.id}" ><span  class="am-text-danger">退出该群（创建者退出当作注销该群）</span></a></li>
		  </ul>
		</div>
      </div>
      <div class="am-u-sm-12 am-u-md-3">
      	<form action="${ctx}/group/searchFile" method="post" class="am-form">
      		<div class="am-input-group am-input-group-sm">
        		<input type="text" class="am-form-field" placeholder="搜索群文件" name="fileName">
	          <span class="am-input-group-btn">
	            <button class="am-btn am-btn-default" type="submit">搜索</button>
	          </span>
        	</div>
      	</form>
      </div>
</div>
    
