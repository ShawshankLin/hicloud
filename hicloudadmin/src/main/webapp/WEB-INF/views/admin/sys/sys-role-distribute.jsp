<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/admin/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>分配角色</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/admin/includes/link.jsp" %>
<link rel="stylesheet" href="${ctx}/resources/plugins/ztree/css/zTreeStyle.css" type="text/css"/>	
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/admin/includes/header.jsp" %>
<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/admin/includes/sidebar.jsp" %>

  <!-- content start -->
  <div class="admin-content">
	<div class="am-cf am-padding">
    <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">角色-${role.roleName }</strong> / <small>分配</small></div>
  	</div>
    <div class="am-g">
		<div class="am-g">
		<div class="am-u-sm-12">
		<div class="zTreeDemoBackground" style="margin-left: 30px;margin-top: 20px;">
			<ul id="user-tree" class="ztree"></ul>
			</div>
			<div class="am-u-lg-6" style="margin-top: 20px;margin-left: 30px;">
				<input type="hidden" id="userIds">
				<button type="submit" class="am-btn am-btn-primary am-btn-xs" id="savebt">提交保存</button>
		    	<button type="button" class="am-btn am-btn-primary am-btn-xs" onclick="history.back()">放弃保存</button>
			</div>
		</div>

		</div>
	</div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/admin/includes/footer.jsp" %>
<!-- ztree -->
<script type="text/javascript" src="${ctx}/resources/plugins/ztree/js/jquery.ztree.core-3.5.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/ztree/js/jquery.ztree.excheck-3.5.min.js" ></script>	
<script type="text/javascript">

	
	
	<!--  
    var setting = {  
        check: {  
            enable: true  
        },  
        data: {  
            simpleData: {  
                enable: true  
            }  
        }
    };  
	

      
    $(document).ready(function(){
	   	$.get("${ctx}/admin/user/getUserInfosByRoleId?roleId=${role.id}",function(data){ 
	   		$.fn.zTree.init($("#user-tree"), setting, data);
        });
		
	    $("#savebt").click(function(){
	    	getCheckedNodes();
	    	$.ajax({
	    		type: "POST",
                url: "${ctx}/admin/role/distribute",
                data : {
                	"userIds":$('#userIds').val(),
                	"roleId":'${role.id}'
                },
                error: function(request) {
                	warning("连接失败");
                },
                success: function(data) {
                	data=$.parseJSON(data);
                	try {
						switch(data.status){
						case "success":
							success(data.message);
							window.location.href = data.target;
							break;
						case "error":
							error(data.message);
							break;
						}
					} catch (e) {
						warning('操作失败'+e);
					}
                }
            });
	    });
	    
    });  
    
    function getCheckedNodes(){
        var treeObj=$.fn.zTree.getZTreeObj("user-tree");
        var nodes=treeObj.getCheckedNodes(true);
        v="";
        for(var i=0;i<nodes.length;i++){
        	if(nodes[i].idt!=null&&nodes[i].idt!="0"){
        		v+=nodes[i].idt + ",";
        	}
        }
        document.getElementById('userIds').value=v;
    }
    
    //--> 
	   
</script>
</body>

</html>
