<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/common.jsp" %>
    <script type="text/javascript" src="${ctx}/static/js/operation.js"></script>
    <title>订单支付流水详情</title>
</head>
<body>
<div class="clearer"></div>
<div class="result">
    <q:table queryService="frontEndQueryService" queryKey="queryRecord" formId="queryRecordForm" class="list">
        <q:nodata>无符合条件的记录</q:nodata>
        <q:column title="选择<input type='checkbox' onclick=selectAll('recordId',this.checked) /><br>序号" escapeHtml="false">
            <span><input type="checkbox" value="${id}" name="recordId"/></span><span>${_rowstatus.globalIndex}</span>
        </q:column>
        <q:column value="${request_system}" title="业务方" escapeHtml="false"  width="100px" />
        <q:column value="${customer_number}" title="商户编号" escapeHtml="false"  width="100px" />
        <q:column title="商户订单号<br>支付中心订单号<br>支付流水号" escapeHtml="false" width="180px" >
            ${request_id}<br>
            ${order_no}<br>
            ${record_no}<br>
        </q:column>
        <q:column value="${platform_type}" title="支付平台" width="70px" />
        <q:column value="${total_amount}" title="支付金额" width="100px" />
        <q:column value="${create_time}" title="创建时间" width="200px" />
        <q:column title="支付错误码<br>错误说明" escapeHtml="false" >
            ${response_code}<br>
            ${response_msg}<br>
        </q:column>
        <q:column title="银行错误码<br>错误说明" escapeHtml="false">
            ${nocard_code}<br>
            ${nocard_msg}<br>
        </q:column>
        <q:column value="${front_value}" title="返回值" escapeHtml="false" />
    </q:table>
</div>
<div class="clearer"></div>
</body>
</html>