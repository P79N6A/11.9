package com.yeepay.g3.facade.nccashier.dto;


import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

public class APIPreauthFirstRequestDTO extends APIBasicRequestDTO{

	private static final long serialVersionUID = 1L;

	/** 用户标识号 */
	private String userNo;

	/** 用户标识类型 */
	private String userType;

	/** 用户IP */
	private String userIp;

	/*卡信息如下 */
	/** 卡号 */
	private String cardNo;

	/** 持卡人姓名 */
	private String owner;

	/** 身份证号 */
	private String idNo;

	/** 银行预留手机号 */
	private String phoneNo;

	/** CVV */
	private String cvv;

	/** 有效期 */
	private String validDate;

	/** 扩展信息，格式为Map<'String,String>序列化为json字符串 */
	private String paymentExt;

	public APIPreauthFirstRequestDTO(){
		
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public String getPaymentExt() {
		return paymentExt;
	}

	public void setPaymentExt(String paymentExt) {
		this.paymentExt = paymentExt;
	}


	@Override
	public String toString() {
		return "APIPreauthFirstRequestDTO{" +
				"userNo='" + HiddenCode.hiddenIdentityId(userNo) + '\'' +
				", userType='" + userType + '\'' +
				", userIp='" + userIp + '\'' +
				", cardNo='" + HiddenCode.hiddenBankCardNO(cardNo) + '\'' +
				", owner='" + HiddenCode.hiddenName(owner) + '\'' +
				", idNo='" + HiddenCode.hiddenIdentityCode(idNo) + '\'' +
				", phoneNo='" + HiddenCode.hiddenMobile(phoneNo) + '\'' +
				", cvv='" + HiddenCode.hiddenCvv(cvv) + '\'' +
				", validDate='" +  HiddenCode.hiddenAbliddate(validDate) + '\'' +
				", merchantNo='" + getMerchantNo() + '\'' +
				", token='" + getToken() + '\'' +
				", bizType='" + getBizType() + '\'' +
				", version='" + getVersion() + '\'' +
				", paymentExt='" + paymentExt + '\'' +
				"} ";
	}


	@Override
	public void validate(){
		super.validate();
		if (StringUtils.isBlank(getToken())) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		if (StringUtils.isBlank(userIp)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userIp不能为空");
		}
		if (StringUtils.isBlank(cardNo)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",cardNo不能为空");
		}
		if(StringUtils.isNotBlank(userType) && !IdentityType.getIdentityTypeMap().containsKey(userType)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userType错误");
		}
		if (StringUtils.isNotBlank(userType) && StringUtils.isBlank(userNo)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userNo不能为空");
		}

		if (StringUtils.isNotBlank(userNo) && StringUtils.isBlank(userType)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + ",userType不能为空");
		}
	}

	/**
	 * 将入参中的卡信息六项，转换成CardInfoDTO返回
	 * @return
	 */
	public CardInfoDTO getCardInfoDTO(){
		CardInfoDTO cardInfoDTO = new CardInfoDTO();
		cardInfoDTO.setCardno(getCardNo());
		cardInfoDTO.setIdno(getIdNo());
		cardInfoDTO.setName(getOwner());
		cardInfoDTO.setPhone(getPhoneNo());
		cardInfoDTO.setCvv2(getCvv());
		cardInfoDTO.setValid(getValidDate());
		return cardInfoDTO;
	}
}
