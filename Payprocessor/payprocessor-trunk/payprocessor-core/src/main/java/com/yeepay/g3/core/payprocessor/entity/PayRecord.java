package com.yeepay.g3.core.payprocessor.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付信息表
 */
public class PayRecord {

    /**
     * 支付订单号,主键,唯一索引
     */
    private String recordNo;

    /**
     * 收款场景
     */
    private String cashierVersion;

    /**
     * 支付产品
     */
    private String payProduct;

    /**
     * 支付场景
     */
    private String payScene;

    /**
     * 订单类型
     */
    private String payOrderType;

    /**
     * paymentRequest主键
     */
    private Long requestId;

    /**
     * 银行子系统订单号
     */
    private String bankOrderNo;

    /**
     * 银行的交易流水号
     */
    private String bankSeq;

    /**
     * 银行子系统交易流水号
     */
    private String bankTrxId;

    /**
     * NCPAY支付订单号
     */
    private String paymentNo;

    /**
     * 平台类型
     */
    private String platformType;

    /**
     * 支付订单状态
     */
    private String status;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 用户手续费
     */
    private BigDecimal userFee;

    /**
     * 短信类型
     */
    private String smsState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付银行编码
     */
    private String bankId;

    /**
     * 通道编码
     */
    private String frpCode;

    /**
     * 卡种
     */
    private String cardType;

    /**
     * 绑卡ID
     */
    private String bindCardInfoId;

    /**
     * 卡账户ID
     */
    private String cardId;

    /**
     * 客户端IP
     */
    private String payerIp;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorMsg;

    /**
     * 请求系统订单号
     */
    private String requestSysId;

    /**
     * 请求系统编码
     */
    private String requestSystem;

    /**
     * 网银支付卡类型{对公、对私}
     */
    private String bankAccountType;

    /**
     * 分期公司
     */
    private String loanCompany;

    /**
     * 分期期数
     */
    private String loanTerm;

    /**
     * 扩展信息
     */
    private ExtendedInfo extendedInfo;

    /**
     * 付款商编
     */
    private String debitCustomerNo;

    /**
     * 付款账号
     */
    private String debitAccountNo;

    /**
     * 付款账户名
     */
    private String debitUserName;

    /**
     * 零售产品码
     */
    private String retailProductCode;
    /**
     * 基础产品码
     */
    private String basicProductCode;

    private String openId;

    /**
     * 分期付款期数
     */
    private Integer cflCount;

    /**
     * 分期付款手续费率
     */
    private BigDecimal cflRate = new BigDecimal(0);

    /**
     * 商户补贴手续费率
     */
    private BigDecimal merchantFeeSubsidy = new BigDecimal(0);

    /**
     * 商户补贴手续费
     */
    private BigDecimal merchantAmountSubsidy = new BigDecimal(0);

    private String pageCallBack;

    /**
     * 是否组合支付
     */
    private boolean combinedPay;

    /**
     * 第一支付金额 = 订单金额 - 第二支付金额
     */
    private BigDecimal firstPayAmount;

//    /**
//     * 现金支付金额
//     */
//    private String cashFee;
//
//    /**
//     * 应结算金额
//     */
//    private String settlementFee;

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getPayOrderType() {
        return payOrderType;
    }

    public void setPayOrderType(String payOrderType) {
        this.payOrderType = payOrderType;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankSeq() {
        return bankSeq;
    }

    public void setBankSeq(String bankSeq) {
        this.bankSeq = bankSeq;
    }

    public String getBankTrxId() {
        return bankTrxId;
    }

    public void setBankTrxId(String bankTrxId) {
        this.bankTrxId = bankTrxId;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getUserFee() {
        return userFee;
    }

    public void setUserFee(BigDecimal userFee) {
        this.userFee = userFee;
    }

    public String getSmsState() {
        return smsState;
    }

    public void setSmsState(String smsState) {
        this.smsState = smsState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getBindCardInfoId() {
        return bindCardInfoId;
    }

    public void setBindCardInfoId(String bindCardInfoId) {
        this.bindCardInfoId = bindCardInfoId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCashierVersion() {
        return cashierVersion;
    }

    public void setCashierVersion(String cashierVersion) {
        this.cashierVersion = cashierVersion;
    }

    public String getPayProduct() {
        return payProduct;
    }

    public void setPayProduct(String payProduct) {
        this.payProduct = payProduct;
    }

    public String getPayScene() {
        return payScene;
    }

    public String getPayerIp() {
        return payerIp;
    }

    public void setPayerIp(String payerIp) {
        this.payerIp = payerIp;
    }

    public void setPayScene(String payScene) {
        this.payScene = payScene;
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

    public String getRequestSysId() {
        return requestSysId;
    }

    public void setRequestSysId(String requestSysId) {
        this.requestSysId = requestSysId;
    }

    public String getRequestSystem() {
        return requestSystem;
    }

    public void setRequestSystem(String requestSystem) {
        this.requestSystem = requestSystem;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getLoanCompany() {
        return loanCompany;
    }

    public void setLoanCompany(String loanCompany) {
        this.loanCompany = loanCompany;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public ExtendedInfo getExtendedInfo() {
        return extendedInfo;
    }

    public void setExtendedInfo(ExtendedInfo extendedInfo) {
        this.extendedInfo = extendedInfo;
    }

    public String getDebitCustomerNo() {
        return debitCustomerNo;
    }

    public void setDebitCustomerNo(String debitCustomerNo) {
        this.debitCustomerNo = debitCustomerNo;
    }

    public String getDebitAccountNo() {
        return debitAccountNo;
    }

    public void setDebitAccountNo(String debitAccountNo) {
        this.debitAccountNo = debitAccountNo;
    }

    public String getDebitUserName() {
        return debitUserName;
    }

    public void setDebitUserName(String debitUserName) {
        this.debitUserName = debitUserName;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getCflCount() {
        return cflCount;
    }

    public void setCflCount(Integer cflCount) {
        this.cflCount = cflCount;
    }

    public BigDecimal getCflRate() {
        return cflRate;
    }

    public void setCflRate(BigDecimal cflRate) {
        this.cflRate = cflRate;
    }

    public BigDecimal getMerchantFeeSubsidy() {
        return merchantFeeSubsidy;
    }

    public void setMerchantFeeSubsidy(BigDecimal merchantFeeSubsidy) {
        this.merchantFeeSubsidy = merchantFeeSubsidy;
    }

    public BigDecimal getMerchantAmountSubsidy() {
        return merchantAmountSubsidy;
    }

    public void setMerchantAmountSubsidy(BigDecimal merchantAmountSubsidy) {
        this.merchantAmountSubsidy = merchantAmountSubsidy;
    }

    public String getPageCallBack() {
        return pageCallBack;
    }

    public void setPageCallBack(String pageCallBack) {
        this.pageCallBack = pageCallBack;
    }

    public boolean isCombinedPay() {
        return combinedPay;
    }

    public void setCombinedPay(boolean combinedPay) {
        this.combinedPay = combinedPay;
    }

    public BigDecimal getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(BigDecimal firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

/*    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getSettlementFee() {
        return settlementFee;
    }

    public void setSettlementFee(String settlementFee) {
        this.settlementFee = settlementFee;
    }*/

    @Override
    public String toString() {
        return "PayRecord{" +
                "recordNo='" + recordNo + '\'' +
                ", cashierVersion='" + cashierVersion + '\'' +
                ", payProduct='" + payProduct + '\'' +
                ", payScene='" + payScene + '\'' +
                ", payOrderType='" + payOrderType + '\'' +
                ", requestId=" + requestId +
                ", bankOrderNo='" + bankOrderNo + '\'' +
                ", bankSeq='" + bankSeq + '\'' +
                ", bankTrxId='" + bankTrxId + '\'' +
                ", paymentNo='" + paymentNo + '\'' +
                ", platformType='" + platformType + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", cost=" + cost +
                ", userFee=" + userFee +
                ", smsState='" + smsState + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", payTime=" + payTime +
                ", bankId='" + bankId + '\'' +
                ", frpCode='" + frpCode + '\'' +
                ", cardType='" + cardType + '\'' +
                ", bindCardInfoId='" + bindCardInfoId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", payerIp='" + payerIp + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", requestSysId='" + requestSysId + '\'' +
                ", requestSystem='" + requestSystem + '\'' +
                ", bankAccountType='" + bankAccountType + '\'' +
                ", loanCompany='" + loanCompany + '\'' +
                ", loanTerm='" + loanTerm + '\'' +
                ", extendedInfo=" + extendedInfo +
                ", debitCustomerNo='" + debitCustomerNo + '\'' +
                ", debitAccountNo='" + debitAccountNo + '\'' +
                ", debitUserName='" + debitUserName + '\'' +
                ", retailProductCode='" + retailProductCode + '\'' +
                ", basicProductCode='" + basicProductCode + '\'' +
                ", openId='" + openId + '\'' +
                ", cflCount=" + cflCount +
                ", cflRate=" + cflRate +
                ", merchantFeeSubsidy=" + merchantFeeSubsidy +
                ", merchantAmountSubsidy=" + merchantAmountSubsidy +
                ", pageCallBack='" + pageCallBack + '\'' +
                ", combinedPay=" + combinedPay +
                ", firstPayAmount=" + firstPayAmount +
/*                ", cashFee=" + cashFee +
                ", settlementFee=" + settlementFee +*/
                '}';
    }


}
