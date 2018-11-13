
package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.facade.frontend.dto.PromotionInfoDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayCardType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description: 被扫返回参数
 * 
 * @author peile.fan
 * @since:2017年2月10日 下午5:30:10
 */
public class PassiveScanPayResponseDTO extends BasicResponseDTO {


	private static final long serialVersionUID = 9129991981769391481L;


	/**
	 * 支付订单状态
	 */
	private String status;

	/**
	 * 支付成本
	 */
	private BigDecimal cost;

	/**
	 * 第三方(微信/支付宝)订单号
	 */
	private String bankTrxId;

	/**
	 * 银行子系统订单号
	 */
	private String bankOrderNo;

	/**
	 * 支付银行编码
	 */
	private String bankId;

	/**
	 * 卡种
	 */
	private PayCardType cardType;

	/**
	 * 银行子系统通道编码
	 */
	private String channelId;

	/**
	 *
	 * added by zengzhi.han 20181016
	 * 一些非重要的扩展参数，比如 被扫是否需要密码(isNeedPassword)
	 */
	private Map<String, String> extParam;


	/**
	 *
	 * added by zengzhi.han 20181024
	 * 现金支付金额
	 */
	private BigDecimal cashFee;

	/**
	 * added by zengzhi.han 20181024
	 * 应结算金额
	 */
	private BigDecimal settlementFee;

	/**
	 * added by zengzhi.han 20181024
	 * 优惠券信息
	 */
	private List<PromotionInfoDTO> promotionInfoDTOS;


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getBankTrxId() {
		return bankTrxId;
	}

	public void setBankTrxId(String bankTrxId) {
		this.bankTrxId = bankTrxId;
	}

	public String getBankOrderNo() {
		return bankOrderNo;
	}

	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public PayCardType getCardType() {
		return cardType;
	}

	public void setCardType(PayCardType cardType) {
		this.cardType = cardType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}


	public BigDecimal getCashFee() {
		return cashFee;
	}

	public void setCashFee(BigDecimal cashFee) {
		this.cashFee = cashFee;
	}

	public BigDecimal getSettlementFee() {
		return settlementFee;
	}

	public void setSettlementFee(BigDecimal settlementFee) {
		this.settlementFee = settlementFee;
	}

	public List<PromotionInfoDTO> getPromotionInfoDTOS() {
		return promotionInfoDTOS;
	}

	public void setPromotionInfoDTOS(List<PromotionInfoDTO> promotionInfoDTOS) {
		this.promotionInfoDTOS = promotionInfoDTOS;
	}
}
