<%@page contentType="text/html" pageEncoding="UTF-8"%>

  <!-- sidebar start -->
  <div class="admin-sidebar am-offcanvas" id="admin-offcanvas" style="width:230px">
    <div class="am-offcanvas-bar admin-offcanvas-bar">
      <ul class="am-list admin-sidebar-list">
        <li class="admin-parent">
          <a class="am-cf" data-am-collapse="{target: '#basic-nav'}"><span class="am-icon-file"></span> 基本数据管理 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
          <ul class="am-list am-collapse admin-sidebar-sub am-in" id="basic-nav">
          	<li><a href="${ctx}/admin/file/list" class="am-cf"> 文件管理</a></li>
          	<li><a href="${ctx}/admin/group/list" class="am-cf"> 群组管理</a></li>
            <li><a href="${ctx}/admin/user/list" class="am-cf"> 用户管理</a></li>
          </ul>
        </li>
        <li class="admin-parent">
          <a class="am-cf" data-am-collapse="{target: '#system-nav'}"><span class="am-icon-file"></span> 系统管理 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
          <ul class="am-list am-collapse admin-sidebar-sub am-in" id="system-nav">
          	<li><a href="${ctx}/admin/role/list" class="am-cf"> 角色管理</a></li>
          	<li><a href="${ctx}/admin/space/list" class="am-cf"> 存储空间管理</a></li>
          </ul>
        </li>
        <!-- <li><a href="admin-table.html"><span class="am-icon-users"></span> 角色管理</a></li> -->
        <li class="admin-parent">
        	<a class="am-cf" data-am-collapse="{target: '#monitor-nav'}"><span class="am-icon-file"></span> 平台监控 <span class="am-icon-angle-right am-fr am-margin-right"></span></a>
        	<ul class="am-list am-collapse admin-sidebar-sub am-in" id="monitor-nav">
	          	<li><a href="${ctx}/druid/index.html" target="_black" class="am-cf"> 系统监控</a></li>
	          	<li><a href="http://master:50070" target="_black" class="am-cf"> hadoop配置</a></li>
          	</ul>
        </li>
      </ul>

<!--       <div class="am-panel am-panel-default admin-sidebar-panel">
        <div class="am-panel-bd">
          <p><span class="am-icon-bookmark"></span> 公告</p>
          <p>时光静好，与君语；细水流年，与君同。—— Amaze UI</p>
        </div>
      </div>

      <div class="am-panel am-panel-default admin-sidebar-panel">
        <div class="am-panel-bd">
          <p><span class="am-icon-tag"></span> wiki</p>
          <p>Welcome to the Amaze UI wiki!</p>
        </div>
      </div> -->
    </div>
  </div>
  <!-- sidebar end -->