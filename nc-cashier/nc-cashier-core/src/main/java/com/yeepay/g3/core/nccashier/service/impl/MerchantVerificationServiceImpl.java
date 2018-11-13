/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import java.util.*;


import com.yeepay.g3.core.nccashier.entity.MerchantProductInfo;
import com.yeepay.g3.core.nccashier.service.BacLoadService;
import com.yeepay.g3.facade.nccashier.dto.MerchantProductDTO;
import com.yeepay.g3.utils.common.json.JSONUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.ConstantUtil;
import com.yeepay.g3.core.nccashier.vo.BankStagingInfo;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.core.nccashier.vo.MerchantConfigRequestParam;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.utils.jdbc.dal.utils.StringUtils;

import javax.annotation.Resource;

import static com.yeepay.g3.core.nccashier.utils.ConfigCenterUtils.isLoadSystem;

/**
 * @author xueping.ni
 *
 */
@Service("merchantVerificationService")
public class MerchantVerificationServiceImpl extends NcCashierBaseService implements
		MerchantVerificationService {
	
	private static Logger logger = LoggerFactory.getLogger(MerchantVerificationServiceImpl.class);

	@Resource
	private BacLoadService bacLoadService;

	private MerchantInNetConfigResult verifyMerchantAuthority(
			String merchantAccountCode, TransactionTypeEnum transactionType, CashierVersionEnum version,
			PayTool payTool, PayTypeEnum payType) {

		if (StringUtils.isBlank(merchantAccountCode) || null == version) {
			// 正常情况下，走不到这个分支
			logger.error("校验商户权限开通 merchantAccountCode:{},version:{}，至少一者为空，不合法",merchantAccountCode,version);
			return null;
		}
		MerchantConfigRequestParam merchantConfigRequestParam = new MerchantConfigRequestParam();
		merchantConfigRequestParam.setMerchantNo(merchantAccountCode);
		merchantConfigRequestParam.setTransactionType(transactionType);
		MerchantInNetConfigModel merchantConfig = merchantConfigService
				.getMerchantInNetConfig(merchantConfigRequestParam);
		if (merchantConfig == null) {
			// 只有产品未开通才会走到这里
			logger.warn("商户配置信息为空，merchantAccountCode:{},version:{},payTool:{},payType:{}", merchantAccountCode,version,payTool,payType);
			return null;
		} else {
			Map<CashierVersionEnum, List<Product>> productMap = merchantConfig
					.getProductMap();
			Map<String, List<String>> payToolAndPayTypeMap = new HashMap<String, List<String>>();
			Map<PayTool, String> paymentSceneMap = new HashMap<PayTool, String>();
			List<BankStagingInfo> bankStagingsOfPayTool = new ArrayList<BankStagingInfo>();
			List<String> allPayTypes = null;
			// 1校验收银台version
			if (MapUtils.isEmpty(productMap)) {
				// 只有产品未开通才会走到这里
				logger.warn("商户未开通任何产品,merchantAccountCode:{},version:{},payTool:{},payType:{}",merchantAccountCode,version,payTool,payType);
				return null;
			} else {
				//获取一级配置下的支付产品
				List<Product> products = productMap.get(version);
				if (CollectionUtils.isEmpty(products)) {
					logger.warn("商户一级配置下"+version+"商户未开通任何产品");
					return null;
				}
				
				for (Product product : products) {
					String payToolName = product.getPayTool().name();
					List<String> payTypes = product.getPayTypes();
					if(PayTool.EWALLETH5.name().equals(payToolName)){
						//特殊过滤： EWALLETH5 下的WECHAT_H5,需替换为 WECHAT_H5_WAP，以保持与现有内部枚举的兼容
						Iterator<String> iterator = payTypes.iterator();
						while (iterator.hasNext()){
							String payType1 = iterator.next();
							if("WECHAT_H5".equals(payType1)){
								iterator.remove();
								payTypes.add(PayTypeEnum.WECHAT_H5_WAP.name());
								break;
							}
						}
					}
					//获取支付工具
					payToolAndPayTypeMap.put(payToolName, product.getPayTypes());
					//获取支付类型
					allPayTypes = new ArrayList<String>();
					for (List<String> values : payToolAndPayTypeMap.values()) {
						allPayTypes.addAll(values);
					}
					//获取支付场景
					if (StringUtils.isNotEmpty(product.getPayScene())) {
						paymentSceneMap.put(product.getPayTool(),
								product.getPayScene());
					}
					// 设置bankStaging
					if (MapUtils.isNotEmpty(product.getBankStaging())) {
						BankStagingInfo bankStaging = product.buildBankStagingInfo();
						bankStagingsOfPayTool.add(bankStaging);
					}
					// 校验支付工具
					if (null != payTool && payTool.name().equals(payToolName)) {
						if (null == payType) {
							// 特殊的分支……貌似目前没有任何可能性会走到这里
							logger.info("二级支付工具校验成功，无需校验三级支付类型; version:{},payTool:{},payType:{}", version,payTool,payType);
							return buildConfigResult(merchantConfig,
									payToolAndPayTypeMap, version,
									paymentSceneMap, allPayTypes, bankStagingsOfPayTool);
						} else {
							// 校验支付类型
							if (CollectionUtils.isNotEmpty(payTypes)) {
								if (payTypes.contains(payType.name())) {
									return buildConfigResult(merchantConfig,
											payToolAndPayTypeMap, version,
											paymentSceneMap, allPayTypes, bankStagingsOfPayTool);
								}
							}else {
								// 这个分支理论上走不到
								logger.error("支付类型未配置");
								return null;
							}
						}

					}
				}
				
				if (null == payTool && null == payType && !CollectionUtils.isEmpty(products)) {
					// 特殊的分支……貌似目前没有任何可能性会走到这里
					logger.info("商户权限校验成功，不需要校验支付类型和支付工具");
					return buildConfigResult(merchantConfig, payToolAndPayTypeMap, version, paymentSceneMap,
							allPayTypes, bankStagingsOfPayTool);
				}
				// 这属于校验不通过的情况，需要打个日志
				logger.info("商户产品开通校验未通过 merchantAccountCode:{},version:{},payTool:{},payType:{}", merchantAccountCode,
						version, payTool, payType);
				return null;
			}
			
		}
	}

	private MerchantInNetConfigResult buildConfigResult(
			MerchantInNetConfigModel merchantConfig,
			Map<String, List<String>> payToolAndPayTypeMap,
			CashierVersionEnum version,
			Map<PayTool, String> paymentSceneMapmerchantConfig,
			List<String> allPayTypes,
			List<BankStagingInfo> bankStagingsOfPayTool) {
		MerchantInNetConfigResult configResult = new MerchantInNetConfigResult();
		configResult.setMerchantAccountCode(merchantConfig
				.getMerchantAccountCode());
		configResult.setVersion(version);
		configResult.setPayToolAndPayTypeMap(payToolAndPayTypeMap);
		configResult.setPayTypes(allPayTypes);
		configResult.setPaymentSceneMap(paymentSceneMapmerchantConfig);
		configResult.setMcc(merchantConfig.getMcc());
		configResult.setFeeType(merchantConfig.getFeeTypeEnum());
		configResult.setBankStagingOfPayTool(bankStagingsOfPayTool);
		return configResult;
	}

	@Override
	public MerchantInNetConfigResult verifyMerchantAuthority(VerifyProductOpenRequestParam verifyProductOpenRequestParam) {
		MerchantInNetConfigResult merchantInNetConfigResult = verifyMerchantAuthority(
				verifyProductOpenRequestParam.getMerchantNo(), verifyProductOpenRequestParam.getTransactionType(),
				verifyProductOpenRequestParam.getProductLevel().getVersion(),
				verifyProductOpenRequestParam.getProductLevel().getPayTool(),
				verifyProductOpenRequestParam.getProductLevel().getPayType());
		if (null == merchantInNetConfigResult) {
			throw CommonUtil.handleException(Errors.NOT_OPEN_PRODUCT_ERROR);
		}
		return merchantInNetConfigResult;
	}

	@Override
	public MerchantInNetConfigResult verifyMerchantAuthorityWithoutNotOpenError(
			VerifyProductOpenRequestParam verifyProductOpenRequestParam) {
		return verifyMerchantAuthority(verifyProductOpenRequestParam.getMerchantNo(),
				verifyProductOpenRequestParam.getTransactionType(),
				verifyProductOpenRequestParam.getProductLevel().getVersion(),
				verifyProductOpenRequestParam.getProductLevel().getPayTool(),
				verifyProductOpenRequestParam.getProductLevel().getPayType());
	}
	
	@Override
	public PayExtendInfo getMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest, PayExtendInfo info) {
		if (StringUtils.isNotBlank(paymentRequest.getMerchantConfigFrom()) && StringUtils.isNotBlank(tokenId)
				&& Constant.MERCHANT_CONFIG_FROM_USER_REQUEST.equals(paymentRequest.getMerchantConfigFrom())) {
			UserRequestInfo userRequestInfo = userRequestInfoDao.getUserRequestInfoBytoken(tokenId);
			if (userRequestInfo != null) {
				info.setCashierVersion(userRequestInfo.getCashierVersion());
				setPayExtendsInfoByJson(info, userRequestInfo.getMerchantConfigInfo(), paymentRequest.getExtendInfo(),paymentRequest.getOrderSysNo());
			} else {
				return null;
			}
		} else {
			info.setCashierVersion(paymentRequest.getCashierVersion());
			setPayExtendsInfoByJson(info, paymentRequest.getRemark(), paymentRequest.getExtendInfo(),paymentRequest.getOrderSysNo());
		}
		return info;
	}
	
	@Override
	public PayExtendInfo getMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest){
		PayExtendInfo info = new PayExtendInfo();
		getMerchantInNetConfig(tokenId, paymentRequest, info);
		if(info.getPayTool()==null || info.getPayTool().length==0){
			// 不是scanpay，也不是大算的场景
			String[] payToolList = parsePayTypeToPayTool(info.getPayTypes(), paymentRequest.getCashierVersion());
			info.setPayTool(payToolList);
		}
		return info;
	}

	private String[] parsePayTypeToPayTool(String payTypes, String cashierVersion){
		// 认为只有收银台1.0版本，且不是scanpay的场景会走到这里,也就是走到这里的要么是PC无卡收银台，要么是WAP TODO
		if(StringUtils.isBlank(payTypes)){
			logger.warn("parsePayTypeToPayTool 商户开通的payType为空");
			return null;
		}
		String[] payTypeList = (payTypes.replace("[", "").replace("]", "")).split(",");
		if(payTypeList==null){
			logger.warn("parsePayTypeToPayTool 商户开通的payType为空");
			return null;
		}
		if(payTypeList.length==1 && com.yeepay.g3.utils.common.StringUtils.isBlank(payTypeList[0])){
			// 这种情况且走WAP，开了NCPAY（一键支付）
			return new String[]{PayTool.NCPAY.name()};
		}
		Set<String> payToolSet = new HashSet<String>();
		for(String payType : payTypeList){
			if(com.yeepay.g3.utils.common.StringUtils.isBlank(payType)){
				continue;
			}
			int payTypeInt = Integer.parseInt(payType.trim());
			if(PayTypeEnum.ALIPAY_SCAN.value() == payTypeInt||PayTypeEnum.WECHAT_SCAN.value() == payTypeInt){
				continue;
			}
			String payTool = ConstantUtil.PAY_TYPE_MAPPING_TO_PAY_TOOL.get(payTypeInt);
			if(com.yeepay.g3.utils.common.StringUtils.isBlank(payTool) && CashierVersionEnum.WAP.name().equals(cashierVersion)){
				if(PayTypeEnum.WECHAT_OPENID.value() == payTypeInt){
					payTool = PayTool.WECHAT_OPENID.name();
				}else if(PayTypeEnum.ZFB_SHH.value() == payTypeInt){
					payTool = PayTool.ZFB_SHH.name();
				}else if (PayTypeEnum.ALIPAY.value() == payTypeInt) { 
					payTool = PayTool.EWALLET.name();
				}else if(PayTypeEnum.WECHAT_H5_WAP.value() == payTypeInt || PayTypeEnum.WECHAT_H5_LOW.value() == payTypeInt || PayTypeEnum.ALIPAY_H5_STANDARD.value() == payTypeInt){
					payTool = PayTool.EWALLETH5.name();
				}
			}
			if(StringUtils.isNotBlank(payTool)){
				payToolSet.add(payTool);
			}
		}
		if(CollectionUtils.isEmpty(payToolSet)){
			return new String[]{PayTool.NCPAY.name()};
		}
		String[] result = new String[payToolSet.size()];
		int i=0;
		for(String payTool : payToolSet){
			result[i++] = payTool;
		}
		return result;
	}


	@Override
	public void setPayExtendsInfoByJson(PayExtendInfo info, String merchantConfigInfo, String extendInfo,String bizSys) {
		if (StringUtils.isNotEmpty(merchantConfigInfo)) {
			try {
				if(isLoadSystem(bizSys)){	//充值系统
					List<MerchantProductDTO> list = JSONObject.parseArray(merchantConfigInfo,MerchantProductDTO.class);
					List<String> payToolList = new ArrayList<String>();
					List<Integer> payTypelList = new ArrayList<Integer>();
					for(MerchantProductDTO config : list){
						if(bacLoadService.isOpenNet(config)){
							payToolList.add(PayTool.LOAD_NET.name());
							payTypelList.add(PayTypeEnum.NET_BANK.value());
						}
						if(bacLoadService.isOpenRemit(config)){
							payToolList.add(PayTool.LOAD_REMIT.name());
							payTypelList.add(PayTypeEnum.NET_BANK.value());
						}
						if(bacLoadService.isOpenAccunt(config)){
							payToolList.add(PayTool.ZF_ZHZF.name());
							payTypelList.add(PayTypeEnum.ZF_ZHZF.value());
						}
					}
					String[] strings = new String[list.size()];
					payToolList.toArray(strings);
					info.setPayTool(strings);
					info.setPayTypes(JSONUtils.toJsonString(payTypelList));
				}else{
					JSONObject json = JSONObject.parseObject(merchantConfigInfo);
					String payTypes = json.getString("PayType");
					info.setPayTypes(payTypes);
					String directPayType = json.getString("DirectPayType");
					info.setDirectPayType(directPayType);
					String[] s = json.getObject("payTool", String[].class);
					if (s != null) {
						info.setPayTool(s);
					}
					info.setEwalletLevelWechat(json.getString("wechatH5preference"));
					info.setAlipayH5Priority(json.getString("alipayH5preference"));
				}
			} catch (JSONException e) {
				logger.warn("支付来源JSON转化异常", e);
			}
		}

		if (StringUtils.isNotEmpty(extendInfo)) {
			try {
				ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(extendInfo);
				JSONObject json = JSONObject.parseObject(extendInfo);
				info.setOrigAppId(json.getString("origAppId"));
				info.setTargetAppId(json.getString("targetAppId"));
				info.setSaleProductCode(json.getString("saleProductCode"));
				info.setAppSecert(AESUtil.aesDecrypt(json.getString("appSecret")));
				// add by meiling.zhuang：将商户（业务方）对接标准链接传入的openId字段对应的原始值
				info.setOrigOpenId(json.getString("origOpenId"));
				info.setSceneTypeExt(json.getString("sceneTypeExt"));
				info.setAliAppSecret(AESUtil.aesDecrypt(json.getString("aliAppSecret")));
				info.setTargetAliAppId(json.getString("targetAliAppId"));
				info.setOrigAlipayUserId(extendInfoFromPayRequest.getOrigAliUserId());
			} catch (JSONException e) {
				logger.warn("APPID JSON转化异常", e);
			}
		}
	}

	@Override
	public List<MerchantProductDTO> getNewMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest) {
		UserRequestInfo userRequestInfo = null;
		if (StringUtils.isNotBlank(paymentRequest.getMerchantConfigFrom()) && StringUtils.isNotBlank(tokenId)
				&& Constant.MERCHANT_CONFIG_FROM_USER_REQUEST.equals(paymentRequest.getMerchantConfigFrom())) {
			userRequestInfo = userRequestInfoDao.getUserRequestInfoBytoken(tokenId);
			if (userRequestInfo == null) {
				return null;
			}
		}
		List<MerchantProductDTO> list = null;
		String config = userRequestInfo.getMerchantConfigInfo();
		if(StringUtils.isNotBlank(config)){
			try{
				list = JSONUtils.jsonToList(config,MerchantProductDTO.class);
			}catch (Throwable e){
				logger.error("获取产品开通配置异常!",e);
				return null;
			}
		}
		return list;
	}

}
