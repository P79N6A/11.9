<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date,java.util.Map,java.sql.Timestamp,java.text.SimpleDateFormat,java.util.Calendar"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>WX订单查询</title>
 	<link rel="stylesheet" href="${ctxPath}/css/layout.css" />
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
	<script type="text/javascript" >
	function checkQueryParams(){
		var startTimeStr = $("#startCreateDate").val().trim();
		var endTimeStr = $("#endCreateDate").val().trim();
		var startTime = getDateByStr(startTimeStr);
		var endTime = getDateByStr(endTimeStr);
		var deviation = endTime.getTime() - startTime.getTime();
		var days = Math.floor(deviation/ONE_DAY);
		if(days < 0){
			alert("截止时间必须大于等于起始时间.");
			return;
		}
		var diffDaysNeed=7;
		if(days <= diffDaysNeed) return;
		else{
			alert("注意：时间范围最多允许7天以内");
		}
		$("#endCreateDate").val(getWannaDate(startTime, diffDaysNeed));
	}
	</script>

</head>
<body >
  <div class="Container">
    <div class="Content fontText">
      <form action="" id="queryMpayOrderForm" name="queryMpayOrderForm" method="get">
        <div class="search">
          <div class="search_tit">
            <h2 class="fw fleft f14">订单查询</h2>
          </div>
          <div class="clearer"></div>
          <div class="search_con">
            <ul id="list" class="fix"> 	
            
              <li class="high-condition">
          		<label class="text_tit">商编：</label>
          		<input type="text" name="customerNo" value="${customerNo}" id="customerNo" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">WX子商户号：</label>
          		<input type="text" name="wechatSubMerchantNumber" value="${wechatSubMerchantNumber}" id="wechatSubMerchantNumber" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">商户订单号：</label>
          		<input type="text" name="outTradeNo" value="${outTradeNo}" id="outTradeNo" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">易宝流水号：</label>
          		<input type="text" name="orderNo" value="${orderNo}" id="orderNo" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">WX支付订单号：</label>
          		<input type="text" name="transactionId" value="${transactionId}" id="transactionId" class="input_text" style="width: 145px;"/>
              </li>
              
              
              <li>
          		<label class="text_tit">订单状态：</label>
          		<div class="select_border">
                	<div class="container">
          				<select class="select" name="status" id="status">
          				    <option value="" selected="selected">请选择</option>
							<c:forEach var="orderStatus" items="${orderStatusMap}" varStatus="ostatus">
								<option value="<c:out value="${orderStatus.key}"/>" ${orderStatus.key eq param.status ? 'selected = "selected"':'' }>
									<c:out value="${orderStatus.value}" />
								</option>
							</c:forEach>
						</select>
					</div>
              	</div>
              </li>
              
              <li>
          		<label class="text_tit">支付来源：</label>
          		<div class="select_border">
                	<div class="container">
          				<select class="select" name="source" id="source">
							<option value="" selected="selected">请选择</option>
							<c:forEach var="source" items="${sourceMap}" varStatus="entry">
							    <option value="<c:out value="${source.key}"/>" >
									<c:out value="${source.value}" />
									</option>
							</c:forEach>
						</select>
					</div>
              	</div>
              </li>
              
              <li>
          		<label class="text_tit">支付类型：</label>
          		<div class="select_border">
                	<div class="container">
          				<select class="select" name="orderType" id="orderType">
							<option value="" selected="selected">请选择</option>
							<c:forEach var="orderType" items="${orderTypeMap}" varStatus="entry">
								<option value="<c:out value="${orderType.key}"/>" ${orderType.key eq param.orderType ? 'selected = "selected"':'' }>
								<c:out value="${orderType.value}" />
							</c:forEach>
						</select>
					</div>
              	</div>
              </li>

							<li>
							<label class="text_tit">创建时间：</label>
							 <input type="text" name="startCreateDate" id="startCreateDate"
								class="input_text Wdate" /></li>
							<li><label class="text_tit">至：</label> <input type="text"
								name="endCreateDate" id="endCreateDate" class="input_text Wdate" />
							</li>

						</ul>
            <div class="clearer"></div>
            <div class="btn">
              <input type="submit" class="btn_sure fw" value="查询" />
              <input type="reset" class="btn_cancel fw" value="重置" >
            </div>
            <div class="clearer"></div>
          </div>
        </div>
      </form>
      <div class="clearer"></div><br>
      
      <q:table queryService="mpaybossQueryService" queryKey="queryMpayOrder" formId="queryMpayOrderForm" class="list" >
        <q:nodata>无符合条件的记录</q:nodata>
        <q:column title="序号" value="${_rowstatus.globalIndex}"/>
        <q:column title="商户编号" value="${customer_no}"/>
        
        <q:column title="支付来源" width="150px" escapeHtml="false">
        	<%--${mq:getMpaySourceByenum(source)}--%>${source}
        </q:column>
	    
	    <q:column title="支付类型" width="150px" escapeHtml="false">
	    	<%--${mq:getMpayOrderTypeByenum(order_type)}--%>${order_type}
	    </q:column>
   
        <q:column title="WX子商户号" value="${wc_sub_mer_number}"/>
        
        <q:column title="商户订单号" width="150px" escapeHtml="false">
        	${out_trade_no}
        </q:column>
        
        <q:column title="易宝流水号" width="150px" escapeHtml="false">
	    	${order_no}
	    </q:column>
	    
        <q:column title="WX订单号" width="150px" escapeHtml="false">
	    	${transaction_id}
	    </q:column>
        
        <q:column title="订单金额" escapeHtml="false">
        	${total_amount}
        </q:column>
        
        <q:column title="订单状态" escapeHtml="false">
        	<%--${mq:getMpayOrderStatusByenum(status)}--%>${status}
        </q:column>
        
         <q:column title="创建时间</br>更新时间" width="200px" escapeHtml="false" >
	    	${_messageFormater.formatDate(create_time)}</br>
	    	${_messageFormater.formatDate(last_modify_time)}</br>
	    </q:column>
	    
      </q:table>
    </div>
  </div>
</body>
</html>