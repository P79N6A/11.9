/**
 * 
 */
package com.yeepay.g3.app.nccashier.wap.action;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.yeepay.g3.app.nccashier.wap.base.ActionBaseTest;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 
 * @Description BindCardAction测试类
 * @author yangmin.peng
 * @since 2017年9月5日上午10:17:54
 */
public class BindCardActionTest extends ActionBaseTest {

	private MockMvc mockMvc;

	@Resource
	private BindCardAction bindCardAction;
	
	@Resource
	private OrderProUniterAction orderUniterAction;
	
	@Before
	public void setUp() throws Exception {
		//this.mockMvc = MockMvcBuilders.standaloneSetup(bindCardAction).build();
		this.mockMvc = MockMvcBuilders.standaloneSetup(orderUniterAction).build();
	}
	
	@Test
	public void testAuthbindRequest() throws Exception{
		 mockMvc.perform(MockMvcRequestBuilders.get("/bindCard/authBindCard").header("Proxy-Client-IP", "127.0.0.1")
				 .header("User-Agent", "xxxxxxx").param("merchantNo", "10040018515").param("merchantFlowId", "20170905123456"))
  		.andExpect(MockMvcResultMatchers.status().isOk());
//  		.andExpect(MockMvcResultMatchers.view().name("pay_fail"));
//  		.andExpect(MockMvcResultMatchers.model().attribute("hotelName", "酒店"))
//  		.andExpect(MockMvcResultMatchers.model().attributeExists("visitRecords"));
	}
	@Test
	public void testOrderBindCard() throws Exception{
		String sign = "E7mS6mNkDMHO2I9-WmqPjDciAbQwv89TQEAJIGw6b7QFBuc8ZLIYIK-7M914y06spwtP59F59_osS6K7yZK69n8T2o2pcuGRpsAmaE3SYdd3s1Hi3-Nyt-9JUGnTgYeh9vtWR7osZC8Zjj0jIZm9LHxciC1-N55LIHu6PRsH61jW2Vm5FJqhkSg8SeEd9aqI7OGHOnCAbuZy89eNiryMHQDzhb_6Rqy6ePFrruMJ3XfKI4Pn78Q69Psdfw0nRHVPwseF4-ZUg3c44cYh9RYiTHRsnj5orzjpBAe6VEm4xdn55tzx44-vSJE-jjgpNubuqXPQM0Zm6Rzq4-zyX-V1_w$SHA256";
		mockMvc.perform(MockMvcRequestBuilders.get("/cashier/bindCard").header("Proxy-Client-IP", "127.0.0.1")
				 .header("User-Agent", "xxxxxxx").param("merchantNo", "10040007800").
				 								  param("merchantFlowId", "20170905123456")
				 								  .param("timestamp", "1504606593")
				 								  .param("userNo", "123456")
				 								  .param("userType", "123456")
				 								  .param("bindCallBackUrl", "www.baidu.com")
				 								  .param("sign", sign))
 												  .andExpect(MockMvcResultMatchers.status().isOk());
	}
//	@Test
//	public void testRequestFirst() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/request/first").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1"))
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.view().name("first_index"));
//	}
//	
//	@Test
//	public void testRequestBind() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/request/bind").param("token", "e41ed694-0c45-4020-9a77-2d0822d5fee3"))
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.view().name("bind_index"));
//	}
//	
//	@Test
//	public void testCardInfo() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/first/cardinfo").param("cardNo", "6214830127031093")
//				.param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("isChangeCard", ""))
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.view().name("first_cardInfo"));
//	}
//	
//	@Test
//	public void testSmsSend() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/smsSend").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")
//				.param("name", "谭振").param("cardno", "6222020200108234213").param("idno", "430224198803246513")
//				.param("phone","15911024890").param("bankCode", "ICBC").param("bankName", "工商银行").param("cardType", "DEBIT")).
//		andExpect(MockMvcResultMatchers.status().isOk());
//	}
//	
//	@Test
//	public void testBindSmsSend() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/smsSend").param("token", "e41ed694-0c45-4020-9a77-2d0822d5fee3")
//				.param("bindId", "2018984").param("reqSmsSendTypeEnum", "VOICE"))
//		.andExpect(MockMvcResultMatchers.status().isOk());
//	}
//	
//	@Test
//	public void testQueryResult() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.get("/wap/query/result").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("isRequery", ""))
//		.andExpect(MockMvcResultMatchers.status().isOk())
//		.andExpect(MockMvcResultMatchers.view().name("query_index"));
//	}
//	
//	
//	@Test
//	public void testBanklist() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/bankList").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")).
//		andExpect(MockMvcResultMatchers.status().isOk());
//	}
//	
//	@Test
//	public void testFirstPay() throws Exception{
//		mockMvc.perform(MockMvcRequestBuilders.post("/wap/pay/first")
//				.param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("verifycode", "123455")
//				.param("name", "谭振").param("cardno", "6222020200108234213")
//				.param("phone","15911024890")).
//		andExpect(MockMvcResultMatchers.status().isOk());
//	}
//
//	// @Test
//	// public void testBindPay() throws Exception {
//	// mockMvc.perform(MockMvcRequestBuilders.post("/wap/pay/bind").param("bindId", "")
//	// .param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")
//	// .param("verifycode", "123455").param("name", "谭振")
//	// .param("cardno", "6222020200108234213").param("phone", "15911024890"))
//	// .andExpect(MockMvcResultMatchers.status().isOk());
//	// }
//

}
