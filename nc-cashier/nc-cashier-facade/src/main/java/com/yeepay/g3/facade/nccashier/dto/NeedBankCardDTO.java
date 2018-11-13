package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.nccashier.enumtype.ReqSmsSendTypeEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
/**
 * 补充项
 */
public class NeedBankCardDTO implements Serializable{
	private static final long serialVersionUID = 2087477610181621029L;
	
	private String cardno;//卡号是否必填
	private String owner;//持卡人姓名是否必填
	private String idno;//证件号是否必填
	private String phoneNo;//银行预留手机号是否必填
	private String ypMobile;//易宝预留手机号是否必填
	private String avlidDate;//有效期是否必填
	private String cvv;//cvv是否必填
	private IdcardType idCardType;//证件类型是否必填
	private String bankPWD;//取款密码是否必填
	private ReqSmsSendTypeEnum reqSmsSendTypeEnum;//短验类型
	private NeedSurportDTO needSurportDTO;//是否是补充项的DTO
	private BankCardDetailDTO bankCardDetailDTO;//在下单时通过卡号查询返回的卡信息，主要是为获取手机号码传到页面，弹框展示发短信到xx手机号上
	
	public NeedBankCardDTO() {
		super();
	}
	
	public ReqSmsSendTypeEnum getReqSmsSendTypeEnum() {
		return reqSmsSendTypeEnum;
	}

	public void setReqSmsSendTypeEnum(ReqSmsSendTypeEnum reqSmsSendTypeEnum) {
		this.reqSmsSendTypeEnum = reqSmsSendTypeEnum;
	}

	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getYpMobile() {
		return ypMobile;
	}
	public void setYpMobile(String ypMobile) {
		this.ypMobile = ypMobile;
	}
	public String getAvlidDate() {
		return avlidDate;
	}
	public void setAvlidDate(String avlidDate) {
		this.avlidDate = avlidDate;
	}
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public IdcardType getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(IdcardType idCardType) {
		this.idCardType = idCardType;
	}
	public String getBankPWD() {
		return bankPWD;
	}
	public void setBankPWD(String bankPWD) {
		this.bankPWD = bankPWD;
	}
	
	public NeedSurportDTO getNeedSurportDTO() {
		return needSurportDTO;
	}

	public void setNeedSurportDTO(NeedSurportDTO needSurportDTO) {
		this.needSurportDTO = needSurportDTO;
	}

	public BankCardDetailDTO getBankCardDetailDTO() {
		return bankCardDetailDTO;
	}

	public void setBankCardDetailDTO(BankCardDetailDTO bankCardDetailDTO) {
		this.bankCardDetailDTO = bankCardDetailDTO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("NeedBankCardDTO{");
		sb.append("owner='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append(", idno='").append(HiddenCode.hiddenIdentityCode(idno)).append('\'');
		sb.append(", cardno='").append(HiddenCode.hiddenBankCardNO(cardno)).append('\'');
		sb.append(", phoneNo='").append(HiddenCode.hiddenMobile(phoneNo)).append('\'');
		sb.append(", ypMobile='").append(HiddenCode.hiddenMobile(ypMobile)).append('\'');
		sb.append(", idCardType='").append(idCardType).append('\'');
		sb.append(", reqSmsSendTypeEnum='").append(reqSmsSendTypeEnum).append('\'');
		sb.append(", needSurportDTO='").append(needSurportDTO).append('\'');
		sb.append('}');
		return sb.toString();
	}
	
	
}
