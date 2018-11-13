package com.yeepay.g3.core.nccashier.entity;

import com.yeepay.g3.facade.nccashier.dto.ThemeResult;

import java.math.BigDecimal;
import java.util.Date;

public class ParamShowInfo {

	public String orderid;
	public BigDecimal amount;
	public String productname;
	public String companyname;
	public ThemeResult theme;
	public Date createTime;
	
	public BigDecimal fee;
	/**
	 * 行业类别码
	 */
	public String industryCatalog;
	private String paySysCode;
	/**
	 * 商品分类（非银行卡使用）
	 */
	private String goodsKind;
	/**
	 * 商品描述（非银行卡使用）
	 */
	private String goodsDesc;
	/**
	 * 订单扩展信息（非银行卡使用）
	 */
	private String goodsExt;
	/**
	 * 商户回调地址（非银行卡跳转使用）
	 */
	private String callBackUrl;
	private String parentMerchantNo;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public ThemeResult getTheme() {
		return theme;
	}

	public void setTheme(ThemeResult theme) {
		this.theme = theme;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getPaySysCode() {
		return paySysCode;
	}

	public void setPaySysCode(String paySysCode) {
		this.paySysCode = paySysCode;
	}

	public String getIndustryCatalog() {
		return industryCatalog;
	}

	public void setIndustryCatalog(String industryCatalog) {
		this.industryCatalog = industryCatalog;
	}

	public String getGoodsKind() {
		return goodsKind;
	}

	public void setGoodsKind(String goodsKind) {
		this.goodsKind = goodsKind;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsExt() {
		return goodsExt;
	}

	public void setGoodsExt(String goodsExt) {
		this.goodsExt = goodsExt;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getParentMerchantNo() {
		return parentMerchantNo;
	}

	public void setParentMerchantNo(String parentMerchantNo) {
		this.parentMerchantNo = parentMerchantNo;
	}
	
}
