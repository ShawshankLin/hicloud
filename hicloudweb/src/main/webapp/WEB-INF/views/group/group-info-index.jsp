<%@page contentType="text/html" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>hicloud群共享</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
  <style type="text/css">
  	#groupPanel .group-item {
  		cursor: pointer
  	}
  	#groupPanel .group-item .info{
  		width: 135px;
  		float: right;
  		font-size:8px;
  	}
  	#groupPanel .group-item .info .name-box{
  		color: #999;
	    float: right;
	    padding: 10px 8px 0px 12px;
	    width: 135px;
  	}
	#groupPanel .group-item .info .name-box .name{
		color: #333;
		width:90px;
	    font-size: 14px;
	    font-weight: 700;
	    line-height: 28px;
	    float:left;
	}
	#groupPanel .group-item .info .name-box .admin{
		background: url("${ctx }/resources/images/group/group_amdin.png") center right no-repeat;
    	height: 28px;
    	width: 20px;
    	float:left;
	}
	#groupPanel .group-item .info .desc-box{
		float:right;
		width: 135px;
		padding: 10px 8px 0 12px;
	}
	#groupPanel .group-item .info .desc-box .count{
		color:#999;
	}
  	#groupPanel .group-item .info .desc-box .count .count_node{
  		color:#dab544;
 	}
 	
 	#groupPanel .group-item .info .desc-box .creator {
	    float: right;
	    overflow: hidden;
	    white-space: nowrap;
	    word-break: break-all;
	    max-width: 80px;
	    color:#999
	}
 	
  </style>
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-g am-container">
  	  <div class="am-u-lg-12 am-margin-top-sm">
  	  	  <div class="am-u-lg-8 am-u-sm-9 am-margin-bottom-xs">
  	  	  	 <button type="button" class="am-btn am-btn-primary am-btn-sm" onclick="window.location.href='${ctx}/group/createGroup?ispage=true'">
			  <i class="am-icon-users"></i>
			 	新建共享群
			</button>
  	  	  </div>
  	  	  <div class="am-u-lg-4 am-u-sm-3 am-margin-bottom-xs">
			 <form action="${ctx}/group/searchGroup" method="post" class="am-form">
      			<div class="am-input-group am-input-group-sm">
        			<input type="text" class="am-form-field" placeholder="搜索群组直接加入" name="groupNo">
	          		<span class="am-input-group-btn">
	            	<button class="am-btn am-btn-default" type="submit">搜索</button>
	          		</span>
        		</div>
      		</form>
  	  	  </div>
	  	  <hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>
		  <!-- content start -->
		  <div class="admin-content">
				<ul class="am-gallery am-avg-sm-4 am-gallery-imgbordered" id="groupPanel">
					<c:if test="${not empty groupUsers }">
						<c:forEach items="${groupUsers }" var="item">  
						<li class="group-item" group-info='{id:"${item.group.id}",groupName:"${item.group.groupName}"}'>
						<div class="am-panel am-panel-default">
						    <img src="${ctx }${item.group.cover}"></img>
						    <div class="info">
						    <div class="name-box">
						    	<span class="name am-text-truncate">${item.group.groupName }</span>
						    	<c:if test="${LOGINUSER.id==item.group.createId }">
						    		<span class="admin"></span>
						    	</c:if>
						    </div>
						    <div class="desc-box">
						    	<span class="count"><%-- ${fn:length(item.group.groupFiles )} --%>
						    		<span class="count_node">${fn:length(item.group.groupFiles )}</span>个文件
						    	</span>
						    	<c:if test="${LOGINUSER.id==item.group.createId }">
						    		<span class="creator">我创建</span>
						    	</c:if>
						    	
						    </div>
						    </div>
						</div>
					</li>
					</c:forEach>
				</c:if>
				<c:if test="${not empty groups }">
						<c:forEach items="${groups }" var="item">  
						<li class="group-item" group-info='{id:"${item.id}",groupName:"${item.groupName}"}'>
						<div class="am-panel am-panel-default">
						    <img src="${ctx }${item.cover}"></img>
						    <div class="info">
						    <div class="name-box">
						    	<span class="name am-text-truncate">${item.groupName }</span>
						    	<c:if test="${LOGINUSER.id==item.createId }">
						    		<span class="admin"></span>
						    	</c:if>
						    </div>
						    <div class="desc-box">
						    	<span class="count"><%-- ${fn:length(item.group.groupFiles )} --%>
						    		<span class="count_node">4</span>个文件
						    	</span>
						    	<c:if test="${LOGINUSER.id==item.createId }">
						    		<span class="creator">我创建</span>
						    	</c:if>
						    	
						    </div>
						    </div>
						</div>
					</li>
					</c:forEach>
				</c:if>

				</ul>
		    </div>
		   <!-- content end -->
  	  </div>

</div>



<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<script type="text/javascript">

$(function(){
	
	$('.group-item').click(function(){
		var group=$(this).attr('group-info');
		group=eval('(' + group + ')');
		window.location.href="${ctx}/group/content?groupId="+group.id;
	});
	
	
});


</script>
</body>
</html>
