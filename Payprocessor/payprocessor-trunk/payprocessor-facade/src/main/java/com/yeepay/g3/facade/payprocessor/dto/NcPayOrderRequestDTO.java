package com.yeepay.g3.facade.payprocessor.dto;

import java.util.Map;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.Length;

import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 无卡下单请求参数
 *
 * @author peile.fan
 *
 */
public class NcPayOrderRequestDTO extends BasicRequestDTO {

	private static final long serialVersionUID = -4284175130243091269L;

	/**
	 * NCPAY定义的业务方
	 */
	@NotNull
	private Long bizType;

	/**
	 * 会员类型(对外不适用枚举以及易宝自定义类型)
	 */
	private MemberTypeEnum memberType;

	/**
	 * 会员编号
	 */
	private String memberNO;

	/**
	 * 支付卡信息类型(临时卡/绑卡)
	 */
	@NotNull
	private CardInfoTypeEnum cardInfoType;

	/**
	 * 支付卡信息id(绑卡记录主键id，临时卡主键id)
	 */
	private Long cardInfoId;
	
	/**
	 * 支付工具
	 */
	private String payTool;
	
	/**
	 * 支付场景
	 */
	@Length(max = 20, message = "payScene最大长度为20")
	private String payScene;

	/**
	 * 零售产品码
	 */
	private String retailProductCode;
	/**
	 * 基础产品码
	 */
	private String basicProductCode;

	/**
	 * 卡信息
	 */
	private BankCardInfoDTO bankCardInfoDTO;

	/**
	 * 支付跳转回调地址
	 */
	private String payRedirectUrl;

	/**
	 * 签约跳转回调地址
	 */
	private String signRedirectUrl;

	/**
	 * 扩展参数
	 */
	private Map<String,String> extParam;


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public CardInfoTypeEnum getCardInfoType() {
		return cardInfoType;
	}

	public void setCardInfoType(CardInfoTypeEnum cardInfoType) {
		this.cardInfoType = cardInfoType;
	}

	public Long getCardInfoId() {
		return cardInfoId;
	}

	public void setCardInfoId(Long cardInfoId) {
		this.cardInfoId = cardInfoId;
	}

	public Long getBizType() {
		return bizType;
	}

	public void setBizType(Long bizType) {
		this.bizType = bizType;
	}

	public MemberTypeEnum getMemberType() {
		return memberType;
	}

	public void setMemberType(MemberTypeEnum memberType) {
		this.memberType = memberType;
	}

	public String getMemberNO() {
		return memberNO;
	}

	public void setMemberNO(String memberNO) {
		this.memberNO = memberNO;
	}

	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getPayTool() {
		return payTool;
	}

	public void setPayTool(String payTool) {
		this.payTool = payTool;
	}
	
	public String getPayScene() {
		return payScene;
	}

	public void setPayScene(String payScene) {
		this.payScene = payScene;
	}

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

	public BankCardInfoDTO getBankCardInfoDTO() {
		return bankCardInfoDTO;
	}

	public void setBankCardInfoDTO(BankCardInfoDTO bankCardInfoDTO) {
		this.bankCardInfoDTO = bankCardInfoDTO;
	}

	public Map<String, String> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, String> extParam) {
		this.extParam = extParam;
	}

	public String getPayRedirectUrl() {
		return payRedirectUrl;
	}

	public void setPayRedirectUrl(String payRedirectUrl) {
		this.payRedirectUrl = payRedirectUrl;
	}

	public String getSignRedirectUrl() {
		return signRedirectUrl;
	}

	public void setSignRedirectUrl(String signRedirectUrl) {
		this.signRedirectUrl = signRedirectUrl;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toStringExclude(this , "goodsInfo");
	}
}
