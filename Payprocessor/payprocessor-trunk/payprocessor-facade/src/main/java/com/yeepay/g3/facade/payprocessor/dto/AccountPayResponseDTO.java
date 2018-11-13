package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.utils.HiddenCode;

public class AccountPayResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = -3740166629722227553L;

    /**
     * 付款方商户编号
     */
    private String debitCustomerNo;

    /**
     * 付款方账号
     */
    private String debitAccountNo;

    /**
     * 扩展信息
     */
    private String extendInfo;

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

    public String getExtendInfo() {
        return extendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        this.extendInfo = extendInfo;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccountPayResponseDTO[");
        builder.append(super.toString());
        builder.append(",debitCustomerNo=");
        builder.append(debitCustomerNo);
        builder.append(",debitAccountNo=");
        builder.append(debitAccountNo);
        builder.append(",extInfo=");
        builder.append(HiddenCode.hiddenExtInfo(extendInfo));
        builder.append("]");
        
        return builder.toString();
    }

}
