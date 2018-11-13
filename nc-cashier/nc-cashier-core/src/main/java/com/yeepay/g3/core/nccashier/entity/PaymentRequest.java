package com.yeepay.g3.core.nccashier.entity;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CashierUserInfo;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaymentRequest implements Serializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentRequest.class);

	private static final long serialVersionUID = -3825472229392004191L;
	/**
	 * 主键
	 */
	private long id;
	/**
	 * 交易系统订单号
	 */
	private String tradeSysOrderId;
	/**
	 * 交易系统编码
	 */
	private String tradeSysNo;
	/**
	 * 订单方订单号
	 */
	private String orderOrderId;
	/**
	 * 订单方编码
	 */
	private String orderSysNo;
	/**
	 * 业务模式编码
	 */
	private String bizModeCode;
	/**
	 * 商户编号
	 */
	private String merchantNo;
	/**
	 * 商户系统订单号
	 */
	private String merchantOrderId;
	/**
	 * 商户名称
	 */
	private String merchantName;
	/**
	 * 商品名称
	 */
	private String productName;
	/**
	 * 指定收银台
	 */
	private String cashierVersion;
	/**
	 * 主题自定义编码
	 */
	private String themeCustomCode;
	/**
	 * 订单金额
	 */
	private BigDecimal orderAmount;
	/**
	 * 成本
	 */
	private BigDecimal cost;
	/**
	 * 支付方式
	 */
	private String payMethod;
	/**
	 * UA信息
	 */
	private String userUA;
	/**
	 * 请求方ip
	 */
	private String userIp;
	/**
	 * 绑卡ID
	 */
	private String bindId;
	/**
	 * 卡信息ID
	 */
	private String cardInfoId;
	/**
	 * 卡号
	 */

	private String cardNo;
	/**
	 * 卡类型
	 */
	private String cardType;
	/**
	 * 银行编码
	 */
	private String bankCode;
	/**
	 * 支付方流水号
	 */
	private String paymentOrderNo;
	/**
	 * 支付方式编码
	 */
	private String paymentSysNo;
	/**
	 * 支付有效期
	 */
	private int orderExpDate;
	/**
	 * 失败编码
	 */
	private String errorCode;
	/**
	 * 失败原因
	 */
	private String errorMsg;
	/**
	 * 状态
	 */
	private String state;
	/**
	 * 行业类别码
	 */
	private String industryCatalog;
	/**
	 * 用户标识
	 */
	private String identityId;
	/**
	 * 用户类型
	 */
	private String identityType;
	/**
	 * 持卡人姓名
	 */

	private String owner;
	/**
	 * 证件类型
	 */
	private String idCardType;
	/**
	 * 证件号
	 */

	private String idCard;
	/**
	 * 手机号
	 */

	private String phoneNo;
	/**
	 * 交易币种
	 */
	private String currency;
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
	 * 商户交易地址
	 */
	private String merchantOrderUrl;
	/**
	 * 支付成功返回商户地址
	 */
	private String frontCallBackUrl;
	/**
	 * 交易发生时间
	 */
	private Date payTime;
	/**
	 * 终端信息
	 */
	private String terminalInfo;
	/**
	 * app信息
	 */
	private String appInfo;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 版本
	 */
	private int version;
	/**
	 * 备注
	 * 
	 */
	private String remark;
	/**
	 * 商户ID
	 */
	private String memberNo;
	/**
	 * 商户类型 JOINLY联名账户、YIBAO易宝三代会员
	 */
	private String memberType;
	
	/**
	 * 网银支付场景
	 */
	private String ebankPayScene;
	
	/**
	 * 扩展信息
	 */
	private String extendInfo;

	/**
	 * 父商户编号
	 */
	private String parentMerchantNo;

	/**
	 * 区分商户开通的配置信息从哪取（来自USER_REQUEST_还是PAYMENT_REQUEST）
	 */
	private String merchantConfigFrom;

	/**
	 *订单有效期的时间类型
	 */
	private String orderExpDateType;

	/**
	 * 费用类型（用户手续费和商户手续费）
	 */
	private String feeType;

	/**
	 * 手续费
	 */
	private BigDecimal fee;
	/**
	 * APPID
	 */
	private String appID;

	/**
	 * 支付系统号
	 */
	private String paySysCode;

	private String riskProduction;

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	/**
	 * 反查订单获取的订单时间
	 */
	private Date orderTime;

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("PaymentRequest{");
		sb.append("id=").append(id);
		sb.append(", tradeSysOrderId='").append(tradeSysOrderId).append('\'');
		sb.append(", tradeSysNo='").append(tradeSysNo).append('\'');
		sb.append(", orderOrderId='").append(orderOrderId).append('\'');
		sb.append(", orderSysNo='").append(orderSysNo).append('\'');
		sb.append(", bizModeCode='").append(bizModeCode).append('\'');
		sb.append(", orderAmount=").append(orderAmount);
		sb.append(", paymentSysNo='").append(paymentSysNo).append('\'');
		sb.append(", paymentOrderNo='").append(paymentOrderNo).append('\'');
		sb.append(", cardInfoId='").append(cardInfoId).append('\'');
		sb.append(", bindId='").append(bindId).append('\'');
		sb.append(", cashierVersion='").append(cashierVersion).append('\'');
		sb.append(", merchantNo='").append(merchantNo).append('\'');
		sb.append(", merchantName='").append(merchantName).append('\'');
		sb.append(", merchantOrderId='").append(merchantOrderId).append('\'');
		sb.append(", productName='").append(productName).append('\'');
		sb.append(", themeCustomCode='").append(themeCustomCode).append('\'');
		sb.append(", tradeRiskInfo='").append(tradeRiskInfo).append('\'');
		sb.append(", queryRiskInfo='").append(queryRiskInfo).append('\'');
		sb.append(", areaInfo='").append(areaInfo).append('\'');
		sb.append(", payMethod='").append(payMethod).append('\'');
		sb.append(", userUA='").append(userUA).append('\'');
		sb.append(", bindCardId=").append(cardInfoId);
		sb.append(", owner='").append(HiddenCode.hiddenName(owner)).append('\'');
		sb.append(", cardType='").append(cardType).append('\'');
		sb.append(", bankCode='").append(bankCode).append('\'');
		sb.append(", cardNo='").append(HiddenCode.hiddenBankCardNO(cardNo)).append('\'');
		sb.append(", idCardType='").append(idCardType).append('\'');
		sb.append(", phoneNo='").append(HiddenCode.hiddenMobile(phoneNo)).append('\'');
		sb.append(", idCard='").append(HiddenCode.hiddenIdentityCode(idCard)).append('\'');
		sb.append(", userIp='").append(userIp).append('\'');
		sb.append(", state='").append(state).append('\'');
		sb.append(", merchantOrderUrl='").append(merchantOrderUrl).append('\'');
		sb.append(", orderExpDate='").append(orderExpDate).append('\'');
		sb.append(", industryCatalog='").append(industryCatalog).append('\'');
		sb.append(", errorCode='").append(errorCode).append('\'');
		sb.append(", errorMsg='").append(errorMsg).append('\'');
		sb.append(", identityId='").append(HiddenCode.hiddenIdentityId(identityId)).append('\'');
		sb.append(", identityType='").append(identityType).append('\'');
		sb.append(", currency='").append(currency).append('\'');
		sb.append(", payTime='").append(payTime).append('\'');
		sb.append(", terminalInfo='").append(terminalInfo).append('\'');
		sb.append(", appInfo='").append(appInfo).append('\'');
		sb.append(", createTime=").append(createTime);
		sb.append(", updateTime=").append(updateTime);
		sb.append(", version=").append(version);
		sb.append(", remark=").append(remark);
		sb.append(", memberNo='").append(memberNo);
		sb.append(", memberType='").append(memberType);
		sb.append(", ebankPayScene='").append(ebankPayScene);
		sb.append(", extendInfo='").append(extendInfo);
		sb.append(", merchantConfigFrom='").append(merchantConfigFrom);
		sb.append(", parentMerchantNo='").append(parentMerchantNo);
		sb.append(", orderTime=").append(orderTime);
		sb.append('}');
		return sb.toString();
	}

	public String getEbankPayScene() {
		return ebankPayScene;
	}

	public void setEbankPayScene(String ebankPayScene) {
		this.ebankPayScene = ebankPayScene;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTradeSysOrderId() {
		return tradeSysOrderId;
	}

	public void setTradeSysOrderId(String tradeSysOrderId) {
		this.tradeSysOrderId = tradeSysOrderId;
	}

	public String getTradeSysNo() {
		return tradeSysNo;
	}

	public void setTradeSysNo(String tradeSysNo) {
		this.tradeSysNo = tradeSysNo;
	}

	public String getOrderOrderId() {
		return orderOrderId;
	}

	public void setOrderOrderId(String orderOrderId) {
		this.orderOrderId = orderOrderId;
	}

	public String getOrderSysNo() {
		return orderSysNo;
	}

	public void setOrderSysNo(String orderSysNo) {
		this.orderSysNo = orderSysNo;
	}

	public String getBizModeCode() {
		return bizModeCode;
	}

	public void setBizModeCode(String bizModeCode) {
		this.bizModeCode = bizModeCode;
	}

	public String getCardInfoId() {
		return cardInfoId;
	}

	public void setCardInfoId(String cardInfoId) {
		this.cardInfoId = cardInfoId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public void setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCashierVersion() {
		return cashierVersion;
	}

	public void setCashierVersion(String cashierVersion) {
		this.cashierVersion = cashierVersion;
	}

	public String getThemeCustomCode() {
		return themeCustomCode;
	}

	public void setThemeCustomCode(String themeCustomCode) {
		this.themeCustomCode = themeCustomCode;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getUserUA() {
		return userUA;
	}

	public void setUserUA(String userUA) {
		this.userUA = userUA;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
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

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getPaymentOrderNo() {
		return paymentOrderNo;
	}

	public void setPaymentOrderNo(String paymentOrderNo) {
		this.paymentOrderNo = paymentOrderNo;
	}

	public String getPaymentSysNo() {
		return paymentSysNo;
	}

	public void setPaymentSysNo(String paymentSysNo) {
		this.paymentSysNo = paymentSysNo;
	}

	public int getOrderExpDate() {
		return orderExpDate;
	}

	public void setOrderExpDate(int orderExpDate) {
		this.orderExpDate = orderExpDate;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getIndustryCatalog() {
		return industryCatalog;
	}

	public void setIndustryCatalog(String industryCatalog) {
		this.industryCatalog = industryCatalog;
	}

	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getMerchantOrderUrl() {
		return merchantOrderUrl;
	}

	public void setMerchantOrderUrl(String merchantOrderUrl) {
		this.merchantOrderUrl = merchantOrderUrl;
	}

	public String getFrontCallBackUrl() {
		return frontCallBackUrl;
	}

	public void setFrontCallBackUrl(String frontCallBackUrl) {
		this.frontCallBackUrl = frontCallBackUrl;
	}

	public Date getPayTime() {
		return payTime;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public String getParentMerchantNo() {
		return StringUtils.isBlank(parentMerchantNo)?merchantNo:parentMerchantNo;
	}

	public void setParentMerchantNo(String parentMerchantNo) {
		this.parentMerchantNo = parentMerchantNo;
	}

	public String getMerchantConfigFrom() {
		return merchantConfigFrom;
	}

	public void setMerchantConfigFrom(String merchantConfigFrom) {
		this.merchantConfigFrom = merchantConfigFrom;
	}

	public String getOrderExpDateType() {
		return orderExpDateType;
	}

	public void setOrderExpDateType(String orderExpDateType) {
		this.orderExpDateType = orderExpDateType;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	/**
	 * @return the appID
	 */
	public String getAppID() {
		return appID;
	}

	/**
	 * @param appID the appID to set
	 */
	public void setAppID(String appID) {
		this.appID = appID;
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
	
	/**
	 * 保存商户传的openId，最终支付使用的openId还是要取决于商户有没有传appId以及预路由的结果
	 * 
	 * @param openId
	 */
	public void setOpenIdToExtendInfo(String openId) {
		LOGGER.info("创建paymentRequest时openId={}", openId);
		if (StringUtils.isNotBlank(openId)) {
			JSONObject extendInfoJson = CommonUtil.parseJson(extendInfo);
			extendInfoJson.put("origOpenId", openId);
			this.setExtendInfo(extendInfoJson.toJSONString());
		}
	}
	
	/**
	 * 保存商户传的openId，最终支付使用的openId还是要取决于商户有没有传appId以及预路由的结果
	 * 
	 * @param openId
	 */
	public void setOpenIdToExtendInfo(String openId, JSONObject extendInfoJson) {
		if(extendInfoJson==null){
			setOpenIdToExtendInfo(openId);
		}
		else if (StringUtils.isNotBlank(openId)) {
			extendInfoJson.put("origOpenId", openId);
		}
	}
	
	/**
	 * @title 根据paymentRequest的数据构造paymentRecord
	 * @param userId
	 * @return
	 */
	public PaymentRecord buildPaymentRecord(String userId, String tmpCardId){
		PaymentRecord paymentRecord = new PaymentRecord();
		paymentRecord.setAreaInfo(areaInfo);
//		paymentRecord.setBankChannelNo(bankChannelNo);
		paymentRecord.setBankCode(bankCode); // 如果是银行卡分期，那就从银行卡分期的签约信息获取
//		paymentRecord.setBankOrderNo(bankOrderNo);
		paymentRecord.setBindId(StringUtils.isNotBlank(bindId) ? bindId : tmpCardId);
		paymentRecord.setBizModeCode(bizModeCode);
		paymentRecord.setCardInfoId(StringUtils.isNotBlank(tmpCardId) ? tmpCardId : cardInfoId); 
		paymentRecord.setCardNo(cardNo);
		paymentRecord.setCardType(cardType);
		paymentRecord.setCost(cost); 
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setIdCard(idCard); // 证件号 以paymentRequest为准
//		paymentRecord.setIdCardType(IdCardTypeEnum.IDENTITY.toString());
		paymentRecord.setMcc(industryCatalog);
		// 
		paymentRecord.setMemberNo(Constant.YIBAO.equals(memberType) ? memberNo : userId);
		paymentRecord.setMemberType(StringUtils.isNotBlank(memberType)? memberType:Constant.JOINLY);
		paymentRecord.setMerchantName(merchantName);
		paymentRecord.setMerchantNo(merchantNo);
		paymentRecord.setMerchantOrderId(merchantOrderId);
//		paymentRecord.setNeedItem(needItem);
		paymentRecord.setOrderOrderId(orderOrderId);
		paymentRecord.setOrderSysNo(orderSysNo);
		paymentRecord.setOwner(owner); // TODO 以paymentRequest为准
		paymentRecord.setPaymentAmount(orderAmount);
//		paymentRecord.setPaymentOrderNo(paymentOrderNo);
		paymentRecord.setPaymentRequestId(id);
//		paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
//		paymentRecord.setPayProductCode(payProductCode);
//		paymentRecord.setPayTool(payTool);
//		paymentRecord.setPayType(payType);
//		paymentRecord.setPeriod(period);
		paymentRecord.setPhoneNo(phoneNo);// 以paymentRequest为准
		paymentRecord.setProductName(productName);
//		paymentRecord.setRemark(remark);
//		paymentRecord.setSmsVerifyType(smsVerifyType);
//		paymentRecord.setState(state);
//		paymentRecord.setTokenId("");
//		paymentRecord.setTradeRiskInfo(tradeRiskInfo);
//		paymentRecord.setTradeSerialNo(tradeSerialNo);
		paymentRecord.setTradeSysNo(tradeSysNo);
		paymentRecord.setTradeSysOrderId(tradeSysOrderId);
		paymentRecord.setUpdateTime(new Date());
//		paymentRecord.setVerifyCode(verifyCode);
		paymentRecord.setVersion(1);
		paymentRecord.setState(PayRecordStatusEnum.INIT); 
		return paymentRecord;
	}

	public CashierUserInfo getCashierUser(){
		CashierUserInfo externalUser = new CashierUserInfo();
		externalUser.setMerchantNo(merchantNo);
		if(StringUtils.isBlank(identityId) || StringUtils.isBlank(identityType)){
			return externalUser;
		}
		externalUser.setUserNo(identityId);
		externalUser.setUserType(identityType);
		if(MemberTypeEnum.YIBAO.name().equals(identityType)){
			externalUser.setType(MemberTypeEnum.YIBAO.name());
		}else {
			externalUser.setType(MemberTypeEnum.JOINLY.name());
		}
		return externalUser;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	/**
	 * 优先获取orderTime,如为空，获取createTime，用于兼容新增字段orderTime上线完成前产生的老数据
	 * @return
	 */
	public Date getOrderTimeNotNull() {
		if(getOrderTime()!=null){
			return getOrderTime();
		}
		return getCreateTime();
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
}
