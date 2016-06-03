<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/taglib.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

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
</head>
<body>
<!--[if lte IE 9]>
<p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
  以获得更好的体验！</p>
<![endif]-->
<%@ include file="/WEB-INF/views/includes/header.jsp" %>

<div class="am-cf admin-main">

  <!-- content start -->
  <div class="admin-content" style="height:auto;">

    <div class="am-cf am-padding">
      <div class="am-fl am-cf"><strong class="am-text-primary am-text-lg">消息提示</strong> / <small>message</small></div>
    </div>

    <div class="am-g">
      <div class="am-u-sm-12">
        <!-- <h2 class="am-text-center am-text-xxxl am-margin-top-lg">500. Server Exception</h2> -->
       <%--  <h2 class="am-text-center am-text-xxxl am-margin-top-lg">${exception}</h2> --%>
        <p class="am-text-center am-text-xl">${exception}</p>
        <pre class="page-404">
          .----.
       _.'__    `.
   .--($)($$)---/#\
 .' @          /###\
 :         ,   #####
  `-..__.-' _.-\###/
        `;_:    `"'
      .'"""""`.
     /,  ya ,\\
    //  500!  \\
    `-._______.-'
    ___`. | .'___
   (______|______)
        </pre>
      </div>
    </div>
  </div>
  <!-- content end -->

</div>

<a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

<%@ include file="/WEB-INF/views/includes/footer.jsp" %>

</body>
</html>

<script type="text/javascript">
	<%-- var url = '${responseURL}';
	var message = "<%=errMsg%>";
	layer.msg(message, {
		icon : 2,
		time : 1000
	}, function() {
		if (url == '') {
			history.back();
		} else {
			document.location.href = '${basePath}' + url;
		}
	}); --%>
</script>
</html>