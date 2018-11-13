package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.common.Amount;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;
import com.yeepay.g3.facade.frontend.enumtype.PayBankcardType;
import com.yeepay.g3.facade.frontend.enumtype.PayStatusEnum;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通知消息
 * @author TML
 *
 */
public class PayResultMessage implements Serializable{

	
	private static final long serialVersionUID = -7815830491988623942L;


	public static final String FRONTEND_NOTIFY_QUEUE = "FRONTEND_NOTIFY_QUEUE";


	/**
	 * 业务方
	 */
	private String requestSystem;

	/**
	 * 业务方订单号
	 */
    private String requestId;

    /**
     * 商户编号
     */
    private String customerNumber;

    /**
     * 商户订单号
     */
    private String outTradeNo;


    /**
     * 银行子系统订单号
     */
    private String orderNo;

    /**
     * 订单类型
     */
    private OrderType orderType;

    /**
     * 平台类型
     */
    private PlatformType platformType;

    /**
     * 错误码
     */
    private String responseCode;

    /**
     * 错误码描述
     */
    private String responseMsg;

	/**
	 * 公众号用户唯一标识
	 */
	private String openId;

	/**
	 * 支付银行
	 */
	private String payBank;

	/**
	 * 支付银行卡类型
	 */
	private PayBankcardType payBankcardType;

	/**
	 * 支付金额
	 */
	private BigDecimal totalAmount;

	/**
	 * 第三方(微信/支付宝)订单号
	 */
	private String transactionId;

	/**
	 * 支付状态
	 */
	private PayStatusEnum payStatus;

	/**
	 * 支付成本
	 */
	private BigDecimal bankTotalCost;

	/**
	 * 支付订单生成时间
	 */
	private Date createTime;

	/**
	 * 支付订单失效时间
	 */
	private Date expireTime;

     /**
     * 银行支付成功时间
     */
    private Date bankSuccessTime;

    /**
     * 系统支付成功时间
     */
    private Date paySuccessTime;

	/**
	 * 银行子系统支付接口
	 */
	private String payInterface;

	/**
	 * 扩展信息前端需要，通道透传前端，暂时只有银联用，例如：银行商户号、卡号、付款凭证号、优惠信息
	 * added by zhijun.wang 2017-05-24
	 */
	private Map<String, String> extParam;

	/**
	 * 零售产品码
	 * added by dongbo.jiao 2017-06-21
	 */
	private String retailProductCode;

	/**
	 * 基础产品码
	 */
	private String basicProductCode;

	/**
	 * FE支付订单号
	 */
	private String payOrderNo;


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



    
	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPayBank() {
		return payBank;
	}

	public void setPayBank(String payBank) {
		this.payBank = payBank;
	}

	public PayBankcardType getPayBankcardType() {
		return payBankcardType;
	}

	public void setPayBankcardType(PayBankcardType payBankcardType) {
		this.payBankcardType = payBankcardType;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}


	public BigDecimal getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(BigDecimal bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public Date getBankSuccessTime() {
		return bankSuccessTime;
	}

	public void setBankSuccessTime(Date bankSuccessTime) {
		this.bankSuccessTime = bankSuccessTime;
	}

	public Date getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(Date paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}

	public PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}

    public String getPayInterface() {
        return payInterface;
    }

    public void setPayInterface(String payInterface) {
        this.payInterface = payInterface;
    }

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	public String getRetailProductCode() {
		return retailProductCode;
	}

	public void setRetailProductCode(String retailProductCode) {
		this.retailProductCode = retailProductCode;
	}

	public String getBasicProductCode() {
		return basicProductCode;
	}

	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}

	@Override
	public String toString() {
		return "PayResultMessage{" +
				"requestSystem='" + requestSystem + '\'' +
				", requestId='" + requestId + '\'' +
				", customerNumber='" + customerNumber + '\'' +
				", outTradeNo='" + outTradeNo + '\'' +
				", orderNo='" + orderNo + '\'' +
				", orderType=" + orderType +
				", platformType=" + platformType +
				", responseCode='" + responseCode + '\'' +
				", responseMsg='" + responseMsg + '\'' +
				", openId='" + openId + '\'' +
				", payBank='" + payBank + '\'' +
				", payBankcardType=" + payBankcardType +
				", totalAmount=" + totalAmount +
				", transactionId='" + transactionId + '\'' +
				", payStatus=" + payStatus +
				", bankTotalCost=" + bankTotalCost +
				", createTime=" + createTime +
				", expireTime=" + expireTime +
				", bankSuccessTime=" + bankSuccessTime +
				", paySuccessTime=" + paySuccessTime +
				", payInterface='" + payInterface + '\'' +
				", extParam=" + (extParam == null ? null : extParam.get("bizChannelId")) +
				", retailProductCode='" + retailProductCode + '\'' +
				", basicProductCode='" + basicProductCode + '\'' +
				", payOrderNo='" + payOrderNo + '\'' +
				", cashFee='" + (cashFee==null?"":cashFee.toString()) + '\'' +
				", settlementFee='" + (settlementFee==null?"":settlementFee.toString()) + '\'' +
				", promotionInfoDTOS='" + (promotionInfoDTOS==null?"":promotionInfoDTOS.toString()) + '\'' +
				'}';
	}


	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
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
