package com.yeepay.g3.core.nccashier.biz;


import org.junit.Test;

import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.utils.common.json.JSONUtils;

public class BindPayBizTest extends BaseTest{
	
//	@Resource
//	private BindCardPayBiz bindCardPayBiz;
	
	@Test
	public void createPaymentTest(){
		FirstBindCardPayRequestDTO requestDTO = new FirstBindCardPayRequestDTO();
		requestDTO.setBindId(2024605l);
		NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
		needBankCardDTO.setCardno("6222024301003055777");
		needBankCardDTO.setOwner("庄梅玲");
		needBankCardDTO.setPhoneNo("18911792273");
		needBankCardDTO.setIdno("350521199101133049");
		requestDTO.setNeedBankCardDTO(needBankCardDTO);
		requestDTO.setRecordId(0l);
		requestDTO.setRequestId(24769l);
		requestDTO.setTokenId("7ee5e56b-e291-41ed-8ccc-3f0678f76623");
		FirstBindCardPayResponseDTO reponse = bindCardPayBiz.createPayment(requestDTO);
		System.out.println(reponse);
	}
	
	public static void main(String[] args){
		NeedBankCardDTO needBankCardDTO = new NeedBankCardDTO();
		System.out.println(JSONUtils.toJsonString(needBankCardDTO));
	}

}
