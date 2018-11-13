package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiewei on 15-10-13.
 */
public class RequestInfoDTO extends BasicResponseDTO implements Serializable {

	private static final long serialVersionUID = 8527837592382158444L;

	private String orderid;
	private BigDecimal amount;
	private String productname;
	private String companyname;
	private ThemeResult theme;
	private String merchantNo;

	private Long paymentRequestId;
	/**
	 * 支付记录ID
	 */
	private Long paymentRecordId;
	
	private String tradeSysNo;

	private Date tradeTime;

	private BigDecimal fee;

	private String paySysCode;

	private CashierVersionEnum cashierVersionEnum;

	/**
	 * 对应user_request_info表的url_param_info字段
	 */
	private OrderProcessorRequestDTO urlParamInfo;

	/**
	 * 订单方编号
	 */
	private String orderSysNo;

	/**
	 * 行业类别码
	 */
	private String industryCatalog;
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
	/**
	 * 系统商商户号
	 */
	private String parentMerchantNo;

	private String payTools;

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public ThemeResult getTheme() {
		return theme;
	}

	public void setTheme(ThemeResult theme) {
		this.theme = theme;
	}


	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

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



	public Long getPaymentRequestId() {
		return paymentRequestId;
	}

	public void setPaymentRequestId(Long paymentRequestId) {
		this.paymentRequestId = paymentRequestId;
	}

	public Long getPaymentRecordId() {
		return paymentRecordId;
	}

	public void setPaymentRecordId(Long paymentRecordId) {
		this.paymentRecordId = paymentRecordId;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public CashierVersionEnum getCashierVersionEnum() {
		return cashierVersionEnum;
	}

	public void setCashierVersionEnum(CashierVersionEnum cashierVersionEnum) {
		this.cashierVersionEnum = cashierVersionEnum;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RequestInfoDTO [orderid=");
		builder.append(orderid);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", productname=");
		builder.append(productname);
		builder.append(", companyname=");
		builder.append(companyname);
		builder.append(", theme=");
		builder.append(theme);
		builder.append(", paymentRequestId=");
		builder.append(paymentRequestId);
		builder.append(", paymentRecordId=");
		builder.append(paymentRecordId);
		builder.append("]");
		return builder.toString();
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getPaySysCode() {
		return paySysCode;
	}

	public void setPaySysCode(String paySysCode) {
		this.paySysCode = paySysCode;
	}


	public static void main(String[] args){
//		BigDecimal amount = new BigDecimal(10.00);
		BigDecimal amount = new BigDecimal("");
		String samount = amount.toString();
		amount =   amount.setScale(2, BigDecimal.ROUND_HALF_UP);
//        DecimalFormat df = new DecimalFormat("#.00");  
//        df.format(arg0)
		double damount = amount.doubleValue();
//		System.out.println(amount);
		System.out.println(damount);
//		System.out.println(samount);
		
	}

	public OrderProcessorRequestDTO getUrlParamInfo() {
        return urlParamInfo != null ? urlParamInfo : new OrderProcessorRequestDTO();
    }

	public void setUrlParamInfo(OrderProcessorRequestDTO urlParamInfo) {
		this.urlParamInfo = urlParamInfo;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}

	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
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


	public String getPayTools() {
		return payTools;
	}

	public void setPayTools(String payTools) {
		this.payTools = payTools;
	}
}
