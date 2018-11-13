$(document).ready(function() {
	
	Form.formatBankCard("J-cardNmu",$("#token").val());
	
	$("#J-cardNmu").focus(function(){
		$("#errorMsg").attr("class", "none");
	});
	
	$("#next").click(function(){
		alert(1)
		if ($("#J-cardNmu").val() == "") {
			$("#errorMsg").attr("class", "error");
			$("#errorMsg").html('卡号不能为空，请输入正确卡号');
		} else {
			if (isNaN($("#J-cardNmu").val().replace(/\s/g,''))) {
				$("#errorMsg").attr("class", "error");
				$("#errorMsg").html('卡号格式错误，请输入正确卡号');
			} else {
				var length = $("#J-cardNmu").val().replace(/\s/g, '').length;
				if (length < 14 || length > 19) {
					$("#errorMsg").attr("class", "error");
					$("#errorMsg").html('请输入正确银行卡号');
				} else {
					var cardNo = $("#J-cardNmu").val().replace(/\s/g, '');
					var token = $("#token").val();
					$.post("/gateway/ajax/check", {
						token : token,
						cardno : cardNo
					}, function(response) {
						var result = eval('(' + response + ')');
						var error = result.error;
						var status = result.status;
						if (status == 1) {// 校验成功
							$("#errorMsg").attr("class", "success");
							$("#errorMsg").html("卡号正确,自动跳转至卡信息填写页面");
							//elem.value = cardNo.replace(/\s/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
							$("#form1").submit();
						} else {// 校验错误
							$("#errorMsg").attr("class", "error");
							$("#errorMsg").html(error);
						}

					});

				}
			}
		}
	});
	
})