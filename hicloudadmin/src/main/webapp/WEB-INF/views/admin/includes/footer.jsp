<%@ include file="../../includes/footer.jsp"%>

<script type="text/javascript">
	//登出
	jQuery(function($) {
		$("#logout").on("click", function(ev) {
			$('#my-confirm').modal({
		        relatedTarget: this,
		        onConfirm: function(options) {
		        	window.location.href = "${ctx}/admin/logout";
		        },
		        // closeOnConfirm: false,
		        onCancel: function() {
		        	
		        }
		      });
		});
	});
</script>