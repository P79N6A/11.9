<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:url var="resourceUrl" value="/static"/>
<spring:url var="ctx" value="/"/>
<script type="text/javascript">
    var GV = {
        ctxPath: '${ctx}',
        imgPath: '${resourceUrl}/images',
        jsPath: '${resourceUrl}/static/js'
    };
</script>
<script type="text/javascript" src="${ctx}static/js/chart/exporting.js"></script>
<script type="text/javascript" src="${ctx}static/js/chart/highcharts.js"></script>
<script type="text/javascript" src="${ctx}static/js/chart/drawchart.js"></script>
<link href="${ctx}static/css/monitor.css" type="text/css" rel="stylesheet" />
<style type="text/css">
</style>
<div class="linkedTitle">
    <ul class="monitor index">
        <a href="${ctx}monitor/realtime"><li class="monitorLi">实时下单统计</li></a>
        <a href="${ctx}monitor/dayCompare"><li class="monitorLi">24小时订单量对比</li></a>
        <a href="${ctx}monitor/customer"><li class="monitorLi">近7日交易成功前十商户</li></a>
        <%--<li class="monitorLi">成功交易统计</li>--%>
        <%--<li class="monitorLi">高级视图</li>--%>
    </ul>
</div>
