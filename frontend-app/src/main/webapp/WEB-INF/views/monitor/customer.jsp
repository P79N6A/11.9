<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta name="decorator" content="none"/>
    <title>7天内商户交易统计</title>
    <%@ include file="commonIndex.jsp" %>
</head>
<body>
<div class="data">
    <div class="data-title">
        ${start} 至 ${end}(不含)交易前十商户统计
    </div>
    <ul class="head adjust_pd${showCost}">
        <li>商户编号</li>
        <li>本期交易金额</li>
        <li>本期下单笔数</li>
        <li>上期交易金额</li>
        <li>上期下单笔数</li>
        <c:if test="${showCost}">
            <li>本期成本</li>
            <li>上期成本</li>
        </c:if>
        <li>环比</li>
        <li></li>
    </ul>
    <c:choose>
        <c:when test="${result == null || result.size() == 0}">
            <div class="no-data">无数据</div>
        </c:when>
        <c:otherwise>
            <c:set var="rowNum" value="0" />
        <c:forEach items="${result}" var="entry">
            <ul class='color${entry.isUp} ${rowNum%2 == 0 ? 'gradual' : ''} adjust_pd${showCost}'>
                <li>${entry.customerNumber}</li>
                <li>${entry.totalAmount != null ? entry.totalAmount : '---'}</li>
                <li>${entry.totalCount != null ? entry.totalCount : '---'}</li>
                <li>${entry.lastTotalAmount != null ? entry.lastTotalAmount : '---'}</li>
                <li>${entry.lastTotalCount != null ? entry.lastTotalCount : '---'}</li>
                <c:if test="${showCost}">
                    <li>${entry.totalCost != null ? entry.totalCost : '---'}</li>
                    <li>${entry.lastTotalCost != null ? entry.lastTotalCost : '---'}</li>
                </c:if>
                <li>${entry.chain != null ? entry.chain : '---'}</li>
                <li>
                    <c:choose>
                        <c:when test="${entry.isUp == 'up'}"><strong>&uarr;</strong></c:when>
                        <c:when test="${entry.isUp == 'dw'}"><strong>&darr;</strong></c:when>
                        <c:when test="${entry.isUp == 'eq'}">--</c:when>
                    </c:choose>
                </li>
            </ul>
            <c:set var="rowNum" value="${rowNum + 1}" />
        </c:forEach>
    </c:otherwise>
</c:choose>
</div>
</body>
</html>
