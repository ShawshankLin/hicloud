<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<div class="am-u-sm-10">
	<c:choose>
		<c:when test="${fn:trim(path)!=''}">
			<ol
				class="am-breadcrumb am-breadcrumb-slash am-padding-0 am-margin-top-xs am-margin-bottom-0">
				<li><a href="${ctx}/group/parentDir?groupId=${group.id }&path=${path}&currentPath=${currentPath}"><small>返回上一级</small></a></li>
				<%
					String current = (String) request.getAttribute("currentPath");//虚拟路径
					String path = (String) request.getAttribute("path");//真实路径
					String currentPath = "";
					String currentPath2 = "";
					String[] array = current.split("/");
					String[] array2 = path.split("/");
					for (int i = 0; i < array.length; i++) {
						String item = array[i];
						String item2=array2[i];
						if (i != 0) {
							currentPath += "/" + item;
							currentPath2 += "/" + item2;
						}
						pageContext.setAttribute("item", item);
						pageContext.setAttribute("item2", item2);
						pageContext.setAttribute("currentPath", currentPath);
						pageContext.setAttribute("currentPath2", currentPath2);
						if (i == array.length - 1) {
				%>

				<li class="am-active"><small>${item == '' ? '全部文件' : item}</small></li>
				<%
					} else {
				%>
				<li><a href="${ctx}/group/content?groupId=${group.id }&path=${currentPath2}&currentPath=${currentPath}"><small>${item == '' ? '全部文件' : item}</small></a></li>
				<%
					}

				}
				%>
			</ol>
		</c:when>
		<c:otherwise>
			<small> 所有文件</small>
		</c:otherwise>

	</c:choose> 
</div>
<div class="am-u-sm-2">
	<div class="am-btn-toolbar am-fr am-margin-top-xs">
		<div class="am-btn-group">
			<a href="javascript:void(0)" onclick="changeStyle('list','${ctx}/group/content?groupId=${group.id }')" class="am-btn am-btn-default am-icon-list-ul am-btn-xs"></a> 
			<a href="javascript:void(0)" onclick="changeStyle('grid','${ctx}/group/content?groupId=${group.id }')" class="am-btn am-btn-default am-icon-th-large am-btn-xs"></a>
		</div>
	</div>
</div>
