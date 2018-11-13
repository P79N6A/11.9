$(document).ready(function() {
    new mulitinputbox('#requestIds');
    new mulitinputbox('#outTradeNos');
    new mulitinputbox('#orderNos');
    new mulitinputbox('#bankTradeIds');
    new mulitinputbox('#transactionIds');
    var options = {
        dateFormat: 'yy-mm-dd',
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1,
        maxDate:0
    };
    DatePickerExt.timeBetween("createDateStart","createDateEnd",options);
    DatePickerExt.timeBetween("successDateStart","successDateEnd",options);
    DatePickerExt.timeBetween("refundDateStart","refundDateEnd",options);
    bodyLoad();
    // $('select').comboSelect();
});

function checkForm(){
    var uniqueField = ['requestIds','outTradeNos','orderNos','bankTradeIds','transactionIds'];
    for (var i=0;i<uniqueField.length;i++) {
        if (!isBlank($('#' + uniqueField[i]).val())) {
            return true;
        }
    }
    var createDateStart = $('#createDateStart').val();
    var createDateEnd = $('#createDateEnd').val();
    var successDateStart = $('#successDateStart').val();
    var successDateEnd = $('#successDateEnd').val();
    var refundDateStart = $('#refundDateStart').val();
    var refundDateEnd = $('#refundDateEnd').val();

    if(!isBlank(createDateEnd)&&!isBlank(createDateStart)){
        return validateTime(createDateStart,createDateEnd,7);
    }
    if(!isBlank(successDateStart)&&!isBlank(successDateEnd)){
        return validateTime(successDateStart,successDateEnd,7);
    }
    if(!isBlank(refundDateStart)&&!isBlank(refundDateEnd)){
        return validateTime(refundDateStart,refundDateEnd,7);
    }
    MessageBoxExt.alert("请输入订单号 或 开始时间及结束时间");
    return false;
}

function bodyLoad(){
    $('div.result ul').clone(true).appendTo($("div.operation"));
    var ul = $("div.operation ul");
    ul.prepend('<li><a href="' + GV.ctxPath + 'monitor/realtime" target="_blank" class="list_link" id="monitor">FE监控~biu~biu~</a></li>');
    ul.prepend('<li><a href="#" onclick="batchRefund(this)" class="list_link" id="refund">补发退款</a></li>');
    ul.prepend('<li><a href="#" onclick="batchReNotify(this)" class="list_link" id="notify">补发通知</a></li>');
//    ul.prepend('<li><a href="#" onclick="batchRepair(this)" class="list_link" id="repair">批量补单</a></li>');

}

function getSelectOrderId(opreation) {
    var orderIdArr = getSelectValue("orderId");
    if(orderIdArr==null||orderIdArr.length<1) {
        throw showMsg("错误:请选择需要" + opreation + "的订单");
    }
    return orderIdArr.join(",");
}

/**
 * 批量补单
 * @param ele
 * @param url
 */
function batchRepair(ele) {
    disableElement(ele);
    var params = "params="+getSelectOrderId("补单");
    var url = GV.ctxPath + "order/batchRepair";
    ajaxPost(params,url,"补单失败");
    enableElement(ele);
}

/**
 * 批量补发通知
 * @param ele
 * @param url
 */
function batchReNotify(ele) {
    disableElement(ele);
    var params = "params="+getSelectOrderId("补发通知");
    var url = GV.ctxPath + "order/batchReNotify";
    ajaxPost(params,url,"补发通知失败");
    enableElement(ele);
}

/**
 * 批量退款
 * @param ele
 */
function batchRefund(ele) {
    var params = "params="+getSelectOrderId("补发退款");
    MessageBoxExt.confirm("警告!请确认选择订单是否正确,确定将发起退款!",refund,false);
}

function refund() {
    var params = "params="+getSelectOrderId("补发退款");
    var url = GV.ctxPath + "refund/batchRefund";
    ajaxPost(params,url,"补发退款失败");
}