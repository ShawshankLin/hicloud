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
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->

<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-g am-container">
	<div class="am-cf am-padding">
    	<div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">群组</strong> / <small>添加</small></div>
  	</div>
    <div class="am-g">
      <div class="am-u-sm-12">
			<form class="am-form" id="addform">
			  <input type="hidden" class="am-input-sm" name="id" value="${group.id }">
			  <input type="hidden" class="am-input-sm" name="optLock" value="${group.optLock }">
			  <input type="hidden" class="am-input-sm" name="cover" value="${group.cover }">
	          <c:if test="${not empty group.id }">
	         	<div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	群组编号
	            </div>
	            <div class="am-u-sm-8 am-u-md-8">
	              <input type="text" class="am-input-sm" name="groupNo" value="${group.groupNo }">
	            </div>
	            <div class="am-hide-sm-only am-u-md-2"></div>
	          </div>
	          </c:if>

	          
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	群组名
	            </div>
	            <div class="am-u-sm-8 am-u-md-8">
	              <input type="text" class="am-input-sm" name="groupName" value="${group.groupName }">
	            </div>
	            <div class="am-hide-sm-only am-u-md-2"></div>
	          </div>
	          
	          <!-- <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	入群方式
	            </div>
	            <div class="am-u-sm-8 am-u-md-8">
	             	<div class="am-btn-group" data-am-button>
			              <label class="am-btn am-btn-default am-btn-xs am-active" >
			                <input type="radio" name="joinWay" value="0"  checked="checked"> 直接加入
			              </label>
			              <label class="am-btn am-btn-default am-btn-xs">
			                <input type=radio name="joinWay" value="1"> 输入邀请码加入
			              </label>
		          	</div>
	            </div>
	            <div class="am-hide-sm-only am-u-md-2"></div>
	          </div> -->
	          
	          <c:if test="${empty group.id }">
	          	<div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	选择添加好友
	            </div>
	            <div class="am-u-sm-8 am-u-md-8">
	              
	              <select data-am-selected="{searchBox: 1}" multiple  placeholder="选择好友" name="friends">
					  <option selected value=""></option>
					  <c:forEach items="${friends }" var="item">
					  	<option label="${item.friend.name }" value="${item.friend.id }">${item.friend.name }</option>
					  </c:forEach>
					</select>
	            </div>
	            <div class="am-hide-sm-only am-u-md-2"></div>
	          </div>
	          </c:if>

	         <div class="am-g am-margin-top-sm">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	             	介绍
	            </div>
	           	<div class="am-u-sm-8 am-u-md-8">
	              <textarea rows="5" name="description"></textarea>
	            </div>
	            <div class="am-hide-sm-only am-u-md-2 am-padding-left"></div>
	          </div>
	         
	          <div class="am-g am-margin-top-sm">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	            </div>
	           	<div class="am-u-sm-8 am-u-md-8">
	           		<button type="button" id="savebt" class="am-btn am-btn-primary am-btn-xs">提交保存</button>
			    	<button type="button" onclick="history.back()" class="am-btn am-btn-primary am-btn-xs">放弃保存</button>
	            </div>
	          </div>
        </form>
      </div>

    </div>

</div>



<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>

<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.metadata.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.messages_cn.js" ></script>
<link rel="stylesheet" href="${ctx}/resources/plugins/jqueryValidate/validate.css" />
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/IDCord.js" ></script>
<script type="text/javascript">

$(function() {	
    
    $("#addform").validate({
		rules: {
			'groupName': {
				required: true,
			},
		},
		messages: {
			'groupName': {
				required:'*请输入群组名'
			},
		},
		
		errorPlacement: function(error, element) { //指定错误信息位置 
			if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox 
			var eid = element.attr('name'); //获取元素的name属性 
			error.appendTo(element.parent().parent()); //将错误信息添加当前元素的父结点后面 
			} else { 
			error.appendTo(element.parent().next()); 
			} 
		},
		errorElement: "check"
		
	 });
    
    
  	//保存
	$("#savebt").click(function() {
		 if($("#addform").valid()){
			 $.ajax({
					type : "POST",
					url : "${ctx}/group/createGroup",
					data : $('#addform').serialize(),
					dataType: "json", 
					error : function(request) {
						warning("连接错误");
					},
					success : function(data){
						switch(data.status){
						case "success":
							success(data.message);
							window.location.href=data.target;
							break;
						case "error":
							danger(data.message);
							break;
						default:
							danger("操作失败");
							break;
						}
					}
				});
		 }
		return false;
	});
    
  })
</script>
</body>
</html>
