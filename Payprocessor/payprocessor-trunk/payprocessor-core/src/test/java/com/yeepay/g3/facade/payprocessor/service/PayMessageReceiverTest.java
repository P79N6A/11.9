
package com.yeepay.g3.facade.payprocessor.service;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeepay.g3.facade.ncpay.dto.PaymentResultMessage;

/**
 * @author peile.fan
 *
 */
public class PayMessageReceiverTest {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			String jsonStr = "{\"bankCode\":\"CMBCHINA\",\"bankName\":\"招商银行\",\"bankOrderNo\":\"1111\",\"bankSeq\":\"1222\",\"bizOrderNum\":\"411511275260938786\",\"bizType\":3,\"cardType\":\"CREDIT\",\"cost\":{\"negative\":false,\"positive\":false,\"value\":0.00,\"zero\":true},\"merchantName\":\"微米公司\",\"merchantNo\":\"10040018759\",\"payAmount\":{\"negative\":false,\"positive\":true,\"value\":0.02,\"zero\":false},\"payCompleteDate\":1448606264494,\"payOrderNum\":\"101511275261009325\",\"payStatus\":\"SUCCESS\",\"tradeSerialNo12312313\":\"2281190950213000\"}";
			PaymentResultMessage msg = objectMapper.readValue(jsonStr, PaymentResultMessage.class);
			System.out.println(msg);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
