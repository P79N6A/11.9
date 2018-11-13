package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.ncpay.enumtype.SmsSendTypeEnum;
/**
 * 
 * Description:
 * @author peile.fan
 * @since:2017年2月10日 下午5:30:46
 */
public class NcPayOrderResponseDTO extends BasicResponseDTO {
	
	/**
	 * 短信发送方式(不发、易宝发、银行发[银行生成验证码])
	 */
	private SmsSendTypeEnum smsType;
	
	/**
	 * 需要补充的卡信息项
	 */
	private int needItem;

	/**
	 * 跳转类型
	 */
	private String pageRedirectType;

	/**
	 * 跳转参数
	 */
	private PageRedirectDTO pageRedirectDTO;
	

	public SmsSendTypeEnum getSmsType() {
		return smsType;
	}

	public void setSmsType(SmsSendTypeEnum smsType) {
		this.smsType = smsType;
	}

	public int getNeedItem() {
		return needItem;
	}

	public void setNeedItem(int needItem) {
		this.needItem = needItem;
	}

	public String getPageRedirectType() {
		return pageRedirectType;
	}

	public void setPageRedirectType(String pageRedirectType) {
		this.pageRedirectType = pageRedirectType;
	}

	public PageRedirectDTO getPageRedirectDTO() {
		return pageRedirectDTO;
	}

	public void setPageRedirectDTO(PageRedirectDTO pageRedirectDTO) {
		this.pageRedirectDTO = pageRedirectDTO;
	}

	@Override
	public String toString() {
		return "NcPayOrderResponseDTO{" +
				"smsType=" + smsType +
				", needItem=" + needItem +
				", pageRedirectType='" + pageRedirectType + '\'' +
				", pageRedirectDTO=" + pageRedirectDTO +
				", recordNo='" + recordNo + '\'' +
				", outTradeNo='" + outTradeNo + '\'' +
				", orderNo='" + orderNo + '\'' +
				", dealUniqueSerialNo='" + dealUniqueSerialNo + '\'' +
				", payOrderType=" + payOrderType +
				", customerNumber='" + customerNumber + '\'' +
				", combResponseDTO=" + combResponseDTO +
				", firstPayAmount=" + firstPayAmount +
				", responseCode='" + responseCode + '\'' +
				", responseMsg='" + responseMsg + '\'' +
				", processStatus=" + processStatus +
				'}';
	}
}
