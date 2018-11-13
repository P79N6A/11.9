package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.nccashier.validator.ErrorDesc;
import com.yeepay.g3.facade.nccashier.validator.NotEmptyValidate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/4/7 14:39
 */
public class OrderInfoDTO implements Serializable {

    private static final long serialVersionUID = -1594691943951721603L;
    /**
     * 订单系统唯一订单号
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "uniqueOrderNo为空")
    private String uniqueOrderNo;

    /**
     * 商编
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantNo为空")
    private String merchantNo;

    /**
     * 商户名称
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantName为空")
    private String merchantName;

    /**
     * 商户订单号
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "merchantOrderId为空")
    private String merchantOrderId;

    /**
     * 订单系统编码(比如:DS,POS,G2NET,EPOS,BC)
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "orderSysNo为空")
    private String orderSysNo;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 订单有效期
     */
    private int orderExpDate;

    /**
     * 交易有效期的类型 SECOND("秒"), MINUTE("分"), HOUR("时"), DAY("天");
     */
    private String orderExpDateType;

    /**
     * 交易下单时间
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "orderTime为空")
    private Date orderTime;

    /**
     * 订单金额
     */
    @NotEmptyValidate
    @ErrorDesc(error = Errors.SYSTEM_INPUT_EXCEPTION, errorMsg = "orderAmount为空")
    private BigDecimal orderAmount;

    /**
     * 交易币种
     */
    private String currency;


    /**
     * 商品类别码
     */
    private String goodsCategoryCode;


    /**
     * 用户标识
     */
    private String identityId;

    /**
     * 用户标示类型(IMEI,MAC,USER_ID,EMAIL,PHONE,ID_CARD,AGREEMENT_NO,WECHAT)
     */
    private IdentityType identityType;

    /**
     * 支付成功返回商户地址
     */
    private String frontCallBackUrl;

    /**
     * UA信息
     */
    private String userUA;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 商品类型 VIRTUAL:虚拟 SUBSTANCE:实体
     */
    private String productType;
    /**
     * 订单类型ONLINE;OFFLINE
     */
    private String orderType;

    /**
     * 零售产品码
     */
    private String saleProductCode;

    /**
     * 产品订单方编码(目前已经有2,9,12,13,20)
     */
    private String productOrderCode;

    /**
     *风控标示(会跟订单方关联,ZHGT,YJZF,QFT,2GTRADE,DS)
     */
    private String riskProduction;

    /**
     *  父商户编号
     */
    private String parentMerchantNo;

    /**
     * 计费项
     */
    private String callFeeItem;

    /**
     * 支付的扩展信息(directPayType,owner,idCardNo,bankCardNo,cardType,bankCode,phone,appId,accountPayMerchantNo)
     */
    private String  paymentParamExt;

    /**
     * 风控参数 JSON格式(reffer)
     */
    private String riskParamExt;

    /**
     * 其扩展参数 JSON格式
     */
    private String otherParamExt;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 交易类型，值SALE（消费）、MEMBER_RECHARGE_2C（个人会员充值）LOAD(充值)，空的话默认为SALE
     */
    private String transactionType;

    public OrderInfoDTO(){}

    public OrderInfoDTO(String errorCode,String errorMsg){
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
    
    public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getUniqueOrderNo() {
        return uniqueOrderNo;
    }

    public void setUniqueOrderNo(String uniqueOrderNo) {
        this.uniqueOrderNo = uniqueOrderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getOrderSysNo() {
        return orderSysNo;
    }

    public void setOrderSysNo(String orderSysNo) {
        this.orderSysNo = orderSysNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getOrderExpDate() {
        return orderExpDate;
    }

    public void setOrderExpDate(int orderExpDate) {
        this.orderExpDate = orderExpDate;
    }

    public String getOrderExpDateType() {
        return orderExpDateType;
    }

    public void setOrderExpDateType(String orderExpDateType) {
        this.orderExpDateType = orderExpDateType;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getGoodsCategoryCode() {
        return goodsCategoryCode;
    }

    public void setGoodsCategoryCode(String goodsCategoryCode) {
        this.goodsCategoryCode = goodsCategoryCode;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public void setIdentityType(IdentityType identityType) {
        this.identityType = identityType;
    }

    public String getFrontCallBackUrl() {
        return frontCallBackUrl;
    }

    public void setFrontCallBackUrl(String frontCallBackUrl) {
        this.frontCallBackUrl = frontCallBackUrl;
    }

    public String getUserUA() {
        return userUA;
    }

    public void setUserUA(String userUA) {
        this.userUA = userUA;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSaleProductCode() {
        return saleProductCode;
    }

    public void setSaleProductCode(String saleProductCode) {
        this.saleProductCode = saleProductCode;
    }

    public String getProductOrderCode() {
        return productOrderCode;
    }

    public void setProductOrderCode(String productOrderCode) {
        this.productOrderCode = productOrderCode;
    }

    public String getRiskProduction() {
        return riskProduction;
    }

    public void setRiskProduction(String riskProduction) {
        this.riskProduction = riskProduction;
    }

    public String getParentMerchantNo() {
        return parentMerchantNo;
    }

    public void setParentMerchantNo(String parentMerchantNo) {
        this.parentMerchantNo = parentMerchantNo;
    }

    public String getCallFeeItem() {
        return callFeeItem;
    }

    public void setCallFeeItem(String callFeeItem) {
        this.callFeeItem = callFeeItem;
    }

    public String getPaymentParamExt() {
        return paymentParamExt;
    }

    public void setPaymentParamExt(String paymentParamExt) {
        this.paymentParamExt = paymentParamExt;
    }

    public String getRiskParamExt() {
        return riskParamExt;
    }

    public void setRiskParamExt(String riskParamExt) {
        this.riskParamExt = riskParamExt;
    }

    public String getOtherParamExt() {
        return otherParamExt;
    }

    public void setOtherParamExt(String otherParamExt) {
        this.otherParamExt = otherParamExt;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("OrderInfoDTO [uniqueOrderNo=");
        builder.append(uniqueOrderNo);
        builder.append(", merchantNo=");
        builder.append(merchantNo);
        builder.append(", merchantName=");
        builder.append(merchantName);
        builder.append(", merchantOrderId="+merchantOrderId);
        builder.append(", orderSysNo="+orderSysNo);
        builder.append(", productName="+productName);
        builder.append(", orderExpDate="+orderExpDate);
        builder.append(", orderExpDateType="+orderExpDateType);
        builder.append(", orderTime="+orderTime);
        builder.append(", orderAmount="+orderAmount);
        builder.append(", currency="+currency);
        builder.append(", goodsCategoryCode="+goodsCategoryCode);
        builder.append(", identityId="+ HiddenCode.hiddenIdentityId(identityId));
        builder.append(", identityType="+identityType);
        builder.append(", userIp="+userIp);
        builder.append(", userUA="+userUA);
        builder.append(", productType="+productType);
        builder.append(", orderType="+orderType);
        builder.append(", saleProductCode="+saleProductCode);
        builder.append(", productOrderCode="+productOrderCode);
        builder.append(", riskProduction="+riskProduction);
        builder.append(", callFeeItem="+callFeeItem);
        builder.append(", parentMerchantNo="+parentMerchantNo);
        builder.append(", paymentParamExt="+paymentParamExt);
        builder.append(", riskParamExt="+riskParamExt);
        builder.append(", otherParamExt="+otherParamExt);
        builder.append(", transactionType="+transactionType);
        builder.append(", errorCode=");
        builder.append(errorCode);
        builder.append(", errorMsg=");
        builder.append(errorMsg);
        builder.append("]");
        return builder.toString();
    }
}
