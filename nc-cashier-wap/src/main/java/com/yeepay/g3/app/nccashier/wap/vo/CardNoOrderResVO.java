package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 卡号下单接口返回值
 * @author duangduang
 *
 */
public class CardNoOrderResVO extends BasicResponseVO{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 卡状态，SIGNED：已签约；UNSIGNED：未签约
	 */
	private String cardState;
	
	/**
	 * 未签约时，银联开通并支付网页访问的相关参数
	 */
	private UrlInfoVO urlInfo;
	
	public CardNoOrderResVO(){
		
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public UrlInfoVO getUrlInfo() {
		return urlInfo;
	}

	public void setUrlInfo(UrlInfoVO urlInfo) {
		this.urlInfo = urlInfo;
	}
	
	@Override
	public String toString() {
		return "CardNoOrderResVO [cardState=" + cardState + ", urlInfo=" + urlInfo + super.toString() + "]";
	}
	
//	public static void main(String[] args){
//		CardNoOrderResVO resVO = new CardNoOrderResVO();
//		resVO.setToken("12345");
//		resVO.setBizStatus("failed");
//		resVO.setErrorcode(Errors.SYSTEM_EXCEPTION.getCode());
//		resVO.setErrormsg(Errors.SYSTEM_EXCEPTION.getMsg());
//		resVO.setCardState("SIGNED");
//		UrlInfoVO urlInfo = new UrlInfoVO();
//		urlInfo.setCharset("UTF-8");
//		urlInfo.setMethod("post");
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("testKey1", "testData1");
//		params.put("testKey2", "testData2");
//		urlInfo.setParams(params);
//		resVO.setUrlInfo(urlInfo);
//		System.out.println(JSON.toJSONString(resVO));
//	}
	
}
