package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;

/**
 * 账户支付同步响应结果
 * 
 * @author：tao.liu
 * @since：2018年2月11日 上午10:41:32
 * @version:
 */
public class AccountSyncPayResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付记录支付状态
     */
    private TrxStatusEnum trxStatus;

    /**
     * 付款方商户编号
     */
    private String debitCustomerNo;

    /**
     * 付款方账号
     */
    private String debitAccountNo;

    /**
     * 银行通道编码
     */
    private String frpCode;

    /**
     * 扩展信息
     */
    private String extendInfo;

    /**
     * 基础产品码
     */
    private String basicProductCode;

    public TrxStatusEnum getTrxStatus() {
        return trxStatus;
    }

    public void setTrxStatus(TrxStatusEnum trxStatus) {
        this.trxStatus = trxStatus;
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

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    public String getBasicProductCode() {
        return basicProductCode;
    }

    public void setBasicProductCode(String basicProductCode) {
        this.basicProductCode = basicProductCode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountSyncPayResponseDTO [trxStatus=").append(trxStatus).append(", debitCustomerNo=").append(debitCustomerNo)
                .append(", debitAccountNo=").append(debitAccountNo).append(", frpCode=").append(frpCode).append(", extendInfo=").append(extendInfo)
                .append(", basicProductCode=").append(basicProductCode).append(", recordNo=").append(recordNo).append(", outTradeNo=").append(outTradeNo)
                .append(", orderNo=").append(orderNo).append(", dealUniqueSerialNo=").append(dealUniqueSerialNo).append(", payOrderType=").append(payOrderType)
                .append(", customerNumber=").append(customerNumber).append(", responseCode=").append(responseCode).append(", responseMsg=").append(responseMsg)
                .append(", processStatus=").append(processStatus).append("]");
        return builder.toString();
    }

}
