<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/admin/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>添加用户</title>
  <meta name="description" content="这是一个 table 页面">
  <meta name="keywords" content="table">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/admin/includes/link.jsp" %>

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
    	<div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">用户</strong> / <small>添加</small></div>
  	</div>
    <div class="am-g">
      <div class="am-u-sm-12">
			<form class="am-form" id="addform">
			  <input type="hidden" class="am-input-sm" name="id" value="${user.id }">
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	账号	
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
	              <input type="text" class="am-input-sm" name="userName" value="${user.userName }">
	            </div>
	            <div class="am-hide-sm-only am-u-md-6"></div>
	          </div>
				  
			  <div class="am-g am-margin-top">
		            <div class="am-u-sm-4 am-u-md-2 am-text-right">
		              	密码
		            </div>
		            <div class="am-u-sm-8 am-u-md-4 am-u-end col-end">
		              <input type="password" class="am-input-sm" name="password" value="${user.password }">
		            </div>
		            <div class="am-hide-sm-only am-u-md-6"></div>
		      </div>
	          
	          <input type="hidden" class="am-input-sm" name="optLock" value="${user.optLock }">
	          
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	真实姓名
	            </div>
	            <div class="am-u-sm-8 am-u-md-4 am-u-end col-end">
	              <input type="text" class="am-input-sm" name="name" value="${user.name }">
	            </div>
	            <div class="am-hide-sm-only am-u-md-6"></div>
	          </div>
	
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	              	邮箱
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
	              <input type="text" class="am-input-sm" name="email" value="${user.email }">
	            </div>
	            <div class="am-hide-sm-only am-u-md-6">选填</div>
	          </div>
	
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right">
	             	手机号
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
	              <input type="text" class="am-input-sm" name="mobilePhone" value="${user.mobilePhone }">
	            </div>
	            <div class="am-u-sm-12 am-u-md-6"></div>
	          </div>
	          
	          <div class="am-g am-margin-top">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	              	出生日期
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
	              <input type="text" name="birthDay" class="am-form-field" placeholder="请选择日期" id="birthDay" value="<fmt:formatDate value="${user.birthDay }" pattern="yyyy-MM-dd" />"/>
	            </div>
	            <div class="am-hide-sm-only am-u-md-6 am-padding-left"></div>
	          </div>
	
	          <div class="am-g am-margin-top-sm">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	              	性别 
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
		              <div class="am-btn-group" data-am-button>
		            
			               <c:if test="${user.sex==1}">
			              	 <label class="am-btn am-btn-default am-btn-xs am-active">
				                <input type=radio name="sex" value="1" checked="checked"> 男
				              </label>
				              <label class="am-btn am-btn-default am-btn-xs">
				                <input type="radio" name="sex" value="2"> 女
				              </label>
			              	</c:if>
							<c:if test="${user.sex==2}">
			              	  <label class="am-btn am-btn-default am-btn-xs">
				                <input type=radio name="sex" value="1" > 男
				              </label>
				              <label class="am-btn am-btn-default am-btn-xs am-active">
				                <input type="radio" name="sex" value="2" checked="checked"> 女
				              </label>
			              	</c:if>
		          	</div>
	             </div>
	            <div class="am-hide-sm-only am-u-md-6 am-padding-left"></div>
	          </div>
	          <div class="am-g am-margin-top-sm">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	              	状态
	            </div>
	            <div class="am-u-sm-8 am-u-md-4">
		              <div class="am-btn-group" data-am-button>
			              <label class="am-btn am-btn-default am-btn-xs am-active">
			                <input type=radio name="status" value="1" checked="checked"> 激活
			              </label>
			              <label class="am-btn am-btn-default am-btn-xs">
			                <input type="radio" name="status" value="0"> 禁用
			              </label>
		          		</div>
	            </div>
	            <div class="am-hide-sm-only am-u-md-6 am-padding-left"></div>
	          </div>
	          <div class="am-g am-margin-top-sm">
	            <div class="am-u-sm-4 am-u-md-2 am-text-right am-padding-right">
	             	备注
	            </div>
	           	<div class="am-u-sm-8 am-u-md-4">
	              <textarea rows="10" name="remark"></textarea>
	            </div>
	            <div class="am-hide-sm-only am-u-md-6 am-padding-left"></div>
	          </div>
			  <div class="am-margin">
			    <button type="button" id="savebt" class="am-btn am-btn-primary am-btn-xs">提交保存</button>
			    <button type="button" onclick="history.back()" class="am-btn am-btn-primary am-btn-xs">放弃保存</button>
			  </div>
        </form>
      </div>

    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/admin/includes/footer.jsp" %>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.metadata.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.js" ></script>
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/jquery.validate.messages_cn.js" ></script>
<link rel="stylesheet" href="${ctx}/resources/plugins/jqueryValidate/validate.css" />
<script type="text/javascript" src="${ctx}/resources/plugins/jqueryValidate/IDCord.js" ></script>
</body>

<script type="text/javascript">

$(function() {	
    var nowTemp = new Date();
    var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
    var $birthDay = $('#birthDay');

    var checkin = $birthDay.datepicker({
      onRender: function(date) {
        return date.valueOf() > now.valueOf() ? 'am-disabled' : '';
      }
    }).on('changeDate.datepicker.amui', function(ev) {
        if (ev.date.valueOf() > checkout.date.valueOf()) {
          var newDate = new Date(ev.date)
          newDate.setDate(newDate.getDate() + 1);
          checkout.setValue(newDate);
        }
        checkin.close();
        $('#birthDay')[0].focus();
    }).data('amui.datepicker');
    
    $("#addform").validate({
		rules: {
			'userName': {
				required: true,
			},
			'password': {
				required: true,
			},
			'name': {
				required: true,
			}
		},
		messages: {
			'userName': {
				required:'*请输入账号'
			},
			'password':{
				required:'*请输入密码'
			},
			'name':{
				required:'*请输入姓名'
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
					url : "${ctx}/admin/user/save",
					data : $('#addform').serialize(),
					dataType: "json", 
					error : function(request) {
						warning("连接错误");
					},
					success : function(data){
						switch(data.status){
						case "success":
							success(data.message,3000);
							window.location.href=data.target;
							break;
						case "error":
							error(data.message,5000);
							break;
						default:
							error("操作失败");
							break;
						}
					}
				});
		 }
		return false;
	});
    
  })
</script>
</html>
