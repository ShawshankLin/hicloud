<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>


<div class="am-u-sm-10">
	<c:choose>
		<c:when test="${fn:trim(fileName)!=''}">
			<ol
				class="am-breadcrumb am-breadcrumb-slash am-padding-0 am-margin-top-xs am-margin-bottom-0">
				<li><a href="javascript:history.back(-1)"><small>返回上一级</small></a></li>
				<li><small><a href="${ctx}/myfile">全部文件</a></small></li>
				<li class="am-active"><small>搜索内容：${fileName}</small></li>
			</ol>
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${type=='img'}">
					<small> 图片</small>
				</c:when>
				<c:when test="${type=='vedio'}">
					<small> 视频</small>
				</c:when>
				<c:when test="${type=='music'}">
					<small> 音乐</small>
				</c:when>
				<c:when test="${type=='document'}">
					<small> 文档</small>
				</c:when>
			</c:choose>
		</c:otherwise>

	</c:choose>
</div>
<div class="am-u-sm-2">
	<div class="am-btn-toolbar am-fr am-margin-top-xs">
		<div class="am-btn-group">
			<a href="javascript:void(0)" onclick="changeStyle('list','${ctx}/myfile/search?fileName=${fileName }&suffix=${type }')" class="am-btn am-btn-default am-icon-list-ul am-btn-xs"></a> 
			<a href="javascript:void(0)" onclick="changeStyle('grid','${ctx}/myfile/search?fileName=${fileName }&suffix=${type }')" class="am-btn am-btn-default am-icon-th-large am-btn-xs"></a>
		</div>
	</div>
</div>
