<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 <div class="am-u-md-3 blog-sidebar">
    <div class="am-panel-group am-margin-top-sm">
       <section class="am-panel am-panel-default">
        <div class="am-panel-bd am-cf am-padding-bottom-0">
          <div class="am-u-md-4 am-padding-0">
          	 <img src="${ctx }${group.cover}" alt="${group.groupName }" width="80px" height="80px">
          </div>
          <div class="am-u-md-8 am-padding-0">
          		<span><strong>${group.groupName }</strong></span><br>
          		<span class="am-text-xs">创建人：${group.createName }</span>
          		<div class="am-text-xs">
          			<span class="am-inline-block">群空间：<tags:fileSize fileSize="${group.usedSpace }"/>/<tags:fileSize fileSize="${group.space.spaceSize }"/></span>
		          	<div class="am-progress am-progress-xs">
		             <div class="am-progress-bar" style="width: ${not empty group.space?group.usedSpace/group.space.spaceSize*100:0}%"></div>
		            </div>
          		</div>
          </div>
        </div>
      </section>
      <section class="am-panel am-panel-default">
        <div class="am-panel-hd">群介绍</div>
        <div class="am-panel-bd">
          <p>${group.description }</p>
          <a class="am-btn am-btn-success am-btn-sm" href="#">查看更多 →</a>
        </div>
      </section>
     	<section class="am-panel am-panel-default">
        <div class="am-panel-hd">群公告</div>
        <ul class="am-list blog-list">
          <c:forEach items="${groupNotices}" var="item">
          	 <li>
          	 	<a href="javascript:void(0)" data-am-modal="{target: '#notice-dialog-${item.id}', closeViaDimmer: 0, width: 400, height: 300}"><div class="am-text-truncate">${item.notice }</div></a>
          	 	<!-- 群公告-->
				<div class="am-modal am-modal-no-btn" tabindex="-1" id="notice-dialog-${item.id}">
				  <div class="am-modal-dialog">
				    <div class="am-modal-hd">
				     	群公告 - <fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
				      <a href="javascript: void(0)" class="am-close am-close-spin" data-am-modal-close>&times;</a>
				    </div>
				    <div class="am-modal-bd">
				    	${item.notice }
				    </div>
				  </div>
				</div>
          	 </li>
          </c:forEach>
        </ul>
      </section>
      <section class="am-panel am-panel-default">
        <div class="am-panel-hd">群成员</div>
        <div class="am-panel-bd">
          <ul class="am-avg-sm-6 blog-team">
            <c:forEach items="${groupUsers}" var="item">
             <li>
            	<img class="am-thumbnail"
                     src="${ctx }${item.user.photo}" alt="${item.user.name}" data-am-popover="{content: '${item.user.name}', trigger: 'hover focus'}"/>
             </li>
            </c:forEach>
  
          </ul>
        </div>
      </section>
    </div>
  </div>