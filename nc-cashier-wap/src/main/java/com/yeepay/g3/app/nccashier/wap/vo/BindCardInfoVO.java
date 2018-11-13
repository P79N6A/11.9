package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.app.nccashier.wap.utils.Base64Util;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * 
 * @Description 鉴权绑卡请求卡信息
 * @author yangmin.peng
 * @since 2017年8月22日下午4:58:06
 */
public class BindCardInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cardno;// 卡号
	private String owner;// 持卡人姓名
	private String idno;// 证件号
	private String phoneNo;// 银行预留手机号
	private String avlidDate;// 有效期
	private String cvv;// cvv

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
	
	/**
	 * 将前端传过来的敏感信息解密
	 */
	public void decode() {
		if (StringUtils.isNotEmpty(this.getAvlidDate()))
			this.setAvlidDate(Base64Util.decode(this.getAvlidDate()));
		if (StringUtils.isNotEmpty(this.getCvv()))
			this.setCvv(Base64Util.decode(this.getCvv()));
		if (StringUtils.isNotEmpty(this.getPhoneNo()))
			this.setPhoneNo(Base64Util.decode(this.getPhoneNo()));
		if (StringUtils.isNotEmpty(this.getOwner()))
			this.setOwner(Base64Util.decode(this.getOwner()));
		if (StringUtils.isNotEmpty(this.getCardno()))
			this.setCardno(Base64Util.decode(this.getCardno()));
		if (StringUtils.isNotEmpty(this.getIdno()))
			this.setIdno(Base64Util.decode(this.getIdno()));
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("BindCardInfoVO{");
		sb.append("cardNo='").append(HiddenCode.hiddenBankCardNO(cardno)).append('\'');
		sb.append("userName='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append("idNo='").append(HiddenCode.hiddenIdentityCode(idno)).append('\'');
		sb.append("phoneNo='").append(HiddenCode.hiddenMobile(phoneNo)).append('\'');
		sb.append("avlidDate='").append(HiddenCode.hiddenAbliddate(avlidDate)).append('\'');
		sb.append("cvv='").append(HiddenCode.hiddenCvv(cvv)).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
