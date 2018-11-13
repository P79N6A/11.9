package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class CustomerServiceResponseVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private static final Logger Log = LoggerFactory.getLogger(CustomerServiceResponseVO.class);

	/**
	 * 收银台版本
	 */
	private String cashierVersion;

	/**
	 * 交易金额
	 */
	private BigDecimal amount;

	/**
	 * 交易时间
	 */
	private Date tradeTime;

	/**
	 * 收款商家
	 */
	private String companyname;

	/**
	 * 商户编号
	 */
	private String merchantNo;

	/**
	 * 商品信息
	 */
	private String productname;
	
	/**
	 * 商户订单号
	 */
	private String orderid;
	
	/**
	 * 报错信息
	 */
	private String errMsg;
	
	/**
	 * 商品开通产品
	 */
	private String openPayToolsInVersion;
	
	private String scanpayPayTypes;
	
	
	public CustomerServiceResponseVO(){
		
	}

	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public Date getTradeTime() {
		return tradeTime;
	}


	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}


	public String getCompanyname() {
		return companyname;
	}


	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}


	public String getMerchantNo() {
		return merchantNo;
	}


	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}


	public String getProductname() {
		return productname;
	}


	public void setProductname(String productname) {
		this.productname = productname;
	}


	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	public String getErrMsg() {
		return errMsg;
	}


	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	public String getOpenPayToolsInVersion() {
		return openPayToolsInVersion;
	}


	public void setOpenPayToolsInVersion(String openPayToolsInVersion) {
		this.openPayToolsInVersion = openPayToolsInVersion;
	}

	public String getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(String cashierVersion) {
		this.cashierVersion = cashierVersion;
	}
	
	public String getScanpayPayTypes() {
		return scanpayPayTypes;
	}

	public void setScanpayPayTypes(String scanpayPayTypes) {
		this.scanpayPayTypes = scanpayPayTypes;
	}

	public void buildScanpayPayTypes(PayExtendInfo payExtendInfo){
		StringBuilder builder  = new StringBuilder();
		if(payExtendInfo.isSccanPay()){
			if (payExtendInfo.containsPayType(PayTypeEnum.WECHAT_ATIVE_SCAN)) {
				builder.append("微信，");
			}
			if (payExtendInfo.containsPayType(PayTypeEnum.ALIPAY)) {
				builder.append("支付宝，");
			}
			if(payExtendInfo.containsPayType(PayTypeEnum.UPOP_ATIVE_SCAN)){
				builder.append("银联，");
			}
			if(payExtendInfo.containsPayType(PayTypeEnum.JD_ATIVE_SCAN)){
				builder.append("京东，");
			}
			if(payExtendInfo.containsPayType(PayTypeEnum.QQ_ATIVE_SCAN)){
				builder.append("QQ钱包，");
			}
		}
		if(StringUtils.isBlank(builder)){
			setScanpayPayTypes("");
			return;
		}
		
		setScanpayPayTypes("（" + builder.substring(0, builder.length()-1) + "）");
	}
	
	public void buildPayTool(PayExtendInfo payExtendInfo){
		String[] payTools = payExtendInfo.getPayTool();
		if(payTools==null || payTools.length==0){
			return;
		}
		StringBuilder builder = new StringBuilder();
		for(String payTool : payTools){
			try{
				PayTool payToolEnum = PayTool.valueOf(payTool);
				builder.append(payToolEnum.getDescription());
				if(PayTool.SCCANPAY.equals(payToolEnum)){
					buildScanpayPayTypes(payExtendInfo);
					builder.append(scanpayPayTypes);
				}
				builder.append("、");
			}catch(Throwable t){
				Log.warn("转换oriPayTool="+payTool+"到枚举值失败", t);
				continue;
			}
		}
		this.openPayToolsInVersion = builder.substring(0,builder.length()-1);
	}
	
	public void buildCashierVersion(String cashierVersion){
		if(StringUtils.isBlank(cashierVersion)) return;
		if(CashierVersionEnum.PC.name().equals(cashierVersion)){
			this.cashierVersion = "PC收银台";
		}else if(CashierVersionEnum.WAP.name().equals(cashierVersion)){
			this.cashierVersion = "H5收银台";
		}else if(CashierVersionEnum.WXGZH.name().equals(cashierVersion)){
			this.cashierVersion = "公众号收银台";
		}else if(CashierVersionEnum.SDK.name().equals(cashierVersion)){
			this.cashierVersion = "SDK收银台";
		}
	}
	
	public String getCustomerServiceInitMessage(){
		StringBuilder builder = new StringBuilder();
		if(StringUtils.isNotBlank(cashierVersion)){
			builder.append("收银台版本:").append(cashierVersion).append("; ");
		}
		if(amount!=null){
			builder.append("交易金额:").append(amount).append("; ");
		}
		if(tradeTime!=null){
			builder.append("交易时间:").append(tradeTime).append("; ");
		}
		if(StringUtils.isNotBlank(companyname)){
			builder.append("收款商家:").append(companyname).append("; ");
		}
		if(StringUtils.isNotBlank(merchantNo)){
			builder.append("商户编号:").append(merchantNo).append("; ");
		}
		if(StringUtils.isNotBlank(productname)){
			builder.append("商品信息:").append(productname).append("; ");
		}
		if(StringUtils.isNotBlank(orderid)){
			builder.append("商户订单号:").append(orderid).append("; ");
		}
		if(StringUtils.isNotBlank(errMsg) && (!"null".equals(errMsg))){
			builder.append("报错信息:").append(errMsg).append("; ");
		}
		if(StringUtils.isNotBlank(openPayToolsInVersion)){
			builder.append("商户开通产品:").append(openPayToolsInVersion).append("; ");
		}
		if(StringUtils.isNotBlank(builder.toString())){
			return builder.toString().substring(0, builder.length()-2);
		}
		return builder.toString();
	}
	
	


}
