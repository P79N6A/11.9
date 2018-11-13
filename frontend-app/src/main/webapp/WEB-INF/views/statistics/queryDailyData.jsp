<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <title>日交易数据统计</title>
    <link href="${ctx}/static/css/jquery.mulitinputbox.css" type="text/css" rel="stylesheet" />
    <script type="text/javascript" src="${ctx}/static/js/jquery-ui/js/jquery.mulitinputbox.js"></script>
    <script type="text/javascript">
        $(document).ready(function() {
            new mulitinputbox('#customerNumber');
            var options = {
                dateFormat: 'yy-mm-dd',
                showSecond: true
            };
            DatePickerExt.between("orderDateStart","orderDateEnd",options);
        });
    </script>
</head>
<body>
<div class="Container">
    <div class="Content fontText">
        <div class="information">
            <form action="${ctx}statistics/dailyData" id="queryOrderForm" name="queryOrderForm" method="get">
                <div class="search">
                    <div class="search_tit"><h2>日交易数据统计</h2></div>
                    <div class="search_con" style="padding-left: 2%;padding-right: 0%">
                        <ul class="fix query_block">
                            <li><label class="text_tit">商户编号：</label>
                                <input type="text" name="customerNumber" class="input_text"
                                       value='<c:out value="${param.customerNumber}"/>'/>
                            </li>
                            <li><label class="text_tit">日期：</label>
                                <input type="text" name="orderDateStart" id="orderDateStart" class="input_text"
                                       value='${param.orderDateStart}'>
                            </li>
                            <li><label class="text_tit">至：</label>
                                <input type="text" name="orderDateEnd" id="orderDateEnd" class="input_text"
                                       value='${param.orderDateEnd}'>
                            </li>
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
                            <li><label class="text_tit">支付平台：</label>
                                <select class="select" name="platform" id="platform">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach var="entry" items='${platformMap}' varStatus="paySta" >
                                        <option value="${entry.key}"
                                            ${entry.key eq param.platform ? 'selected = "selected"':''} >
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
                        </ul>
                        <div class="clearer"></div>
                        <div class="btn">
                            <input type="hidden" name="queryOrder._cpage" value="${param.queryOrder._cpage}">
                            <input type="hidden" name="queryOrder._pagesize" value="${param.queryOrder._pagesize}">
                            <input type="submit" class="btn_sure fw" value="查询"/>
                            <input type="button" class="btn_cancel fw" value="清空" onclick="clearAll()"/>
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
    <q:table queryService="frontEndQueryService" queryKey="queryDailyData" formId="queryOrderForm" class="list" >
        <q:nodata>无符合条件的记录</q:nodata>
        <%--<q:column title="业务方</br>支付平台<br>支付方式" escapeHtml="false" width="100px">--%>
            <%--${requestSystemMap.get(request_system) }</br>--%>
            <%--${platform_type!=null?platformMap.get(platform_type):"------"}<br>--%>
            <%--${orderTypeMap.get(order_type)}</br>--%>
        <%--</q:column>--%>
        <q:column title="商户编号" escapeHtml="false" width="" >
            ${customer_number}
        </q:column>
        <q:column title="平台类型" escapeHtml="false" width="" >
            ${platform_type!=null?platformMap.get(platform_type):"------"}
        </q:column>
        <q:column title="业务方" escapeHtml="false" width="" >
            ${requestSystemMap.get(request_system) }
        </q:column>
        <q:column title="订单方" escapeHtml="false" width="" >
            ${order_system!=null?order_system:"------"}
        </q:column>
        <q:column title="订单类型" escapeHtml="false" width="" >
            ${orderTypeMap.get(order_type)}
        </q:column>
        <q:column title="订单状态" escapeHtml="false" width="" >
            ${payStatusMap.get(pay_status)}
        </q:column>
        <q:column title="交易笔数" escapeHtml="false" width="" >
            ${trx_count}
        </q:column>
        <q:column title="交易金额" escapeHtml="false" width="" >
            ${trx_amount}
        </q:column>
        <q:column title="成本" escapeHtml="false" width="" >
            ${bank_cost}
        </q:column>
        <q:column title="日期" escapeHtml="false" width="" >
            ${order_date}
        </q:column>
    </q:table>
</div>
<div class="clearer"></div>
</body>
</html>