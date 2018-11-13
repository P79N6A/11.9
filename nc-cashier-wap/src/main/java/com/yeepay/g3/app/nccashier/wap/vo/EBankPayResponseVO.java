package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * pc网银支付 确认支付页面返回对象 view object to return
 * 
 * @author duangduang
 * @since 2016-11-08
 */
public class EBankPayResponseVO extends ResponseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 网银支付跳转跳板机的链接。
	 * TODO 2018.03.19 把bankPayUrl改掉的话，在上线过程中可能会有问题，所以还是要返回，等到此次上线完成后，再去掉
	 */
	private String bankPayUrl;
	
	/**
	 * 网银支付跳转跳板机的链接
	 */
	private String payUrl;
	
	/**
	 * 网银支付绕过跳板机直接跳转银行的报文信息
	 */
	private UrlInfoVO ebankUrlInfo;
	
	private String redirectType;
	
	/**
	 * 支付遇到问题 客服及问题页展示页的访问链接
	 */
	private String questionUrl;
	
	/**
	 * 网银支付结果查询URL
	 */
	private String resultUrl;

	/**
	 * 商户订单号
	 */
	private String merchantOrderId;

	public EBankPayResponseVO() {

	}

	public String getBankPayUrl() {
		return bankPayUrl;
	}

	public void setBankPayUrl(String bankPayUrl) {
		this.bankPayUrl = bankPayUrl;
	}

	public String getQuestionUrl() {
		return questionUrl;
	}

	public void setQuestionUrl(String questionUrl) {
		this.questionUrl = questionUrl;
	}

	public String getResultUrl() {
		return resultUrl;
	}

	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public UrlInfoVO getEbankUrlInfo() {
		return ebankUrlInfo;
	}

	public void setEbankUrlInfo(UrlInfoVO ebankUrlInfo) {
		this.ebankUrlInfo = ebankUrlInfo;
	}

	public String getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(String redirectType) {
		this.redirectType = redirectType;
	}
	
}
