function getCook(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    arr = document.cookie.match(reg);
    if (arr) {
        return unescape(arr[2]);
    } else {
        return null;
    }
}
function chatClick(gId, c) {
    if ("undefined" == typeof jesong) {
        openChat(gId, c);
    } else {
        openJesongChat(gId, c);
    }
}
function openChat(gId, c) {
    var url = "https://kefu.yeepay.com/live/chat.dll?";
    if (c != null) {
        url += "&c=" + c;
    }
    if (gId != null) {
        url += "&g=" + gId;
    }
    url = url + "&chatUrl=" + window.encodeURIComponent(window.location.href);
    if (document.referrer) {
        url = url + "&ref=" + encodeURIComponent(document.referrer);
    }
    var p = "height=500,width=800,directories=no,location=no,menubar=no,resizeable=no,status=no,toolbar=no,top=100,left=200";
    try {
        var cw = window.open(url, 'chat_' + 1, p);
        cw.focus();
    } catch (e) {
        if (c.force) window.location = url;
    }
}

function setRParams(orderId, price, time, orderType, orderState, shop, shopOrderId, bankOrderId, error, chat_type) {
    var param = {};
    param.orderId = orderId;
    param.price = price;
    param.time = time;
    param.orderType = orderType;
    param.shop = shop;
    param.orderState = orderState;
    param.shopOrderId = shopOrderId;
    param.bankOrderId = bankOrderId;
    param.error = error;
    param.chat_type = chat_type;
    return param;
}
function getRParams(param) {
    var str = "#params:";
    for(var key in param){
        str += "" + key + "," + param[key] + ",";
    }
    return str.substring(0, str.length - 1);
}

/**
 * 设置用户信息
 * @param orderId 订单id
 * @param price 金钱
 * @param time 创建时间
 * @param orderType 交易类型
 * @param orderState 订单状态
 * @param shop 商家名称
 * @param shopOrderId  商家订单编号
 * @param bankOrderId 银行订单编号
 * @param error 错误信息
 * @param chat_type 接入渠道
 */
function setChatUserInfo(orderId, price, time, orderType, orderState, shop, shopOrderId, bankOrderId, error, chat_type) {
	orderId = isUndefined(orderId);
	price = isUndefined(price);
	orderType = isUndefined(orderType);
	orderState = isUndefined(orderState);
	shop = isUndefined(shop);
	shopOrderId = isUndefined(shopOrderId);
	bankOrderId = isUndefined(bankOrderId);
	error = isUndefined(error);
	chat_type = isUndefined(chat_type);
    var params = setRParams(orderId, price, time, orderType, orderState, shop, shopOrderId, bankOrderId, error, chat_type);
    var paramsStr = getRParams(params);
    document.cookie="JESONG_EXT_DATA="+  escape(paramsStr);
}
/**
 * 示例代码
 */
function testCreateChat(){
    setChatUserInfo(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    openPcPosChat();
    //或者直接输入参数信息
    //openPcPosChatWithUserInfo(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
}
/**
 * 打开对话接口
 */
function openPcPosChat() {
    openJesongChat(2530, 1);
}
/**
 * 打开对话接口,附带用户参数信息
 * @param orderId 订单id
 * @param price 金钱
 * @param time 创建时间
 * @param orderType 交易类型
 * @param orderState 订单状态
 * @param shop 商家名称
 * @param shopOrderId  商家订单编号
 * @param bankOrderId 银行订单编号
 * @param error 错误信息
 * @param chat_type 接入渠道
 */
function openPcPosChatWithUserInfo(orderId, price, time, orderType, orderState, shop, shopOrderId, bankOrderId, error, chat_type)
{
    setChatUserInfo(orderId, price, time, orderType, orderState, shop, shopOrderId, bankOrderId, error, chat_type);
    openJesongChat(2530, 1);
}
/**
 * 创建对话
 * @param gId 技能组id
 * @param c 公司id
 */
function openJesongChat(gId, c) {
    var url = "https://kefu.yeepay.com/live/chat.dll?";
    if (c != null) {
        url += "&c=" + c;
    }
    if (gId != null) {
        url += "&g=" + gId;
    }
    var v = this.getCook('JESONG_VISITOR_ID');
    if (v != null && v.length != 0) {
        url += "&v=" + v;
    }
    var uId = this.getCook('JESONG_USER_ID');
    if (uId != null && uId.length != 0) {
        url += "&v=" + uId;
    }
    var cKey = this.getCook("reseveKey");
    if (cKey != null && cKey.length != 0) {
        url += '&r=' + window.encodeURIComponent(uKey);
    }
    var exts = null;
    if(this.getCook("JESONG_EXT_DATA")){
        exts = this.getCook("JESONG_EXT_DATA");
    }
    if (exts != null && exts != "") {
        url += '&ext=' + window.encodeURIComponent(exts);
    }
    if (this.getCook("JESONG_S_FLAG")) {
        url += '&sf=' + window.encodeURIComponent(this.getCook("JESONG_S_FLAG"));
    }
    if (this.getCook("JESONG_REFER")) {
        url += '&ref=' + window.encodeURIComponent(this.getCook("JESONG_REFER"));
    }
    if (this.getCook("JESONG_FIRST")) {
        url = url + "&first=" + window.encodeURIComponent(this.getCook("JESONG_FIRST"));
    }
    if (this.getCook('JESONG_VC')) {
        url = url + "&vc=" + window.encodeURIComponent(this.getCook("JESONG_VC"));
    }
    url = url + "&chatUrl=" + window.encodeURIComponent(window.location.href);
    var p = "height=660,width=1366,directories=no,location=no,menubar=no,resizeable=no,status=no,toolbar=no,top=100,left=200";
    try {
        // window.location.href = url;
        var cw = window.open(url,'chat_'+1,p);cw.focus();
    } catch (e) {
        if (c.force) window.location = url;
    }
}

//判断是否为undefined
function isUndefined(value){
	if(typeof(value)=="undefined"||value=="null"){
		return value;
	}else{
		var tempValue = value.replace(/;/g,' ');
		return tempValue;
	}
}