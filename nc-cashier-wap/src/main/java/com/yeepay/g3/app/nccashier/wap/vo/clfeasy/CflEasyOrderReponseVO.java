package com.yeepay.g3.app.nccashier.wap.vo.clfeasy;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.vo.CardBinInfoResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardItemNecessary;
import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;

public class CflEasyOrderReponseVO extends ResponseVO{
	
	private static final long serialVersionUID = 1L;

	private String smsType;
	
	private CardBinInfoResponseVO cardBin;
	
	private CardItemNecessary needItem;
	
	public CflEasyOrderReponseVO(){
		
	}

	public String getSmsType() {
		return smsType;
	}

	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	public CardItemNecessary getNeedItem() {
		return needItem;
	}

	public void setNeedItem(CardItemNecessary needItem) {
		this.needItem = needItem;
	}

	public CardBinInfoResponseVO getCardBin() {
		return cardBin;
	}

	public void setCardBin(CardBinInfoResponseVO cardBin) {
		this.cardBin = cardBin;
	}

	public static void main(String[] args){
		CflEasyOrderReponseVO reponseVO = new CflEasyOrderReponseVO();
		reponseVO.setBizStatus("success");
		reponseVO.setToken("example");
		reponseVO.setNeedItem(new CardItemNecessary());
		System.out.print(JSON.toJSONString(reponseVO));
	}
}
