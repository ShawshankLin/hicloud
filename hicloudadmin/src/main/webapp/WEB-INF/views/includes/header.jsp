<%@page contentType="text/html" pageEncoding="UTF-8"%>

<header class="am-topbar admin-header">
  <div class="am-topbar-brand">
    <strong>Hicloud</strong> <small>东莞理工学院城市学院校园资源存储</small>
  </div>

  <button class="am-topbar-btn am-topbar-toggle am-btn am-btn-sm am-btn-success am-show-sm-only"
          data-am-collapse="{target: '#doc-topbar-collapse'}"><span class="am-sr-only">导航切换</span> <span
      class="am-icon-bars"></span></button>

  <div class="am-collapse am-topbar-collapse" id="doc-topbar-collapse">
    <ul class="am-nav am-nav-pills am-topbar-nav" id="nav">
      <c:if test="${not empty LOGINUSER }">
      	<li><a href="#">首页</a></li>
      	<li><a href="${ctx}/myfile">网盘</a></li>
      	<li><a href="${ctx}/group">分享</a></li>
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
      		<li><a href="javascript:;"><span class="am-icon-envelope-o"></span> 收件箱 <span class="am-badge am-badge-warning">5</span></a></li>
	      	<li class="am-dropdown" data-am-dropdown>
	        <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
	          <span class="am-icon-user"></span> ${LOGINUSER.name } <span class="am-icon-caret-down"></span>
	        </a>
	        <ul class="am-dropdown-content">
	          <li><a href="${ctx}/user/getUserInfo"><span class="am-icon-user"></span> 资料</a></li>
	          <li><a href="#"><span class="am-icon-cog"></span> 设置</a></li>
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
	  <h3 id="msg-title"></h3>
	  <p><a id="msg-content" href=""></a></p>
</div>

<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/jquery.min.js"></script>
<script src="https://cdn.goeasy.io/goeasy.js"></script>
<script type="text/javascript">
	//登出
	jQuery(function($) {
		
		var url=window.location.href;
		url=url.substring(url.lastIndexOf('/')+1, url.length);
		$('#nav > li > a').each(function(i,item){
			var href=$(item).attr('href');
			href=href.substring(href.lastIndexOf('/')+1,href.length);
			if(href==url){
				$(item).parent().addClass('am-active');
			}
		});
		
		var goEasy = new GoEasy({
			appkey: "a57f10ee-5123-4c50-b396-a442d52e163a"
		});
		goEasy.subscribe({
				channel: "${LOGINUSER.id}",
				onMessage: function (message) {
					var content=message.content.replace(/&quot;/g,'\"');
					var json = $.parseJSON($.parseJSON(content));
					$('#msg-title').text(json.title);
					$('#msg-content').text(json.content);
					$('#msg-content').attr('href',json.target);
					$('#msg-notice').show();
					setTimeout("$('#msg-notice').hide()",5000);
			}
		});

	});
</script>