package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;
import com.yeepay.g3.facade.nccashier.validator.NumberValidate;

/**
 * nccashier下单请求入参
 * 
 * @author zhen.tan
 *
 */
public class CashierPaymentRequestDTO implements Serializable {

	private static final long serialVersionUID = 7882074526199720285L;

	/**
	 * 必填
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="tokenId未传")
	private String tokenId;

	/**
	 * 支付请求ID，必填
	 */

	@NumberValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="requestId未传")
	private Long requestId;

	/**
	 * 支付记录ID
	 */
	private long recordId = 0;

	/**
	 * 下单类型，必填
	 */
	@NotEmptyValidate
	@ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION,errorMsg="orderType未传")
	private NCCashierOrderTypeEnum orderType;

	/**
	 * 绑卡ID：绑卡下单时必填
	 */
	private long bindId;

	/**
	 * 卡信息，首次支付下单必填
	 */
	private CardInfoDTO cardInfo;
	
	/**
	 * 二级支付工具，以此区分一键/绑卡支付产品
	 */
	private String payTool;
	
	private boolean bkFirst;

	/**
	 * 签约回调地址
	 */
	private String signRedirectUrl;

	/**
	 * 支付回调地址
	 */
	private String payRedirectUrl;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public NCCashierOrderTypeEnum getOrderType() {
		return orderType;
	}

	public void setOrderType(NCCashierOrderTypeEnum orderType) {
		this.orderType = orderType;
	}

	public long getBindId() {
		return bindId;
	}

	public void setBindId(long bindId) {
		this.bindId = bindId;
	}

	public CardInfoDTO getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(CardInfoDTO cardInfo) {
		this.cardInfo = cardInfo;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}

	public boolean isBkFirst() {
		return bkFirst;
	}

	public void setBkFirst(boolean bkFirst) {
		this.bkFirst = bkFirst;
	}

	public String getSignRedirectUrl() {
		return signRedirectUrl;
	}

	public void setSignRedirectUrl(String signRedirectUrl) {
		this.signRedirectUrl = signRedirectUrl;
	}

	public String getPayRedirectUrl() {
		return payRedirectUrl;
	}

	public void setPayRedirectUrl(String payRedirectUrl) {
		this.payRedirectUrl = payRedirectUrl;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierPaymentRequestDTO{");
		sb.append("tokenId='").append(this.tokenId).append('\'');
		sb.append(", requestId='").append(this.requestId).append('\'');
		sb.append(", recordId='").append(this.recordId).append('\'');
		sb.append(", orderType='").append(this.orderType==null?"null":this.orderType.getDescription()).append('\'');
		sb.append(", bindId='").append(this.bindId).append('\'');
		sb.append(", cardInfo='").append(this.cardInfo==null?"null":this.cardInfo.toString()).append('\'');
		sb.append(", signRedirectUrl='").append(this.signRedirectUrl).append('\'');
		sb.append(", payRedirectUrl='").append(this.payRedirectUrl).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
