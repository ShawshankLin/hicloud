<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<div class="am-modal am-modal-loading am-modal-no-btn" tabindex="-1" id="my-modal-loading">
  <div class="am-modal-dialog">
    <div class="am-modal-hd">请稍等...</div>
    <div class="am-modal-bd">
      <span class="am-icon-spinner am-icon-spin"></span>
    </div>
  </div>
</div>

<!-- <footer data-am-widget="footer"
        class="am-footer am-footer-default"
         data-am-footer="{  }">
  <div class="am-footer-miscs ">
      <p>CopyRight©2016 东莞理工学院城市学院.</p>
      <p>京ICP备xxxxxx</p>
  </div>
</footer> -->


<!-- Script -->
<!--[if lt IE 9]>
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/amazeui.ie8polyfill.min.js"></script>
<![endif]-->

<!--[if (gte IE 9)|!(IE)]><!-->
<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/jquery.min.js"></script>
<!--<![endif]-->
<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/amazeui.min.js"></script>
<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/app.js"></script>
<script src="${ctx}/resources/AmazeUI-2.5.2/assets/js/amazeui.datatables.min.js"></script>
<script src="${ctx}/resources/scripts/dataTables.responsive.js"></script>
<!-- common -->
<script type="text/javascript" src="${ctx}/resources/scripts/common.js"></script>

<script src="https://cdn.goeasy.io/goeasy.js"></script>
<%-- <script src="${ctx}/resources/scripts/goeasy.js"></script> --%>

<script type="text/javascript">
	//登出
	jQuery(function($) {
		
		var cookie = $.AMUI.utils.cookie; // 获取cookie
		if(cookie.get("NAV")==null){
			cookie.set("NAV","myfile",cookieTime(),"/hicloud");
		}
		if(cookie.get("STYLE")==null){
			cookie.set("STYLE","grid",cookieTime(),"/hicloud");
		}
		
		var nav=cookie.get("NAV");
		$('#nav > li').each(function(i,item){
			if($(item).attr('nav')==nav){
				$(item).addClass('am-active');
			}
		});
		
		$('#nav > li').click(function(){
			cookie.set("NAV",$(this).attr('nav'),cookieTime(),"/hicloud");
		});
		
		var goEasy = new GoEasy({
			appkey: "a57f10ee-5123-4c50-b396-a442d52e163a"
		});
		goEasy.subscribe({
				channel: "${LOGINUSER.id}",
				onMessage: function (message) {
					var notice=message.content.replace(/&quot;/g,'\"');
					var json = $.parseJSON($.parseJSON(notice));
					$('#msg-content').text(json.message);
					$('#msg-target').empty().append(json.target);
					$('#msg-notice').show();
					setTimeout("$('#msg-notice').hide()",5000);
			}
		});	
	});
	
</script>
