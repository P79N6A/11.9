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

/**
 * @author tanzhen
 *
 */
public class WapPayActionTest extends ActionBaseTest {

	private MockMvc mockMvc;

	@Resource
	private WapBaseAction wapPayAction;
	
	@Before
	public void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.standaloneSetup(wapPayAction).build();
	}
	
	@Test
	public void testRequestProcess() throws Exception{
		 mockMvc.perform(MockMvcRequestBuilders.get("/wap/request/10040018515/VilZyQokVoo3U6jp7eQXww%3D%3D").header("Proxy-Client-IP", "127.0.0.1")
				 .header("User-Agent", "xxxxxxx").param("requestId", "VilZyQokVoo3U6jp7eQXww%3D%3D").param("merchantNo", "10040018515"))
  		.andExpect(MockMvcResultMatchers.status().isOk())
  		.andExpect(MockMvcResultMatchers.view().name("pay_fail"));
//  		.andExpect(MockMvcResultMatchers.model().attribute("hotelName", "酒店"))
//  		.andExpect(MockMvcResultMatchers.model().attributeExists("visitRecords"));
	}
	
	@Test
	public void testRequestFirst() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/request/first").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("first_index"));
	}
	
	@Test
	public void testRequestBind() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/request/bind").param("token", "e41ed694-0c45-4020-9a77-2d0822d5fee3"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("bind_index"));
	}
	
	@Test
	public void testCardInfo() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/first/cardinfo").param("cardNo", "6214830127031093")
				.param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("isChangeCard", ""))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("first_cardInfo"));
	}
	
	@Test
	public void testSmsSend() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/smsSend").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")
				.param("name", "谭振").param("cardno", "6222020200108234213").param("idno", "430224198803246513")
				.param("phone","15911024890").param("bankCode", "ICBC").param("bankName", "工商银行").param("cardType", "DEBIT")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testBindSmsSend() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/smsSend").param("token", "e41ed694-0c45-4020-9a77-2d0822d5fee3")
				.param("bindId", "2018984").param("reqSmsSendTypeEnum", "VOICE"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testQueryResult() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/wap/query/result").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("isRequery", ""))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("query_index"));
	}
	
	
	@Test
	public void testBanklist() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/ajax/bankList").param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void testFirstPay() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/wap/pay/first")
				.param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1").param("verifycode", "123455")
				.param("name", "谭振").param("cardno", "6222020200108234213")
				.param("phone","15911024890")).
		andExpect(MockMvcResultMatchers.status().isOk());
	}

	// @Test
	// public void testBindPay() throws Exception {
	// mockMvc.perform(MockMvcRequestBuilders.post("/wap/pay/bind").param("bindId", "")
	// .param("token", "c2905865-01af-49bc-9ca1-fa280c905ec1")
	// .param("verifycode", "123455").param("name", "谭振")
	// .param("cardno", "6222020200108234213").param("phone", "15911024890"))
	// .andExpect(MockMvcResultMatchers.status().isOk());
	// }


}
