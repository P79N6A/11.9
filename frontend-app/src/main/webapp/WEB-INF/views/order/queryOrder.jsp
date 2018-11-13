<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <title>订单查询--前端支付</title>
    <link href="${ctx}/static/css/operation.css" type="text/css" rel="stylesheet" />
    <link href="${ctx}/static/css/jquery.mulitinputbox.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/static/js/jquery-ui/js/jquery.mulitinputbox.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/validate.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/operation.js"></script>
    <script type="text/javascript" src="${ctx}/static/js/orderOperate.js"></script>
</head>
<body>
<div class="Container">
    <div class="Content fontText">
        <div class="information">
            <form action="${ctx}order/queryOrder" id="queryOrderForm" name="queryOrderForm" method="get"
                  onsubmit="return checkForm()">
                <div class="search">
                    <div class="search_tit"><h2>订单查询</h2></div>
                    <div class="search_con" style="padding-left: 2%;padding-right: 0%;min-width: 1300px">
                        <ul class="fix query_block">
                            <li><label class="text_tit">商户编号：</label>
                                <input type="text" name="customerNumber" class="input_text"
                                       value='<c:out value="${param.customerNumber}"/>'/>
                            </li>
                            <li><label class="text_tit">商户订单号：</label>
                                <input type="text" name="outTradeNos" class="input_text" id="outTradeNos"
                                       value='<c:out value="${param.outTradeNos}"/>'/>
                            </li>
                            <li><label class="text_tit">交易系统订单号：</label>
                                <input type="text" name="requestIds" class="input_text" id="requestIds"
                                       value='<c:out value="${param.requestIds}"/>'/>
                            </li>
                            <li><label class="text_tit">银行子系统订单号：</label>
                                <input type="text" name="orderNos" class="input_text" id="orderNos"
                                       value='<c:out value="${param.orderNo}"/>'/>
                            </li>
                            <li><label class="text_tit">渠道订单号：</label>
                                <input type="text" name="bankTradeIds" class="input_text" id="bankTradeIds"
                                       value='<c:out value="${param.bankTradeIds}"/>'/>
                            </li>
                            <li><label class="text_tit">第三方订单号：</label>
                                <input type="text" name="transactionIds" class="input_text" id="transactionIds"
                                       value='<c:out value="${param.transactionIds}"/>'/>
                            </li>
                        </ul>
                        <ul class="fix query_block">
                            <li><label class="text_tit">业务方：</label>
                                <select class="select" name="requestSystem" id="requestSystem">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items="${requestSystemMap}" varStatus="ostatus">
                                        <option value="<c:out value="${entry.key}"/>" ${entry.key eq param.requestSystem ? 'selected = "selected"':'' }>
                                            <c:out value="${entry.value}" />
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">支付平台：</label>
                                <select class="select" name="platformType" id="platformType">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${platformMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.platformType ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">支付方式：</label>
                                <select class="select" name="orderType" id="orderType">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${orderTypeMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.orderType ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">支付银行：</label>
                                <select class="select" name="payBank" id="payBank">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${bankMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.payBank ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">支付卡种类型：</label>
                                <select class="select" name="cardType" id="cardType">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${cardTypeMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.cardType ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">支付产品：</label>
                                <select class="select" name="paymentProduct" id="paymentProduct">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${paymentProductMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.paymentProduct ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">订单方：</label>
                                <select class="select" name="orderSystem" id="orderSystem">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${orderSystemMap}' varStatus="orderSys" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.orderSystem ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                        </ul>
                        <ul class="fix query_block">
                            <li><label class="text_tit">订单状态：</label>
                                <select class="select" name="payStatus" id="payStatus">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${payStatusMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.payStatus ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">通知状态：</label>
                                <select class="select" name="notifyStatus" id="notifyStatus">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${notifyStatusMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.notifyStatus ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">退款状态：</label>
                                <select class="select" name="refundStatus" id="refundStatus">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${refundStatusMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.refundStatus ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">退款类型：</label>
                                <select class="select" name="refundType" id="refundType">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${refundTypeMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.refundType ? 'selected = "selected"':''} >
                                                ${entry.value}
                                        </option>
                                    </c:forEach>
                                </select>
                            </li>
                            <li><label class="text_tit">退款时间：</label>
                                <input type="text" name="refundDateStart" id="refundDateStart" class="input_text">
                            </li>
                            <li><label class="text_tit">至：</label>
                                <input type="text" name="refundDateEnd" id="refundDateEnd" class="input_text">
                            </li>
                        </ul>
                        <ul class="fix query_block">
                            <li><label class="text_tit">订单创建时间：</label>
                                <input type="text" name="createDateStart" id="createDateStart" class="input_text"
                                       value="${param.createDateStart!=null?param.createDateStart:param.dateStart}">
                            </li>
                            <li><label class="text_tit">至：</label>
                                <input type="text" name="createDateEnd" id="createDateEnd" class="input_text"
                                       value="${param.createDateEnd!=null?param.createDateEnd:param.dateEnd}">
                            </li>
                            <li><label class="text_tit">订单成功时间：</label>
                                <input type="text" name="successDateStart" id="successDateStart" class="input_text"
                                       value="${param.successDateStart}">
                            </li>
                            <li><label class="text_tit">至：</label>
                                <input type="text" name="successDateEnd" id="successDateEnd" class="input_text"
                                       value="${param.successDateStart}">
                            </li>
                            <li><label class="text_tit">支付金额：</label>
                                <input type="input" name="totalAmountStart" id="totalAmountStart" class="input_text"
                                       value="${param.totalAmountStart}" onblur="checkNum(this)">
                            </li>
                            <li><label class="text_tit">至：</label>
                                <input type="input" name="totalAmountEnd" id="totalAmountEnd" class="input_text"
                                       value="${param.totalAmountEnd}" onblur="checkNum(this)">
                            </li>
                        </ul>
                        <div class="clearer"></div>
                        <div class="btn">
                            <span style="color:red;margin-right: 300px">批量查询时请用换行或英文逗号隔开</span>
                            <input type="hidden" name="queryOrder._cpage" value="1">
                            <input type="hidden" name="queryOrder._pagesize" value="${param.queryOrder._pagesize}">
                            <input type="submit" class="btn_sure fw" value="查询"/>
                            <input type="button" class="btn_cancel fw" value="清空" onclick="clearAll()"/>
                            <span style="color:red;margin-left: 250px">时间间隔最大为7天</span>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="operation">
</div>
<div class="clearer"></div>
<div class="result">
    <q:table queryService="frontEndQueryService" queryKey="queryOrder" formId="queryOrderForm" class="list" style="width:2100px" >
        <q:nodata>无符合条件的记录</q:nodata>
        <q:column title="选择当前页<input type='checkbox' onclick=selectAll('orderId',this.checked) /><br>序号" escapeHtml="false">
            <span><input type="checkbox" value="${request_id}-${request_system}-${platform_type}" name="orderId"/></span><span>${_rowstatus.globalIndex}</span>
        </q:column>
        <q:column title="业务方</br>订单方</br>支付产品" escapeHtml="false" width="100px">
            ${requestSystemMap.get(request_system) }</br>
            ${order_system!=null?orderSystem.get(order_system):"------"}</br>
            ${payment_product!=null ? paymentProductMap.get(payment_product):"------"}</br>
        </q:column>
        <q:column title="支付平台<br>支付方式" escapeHtml="false" width="100px">
            ${platform_type!=null?platformMap.get(platform_type):"------"}<br>
            ${orderTypeMap.get(order_type)}</br>
        </q:column>
        <q:column title="商户编号<br>商户订单号<br>第三方订单号" escapeHtml="false" width="250px">
            ${customer_number}<br>
            ${out_trade_no}<br>
            ${transaction_id!=null?transaction_id:"------"}<br>
        </q:column>
        <q:column title="交易系统订单号<br>银行子系统订单号<br>渠道订单号" escapeHtml="false" width="250px">
            ${request_id}<br>
            ${order_no!=null?order_no:"------"}<br>
            ${bank_trade_id!=null?bank_trade_id:"------"}<br>
        </q:column>
        <q:column title="交易金额<br>分期公司<br>分期期数" escapeHtml="false" width="130px">
            ${total_amount}<br>
            ${company != null ? company : "------"}<br>
            ${loan_term != null ? loan_term : "------"}<br>
        </q:column>
        <c:if test="${showCost}">
            <q:column title="成本" escapeHtml="true" width="130px">
                ${bank_total_cost!=null?bank_total_cost:"------"}
            </q:column>
        </c:if>
        <q:column title="商品描述<br>WX-OPENID" escapeHtml="false" width="150px" >
            ${goods_description!=null?goods_description:"------"}<br>
            ${open_id!=null?open_id:"------"}<br>
        </q:column>
        <q:column title="银行<br>卡种" escapeHtml="false" width="70px">
            ${pay_bank!=null?pay_bank:"------"}<br>
            ${pay_bankcard_type!=null?cardTypeMap.get(pay_bankcard_type):"------"}<br>
        </q:column>
        <q:column title="支付状态<br>通知状态" escapeHtml="false" width="70px">
            ${payStatusMap.get(pay_status)}<br>
            ${notify_status!=null?notifyStatusMap.get(notify_status):"------"}<br>
        </q:column>
        <q:column title="下单时间<br>修改时间<br>过期时间" escapeHtml="false" width="190px">
            ${create_time}<br>
            ${modify_time!=null?modify_time:"------"}<br>
            ${expire_time!=null?expire_time:"------"}<br>
        </q:column>
        <q:column title="退款状态<br>退款类型<br>退款时间" escapeHtml="false" width="190px">
            ${refund_status!=null?refundStatusMap.get(refund_status):"------"}<br>
            ${refund_type!=null?refundTypeMap.get(refund_type):"------"}<br>
            ${refund_date!=null?refund_date:"------"}<br>
        </q:column>
        <q:column title="第三方成功时间<br>银行子系统成功时间" escapeHtml="false" width="190px">
            ${bank_success_time!=null?bank_success_time:"------"}<br>
            ${pay_success_time!=null?pay_success_time:"------"}<br>
        </q:column>
        <q:column title="下单IP<br>支付接口<br>限制卡种" width="120px" escapeHtml="false" >
            ${payer_ip!=null?payer_ip:"------"}<br>
            ${pay_interface!=null?pay_interface:"------"}<br>
            ${pay_limit_type!=null?pay_limit_type:"------"}
        </q:column>
        <q:column title="支付流水" escapeHtml="false" width="70px">
            <a href="${ctx}payRecord/queryDetail.action?orderNo=${order_no}&platformType=${platform_type}">详情</a>
        </q:column>
    </q:table>
</div>
<div class="clearer"></div>
</body>
</html>