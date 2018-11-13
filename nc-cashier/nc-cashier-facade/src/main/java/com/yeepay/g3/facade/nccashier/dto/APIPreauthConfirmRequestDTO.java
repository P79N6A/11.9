package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

public class APIPreauthConfirmRequestDTO extends APIBasicRequestDTO{

	private static final long serialVersionUID = 1L;



	/** 支付记录号 */
//	private String recordId;
	/** 验证码 */
	private String verifyCode;

	/** 补充项：持卡人姓名 */
	private String owner;
	/** 补充项：身份证号 */
	private String idNo;
	/** 补充项：银行预留手机号 */
	private String phoneNo;
	/** 补充项：CVV */
	private String cvv;
	/** 补充项：有效期 */
	private String validDate;
	/** 补充项：取款密码 */
	private String bankPWD;
	/** 扩展信息，格式为Map<'String,String>序列化为json字符串 */
	private String paymentExt;

	/**
	 * 支付处理器的子单号
	 */
	private String paymentRecordNo;

	public CardInfoDTO getCardInfoDTO() {
		if(StringUtils.isBlank(getIdNo()) && StringUtils.isBlank(getOwner()) && StringUtils.isBlank(getPhoneNo()) &&
				StringUtils.isBlank(getCvv()) && StringUtils.isBlank((getValidDate())) && StringUtils.isBlank(getBankPWD())){
			return null;
		}
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		cardInfoDTO.setName(getOwner());
		cardInfoDTO.setIdno(getIdNo());
		cardInfoDTO.setPhone(getPhoneNo());
		cardInfoDTO.setCvv2(getCvv());
		cardInfoDTO.setValid(getValidDate());
		cardInfoDTO.setPass(getBankPWD());
		return cardInfoDTO;
	}

	/**
	 * 清空入参中的卡信息，用于不需要提交补充项而调用方传入了补充项时
	 */
	public void cleanCardInfo(){
		setIdNo(null);
		setOwner(null);
		setPhoneNo(null);
		setCvv(null);
		setValidDate(null);
		setBankPWD(null);
	}

	public APIPreauthConfirmRequestDTO(){
		
	}

//	public String getRecordId() {
//		return recordId;
//	}
//
//	public void setRecordId(String recordId) {
//		this.recordId = recordId;
//	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getBankPWD() {
		return bankPWD;
	}

	public void setBankPWD(String bankPWD) {
		this.bankPWD = bankPWD;
	}

	public String getPaymentExt() {
		return paymentExt;
	}

	public void setPaymentExt(String paymentExt) {
		this.paymentExt = paymentExt;
	}
	
	public String getPaymentRecordNo() {
		return paymentRecordNo;
	}

	public void setPaymentRecordNo(String paymentRecordNo) {
		this.paymentRecordNo = paymentRecordNo;
	}

	@Override
	public String toString() {
		return "APIPreauthConfirmRequestDTO{" +
				"paymentRecordNo='" + paymentRecordNo + '\'' +
				", verifyCode='" + HiddenCode.hiddenVerifyCode(verifyCode) + '\'' +
				", owner='" + HiddenCode.hiddenName(owner) + '\'' +
				", idNo='" + HiddenCode.hiddenIdentityCode(idNo) + '\'' +
				", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
				", cvv='" + HiddenCode.hiddenCvv(cvv) + '\'' +
				", validDate='" + HiddenCode.hiddenAbliddate(validDate) + '\'' +
				", bankPWD='" + HiddenCode.HiddenBankPwd(bankPWD) + '\'' +
				", merchantNo='" + getMerchantNo() + '\'' +
				", token='" + getToken() + '\'' +
				", bizType='" + getBizType() + '\'' +
				", version='" + getVersion() + '\'' +
				", paymentExt='" + paymentExt + '\'' +
				'}';
	}

	@Override
	public void validate(){
		super.validate();
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		if (StringUtils.isBlank(paymentRecordNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",paymentRecordNo不能为空");
		}
	}
}
