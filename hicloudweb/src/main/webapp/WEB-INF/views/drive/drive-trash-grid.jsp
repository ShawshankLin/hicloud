<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>hicloud-我的云盘</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/styles/grid.css">
  <link rel="stylesheet" href="${ctx}/resources/plugins/contextjs/css/context.standalone.css"> 
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/includes/sidebar.jsp" %>

  <!-- content start -->
  <div class="admin-content">
    <%@ include file="/WEB-INF/views/drive/drive-trash-toolbar.jsp" %>
	<hr data-am-widget="divider" class="am-divider am-divider-default" style="margin:2px 0;"/>
    <div class="am-g">
      <div class="am-u-sm-12 am-padding-0">
      		<%@ include file="/WEB-INF/views/drive/drive-trash-navbar.jsp" %>
			<div  class="grid" id="grid">
				<ul>
				<c:forEach items="${requestScope.files }" var="item" varStatus="status">					
			        	<c:choose>
          					<c:when test="${item.isDir==1}"><!-- 文件夹 -->
          						<li id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}",link:"${ctx}/myfile/download?fileId=${item.id}",path:"${ctx}/myfile?path=${fn:trim(path)}/${item.fileName }"}' class="fileDiv" data-am-popover="{content: '${item.name } <br/> 创建时间：<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>', trigger: 'hover focus'}">
          							<div class="img" style="background: url(${ctx}/resources/images/drive_80x80/folder_80x80.png) no-repeat;"></div>
          							<span class="filename am-text-truncate">
          								${item.name }
          							</span>
          						</li>
          					</c:when>
          					<c:otherwise><!-- 文件 -->
          						<li id="${item.id}" file-info='{id:"${item.id}",isDir:"${item.isDir}",type:"${fileUtil.getFileTypeBySuffix(item.suffix)}",link:"${ctx}/myfile/download?fileId=${item.id}"}' class="fileDiv" data-am-popover="{content: '${item.name }.${item.suffix } <br/> 创建时间：<fmt:formatDate value="${item.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>', trigger: 'hover focus'}">
          							<c:choose>
          								<c:when test="${fileUtil.getFileTypeBySuffix(item.suffix)==FileType.IMG}">
		          							<div class="img">
		          							  <img src="${ctx}/resources/images/drive_80x80/${item.suffix}_80x80.png" onerror="this.src='${ctx}/resources/images/drive_30x30/file_80x80.png'" data-rel="${ctx}/dynimage/download?location=${item.ref}"/>
										    </div>
          								</c:when>
          								<c:otherwise>
          									<div class="img" style="background: url(${ctx}/resources/images/drive_80x80/${item.suffix}_80x80.png) no-repeat;"></div>
          								</c:otherwise>
          							</c:choose>
          							<span class="filename am-text-truncate">
          								${item.name}.${item.suffix }
          							</span>
          						</li>
          					</c:otherwise>
          				</c:choose>			        	
				</c:forEach>
			    </ul>			    
			</div>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>


<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<%@ include file="/WEB-INF/views/drive/drive-trash-deal.jsp" %>
<script type="text/javascript">
$(function(){
	
	//添加右键菜单
	$('.grid ul li').each(function(){
		var file=$(this).attr("file-info");
		file=eval('(' + file + ')');
		attachTrashContext(file);
    }).click(function(){
	    $(this).addClass("seled").siblings().removeClass("seled");
		$('#fileId').val($(this).attr('id'));
		$('#restore-bt').attr('disabled',false);
		$('#del-complete-bt').attr('disabled',false);
	});


	document.getElementById('grid').onmousedown = function(event) {  
        if(event.button==0){
	        var selList = [];  
	        var fileNodes = document.getElementsByTagName("li");  
	        for ( var i = 0; i < fileNodes.length; i++) {  
	            if (fileNodes[i].className.indexOf("fileDiv") != -1) {  
	                fileNodes[i].className = "fileDiv";  
	                selList.push(fileNodes[i]);  
	            }  
	        }  
	  
	        var isSelect = true;  
	        var evt = window.event || arguments[0];  
	        var startX = (evt.x || evt.clientX);  
	        var startY = (evt.y || evt.clientY);  
	        var selDiv = document.createElement("div");  
	        selDiv.style.cssText = "position:absolute;width:0px;height:0px;font-size:0px;margin:0px;padding:0px;border:1px dashed #0099FF;background-color:#C3D5ED;z-index:1000;filter:alpha(opacity:60);opacity:0.6;display:none;";  
	        selDiv.id = "selectDiv";  
	        document.body.appendChild(selDiv);  
	  
	        selDiv.style.left = startX + "px";  
	        selDiv.style.top = startY + "px";  
	  
	        var _x = null;  
	        var _y = null;  
	        clearEventBubble(evt);  
	  
	        document.onmousemove = function() {  
	            evt = window.event || arguments[0];  
	            if (isSelect) {  
	                if (selDiv.style.display == "none") {  
	                    selDiv.style.display = "";  
	                }  
	                _x = (evt.x || evt.clientX);  
	                _y = (evt.y || evt.clientY);  
	                selDiv.style.left = Math.min(_x, startX) + "px";  
	                selDiv.style.top = Math.min(_y, startY) + "px";  
	                selDiv.style.width = Math.abs(_x - startX) + "px";  
	                selDiv.style.height = Math.abs(_y - startY) + "px";  
	  
	                // ---------------- 关键算法 ---------------------   
	                var _l = selDiv.offsetLeft, _t = selDiv.offsetTop;  
	                var _w = selDiv.offsetWidth, _h = selDiv.offsetHeight;  
	                
	                for ( var i = 0; i < selList.length; i++) {  
	                    var sl = selList[i].offsetWidth + selList[i].offsetLeft;  
	                    var st = selList[i].offsetHeight + selList[i].offsetTop;  
	                    if (sl > _l && st > _t && selList[i].offsetLeft < _l + _w && selList[i].offsetTop < _t + _h) {  
	                        if (selList[i].className.indexOf("seled") == -1) { 
	                            selList[i].className = selList[i].className + " seled";  
	                        }  
	                    } else {  
	                        if (selList[i].className.indexOf("seled") != -1) {  
	                            selList[i].className = "fileDiv";  
	                        }  
	                    }  
	                }  
	  
	            }  
	            clearEventBubble(evt);  
	        }  
	  
	        document.onmouseup = function() {  
	            isSelect = false;  
	            if (selDiv) {  
	                document.body.removeChild(selDiv);  
	                dealSel(selList);  
	            }
	            selList = null, _x = null, _y = null, selDiv = null, startX = null, startY = null, evt = null;  
	        } 
        }
         
    }
	
	
});


function clearEventBubble(evt) {  
    if (evt.stopPropagation)  
        evt.stopPropagation();  
    else  
        evt.cancelBubble = true;  
    if (evt.preventDefault)  
        evt.preventDefault();  
    else  
        evt.returnValue = false;  
}  
function dealSel(arr) {   
    var selIds = "";  
    for ( var i = 0; i < arr.length; i++) {  
        if (arr[i].className.indexOf("seled") != -1) {  
            selIds+=arr[i].id+",";
        }  
    }
    if(selIds!=null&&selIds.length>0){
        document.getElementById("fileId").value=selIds.substring(0,selIds.length-1);
        $('#restore-bt').attr('disabled',false);
		$('#del-complete-bt').attr('disabled',false);
	}else{
		$('#fileId').val("");//清楚文件id，重要
		$('#restore-bt').attr('disabled',true);
		$('#del-complete-bt').attr('disabled',true);
	}
} 
</script>
</body>
</html>
