function showMsg(msg){
    MessageBoxExt.alert(msg);
}

//获取当前时间
function getNowDate(){
    var date=new Date();
    var month=date.getMonth()+1;
    var day=date.getDate();
    if(month<10)
        month=0+""+month;
    if(day<10)
        day=0+""+day;
    return date.getFullYear()+"-"+month+"-"+day;
}
/**
 * 添加日期
 * @param date
 * @param days
 * @returns {Date}
 */
function addDate(date,days){
    var d=new Date(date);
    d.setDate(d.getDate()+days);
    return d;
}

//设置时间格式
var defaultTime = function(date){
    try {
        var format = format || 'yyyy-MM-dd HH:mm:ss';
        var lang = {
            'M+': date.getMonth() + 1,
            'd+': date.getDate(),
            'H+': date.getHours(),
            'm+': date.getMinutes(),
            's+': date.getSeconds()
        };
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var key in lang) {
            if (new RegExp('(' + key + ')').test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ?
                    lang[key] : ('00' + lang[key]).substr(('' + lang[key]).length));
            }
        }
        return format;
    } catch (e) {
        return '-';
    }
}

function initDatePick(start,end,set){
    DatePickerExt.between(start, end, {maxDate: 0});
    if(set) {
        $("#" + start).attr("value", getNowDate());
        $("#" + end).attr("value", getNowDate());
    }else{
        $("#" + start).attr("value", "");
        $("#" + end).attr("value", "");
    }
}

function selectAll(name,value) {
    var valueList=$("input[name='"+name+"']");
    for(var i=valueList.length;i>0;i--){
        if (value)
            valueList[i-1].checked=true;
        else
            valueList[i-1].checked=false;
    }
}

function getSelectValue(name) {
    var valueList=$("input[name='"+name+"']");
    var values=[];
    for(var i=valueList.length;i>0;i--){
        if(valueList[i-1].checked){
            values.push(valueList[i-1].value);
        }
    }
    return values;
}

function ajaxPost(data, url, msg) {
    $.ajax({
        type: 'POST',
        url: url,
        // contentType: "text/plain;charset=UTF-8",
        // contentType: "application/json; charset=utf-8",
        dataType: 'json',
        // traditional:true,
        data: data,
        success: function (json) {
            console.log(json);
            var show = "成功数:" + json.detail.success + ", 忽略数:" + json.detail.ignore + "<br>";
            if (json.detail.errorList != null && json.detail.errorList.length > 0) {
                show += "失败数:" + json.detail.errorList.length + ";失败信息如下:<br>"
                for (var i= 0; i<json.detail.errorList.length; i++){
                    show += json.detail.errorList[i] + "<br>";
                }
            }
            showMsg(show);
        }
    });
}

function initSelect(eid, value) {
    if(value!=null&&value!=""){
        var eSelect=$('#'+eid).children();
        for(var i=0;i<eSelect.length;i++){
            if(eSelect[i].value==value){
                eSelect[i].selected=true;
                break;
            }
        }
    }
}

function changeInputType(ele) {
    
}

/**
 * 禁止元素点击
 * @param ele
 */
function disableElement(ele) {
    ele.disabled = "disabled";
}
/**
 * 允许元素点击
 * @param ele
 */
function enableElement(ele) {
    ele.removeAttribute("disabled");
}