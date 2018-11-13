function updateCustomerStatus(customerNumber){
	MessageBoxExt.ajax({
		url : GV.ctxPath + "customer/viewCustomerStatus",
		data : {
			"customerNumber" : customerNumber
		},
		style : "BASIC",
		success : function(result) {
			initEditPanel(result);
		}
	});
}

function initEditPanel(result){
	var form = $("#updateCustomerForm");
	form[0].reset();
	form.find("input,textarea,select").each(function() {
		var item = $(this);
		var name = item.attr("name");
		var info = result[name];
		if (typeof (info) != "undefined") {
			item.val(info + "");
		}
	});
	var title = "修改商户状态";
	var buttons = [ {
		text : '提交',
		click : function() {
			MessageBoxExt.ajax({
				url : GV.ctxPath + "customer/updateCustomerStatus",
				data : form.serialize(),
				style : "REDIRECT"
			});
		
		}
	}, {
		text : '取消',
		click : function() {
			$(this).dialog("close");
		}
	} ];
	MessageBoxExt.popup("updateCustomerDiv", {
		"title" : title,
		"width" : "auto",
		"height" : "auto",
		"buttons" : buttons
	});
}
