package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 统一API收银台，（聚合及被扫支付）返回参数
 * Created by ruiyang.du on 2017/6/28.
 */
public class UnifiedAPICashierResponseDTO implements Serializable{
    private static final long serialVersionUID = 8569128530424474949L;

    /**
     * 状态码
     */
    private String code;
    /**
     * 信息
     */
    private String message;
    /*同入参返回start*/
    private String payTool;
    private String payType;
    private String merchantNo;
    private String token;
    /*同入参返回end*/

    /**
     * 仅用于公众号支付，用于区分不同通道返回结果的处理方式
     */
    private String resultType;
    /**
     * 接口返回数据
     */
    private String resultData;

    /**
     *
     * added by zengzhi.han 20181016
     * 一些非重要的扩展参数，比如 被扫是否需要密码(isNeedPassword)
     * json字符串 map 类型
     */
    private String extParamMap;



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
    private List<BankPromotionInfoDTO> bankPromotionInfoDTOS;




    @Override
    public String toString() {
        return "UnifiedAPICashierResponseDTO{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", payTool='" + payTool + '\'' +
                ", payType='" + payType + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", token='" + token + '\'' +
                ", resultType='" + resultType + '\'' +
                ", resultData='" + resultData + '\'' +
                ", extParamMap='" + extParamMap + '\'' +
                ", cashFee='" + (cashFee==null?"":cashFee.toString()) + '\'' +
                ", settlementFee='" + (settlementFee==null?"":settlementFee.toString())+ '\'' +
                ", bankPromotionInfoDTOS='" + (bankPromotionInfoDTOS==null?"":bankPromotionInfoDTOS.toString()) + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayTool() {
        return payTool;
    }

    public void setPayTool(String payTool) {
        this.payTool = payTool;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(String extParamMap) {
        this.extParamMap = extParamMap;
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

    public List<BankPromotionInfoDTO> getBankPromotionInfoDTOS() {
        return bankPromotionInfoDTOS;
    }

    public void setBankPromotionInfoDTOS(List<BankPromotionInfoDTO> bankPromotionInfoDTOS) {
        this.bankPromotionInfoDTOS = bankPromotionInfoDTOS;
    }
}
