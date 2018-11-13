package com.yeepay.g3.app.nccashier.wap.vo;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.facade.cwh.param.HiddenCode;

public class CardValidateResponseVO extends BasicResVO{

	private static final long serialVersionUID = 1L;

	/**
	 * cardBin信息
	 */
	private CardBinInfoResponseVO cardBinInfo;
	
	/**
	 * 需要补充的卡信息/卡必填项
	 */
	private CardItemNecessary cardItemNecessary;
	
	/**
	 * 卡信息
	 */
	private CardInfoVO cardInfo;
	
	public CardValidateResponseVO(){
		
	}

	public CardBinInfoResponseVO getCardBinInfo() {
		return cardBinInfo;
	}

	public void setCardBinInfo(CardBinInfoResponseVO cardBinInfo) {
		this.cardBinInfo = cardBinInfo;
	}

	public CardItemNecessary getCardItemNecessary() {
		return cardItemNecessary;
	}

	public void setCardItemNecessary(CardItemNecessary cardItemNecessary) {
		this.cardItemNecessary = cardItemNecessary;
	}

	public CardInfoVO getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfoVO cardInfo) {
		this.cardInfo = cardInfo;
	}

	public static void main(String[] args){
		CardValidateResponseVO responseVO = new CardValidateResponseVO();
		CardBinInfoResponseVO binInfo = new CardBinInfoResponseVO();
		binInfo.setBankCode("ICBC");
		binInfo.setBankName("工商银行");
		binInfo.setCardLater4("5777");
		binInfo.setCardType("CREDIT");
		responseVO.setCardBinInfo(binInfo);
		CardItemNecessary cardItemNecessary = new CardItemNecessary();
		responseVO.setCardItemNecessary(cardItemNecessary);
		CardInfoVO cardInfoVO = new CardInfoVO();
		cardInfoVO.setOwner(HiddenCode.hiddenName("庄梅玲"));
		cardInfoVO.setIdNo(HiddenCode.hiddenIdentityCode("350521199101133049"));
		responseVO.setCardInfo(cardInfoVO);
		responseVO.setBizStatus("SUCCESS");
		System.out.println(JSON.toJSONString(responseVO));
	}
}
