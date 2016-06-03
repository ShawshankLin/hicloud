<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head lang="en">
  <meta charset="UTF-8">
  <title>登陆</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="format-detection" content="telephone=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp" />
  <%@ include file="/WEB-INF/views/includes/link.jsp" %>
  <style>
    .header {
      text-align: center;
    }
    .header h1 {
      font-size: 200%;
      color: #333;
      margin-top: 30px;
    }
    .header p {
      font-size: 14px;
    }
  </style>
</head>
<body>
<div class="header">
  <div class="am-alert am-alert-danger" data-am-alert style="display:none;">
  	<button type="button" class="am-close">&times;</button>
  	<p id="msg"></p>
  </div>
  <div class="am-g">
    <h1>东莞理工学院城市学院</h1>
    <p>校园资源云存储设计与实现</p>
    <!-- <p>东莞理工学院城市学院<br/>校园资源云存储设计与实现</p> -->
  </div>
  <hr />
</div>
<div class="am-g">
  <div class="am-u-lg-6 am-u-md-8 am-u-sm-centered">
  	<h3>登陆</h3>
    <form method="post" class="am-form">
      <label for="userName">账号:</label>
      <input type="text" name="userName" id="userName" placeholder="请输入用户名" >
      <br>
      <label for="password">密码:</label>
      <input type="password" name="password" id="password" placeholder="请输入密码">
      <br>
      <input type="hidden" name="service" id="service">
      <label for="remember-me">
      <input id="remember-me" type="checkbox">
        记住密码
      </label>
      <br />
      <div class="am-cf">
        <input type="button" id="login" value="登 录" class="am-btn am-btn-primary am-btn-sm am-fl">
        <%-- <input type="button" onclick="javascript:window.location.href='${ctx}/register?isPage=true'" value="注 册" class="am-btn am-btn-secondary am-btn-sm am-fl"> --%>
        <input type="button" value="忘记密码 ^_^? " class="am-btn am-btn-default am-btn-sm am-fr">
      </div>
    </form>  
    <hr>
    <p class="am-text-xs">© 2016 软件工程（1）林晓升 毕设.</p>
  </div>
</div>
<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
<script type="text/javascript">
			jQuery(function($) {
				 $("#login").click(function() {
						var userName = $("#userName").val();
						var password = $("#password").val();
						if (!!!userName) {
							warning("账号不允许为空");
							$("#userName").focus();
							return;
						}
						if (!!!password) {
							warning("密码不允许为空");
							$("#password").focus();
							return;
						}						
						  $.ajax({
								type : 'post',
								url : '${ctx}/login',
								data : {
									"userName" : userName,
									"password" : password
								},
								dataType:'json',
								error : function(request) {
									warning('连接失败');
								},
								success : function(data) {
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
				//回车事件
				$(document).keypress(function(e) {
					if(e.which == 13) {
						$("#login").click();
					}
				});
			
		});
		</script>
		
</body>
</html>
