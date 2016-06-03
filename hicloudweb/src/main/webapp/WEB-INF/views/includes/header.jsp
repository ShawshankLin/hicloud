<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header class="am-topbar admin-header">
  <div class="am-topbar-brand">
    <!-- <small>东莞理工学院城市学院校园资源存储</small> -->
    <strong>东莞理工学院城市学院校园资源存储</strong> 
  </div>

  <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only"
          data-am-collapse="{target: '#doc-topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span
      class="am-icon-bars"></span></button>

  <div class="am-collapse am-topbar-collapse" id="doc-topbar-collapse">
    <ul class="am-nav am-nav-pills am-topbar-nav" id="nav">
      <c:if test="${not empty LOGINUSER }">
      	<li nav="portal"><a href="${ctx}/share/openshare">首页</a></li>
      	<li nav="myfile"><a href="${ctx}/myfile">网盘</a></li>
		<li nav="friend"><a href="${ctx }/friend">好友</a></li>
      	<li nav="group"><a href="${ctx}/group">共享群</a></li>
      </c:if>
      
      <!-- <li class="am-dropdown" data-am-dropdown>
        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
          菜单 <span class="am-icon-caret-down"></span>
        </a>
        <ul class="am-dropdown-content">
          <li class="am-dropdown-header">标题</li>
          <li><a href="#">关于我们</a></li>
          <li><a href="#">关于字体</a></li>
          <li><a href="#">TIPS</a></li>
        </ul>
      </li> -->
    </ul>
    <ul class="am-nav am-nav-pills am-topbar-nav am-topbar-right admin-header-list">
      <c:choose>
      	<c:when test="${not empty LOGINUSER }">
      		<li><a href="${ctx}/notice"><span class="am-icon-envelope-o"></span> 收件箱<%--  <span class="am-badge am-badge-warning">${fn:length(notices)}</span> --%></a></li>
	      	<li class="am-dropdown" data-am-dropdown>
	        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
	          <span class="am-icon-user"></span> ${LOGINUSER.name } <span class="am-icon-caret-down"></span>
	        </a>
	        <ul class="am-dropdown-content">
	          <li><a href="${ctx}/user"><span class="am-icon-user"></span> 资料</a></li>
	          <li><a href="#"><span class="am-icon-cog"></span> 修改密码</a></li>
	          <li><a href="${ctx}/logout" id="logout"><span class="am-icon-power-off"></span> 退出</a></li>
	        </ul>
	      </li>
      	</c:when>
      	<c:otherwise>
      		<li><a href="${ctx }"><span class="am-icon-user"></span> 登陆 </a></li>
      	</c:otherwise>
      </c:choose>
      <li class="am-hide-sm-only"><a href="javascript:;" id="admin-fullscreen"><span class="am-icon-arrows-alt"></span> <span class="admin-fullText">开启全屏</span></a></li>
    </ul>
  </div>
</header>

<div id="msg-notice" class="am-alert" data-am-alert style="width:400px;position:absolute;right:10px;top:150px;z-index:999;display:none;">
	  <button type="button" class="am-close">&times;</button>
	  <h3 id="msg-content"></h3>
	  <p id="msg-target"></p>
</div>



