/**
 * 
 */
package com.yeepay.g3.core.nccashier.vo;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhen.tan
 * 订单详情
 */
public class OrderDetailInfoModel {
	
	/**
	 * 订单系统统一订单号-cashier必填
	 */
	private String uniqueOrderNo;

	/**
	 * 商编
	 */
	private String merchantAccountCode;

	/**
	 * 签名商编（校验订单商编专用）
	 */
	private String signedMerchantAccountCode;
	
	/**
	 * 商户订单号
	 */
	private String merchantOrderId;
	
	/**
	 * 订单系统编码
	 */
	private String orderSysNo;
	
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 商品分类（非银行卡支付用）
	 */
	private String productKind;
	/**
	 * 商品描述（非银行卡支付用）
	 */
	private String productDesc;
	/**
	 * 商户回调地址（非银行卡支付用）
	 */
	private String notBankPaycallBackUrl;
	/**
	 * 订单扩展信息（非银行卡支付用）
	 */
	private String orderExtendInfo;
	/**
	 * 商户名称
	 */
	private String merchantName;
	
	/**
	 * 订单有效期
	 */
	private int orderExpDate;
	
	/**
	 * 交易下单时间
	 */
	private Date orderTime;
	
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	
	/**
	 * 交易币种
	 */
	private CurrencyEnum currency;
	
	/**
	 * 指定收银台
	**/
	private CashierVersionEnum cashierVersion;
	
	/**
	 * 支付工具
	 */
	private PayTool payTool;
	
	/**
	 * 直连类型
	 */
	private DirectPayType directPayType;
	
	/**
	 * 主题自定义编码
	 */
	private String themeCustomCode;
	
	/**
	 * 商品类别码
	 */
	private String goodsCategoryCode;
	
	/**
	 * 地区
	 */
	private String areaInfo;
	
	/**
	 * 交易风控参数
	 */
	private String tradeRiskInfo;
	
	/**
	 * 查询风控参数
	 */
	private String queryRiskInfo; 
	
	/**
	 * 用户标识
	 */
	private String identityId;
	
	/**
	 * 用户类型
	 */
	private IdentityType identityType;
	
	/**
	 * 支付成功返回商户地址
	 */
	private String frontCallBackUrl;
	
	/**
	 * UA信息
	 */
	private String userUA;
	
	/**
	 * 用户ip
	 */
	private String userIp;
	
	/**
	 * 终端信息
	 */
	private String terminalInfo;
	/**
	 * app信息
	 */
	private String appInfo;
	
	/**
	 * 透传卡信息
	 */
	private TransparentCardInfo cardInfo;
	
    /**
     * 备注
     */
    private String remark;

	/**
	 * 商户ID
	 */
	private String memberNo;
	/**
	 * 商户类型
	 */
	private String memberType;
	/**
	 * 商品类型 VIRTUAL:虚拟 SUBSTANCE:实体
	 */
	private String productType;
	/**
	 * 订单类型ONLINE;OFFLINE
	 */
	private String orderType;
	
	private String appId;
	
	/**
	 * 零售产品码
	 */
	private String saleProductCode;

	/**
	 * 产品订单方
	 */
	private String productOrderCode;

	/**
	 *  父商户编号
	 */
	private String parentMerchantNo;

	/**
	 * 交易有效期的类型 SECOND("秒"), MINUTE("分"), HOUR("时"), DAY("天");
	 */
	private String orderExpDateType;

	private String callFeeItem;

	private String reffer;

	private String paySysCode;

	private String riskProduction;
	
	/**
	 * 账户支付所需的扩展字段
	 */
	private Map<String, String> accountPayExtInfo;
	
	/**
	 * 账户支付扣款商编
	 */
	private String accountPayMerchantNo;
	
	private TransactionTypeEnum transactionType;

	/**
	 * 区域编码（银联二维码被扫必填）
	 */
	private String areaCode;


	/**
	 * 订单处理器反查得到的拓展字段,用于路由
	 */
	private String groupTag;

	/**
	 * 订单系统如opr等当前的预授权状态 -- 针对预授权订单
	 */
	private String currentPreauthStatus;
	
	/**
	 * 预授权金额（预授权发起阶段冻结金额，订单处理器再未接受到完成回调时该字段与订单金额一致，当接受到完成成功回调时，该字段变为预授权完成金额）
	 */
	private BigDecimal preauthAmount;
	
	/**
	 * 目前该参数是从opr反查接口查得的，用以做银行卡分期补贴费率查询的计费项
	 */
	private String productVersion;

	/**
	 * 用于透传绑卡id
	 */
	private String bindId;

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getGroupTag() {
		return groupTag;
	}

	public void setGroupTag(String groupTag) {
		this.groupTag = groupTag;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	public TransactionTypeEnum getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(TransactionTypeEnum transactionType) {
		this.transactionType = transactionType;
	}

	public String getAccountPayMerchantNo() {
		return accountPayMerchantNo;
	}
	
	public void setAccountPayMerchantNo(String accountPayMerchantNo) {
		this.accountPayMerchantNo = accountPayMerchantNo;
	}
	
	public String getSaleProductCode() {
		return saleProductCode;
	}
	public void setSaleProductCode(String saleProductCode) {
		this.saleProductCode = saleProductCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getMerchantAccountCode() {
		return merchantAccountCode;
	}
	public String getUniqueOrderNo() {
		return uniqueOrderNo;
	}
	public void setUniqueOrderNo(String uniqueOrderNo) {
		this.uniqueOrderNo = uniqueOrderNo;
	}
	public void setMerchantAccountCode(String merchantAccountCode) {
		this.merchantAccountCode = merchantAccountCode;
	}
	public String getMerchantOrderId() {
		return merchantOrderId;
	}
	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}
	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
	}
	
	public String getProductName() {
		return productName == null?"":productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getOrderExpDate() {
		return orderExpDate;
	}
	public void setOrderExpDate(int orderExpDate) {
		this.orderExpDate = orderExpDate;
	}
	public String getMerchantName() {
		return merchantName == null?"":merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public CurrencyEnum getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEnum currency) {
		this.currency = currency;
	}
	public CashierVersionEnum getCashierVersion() {
		return cashierVersion;
	}
	public void setCashierVersion(CashierVersionEnum cashierVersion) {
		this.cashierVersion = cashierVersion;
	}
	public PayTool getPayTool() {
		return payTool;
	}
	public void setPayTool(PayTool payTool) {
		this.payTool = payTool;
	}
	public DirectPayType getDirectPayType() {
		return directPayType;
	}
	public void setDirectPayType(DirectPayType directPayType) {
		this.directPayType = directPayType;
	}
	public String getThemeCustomCode() {
		return themeCustomCode;
	}
	public void setThemeCustomCode(String themeCustomCode) {
		this.themeCustomCode = themeCustomCode;
	}
	public String getGoodsCategoryCode() {
		return goodsCategoryCode;
	}
	public void setGoodsCategoryCode(String goodsCategoryCode) {
		this.goodsCategoryCode = goodsCategoryCode;
	}
	public String getAreaInfo() {
		return areaInfo;
	}
	public void setAreaInfo(String areaInfo) {
		this.areaInfo = areaInfo;
	}
	public String getTradeRiskInfo() {
		return tradeRiskInfo;
	}
	public void setTradeRiskInfo(String tradeRiskInfo) {
		this.tradeRiskInfo = tradeRiskInfo;
	}
	public String getQueryRiskInfo() {
		return queryRiskInfo;
	}
	public void setQueryRiskInfo(String queryRiskInfo) {
		this.queryRiskInfo = queryRiskInfo;
	}
	public String getIdentityId() {
		return identityId;
	}
	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}
	public IdentityType getIdentityType() {
		return identityType;
	}
	public void setIdentityType(IdentityType identityType) {
		this.identityType = identityType;
	}
	public String getFrontCallBackUrl() {
		return frontCallBackUrl;
	}
	public void setFrontCallBackUrl(String frontCallBackUrl) {
		this.frontCallBackUrl = frontCallBackUrl;
	}
	public String getUserUA() {
		return userUA;
	}
	public void setUserUA(String userUA) {
		this.userUA = userUA;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getTerminalInfo() {
		return terminalInfo;
	}
	public void setTerminalInfo(String terminalInfo) {
		this.terminalInfo = terminalInfo;
	}
	public String getAppInfo() {
		return appInfo;
	}
	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}
	public TransparentCardInfo getCardInfo() {
		return cardInfo;
	}
	public void setCardInfo(TransparentCardInfo cardInfo) {
		this.cardInfo = cardInfo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProductOrderCode() {
		return productOrderCode;
	}

	public void setProductOrderCode(String productOrderCode) {
		this.productOrderCode = productOrderCode;
	}

	public String getParentMerchantNo() {
		return parentMerchantNo;
	}

	public void setParentMerchantNo(String parentMerchantNo) {
		this.parentMerchantNo = parentMerchantNo;
	}

	public String getReffer() {
		return reffer;
	}

	public void setReffer(String reffer) {
		this.reffer = reffer;
	}
	
	public String getCurrentPreauthStatus() {
		return currentPreauthStatus;
	}

	public void setCurrentPreauthStatus(String currentPreauthStatus) {
		this.currentPreauthStatus = currentPreauthStatus;
	}
	
	public BigDecimal getPreauthAmount() {
		return preauthAmount;
	}

	public void setPreauthAmount(BigDecimal preauthAmount) {
		this.preauthAmount = preauthAmount;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public PaymentRequest toPaymentRequest() {
		PaymentRequest paymentRequest = new PaymentRequest();
		// 商户编号
		paymentRequest.setMerchantNo(this.getMerchantAccountCode());
		// 商户订单号
		paymentRequest.setMerchantOrderId(this.getMerchantOrderId());
		paymentRequest.setMerchantName(this.getMerchantName().length() > 32 ? this.getMerchantName().substring(0, 32) : this.getMerchantName());
		
		// modify by meiling.zhuang : 若是为预授权订单，则paymentRequest存储的金额为预授权发起金额
		if(this.getTransactionType()!=null && TransactionTypeEnum.PREAUTH == this.getTransactionType()){
			paymentRequest.setOrderAmount(this.getPreauthAmount()); // 预授权发起金额
		}else{
			paymentRequest.setOrderAmount(this.getOrderAmount()); // 订单金额
		}
		
		// 收银台版本
		paymentRequest.setCashierVersion(this.getCashierVersion().toString());

		// 币种
		if (null != this.getCurrency()) {
			paymentRequest.setCurrency(this.getCurrency().toString());
		}
		
		// 交易系统编号
		paymentRequest.setTradeSysNo(this.getOrderSysNo());
		// 交易系统订单号
		paymentRequest.setTradeSysOrderId(this.getUniqueOrderNo());
		
		// 订单方订单号,无订单方
		paymentRequest.setOrderOrderId(this.getUniqueOrderNo());
		// 订单方编码,无订单方
		paymentRequest.setOrderSysNo(this.getProductOrderCode());
	
		paymentRequest.setFrontCallBackUrl(this.getFrontCallBackUrl());
		
		// 用户类型
		if (null != this.getIdentityType()) {
			paymentRequest.setIdentityType(this.getIdentityType().toString());
		}
		// 用户标识
		paymentRequest.setIdentityId(this.getIdentityId());
			
		// 商品名称
		paymentRequest.setProductName(this.getProductName().length() > 50 ? this.getProductName().substring(0, 50) : this.getProductName());
//		// 状态
		paymentRequest.setState(PayRequestStatusEnum.INIT.getValue());// 待支付
		// 终端信息
		paymentRequest.setTerminalInfo(this.getTerminalInfo());
		// 主题编码
		paymentRequest.setThemeCustomCode(this.getThemeCustomCode());
		// 交易风控信息
		paymentRequest.setTradeRiskInfo(this.getTradeRiskInfo());
		// 查询风控参数
		paymentRequest.setQueryRiskInfo(this.getQueryRiskInfo());
		
		// 用户ip
		paymentRequest.setUserIp(this.getUserIp());
		// UA信息
		String ua = this.getUserUA();
		if (StringUtils.isNotEmpty(ua) && ua.length() > 150) {
			ua = ua.substring(0, 150);
		}
		paymentRequest.setUserUA(ua);
		// 版本
		
		paymentRequest.setVersion(CashierVersionEnum.getVersionValue(this.getCashierVersion()));
				
		//商户类型,默认商户类型为JOINLY联名账户
		if(StringUtils.isNotBlank(this.getMemberType())){
			paymentRequest.setMemberType(this.getMemberType());
		}else{
			paymentRequest.setMemberType(Constant.JOINLY);
		}
		//商户ID
		if(StringUtils.isNotBlank(this.getMemberNo())){
			paymentRequest.setMemberNo(this.getMemberNo());
		}
				
		// app信息
		paymentRequest.setAppInfo(this.getAppInfo());
		// 地域信息
		paymentRequest.setAreaInfo(this.getAreaInfo());
		
		
		if(this.getCardInfo() != null){
			
			TransparentCardInfo cardInfo = this.getCardInfo();
			// 银行编码
			paymentRequest.setBankCode(cardInfo.getBankCode());
			
			// 卡号
			paymentRequest.setCardNo(cardInfo.getCardNo());
			
			// 卡类型
			if (cardInfo.getCardType() != null) {
				paymentRequest.setCardType(cardInfo.getCardType().toString());
			}
		
			paymentRequest.setIdCard(cardInfo.getIdcard());
			if(cardInfo.getIdcardType()!=null){
				paymentRequest.setIdCardType(cardInfo.getIdcardType().toString());
			}
			paymentRequest.setOwner(cardInfo.getOwner());
			paymentRequest.setPhoneNo(cardInfo.getPhoneNo());
		}

		paymentRequest.setBindId(this.getBindId());

		// payment_request创建日期
		paymentRequest.setCreateTime(new Date());
		// 订单系统的订单创建日期
		paymentRequest.setOrderTime(this.getOrderTime());

		// 支付时间
		paymentRequest.setPayTime(new Date());
		
		// 更新时间
		paymentRequest.setUpdateTime(new Date());
		
		// 订单有效期
		paymentRequest.setOrderExpDate(this.getOrderExpDate() <= 0 ? 24*60
				: this.getOrderExpDate());	
		
		JSONObject extendInfo = new JSONObject();
		
		if(StringUtils.isNotEmpty(this.getAppId())){
			extendInfo.put("origAppId", this.getAppId());
		}
		if(StringUtils.isNotEmpty(this.getSaleProductCode())){
			extendInfo.put("saleProductCode", this.getSaleProductCode());	
		}
		if(StringUtils.isNotBlank(this.getProductType())){
			extendInfo.put("productType", this.getProductType());
		}
		if(StringUtils.isNotBlank(this.getOrderType())){
			extendInfo.put("orderType", this.getOrderType());
		}

		if(this.getAccountPayExtInfo()!=null){
			extendInfo.put("accountPayExt", this.getAccountPayExtInfo());
		}
		//以下四个参数供非银行卡支付使用
		if(StringUtils.isNotBlank(this.getProductKind())){
			extendInfo.put("goodsKind", this.getProductKind());
		}
		if(StringUtils.isNotBlank(this.getProductDesc())){
			extendInfo.put("goodsDesc", this.getProductDesc());
		}
		if(StringUtils.isNotBlank(this.getOrderExtendInfo())){
			extendInfo.put("goodsExt", this.getOrderExtendInfo());
		}
		if(StringUtils.isNotBlank(this.getNotBankPaycallBackUrl())){
			extendInfo.put("callBackUrl", this.getNotBankPaycallBackUrl());
		}
		// 区域编码字段，用作银联二维码被扫必填
		if(StringUtils.isNotBlank(this.getAreaCode())){
			extendInfo.put("areaCode", this.getAreaCode());
		}
		if(StringUtils.isNotBlank(this.getGroupTag())){
			extendInfo.put("groupTag", this.getGroupTag());
		}

		// 用于银行卡分期补贴计费项
		if(StringUtils.isNotBlank(this.getProductVersion())){
			extendInfo.put("productVersion", this.getProductVersion());
		}
		
		paymentRequest.setExtendInfo(extendInfo.toJSONString());
		paymentRequest.setParentMerchantNo(this.getParentMerchantNo());
		paymentRequest.setOrderExpDateType(this.getOrderExpDateType());
		paymentRequest.setAppID(this.getAppId());
		paymentRequest.setPaySysCode(this.getPaySysCode());
		paymentRequest.setRiskProduction(this.getRiskProduction());
		return paymentRequest;

	}

	@Override
	public String toString() {
		return "OrderDetailInfoModel{" +
				"uniqueOrderNo='" + uniqueOrderNo + '\'' +
				", merchantAccountCode='" + merchantAccountCode + '\'' +
				", signedMerchantAccountCode='" + signedMerchantAccountCode + '\'' +
				", merchantOrderId='" + merchantOrderId + '\'' +
				", orderSysNo='" + orderSysNo + '\'' +
				", productName='" + productName + '\'' +
				", merchantName='" + merchantName + '\'' +
				", orderExpDate=" + orderExpDate +
				", orderTime=" + orderTime +
				", orderAmount=" + orderAmount +
				", currency=" + currency +
				", cashierVersion=" + cashierVersion +
				", payTool=" + payTool +
				", directPayType=" + directPayType +
				", themeCustomCode='" + themeCustomCode + '\'' +
				", goodsCategoryCode='" + goodsCategoryCode + '\'' +
				", areaInfo='" + areaInfo + '\'' +
				", tradeRiskInfo='" + tradeRiskInfo + '\'' +
				", queryRiskInfo='" + queryRiskInfo + '\'' +
				", identityId='" + HiddenCode.hiddenIdentityId(identityId) + '\'' +
				", identityType=" + identityType +
				", frontCallBackUrl='" + frontCallBackUrl + '\'' +
				", userUA='" + userUA + '\'' +
				", userIp='" + userIp + '\'' +
				", terminalInfo='" + terminalInfo + '\'' +
				", appInfo='" + appInfo + '\'' +
				", cardInfo=" + "【卡信息不打印】" +
				", remark='" + remark + '\'' +
				", memberNo='" + memberNo + '\'' +
				", memberType='" + memberType + '\'' +
				", productType='" + productType + '\'' +
				", orderType='" + orderType + '\'' +
				", appId='" + appId + '\'' +
				", saleProductCode='" + saleProductCode + '\'' +
				", productOrderCode='" + productOrderCode + '\'' +
				", parentMerchantNo='" + parentMerchantNo + '\'' +
				", orderExpDateType='" + orderExpDateType + '\'' +
				", callFeeItem='" + callFeeItem + '\'' +
				", reffer='" + reffer + '\'' +
				", paySysCode='" + paySysCode + '\'' +
				", riskProduction='" + riskProduction + '\'' +
				", accountPayExtInfo=" + accountPayExtInfo +
				'}';
	}

	public String getOrderExpDateType() {
		return orderExpDateType;
	}

	public void setOrderExpDateType(String orderExpDateType) {
		this.orderExpDateType = orderExpDateType;
	}

	public String getCallFeeItem() {
		return callFeeItem;
	}

	public void setCallFeeItem(String callFeeItem) {
		this.callFeeItem = callFeeItem;
	}

	public String getPaySysCode() {
		return paySysCode;
	}

	public void setPaySysCode(String paySysCode) {
		this.paySysCode = paySysCode;
	}

	public String getRiskProduction() {
		return riskProduction;
	}

	public void setRiskProduction(String riskProduction) {
		this.riskProduction = riskProduction;
	}
	
	public Map<String, String> getAccountPayExtInfo() {
		return accountPayExtInfo;
	}
	
	public void setAccountPayExtInfo(Map<String, String> accountPayExtInfo) {
		this.accountPayExtInfo = accountPayExtInfo;
	}


	public String getSignedMerchantAccountCode() {
		return signedMerchantAccountCode;
	}

	public void setSignedMerchantAccountCode(String signedMerchantAccountCode) {
		this.signedMerchantAccountCode = signedMerchantAccountCode;
	}
	public String getProductKind() {
		return productKind;
	}
	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getNotBankPaycallBackUrl() {
		return notBankPaycallBackUrl;
	}
	public void setNotBankPaycallBackUrl(String notBankPaycallBackUrl) {
		this.notBankPaycallBackUrl = notBankPaycallBackUrl;
	}
	public String getOrderExtendInfo() {
		return orderExtendInfo;
	}
	public void setOrderExtendInfo(String orderExtendInfo) {
		this.orderExtendInfo = orderExtendInfo;
	}		
	
	/**
	 * 解析其他扩展参数
	 * 
	 * @param otherParamExt
	 * @param orderDetailInfoModel
	 */
	public void buildOtherParamExt(String otherParamExt) {
		if (StringUtils.isBlank(otherParamExt)) {
			return;
		}
		JSONObject jsonObject = CommonUtil.parseJson(otherParamExt);
		Map<String, String> accountPayExtInfo = new HashMap<String, String>();
		String historyType = jsonObject.getString("historyType");
		if (StringUtils.isNotBlank(historyType)) {
			accountPayExtInfo.put("historyType", historyType);
		}
		String bizType = jsonObject.getString("bizType");
		if (StringUtils.isNotBlank(bizType)) {
			accountPayExtInfo.put("bizType", bizType);
		}
		String settleable = jsonObject.getString("settleable");
		if (StringUtils.isNotBlank(settleable)) {
			accountPayExtInfo.put("settleable", settleable);
		}
		String tradeType = jsonObject.getString("tradeType");
		if (StringUtils.isNotBlank(tradeType)) {
			accountPayExtInfo.put("tradeType", tradeType);
		}
		String trxId = jsonObject.getString("trxId");
		if (StringUtils.isNotBlank(trxId)) {
			accountPayExtInfo.put("trxId", trxId);
		}
		String historyDesc = jsonObject.getString("historyDesc");
		accountPayExtInfo.put("historyDesc", historyDesc);
		this.setAccountPayExtInfo(accountPayExtInfo);
	}
	
	
}
