<%@page contentType="text/html" pageEncoding="UTF-8"%>

  <!-- sidebar start -->
  <div class="admin-sidebar am-offcanvas" id="admin-offcanvas" style="width:230px">
    <div class="am-offcanvas-bar admin-offcanvas-bar">
      <ul class="am-list admin-sidebar-list">
        <li class="am-active"><a href="${ctx }/myfile"><span class="am-icon-folder"></span>&nbsp;&nbsp;所有文件</a></li>
        <li><a href="${ctx }/myfile/search?suffix=img"><span class="am-icon-photo"></span>&nbsp;&nbsp;图片</a></li>
        <li><a href="${ctx }/myfile/search?suffix=document"><span class="am-icon-file"></span>&nbsp;&nbsp;文档</a></li>
        <li><a href="${ctx }/myfile/search?suffix=vedio"><span class="am-icon-video-camera"></span>&nbsp;&nbsp;视频</a></li>
        <li><a href="${ctx }/myfile/search?suffix=music"><span class="am-icon-music"></span>&nbsp;&nbsp;音乐</a></li>
        <li><a href="${ctx }/share/myshare"><span class="am-icon-share-alt"></span>&nbsp;&nbsp;我的分享</a></li>
        <li><a href="${ctx }/myfile/trash"><span class="am-icon-trash"></span>&nbsp;&nbsp;回收站</a></li>
      </ul>

      <div class="am-panel am-panel-default admin-sidebar-panel">
        <div class="am-panel-bd">
          <p><span class="am-icon-tag"></span> 已使用<tags:fileSize fileSize="${not empty LOGINUSER.usedSpace?LOGINUSER.usedSpace:0}"/>（共<tags:fileSize fileSize="${not empty LOGINUSER.space.spaceSize?LOGINUSER.space.spaceSize:0}"/>）</p>
          <div class="am-progress am-progress-sm">
             <div class="am-progress-bar" style="width: ${not empty LOGINUSER.space?LOGINUSER.usedSpace/LOGINUSER.space.spaceSize*100:0}%"></div>
           </div>
        </div>
      </div>
    </div>
  </div>
  <!-- sidebar end -->