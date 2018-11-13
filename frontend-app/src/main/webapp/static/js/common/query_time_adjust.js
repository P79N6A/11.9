/**
 * @description 将对查询条件中对时间属性的限制都写在这个文件中
 * @rule 带索引的查询条件不为空时，创建时间范围控制在365天内／／带索引的查询条件为空时，创建时间范围控制在7天内
 * @author meiling.zhuang
 * @date   2016/01/08
 */

var ONE_DAY = 24*3600*1000;
var SHORT_DAYS = 7;
var LONG_DAYS = 365;


function checkTime(attriesWithIdx, attriesNameWithIdx, startTimeElement, endTimeElement){
	
	if(startTimeElement.length == 0){
		alert("该页面找不到id为" + startTimeElement + "的元素.");
		return false;
	}
	
	var startTimeStr = startTimeElement.val().trim();
	var endTimeStr = endTimeElement.val().trim();
	var startTime = getDateByStr(startTimeStr);
	var endTime = getDateByStr(endTimeStr);

	var diffDaysNeed = LONG_DAYS;
	var isEmpty = true;
	// judge if the values of elements with index are all empty 
	for(var i=0;i<attriesWithIdx.length;i++)
		if(attriesWithIdx[i]!="") 
			isEmpty = false;
	// if all empty, we need to limit createDate/updateDate's range 
	if(isEmpty) diffDaysNeed = SHORT_DAYS;
	adjustCreateTime(startTime, endTime, diffDaysNeed, attriesNameWithIdx, endTimeElement);
	
	return true;

}


/**
 * 没有带索引的查询条件时只允许创建时间范围在一周内的
 */
function adjustCreateTime(startTime, endTime, diffDaysNeed, attriesNameWithIdx, endTimeElement){
	var deviation = endTime.getTime() - startTime.getTime();
	var days = Math.floor(deviation/ONE_DAY);
	if(days < 0){
		alert("截止时间必须大于等于起始时间.");
		return;
	}
	if(days <= diffDaysNeed) return;
	if(diffDaysNeed == SHORT_DAYS){
		var names = "";
		for(var i=0;i<attriesNameWithIdx.length;i++)
			names = names + attriesNameWithIdx[i] + "、";
		names = names.substring(0, names.length-1);
		alert("当" + names + "同时为空时，时间范围不得超过7天");
	}
	else{
		alert("注意：时间范围最多允许一年以内");
	}
	endTimeElement.val(getWannaDate(startTime, diffDaysNeed));
}