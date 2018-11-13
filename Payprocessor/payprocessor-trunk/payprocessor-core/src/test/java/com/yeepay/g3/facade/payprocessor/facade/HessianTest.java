
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.facade.payprocessor.dto.QueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.QueryResponseDTO;

/**
 * @author peile.fan
 *
 */
public class HessianTest {
	public static void main(String[] args) {
//		PayOrderFacade payOrderFacade = HessianFactory.getService(PayOrderFacade.class);
//		CflPayRequestDTO dto=new CflPayRequestDTO();
//        dto.setRequestSystem("NCTRAD");                               //请求系统编码
//        dto.setRequestSysId("PP" + RandomUtils.nextInt());  //"PP" + RandomUtils.nextInt()                //请求系统方订单号 非必填
//        dto.setOrderSystem("YJZF");//交易系统订单方
//        dto.setOutTradeNo("201701093043301511");                    //商户订单号
//        dto.setOrderNo("2017010903301511");                       //业务方订单号
//        dto.setCustomerNumber("10012413438");                //商户编码
//        dto.setCustomerName("支付处理器分期下单测试商户");// 商户名
//        dto.setProductType("SUBSTANCE");
//        dto.setPayProduct("CFL");
//        dto.setPageCallBack("http://www.hao123.com");
//        dto.setPayOrderType(PayOrderType.ONLINE);
//        dto.setIdentityId("lili001");
//        dto.setCashierVersion(CashierVersion.WEB);
//        dto.setProductName("分期支付");
//        dto.setAmount(new BigDecimal("1.00"));
//        dto.setDealUniqueSerialNo("deal" + System.currentTimeMillis());
//        dto.setGoodsInfo("");                    // 商品信息  非必填
//        dto.setToolsInfo("");                     //工具信息 非必填
//        dto.setCustomerLevel("");                  //商户等级
//        dto.setDeviceInfo("");                     //设备信息
//		CflPayResponseDTO response=payOrderFacade.cflRequest(dto);
//        System.out.println(JSON.toJSONString(response));
//		
//		OpenPrePayRequestDTO requestDTO=new OpenPrePayRequestDTO();
//		OpenPrePayResponseDTO response=payOrderFacade.openPrePay(requestDTO);
		
		PayProcessorQueryFacade payProcessorQueryFacade = HessianFactory.getService(PayProcessorQueryFacade.class);
		QueryRequestDTO requestDTO=new QueryRequestDTO();
		requestDTO.setOrderSystem("DS");
		requestDTO.setOrderNo("20170110033011");
		QueryResponseDTO response=payProcessorQueryFacade.query(requestDTO);
		System.out.println(response);
	}
}
