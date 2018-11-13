package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.utils.common.StringUtils;

public class RecordCondition {
	
	private String bindId;
	
	private String recordPayTypes[];
	
	private String payTool;
	
	private String period;
	
	/**
	 * 下游的支付订单号
	 */
	private String payOrderId;
	
	private String cardN0;
	
	private String recordId;
	
	private String recordState;
	
	private OrderAction orderAction;
	
	private String tradeSysOrderId;
	
	private String tradeSysNo;
	
	/**
	 * 卡信息ID
	 */
	private String cardInfoId;
	
	private CashierVersionEnum cashierVersion;
	
	private String token;
	
	private String memberNo;
	
	/**
	 * 是否校验订单状态
	 */
	private boolean validateStatus = true;
	
	/**
	 * 默认不复用PAYING状态的RECORD
	 */
	private boolean reusePayingRecord = false;
	
	public RecordCondition(){
		
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String[] getRecordPayTypes() {
		return recordPayTypes;
	}

	public void setRecordPayTypes(String[] recordPayTypes) {
		this.recordPayTypes = recordPayTypes;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public String getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(String payOrderId) {
		this.payOrderId = payOrderId;
	}
	
	public String getCardN0() {
		return cardN0;
	}

	public void setCardN0(String cardN0) {
		this.cardN0 = cardN0;
	}
	
	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}

	public OrderAction getOrderAction() {
		return orderAction;
	}

	public void setOrderAction(OrderAction orderAction) {
		this.orderAction = orderAction;
	}
	
	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getCardInfoId() {
		return cardInfoId;
	}

	public void setCardInfoId(String cardInfoId) {
		this.cardInfoId = cardInfoId;
	}
	
	public CashierVersionEnum getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(CashierVersionEnum cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean isValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(boolean validateStatus) {
		this.validateStatus = validateStatus;
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	
	public boolean isReusePayingRecord() {
		return reusePayingRecord;
	}

	public void setReusePayingRecord(boolean reusePayingRecord) {
		this.reusePayingRecord = reusePayingRecord;
	}

	private boolean compareRecordPayType(String payType){
		if(recordPayTypes!=null && recordPayTypes.length > 0){
			boolean illage = false;
			for(String recordPayType : recordPayTypes){
				if (StringUtils.isNotBlank(recordPayType) && recordPayType.equals(payType)) {
					illage = true;
					break;
				}
			}
			if(!illage){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 将record中对应的字段值与比较条件字段值对比（比较字段非空才比），任一比较不通过，返回false，否则返回true
	 * 
	 * @param record
	 * @return
	 */
	public boolean compare(PaymentRecord record) {
		if (StringUtils.isNotBlank(cardN0) && !cardN0.equals(String.valueOf(record.getCardNo()))){
			return false;
		}
		if (StringUtils.isNotBlank(recordId) && !recordId.equals(String.valueOf(record.getId()))) {
			return false;
		}
		if (StringUtils.isNotBlank(payTool) && !payTool.equals(record.getPayTool())) {
			return false;
		}
		if (!compareRecordPayType(record.getPayType())) {
			return false;
		}
		if (StringUtils.isNotBlank(cardInfoId) && !cardInfoId.equals(record.getCardInfoId())) {
			return false;
		}
		if (StringUtils.isNotBlank(bindId) && !bindId.equals(record.getBindId())) {
			return false;
		}
		if (StringUtils.isNotBlank(period) && !period.equals(record.getPeriod()+"")) {
			return false;
		}
		if (StringUtils.isNotBlank(recordState) && !recordState.equals(record.getState())) {
			return false;
		}

		if (StringUtils.isNotBlank(tradeSysNo) && !tradeSysNo.equals(record.getTradeSysNo())) {
			return false;
		}

		if (StringUtils.isNotBlank(tradeSysOrderId) && !tradeSysOrderId.equals(record.getTradeSysOrderId())) {
			return false;
		}
		// token不为空，代表要比较token，前置收银台不会比较token
		if(StringUtils.isNotBlank(token) && !token.equals(record.getTokenId())){
			return false;
		}
		
		// 比较payOrderId,找到与该payOrderId一致的record
		if(StringUtils.isNotBlank(payOrderId) && !payOrderId.equals(record.getPaymentOrderNo())){
			return false;
		}
		
		// 比较会员编号
		if(StringUtils.isNotBlank(memberNo) && !memberNo.equals(record.getMemberNo())){
			return false;
		}
		return true;
	}
	
}
