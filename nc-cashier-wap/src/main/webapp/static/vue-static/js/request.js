(function(global){
	'use strict'
	var  IS_SIGN = "is_production";  //前缀
	var Request = Request || {};
	Request.matchUrl=function (){
		var requestUrl = '';
		switch (IS_SIGN) {
		    case 'is_production':  //测试环境
		        requestUrl = contextPath;
		        break;
		    case 'is_test':  //本地开发
		        requestUrl = './dev/mock';
		        // requestUrl = './dev/mock';
		        break;
		    case 'is_qa':   //qa
		        requestUrl = 'http://172.18.166.152:7080/nc-cashier-wap';
		        break;
		}
		return requestUrl;
	}
	Request.url = Request.matchUrl()
	global.Request = Request;
}(window));