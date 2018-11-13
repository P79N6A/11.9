package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.core.nccashier.enumtype.InstallmentPayTypeEnum;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.utils.common.CollectionUtils;

/**
 * 
 * @author duangduang
 *
 */
public class InstallmentInfoNeeded extends CombinedPaymentDTO {

	private InstallmentBankInfo currentBankInfo;

	private SignRelationInfo currentSignInfo;

	private CashierUserInfo cashierUser;

	private InstallmentPayTypeEnum payType;

	private BusinessSubsidie businessSubsidie;
	
	private String token;

	public InstallmentInfoNeeded() {

	}

	public InstallmentBankInfo getCurrentBankInfo() {
		return currentBankInfo;
	}

	public void setCurrentBankInfo(InstallmentBankInfo currentBankInfo) {
		this.currentBankInfo = currentBankInfo;
	}

	public SignRelationInfo getCurrentSignInfo() {
		return currentSignInfo;
	}

	public void setCurrentSignInfo(SignRelationInfo currentSignInfo) {
		this.currentSignInfo = currentSignInfo;
	}

	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public InstallmentPayTypeEnum getPayType() {
		return payType;
	}

	public void setPayType(InstallmentPayTypeEnum payType) {
		this.payType = payType;
	}

	public BusinessSubsidie getBusinessSubsidie() {
		return businessSubsidie;
	}

	public void setBusinessSubsidie(BusinessSubsidie businessSubsidie) {
		this.businessSubsidie = businessSubsidie;
	}

	/**
	 * 构造record的比较条件对象
	 * 
	 * @param cardN0
	 * @return
	 */
	public RecordCondition buildRecordCondition(String cardNo, OrderAction orderAction, CashierVersionEnum cashierVersion, String token) {
		RecordCondition condition = new RecordCondition();
		condition.setCardN0(cardNo);
		if (currentSignInfo != null) {
			if (currentSignInfo.isCardSigned()) {
				condition.setCardInfoId(currentSignInfo.getSignCardInfo().getSignInfoId());
			}
			condition.setBindId(currentSignInfo.getSignRelationId());
		}

		condition.setPayTool(PayTool.YHKFQ_ZF.name());
		String[] recordPayTypes = { payType == null ? null : payType.name() };
		condition.setRecordPayTypes(recordPayTypes);
		if(currentBankInfo!=null){
			if(currentBankInfo.getTargetInstallmentRateInfo()!=null){
				condition.setPeriod(currentBankInfo.getTargetInstallmentRateInfo().getPeriod()+"");
			}else{
				condition.setPeriod(CollectionUtils.isEmpty(currentBankInfo.getInstallmentRateInfos())
						&& currentBankInfo.getInstallmentRateInfos().get(0) != null ? null
								: currentBankInfo.getInstallmentRateInfos().get(0).getPeriod() + "");
			}
		}
		
		condition.setOrderAction(orderAction);
		condition.setCashierVersion(cashierVersion);
		condition.setToken(token);
		return condition;
	}

	public PersonHoldCard buildPersonHoldCard() {
		PersonHoldCard person = new PersonHoldCard();
		CardInfo card = new CardInfo();
		if (currentSignInfo != null && currentSignInfo.isCardSigned()) {
			person.setPhoneN0(currentSignInfo.getSignCardInfo().getPhoneNo());
		}
		card.setBank(currentBankInfo.getBankInfo());
		person.setCard(card);
		return person;
	}

	public CashierUserInfo getCashierUser() {
		return cashierUser;
	}

	public void setCashierUser(CashierUserInfo cashierUser) {
		this.cashierUser = cashierUser;
	}
}
