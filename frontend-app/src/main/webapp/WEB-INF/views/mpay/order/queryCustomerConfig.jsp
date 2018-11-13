<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date,java.util.Map,java.sql.Timestamp,java.text.SimpleDateFormat,java.util.Calendar"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<title>WX商户配置查询</title>
 	<link rel="stylesheet" href="${ctxPath}/css/layout.css" />
	<%@ include file="/WEB-INF/views/common/header.jsp" %>
	
	  <script type="text/javascript">
        var path = GV.ctxPath + "/customerConfig/";
        $(document).ready(function(){
			  initStartDate("startCreateDate");
		  });
		  function initStartDate(startDateElementId){
			  if($("#"+startDateElementId).length == 0)
				  return ;
				var start = new Date(2014,0,01);
				$("#"+startDateElementId).val(start.format("yyyy-MM-dd") + " 00:00:00");
		  }
        function addData() {
            window.location.href = path + "add";
        }
    </script>

</head>
<body >
  <div class="Container">
    <div class="Content fontText">
      <form action="" id="queryCustomerForm" name="queryCustomerForm" method="get">
        <div class="search">
          <div class="search_tit">
            <h2 class="fw fleft f14">商户配置查询</h2>
          </div>
          <div class="clearer"></div>
          <div class="search_con">
            <ul id="list" class="fix"> 	
            
              <li class="high-condition">
          		<label class="text_tit">商户编号：</label>
          		<input type="text" name="customerNumber" value="${customerNumber}" id="customerNumber" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">WX子商户号：</label>
          		<input type="text" name="wechatSubMerchantNumber" value="${wechatSubMerchantNumber}" id="wechatSubMerchantNumber" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">支付APPID：</label>
          		<input type="text" name="wechatAppIDPay" value="${wechatAppIDPay}" id="wechatAppIDPay" class="input_text" style="width: 145px;"/>
              </li>
              
              <li class="high-condition">
          		<label class="text_tit">推荐关注APPID：</label>
          		<input type="text" name="wechatAppIDRecommend" value="${wechatAppIDRecommend}" id="wechatAppIDRecommend" class="input_text" style="width: 145px;"/>
              </li>
              
              
              <li>
          		<label class="text_tit">扫码支付：</label>
          		<div class="select_border">
                	<div class="container">
          				<select class="select" name="scanCodePaySwitch" id="scanCodePaySwitch">
							<option value="" selected="selected">请选择</option>
							<option value="1" >开通</option>
							<option value="0" >关闭</option>
						</select>
					</div>
              	</div>
              </li>
              
              <li>
          		<label class="text_tit">公众号支付：</label>
          		<div class="select_border">
                	<div class="container">
          				<select class="select" name="JSAPIPaySwitch" id="JSAPIPaySwitch">
							<option value="" selected="selected">请选择</option>
							<option value="1">开通</option>
							<option value="0">关闭</option>
						</select>
					</div>
              	</div>
              </li>
              
             <li>
                <label class="text_tit">创建时间：</label>
                <input type="text" class="input_text Wdate" name="startCreateDate" id="startCreateDate" value="${startCreateDate }" style="width: 145px;" />
              </li>
              <li>
                <label class="text_tit">至：</label>
                <input type="text" class="input_text Wdate" name="endCreateDate" id="endCreateDate" value="${endCreateDate }" style="width: 145px;"/>
              </li>
                
            </ul>
            <div class="clearer"></div>
            <div class="btn">
              <input type="submit" class="btn_sure fw" value="查询" onclick="return checkQueryParams()"/>
              <input type="button" class="btn_sure fw" value="新增" onclick="addData();">
            </div>
            <div class="clearer"></div>
          </div>
        </div>
      </form>
      <div class="clearer"></div><br>
      
      <q:table queryService="mpaybossQueryService" queryKey="queryCustomer" formId="queryCustomerForm" class="list" >
        <q:nodata>无符合条件的记录</q:nodata>
        <q:column title="序号" value="${_rowstatus.globalIndex}"/>
        <q:column title="商户编号" value="${customer_number}"/>
        
        <q:column title="允许的支付来源" width="150px" escapeHtml="false">
        	<%--${mq:getMpaySource(source) }--%>${source}
        </q:column>
	    <q:column title="WX支付状态" value="${(wechat_status eq 'ACTIVE')?'活动':((wechat_status eq 'INACTIVE')?'冻结':'非法')}"/>
	    <q:column title="扫码支付" width="150px" escapeHtml="false" value="${(scan_pay_switch eq '0')?'关闭':'开通'}"/>
   
        <q:column title="公众号支付" value="${(jsapi_pay_switch eq '0')?'关闭':'开通'}"/>
        
        <q:column title="WX子商户号" width="150px" escapeHtml="false">
        	${wc_sub_mer_number}
        </q:column>
        
        <q:column title="支付APPID</br>推荐关注APPID" width="150px" escapeHtml="false">
	    	${wc_appid_pay }</br>
	    	${wc_appid_recommend }</br>
	    </q:column>
	    
	    <q:column title="支付授权URL" width="150px" escapeHtml="false">
	    	${jsapi_pay_url}
	    </q:column>
	    <q:column title="创建时间</br>更新时间" width="150px" escapeHtml="false">
	    	${create_time}</br>
	    	 ${update_time}</br>
	    </q:column>
	     <q:column title="操作" escapeHtml="false" width="40px">
            <a href="edit?customerNumber=${customer_number}">编辑</a></br>
        </q:column>
	    
      </q:table>
    </div>
  </div>
</body>
</html>