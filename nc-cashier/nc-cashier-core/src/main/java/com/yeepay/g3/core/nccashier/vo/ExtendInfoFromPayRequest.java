package com.yeepay.g3.core.nccashier.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.utils.common.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * paymentRequest中对应的extendInfo信息
 * 
 * @author duangduang
 * @date 2017-06-13
 */
public class ExtendInfoFromPayRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String orderType;
	
	private String productType;

	/**
	 * 零售产品码
	 */
	private String saleProductCode;
	
	/**
	 * 商户下单appId入参
	 */
	private String origAppId;
	
	/**
	 * 商户下单openId入参
	 */
	private String origOpenId;
	
	/**
	 * 商户下单透传的支付宝生活号userId
	 */
	private String origAliUserId;

	/**
	 * 商户下单透传的支付宝生活号appId
	 */
	private String origAliAppId;
	
	/**
	 * 账户支付扩展字段
	 */
	private Map<String, String> accountPayExt;
	
	/**
	 * 支付宝生活号预路由返回
	 */
	private String targetAliAppId;
	
	/**
	 * 微信公众号预路由返回
	 */
	private String targetAppId;
	
	/**
	 * 支付宝生活号预路由返回
	 */
	private String aliAppSecret;
	
	/**
	 * 微信公众号预路由返回
	 */
	private String appSecret;
	
	/**
	 * 银行通道成本（有用么？）
	 */
	private String bankTotalCost;
	
	/**
	 * 业务通道编码（商户下单传入，主扫、被扫、公众号、生活号支付时传给PP）
	 */
	private String specifyChannelCodes;

	/**
	 * 实体通道编码（预路由返回，公众号生活号支付时传给PP）
	 */
	private String payInterface;
	
	/**
	 * 二级商户号
	 */
	private String reportMerchantNo;

	/** 以下2个字段 粉丝路由新增字段 **/
	/**
	 * 哆啦宝粉丝路由功能新增字段
	 */
	private String reportId;

	/**
	 * 哆啦宝粉丝路由功能新增字段
	 */
	private String focusAppId;

	
	/**
	 * 微信预路由返回 有必要存么
	 */
	private String sceneTypeExt;
	
	/** 以下三个字段 微信H5支付所需 **/
    
    private String platForm; 
	private String appName;
	private String appStatement;
	
	/** 以下字段 为非银行卡支付专用 **/
	
	private String goodsKind;
	
	private String goodsDesc;
	
	private String goodsExt;
	
	private String callBackUrl;
	
	/** 以下字段为授权所需 **/
	private String smsStatus;
	
	private String basicProductCode;


	/**
	 * 透传的银行卡号
	 */
	private String idCard;

	/**
	 * 透传的姓名
	 */
	private String owner;

	/** 以下字段为 区域编码-银联二维码被扫必填 **/
	private String areaCode;

	private String groupTag;

	/**
	 * 报备费率，用于聚合支付区分线上线下
	 */
	private String reportFee;
	
	/**
	 * 目前该参数是从opr反查接口查得的，用以做银行卡分期补贴费率查询的计费项
	 */
	private String productVersion;
	
	/**
	 * 该字段作为冗余字段，解决问题：https://blog.csdn.net/u013008179/article/details/78904395
	 */
	private String ext;

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

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public ExtendInfoFromPayRequest(){
		
	}
	
	public String getBasicProductCode() {
		return basicProductCode;
	}

	public void setBasicProductCode(String basicProductCode) {
		this.basicProductCode = basicProductCode;
	}



	public String getSaleProductCode() {
		return saleProductCode;
	}

	public void setSaleProductCode(String saleProductCode) {
		this.saleProductCode = saleProductCode;
	}

	public String getOrigAppId() {
		return origAppId;
	}

	public void setOrigAppId(String origAppId) {
		this.origAppId = origAppId;
	}

	public String getOrigOpenId() {
		return origOpenId;
	}

	public void setOrigOpenId(String origOpenId) {
		this.origOpenId = origOpenId;
	}

	public Map<String, String> getAccountPayExt() {
		return accountPayExt;
	}

	public void setAccountPayExt(Map<String, String> accountPayExt) {
		this.accountPayExt = accountPayExt;
	}
	
	
	public String getOrigAliUserId() {
		return origAliUserId;
	}

	public void setOrigAliUserId(String origAliUserId) {
		this.origAliUserId = origAliUserId;
	}

	public String getOrigAliAppId() {
		return origAliAppId;
	}

	public void setOrigAliAppId(String origAliAppId) {
		this.origAliAppId = origAliAppId;
	}
	
	public String getTargetAliAppId() {
		return targetAliAppId;
	}

	public void setTargetAliAppId(String targetAliAppId) {
		this.targetAliAppId = targetAliAppId;
	}
	
	public String getTargetAppId() {
		return targetAppId;
	}

	public void setTargetAppId(String targetAppId) {
		this.targetAppId = targetAppId;
	}
	

	public String getAliAppSecret() {
		return aliAppSecret;
	}

	public void setAliAppSecret(String aliAppSecret) {
		this.aliAppSecret = aliAppSecret;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	

	public String getBankTotalCost() {
		return bankTotalCost;
	}

	public void setBankTotalCost(String bankTotalCost) {
		this.bankTotalCost = bankTotalCost;
	}

	public String getPayInterface() {
		return payInterface;
	}

	public void setPayInterface(String payInterface) {
		this.payInterface = payInterface;
	}

	public String getReportMerchantNo() {
		return reportMerchantNo;
	}

	public void setReportMerchantNo(String reportMerchantNo) {
		this.reportMerchantNo = reportMerchantNo;
	}

	public String getSceneTypeExt() {
		return sceneTypeExt;
	}

	public void setSceneTypeExt(String sceneTypeExt) {
		this.sceneTypeExt = sceneTypeExt;
	}
	
	public String getPlatForm() {
		return platForm;
	}

	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppStatement() {
		return appStatement;
	}

	public void setAppStatement(String appStatement) {
		this.appStatement = appStatement;
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
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getSmsStatus() {
		return smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getFocusAppId() {
		return focusAppId;
	}

	public void setFocusAppId(String focusAppId) {
		this.focusAppId = focusAppId;
	}

	/**
	 * 解析json串，反序列化为ExtendInfoFromPayRequest对象
	 * 
	 * @param json
	 * @return
	 */
	public static ExtendInfoFromPayRequest getFromJson(String json) {
		JSONObject jsonObject = CommonUtil.parseJson(json);
		if(jsonObject==null){
			return new ExtendInfoFromPayRequest();
		}
		return jsonObject.toJavaObject(ExtendInfoFromPayRequest.class);
	}


	
	
	@Override
	public String toString(){
		return JSON.toJSONString(this, propertyFilter);
	}
	
	public static void main(String[] args){
		ExtendInfoFromPayRequest extendInfoFromPayRequest = new ExtendInfoFromPayRequest();
		extendInfoFromPayRequest.setOrigAliAppId("1233");
		System.out.println(extendInfoFromPayRequest.toString());
	}
	
	
	PropertyFilter propertyFilter = new PropertyFilter() {
		
		@Override
		public boolean apply(Object object, String name, Object value) {
			if(value instanceof String){
				String val = (String)value;
				if(StringUtils.isNotBlank(val)){
					return true;
				}
			}
			// TODO 有int 之类的
			else if(value != null){
				return true;
			}
			return false;
		}
	};
	

	public String getSpecifyChannelCodes() {
		return specifyChannelCodes;
	}

	public void setSpecifyChannelCodes(String specifyChannelCodes) {
		this.specifyChannelCodes = specifyChannelCodes;
	}

	public String getReportFee() {
		return reportFee;
	}

	public void setReportFee(String reportFee) {
		this.reportFee = reportFee;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}
	
}
