/**
 * 时间及时间控件初始化等操作文件
 * @author meiling.zhuang
 * @date   2015/12/28
 */

var DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

Date.prototype.format = function(format){ 
	var o = { 
			"M+" : this.getMonth()+1, //month 
			"d+" : this.getDate(), //day 
			"h+" : this.getHours(), //hour 
			"m+" : this.getMinutes(), //minute 
			"s+" : this.getSeconds(), //second 
			"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
			"S" : this.getMilliseconds() //millisecond 
	} 
	
	if(/(y+)/.test(format)) { 
		format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 
	
	for(var k in o) { 
		if(new RegExp("("+ k +")").test(format)) { 
			format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
		} 
	} 
	return format; 
} 

/**
 * 页面加载时，初始化日期控件
 */
$(document).ready(function(){
	
	//选择时间类型 创建时间 ｜ 更新时间 二者选一
	$("#timeType").change(function(){
		InitTimeTypeBySelectedOption();
	});
	
	
	// 页面刚打开时 初始化日期控件
	InitTimeTypeBySelectedOption();
	initDate("startCreateDate", "endCreateDate");
	initDate("startUpdateDate", "endUpdateDate");
	
	// 为兼容鉴权豁免白名单 (不存在切换）
	initDateRangeElements("beginTime", "endTime");
	initDate("beginTime", "endTime");
	
	if($("#timeType").length == 0){
		initDateRangeElements("startCreateDate", "endCreateDate");
		initDateRangeElements("startUpdateDate", "endUpdateDate");
	}
});

function InitTimeTypeBySelectedOption(){
	if($("#timeType").length == 0) return;
	var timeType = $("#timeType").val();
	var curStartDateElement = "";
	var curEndDateElement = "";
	var curStartDate = "";
	var curEndDate = "";
	
	// 选中的是创建时间
	if(timeType == "createTime"){
		curStartDateElement = "startCreateDate"; 
		curEndDateElement = "endCreateDate";
		// 前一个状态：更新时间状态
		var obj = $("#startUpdateDate");
		if(obj.length > 0){
			curStartDate = obj.val();
			$("<input type='text' name='startCreateDate' id='startCreateDate' class='input_text Wdate' />").insertAfter("#startUpdateDate")
			obj.remove();
		}
		obj = $("#endUpdateDate");
		if(obj.length > 0){
			curEndDate = obj.val();
			$("<input type='text' name='endCreateDate' id='endCreateDate' class='input_text Wdate' />").insertAfter("#endUpdateDate")
			obj.remove();
		}
	}
	
	// 选中的是更新时间
	else{
		curStartDateElement = "startUpdateDate";
		curEndDateElement = "endUpdateDate";
		//前一个状态是创建时间
		var obj = $("#startCreateDate");
		if(obj.length > 0){
			curStartDate = obj.val();
			$("<input type='text' name='startUpdateDate' id='startUpdateDate' class='input_text Wdate' />").insertAfter("#startCreateDate")
			obj.remove();
		}
		var obj = $("#endCreateDate");
		if(obj.length > 0){
			curEndDate = obj.val();
			$("<input type='text' name='endUpdateDate' id='endUpdateDate' class='input_text Wdate' />").insertAfter("#endCreateDate")
			obj.remove();
		}
	}
	
	// init 日期控件
	initDateRangeElements(curStartDateElement,curEndDateElement);
	if(curStartDate == "") return;
	$("#" + curStartDateElement).val(curStartDate);
	$("#" + curEndDateElement).val(curEndDate);
}

function initDate(startDateElementId,endDateElementId){
	if($("#"+startDateElementId).length == 0) return ;
	var today = new Date();
	var tomorrow = new Date(today.getTime() + 1 * 24 * 60 * 60 * 1000);
	$("#"+startDateElementId).val(today.format("yyyy-MM-dd") + " 00:00:00");
	$("#"+endDateElementId).val(tomorrow.format("yyyy-MM-dd") + " 00:00:00");
}


/**
 * 初始化时间控件（初始时间 － 截至时间）
 * @param startDateElementId
 * 		初始时间控件的id
 * @param endDateElementId
 * 		截至时间控件的id
 */
function initDateRangeElements(startDateElementId,endDateElementId){
	if($("#"+startDateElementId).length == 0) return ;
	if($("#"+endDateElementId).length == 0 ) return;
	$("#"+startDateElementId).focus(function(){
		var endCreateTimeElement = $dp.$(endDateElementId);
		WdatePicker({
			onpicked:function(){
				endCreateTimeElement.focus();
			},
			maxDate:'#F{$dp.$D(' + endDateElementId + ')}',
			skin:'whyGreen', 
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			startDate:'%y-%M-%d 00:00:00'
		});
	});
	
	$("#"+endDateElementId).focus(function(){
		WdatePicker({
			minDate:'#F{$dp.$D('+ startDateElementId + ')}',
			skin:'whyGreen', 
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			startDate:'%y-%M-{%d+1} 00:00:00'
		});
	});
	
	
}



/**
 * 获取当前时间curDate dayBetween天后／前的日期
 * @param curDate
 * @param dayBetween
 * @returns
 */
function getWannaDate(curDate,dayBetween){
	var wannaDate = new Date(curDate.getTime() + 1000*3600*24*dayBetween);
	return wannaDate.format(DATE_FORMAT);
}


/**
 * 将从时间插件获取到的字符串转换为javascript的Date类型
 * @param timeStr
 * @returns {Date}
 */
function getDateByStr(timeStr){
	var wannaDate = new Date(Date.parse(timeStr.replace(/-/g, "/")));
	return wannaDate;
}