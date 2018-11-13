package com.yeepay.g3.facade.payprocessor.dto;

import com.yeepay.g3.facade.payprocessor.enumtype.TrxStatusEnum;



public class PersonalMemberSyncPayResponseDTO extends BasicResponseDTO {

    private static final long serialVersionUID = 4532300341351324583L;

    private TrxStatusEnum trxStatus;
    
    /**
     * 零售产品码
     */
    private String retailProductCode;
    /**
     * 基础产品码
     */
    private String basicProductCode;

    /**
     * 个人会员支付流水号
     */
    private String paymentFlowNo;

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

	public String getPaymentFlowNo() {
		return paymentFlowNo;
	}

	public void setPaymentFlowNo(String paymentFlowNo) {
		this.paymentFlowNo = paymentFlowNo;
	}

	public TrxStatusEnum getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(TrxStatusEnum trxStatus) {
		this.trxStatus = trxStatus;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( "PersonalMemberSyncPayResponseDTO ["  );
	 	builder.append(super.toString());
	 	builder.append("]" );
	 	return builder.toString();
	}


	
}
