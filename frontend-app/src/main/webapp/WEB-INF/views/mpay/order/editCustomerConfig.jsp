<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="java.util.Date,java.util.Map,java.sql.Timestamp,java.text.SimpleDateFormat,java.util.Calendar"%>
<%@ include file="/WEB-INF/views/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增商户</title>
<%@ include file="/WEB-INF/views/common/metas.jsp"%>
  <script type="text/javascript">
   
    var path = GV.ctxPath+"/customerConfig/";
    function submitForm() {
    	var customerNumber = $('#customerNumber').val().trim();
    	var customerName = $('#customerName').val().trim();
    	var scanCodePaySwitch= $('#scanCodePaySwitch:checked').val().trim();
    	var JSAPIPaySwitch = $('#JSAPIPaySwitch:checked').val().trim();
    	var wechatStatus= $('#wechatStatus:checked').val().trim();
    	
    	var wechatSubMerchantNumber = $('#wechatSubMerchantNumber').val().trim();
    	var wechatAppIDPay = $('#wechatAppIDPay').val().trim();
    	var wechatAppIDRecommend = $('#wechatAppIDRecommend').val().trim();
    	var JSAPIPayURL = $('#JSAPIPayURL').val().trim();
    	
    	var sourcearray = new Array();
    	var checks = document.getElementsByName("source");
    	for(i=0;i<checks.length;i++){
    		if(checks[i].checked)
    			sourcearray[i] = checks[i].value;
    	}
    	var sourceStr = sourcearray.join(',');
    	
    	var re = /[\u4E00-\u9FA5]+/;
		var resulta =  re.test(customerNumber);
		if(resulta) {
	        alert("商户编号格式错误");  
	        return;
		}
		
		if(customerNumber.length > 32 ){
    		alert("商户编号标识过长");
    		return;
    	}
		if(customerName == ''){
    		alert("商户名称不能为空");
    		return;
    	}
    	if(wechatSubMerchantNumber == ''){
    		alert("WX子商户号不能为空");
    		return;
    	}
    	if(wechatStatus=='INACTIVE' && (JSAPIPaySwitch == '1'||scanCodePaySwitch=='1')){
    		alert("WX支付状态为冻结，不允许开通扫码支付或者公众号支付");
    		return;
    	}
    	if(JSAPIPaySwitch=='1' && (wechatAppIDPay == ''||JSAPIPayURL=='')){
    		alert("开通公众号支付要填写支付APPID和授权支付URL");
    		return;
    	}
    	if(sourceStr ==''){
    		alert("请选择一项支付来源");
    		return;
    	}
  	  
  		  $.ajax({ //一个Ajax过程 
  				 type: "post", //以post方式与后台沟通 
  				 url : "update", 
  				 dataType:'text',
  				 data:{'customerNumber' : customerNumber,'customerName' : customerName,'scanCodePaySwitch' : scanCodePaySwitch,
  					 'JSAPIPaySwitch' : JSAPIPaySwitch,'wechatStatus' : wechatStatus, 'wechatSubMerchantNumber' : wechatSubMerchantNumber, 'wechatAppIDPay' : wechatAppIDPay,
  					'wechatAppIDRecommend' : wechatAppIDRecommend,'sourceStr' : sourceStr,'JSAPIPayURL':JSAPIPayURL}, 
  				 success: function(result){
  					 alert(result);
  					    if(result == '操作成功'){
  					    	window.location.href = path+"query";
  					    }
  				 } ,
  				 error: function(){
  					 alert("修改失败");
  				 } 
  				 }); 
  	  
    }	
    
    
  </script>
</head>
<body>
  <div class="Container">
    <div class="Content fontText">
       <form id="editCustomerConfigForm" action="" method="post">
            <div class="clearer"></div>
                <div class="information">
                    <h1 class="fw">编辑</h1>
                        <div class="clearer"></div>
                        <div class="input_cont">
                           <ul>
                            
                                <li><label class="text_tit">商户编号：</label>
                                   <input type="text" name="customerNumber" value="${customerConfigReqDTO.customerNumber}" id="customerNumber" class="input_text" readonly="readonly"/>
                                </li>
                                 <li><label class="text_tit">商户名称：</label>
                                   <input type="text" name="customerName" value="${customerConfigReqDTO.customerName}" id="customerName" class="input_text" readonly="readonly"/>&nbsp<font color=red>(*必填)</font>
                                </li>
                                
                                <li><label class="text_tit">WX支付状态：</label>
                                    
	                                <input name="wechatStatus" type="radio" id = "wechatStatus" value="ACTIVE" ${'ACTIVE' eq customerConfigReqDTO.wechatStatus ? 'checked="checked"':'' }/>开通 
	                                <input name="wechatStatus" type="radio" id = "wechatStatus" value="INACTIVE" ${'INACTIVE' eq customerConfigReqDTO.wechatStatus ? 'checked="checked"':'' }/>关闭 
                                </li>
                                
                                <li><label class="text_tit">扫码支付开通状态：</label>
	                                <input name="scanCodePaySwitch" type="radio" id = "scanCodePaySwitch" value="1" ${true eq customerConfigReqDTO.scanCodePaySwitch ? 'checked="checked"':'' }/>开通  
	                                <input name="scanCodePaySwitch" type="radio" id = "scanCodePaySwitch" value="0" ${false eq customerConfigReqDTO.scanCodePaySwitch ? 'checked="checked"':'' }/>关闭  
                                </li> 
                                
                                <li><label class="text_tit">公众号支付开通状态：</label>
	                                <input name="JSAPIPaySwitch" type="radio" id = "JSAPIPaySwitch" value="1" ${true eq customerConfigReqDTO.JSAPIPaySwitch ? 'checked="checked"':'' } />开通  
	                                <input name="JSAPIPaySwitch" type="radio" id = "JSAPIPaySwitch" value="0" ${false eq customerConfigReqDTO.JSAPIPaySwitch ? 'checked="checked"':'' } />关闭  
                                </li>
                                
                                <li><label class="text_tit">WX子商户号：</label>
                                   <input type="text" name="wechatSubMerchantNumber" value="${customerConfigReqDTO.wechatSubMerchantNumber}" id="wechatSubMerchantNumber" class="input_text" />&nbsp<font color=red>(*必填)</font>
                                </li>
                                
                                <li><label class="text_tit">支付APPID：</label>
                                   <input type="text" name="wechatAppIDPay" value="${customerConfigReqDTO.wechatAppIDPay}" id="wechatAppIDPay" class="input_text" />&nbsp<font color=red>(*开通公众号支付必填)</font>
                                </li>
                                
                                <li><label class="text_tit">推荐关注APPID：</label>
                                   <input type="text" name="wechatAppIDRecommend" value="${customerConfigReqDTO.wechatAppIDRecommend}" id="wechatAppIDRecommend" class="input_text" />
                                </li>
                                
                                <li><label class="text_tit">授权支付URL：</label>
                                   <input type="text" name="JSAPIPayURL" value="${customerConfigReqDTO.JSAPIPayURL}" id="JSAPIPayURL" class="input_text" />&nbsp<font color=red>(*开通公众号支付必填，多个URL用;分隔)</font>
                                </li>
                                <li><label class="text_tit">支付来源列表：</label>
                                   <c:forEach var="source" items="${sourceMap}" varStatus="entry">
									     <input name="source" type="checkbox" id = "source" value="${source.key}"  ${true eq source.value.select ? 'checked="checked"':'' }/> <c:out value="${source.value.desc}" />   
							       </c:forEach>
                                </li>
                                
                            </ul>
                        </div>
                    <div class="clearer"></div>
                    <div class="btn">
                        <input type="button" id="button" class="btn_sure fw" value="保存" onclick="submitForm()" />
                        <input type="button" class="btn_cancel fw" value="取消" onclick="history.go(-1);" />
                    </div>
                  </div>
                  
        </form>
        <div class="clearer"></div>
    </div>
  </div>
</body>
</html>