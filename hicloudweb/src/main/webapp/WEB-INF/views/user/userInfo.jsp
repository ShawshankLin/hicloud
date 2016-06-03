<%@page contentType="text/html" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>个人信息</title>
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

<div class="am-cf admin-main">
  <%@ include file="/WEB-INF/views/includes/sidebar.jsp" %>

<!-- content start -->
  <div class="admin-content">
    <div class="am-cf am-padding">
      <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">个人资料</strong> / <small>Personal information</small></div>
    </div>
    <div class="am-g">

      <div class="am-u-sm-12 am-u-md-4 am-u-md-push-8">
        <div class="am-panel am-panel-default">
          <div class="am-panel-bd">
            <div class="am-g">
              <div class="am-u-md-4">
                <img class="am-img-circle am-img-thumbnail" src="${ctx }${user.photo}" alt="头像"/>
              </div>
              <div class="am-u-md-8">
                <p>你可以使用<a href="#">gravatar.com</a>提供的头像或者使用本地上传头像。 </p>
                <form class="am-form">
                  <div class="am-form-group">
                    <input type="file" id="user-pic">
                    <p class="am-form-help">请选择要上传的文件...</p>
                    <button type="button" class="am-btn am-btn-primary am-btn-xs">保存</button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>

        <div class="am-panel am-panel-default">
          <div class="am-panel-bd">
            <div class="user-info">
              <p>存储空间</p>
              <div class="am-progress am-progress-sm">
                <div class="am-progress-bar" style="width: ${not empty user.space?user.usedSpace/user.space.spaceSize*100:0}%"></div>
              </div>
              <p class="user-info-order">当前存储空间：<strong><tags:fileSize fileSize="${not empty user.usedSpace?user.usedSpace:0}"/></strong> 剩余空间：<strong><tags:fileSize fileSize="${not empty (user.space.spaceSize-user.usedSpace)? (user.space.spaceSize-user.usedSpace):0}"/></strong> 总空间：<strong><tags:fileSize fileSize="${not empty user.space.spaceSize?user.space.spaceSize:0}"/></strong></p>
            </div>
            <!-- <div class="user-info">
              <p>信用信息</p>
              <div class="am-progress am-progress-sm">
                <div class="am-progress-bar am-progress-bar-success" style="width: 80%"></div>
              </div>
              <p class="user-info-order">信用等级：正常当前 信用积分：<strong>80</strong></p>
            </div> -->
          </div>
        </div>

      </div>

      <div class="am-u-sm-12 am-u-md-8 am-u-md-pull-4">
        <form class="am-form am-form-horizontal" id="addform">
          <input type="hidden" name="id" value="${user.id }">
          <input type="hidden" name="optLock" value="${user.optLock }">
          
          <div class="am-form-group">
            <label for="name" class="am-u-sm-3 am-form-label">姓名 / Name</label>
            <div class="am-u-sm-9">
              <input type="text" name="name" id="name" placeholder="姓名 / Name" value="${user.name }">
            </div>
          </div>

          <div class="am-form-group">
            <label for="email" class="am-u-sm-3 am-form-label">电子邮件 / Email</label>
            <div class="am-u-sm-9">
              <input type="email" name="email" id="email" placeholder="输入你的电子邮件 / Email" value="${user.email }">
            </div>
          </div>

          <div class="am-form-group">
            <label for="mobilePhone" class="am-u-sm-3 am-form-label">电话 / Telephone</label>
            <div class="am-u-sm-9">
              <input type="text" name="mobilePhone" id="mobilePhone" placeholder="输入你的电话号码 / Telephone" value="${user.mobilePhone }">
            </div>
          </div>

          <div class="am-form-group">
            <label for="birthDay" class="am-u-sm-3 am-form-label">出生日期</label>
            <div class="am-u-sm-9">
              <input type="text" id="birthDay" placeholder="请选择日期" name="birthDay" value="<fmt:formatDate value="${user.birthDay }" pattern="yyyy-MM-dd" />">
            </div>
          </div>

          <div class="am-form-group">
            <label for="sex" class="am-u-sm-3 am-form-label">性别</label>
            <div class="am-u-sm-9">
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
          </div>

          <div class="am-form-group">
            <label for="remark" class="am-u-sm-3 am-form-label">简介 / Intro</label>
            <div class="am-u-sm-9">
              <textarea class="" rows="5" name="remark" id="remark" placeholder="输入个人简介">${user.remark }</textarea>
            </div>
          </div>

          <div class="am-form-group">
            <div class="am-u-sm-9 am-u-sm-push-3">
              <button type="button" id="savebt" class="am-btn am-btn-primary am-btn-sm">保存修改</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
  <!-- content end -->
</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
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
			}
		},
		messages: {
			'userName': {
				required:'*请输入账号'
			}
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
					url : "${ctx}/user/update",
					data : $('#addform').serialize(),
					dataType: "json", 
					error : function(request) {
						warning("连接错误");
					},
					success : function(data){
						switch(data.status){
						case "success":
							success(data.message);
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
</html>
