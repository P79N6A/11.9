package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;

/**
 * 下单返回结果
 * @author yp-od-m-2591
 *
 */
public class CashierPaymentResponseDTO extends BasicResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4832038647145055511L;

	private String tokenId;
	
	private ReqSmsSendTypeEnum reqSmsSendTypeEnum;

	private NeedBankCardDTO needBankCardDto;

	private String redirectType;

	private PageRedirectDTO pageRedirectDTO;

	public NeedBankCardDTO getNeedBankCardDto() {
		return needBankCardDto;
	}

	public void setNeedBankCardDto(NeedBankCardDTO needBankCardDto) {
		this.needBankCardDto = needBankCardDto;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	/**
	 * @return the reqSmsSendTypeEnum
	 */
	public ReqSmsSendTypeEnum getReqSmsSendTypeEnum() {
		return reqSmsSendTypeEnum;
	}

	/**
	 * @param reqSmsSendTypeEnum the reqSmsSendTypeEnum to set
	 */
	public void setReqSmsSendTypeEnum(ReqSmsSendTypeEnum reqSmsSendTypeEnum) {
		this.reqSmsSendTypeEnum = reqSmsSendTypeEnum;
	}

	public String getRedirectType() {
		return redirectType;
	}

	public void setRedirectType(String redirectType) {
		this.redirectType = redirectType;
	}

	public PageRedirectDTO getPageRedirectDTO() {
		return pageRedirectDTO;
	}

	public void setPageRedirectDTO(PageRedirectDTO pageRedirectDTO) {
		this.pageRedirectDTO = pageRedirectDTO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierPaymentResponseDTO{");
		sb.append("tokenId='").append(this.tokenId).append('\'');
		sb.append("reqSmsSendTypeEnum='").append(this.reqSmsSendTypeEnum).append('\'');
		sb.append("redirectType='").append(this.redirectType).append('\'');
		sb.append(", needBankCardDto='").append(this.needBankCardDto == null?"null":this.needBankCardDto.toString()).append('\'');
		sb.append(", pageRedirectDTO='").append(this.pageRedirectDTO == null?"null":this.pageRedirectDTO.toString()).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
	
}
