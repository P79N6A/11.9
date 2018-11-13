package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.List;

public class CardOwnerConfirmResDTO extends BasicResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8831209247346945444L;
	private boolean needChooseCardOwner;
	private List<Person> persons;
	private long paymentRequestId;
	private int showTimes;
	
	public boolean isNeedChooseCardOwner() {
		return needChooseCardOwner;
	}
	public void setNeedChooseCardOwner(boolean needChooseCardOwner) {
		this.needChooseCardOwner = needChooseCardOwner;
	}
	public List<Person> getPersons() {
		return persons;
	}
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	public long getPaymentRequestId() {
		return paymentRequestId;
	}
	public void setPaymentRequestId(long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}
	public int getShowTimes() {
		return showTimes;
	}
	public void setShowTimes(int showTimes) {
		this.showTimes = showTimes;
	}
	@Override
	public String toString(){
		
		StringBuffer sb = new StringBuffer("CardOwnerConfirmResDTO{");
		sb.append("needChooseCardOwner='").append(needChooseCardOwner).append('\'');
		sb.append(", persons='").append(persons).append('\'');
		sb.append(", paymentRequestId='").append(paymentRequestId).append('\'');
		sb.append(", showTimes='").append(showTimes).append('\'');
		sb.append('}');
		return sb.toString();
	
	}
	
}
