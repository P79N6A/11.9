
package com.yeepay.g3.core.payprocessor.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.yeepay.g3.core.payprocessor.entity.ExtendedInfo;
import com.yeepay.g3.core.payprocessor.util.CommonUtils;
import com.yeepay.g3.core.payprocessor.util.log.PayLoggerFactory;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年2月17日 下午4:20:33
 */
public class ExtendedInfoHandler implements TypeHandler {
    
    private static final Logger logger = PayLoggerFactory.getLogger(ExtendedInfoHandler.class);

	/**
	 * 布尔，风控返回的是否短验
	 */
	public static final String USER_ID = "USER_ID";

	/**
	 * 布尔，是否要发送扣款通知短信
	 */
	private static final String USER_TYPE = "USER_TYPE";
	
	private static final String BANK_TRADE_ID = "BANK_TRADE_ID";
	
	private static final String COUPON_INFO = "COUPON_INFO";
	
	private static final String PAYER_BANKACCOUNT_NO = "PAYER_BANKACCOUNT_NO";
	
	private static final String REPORT_MERCHANT_NO = "REPORT_MERCHANT_NO";
	
	/**
	 * 账户支付扩展信息
	 */
	private static final String ACCOUNT_EXTEND_INFO = "ACCOUNT_EXT_INFO";
	/**
	 * 钱包高低配版本标记
	 */
	private static final String WALLET_LEVEL = "WALLET_LEVEL";

	/**
	 * 扩展字段
	 */
	private static final String EXT_PARAM = "EXT_PARAM";

	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
		if (parameter != null) {
			if (parameter instanceof ExtendedInfo) {
				ps.setString(i, transferResultToJsonStr((ExtendedInfo) parameter));
			} else if (parameter instanceof String) {
				ps.setString(i, parameter.toString());
			}
		} else {
			ps.setString(i, "");
		}

	}

	/**
	 * @param parameter
	 * @return
	 */
	private String transferResultToJsonStr(ExtendedInfo parameter) {
		if (parameter == null) {
			return "";
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(parameter.getUserId())) {
			resultMap.put(USER_ID, parameter.getUserId());
		}
		if (StringUtils.isNotBlank(parameter.getUserType())) {
			resultMap.put(USER_TYPE, parameter.getUserType());
		}
		if (StringUtils.isNotBlank(parameter.getBankTradeId())) {
			resultMap.put(BANK_TRADE_ID, parameter.getBankTradeId());
		}
		if (StringUtils.isNotBlank(parameter.getCouponInfo())) {
			resultMap.put(COUPON_INFO, parameter.getCouponInfo());
		}
		if (StringUtils.isNotBlank(parameter.getPayerBankAccountNo())) {
			resultMap.put(PAYER_BANKACCOUNT_NO, CommonUtils.encrypt(parameter.getPayerBankAccountNo()));
		}
		if (StringUtils.isNotBlank(parameter.getReportMerchantNo())) {
			resultMap.put(REPORT_MERCHANT_NO, parameter.getReportMerchantNo());
		}
		if(MapUtils.isNotEmpty(parameter.getAccountPayExtInfo())) {
		    resultMap.put(ACCOUNT_EXTEND_INFO, JSONObject.toJSONString(parameter.getAccountPayExtInfo()));
		}
		if(StringUtils.isNotBlank(parameter.getWalletLevel())) {
		    resultMap.put(WALLET_LEVEL, parameter.getWalletLevel());
		}
		if(MapUtils.isNotEmpty(parameter.getExtParam())) {
			resultMap.put(EXT_PARAM, JSONObject.toJSONString(parameter.getExtParam()));
		}
		return JSONObject.toJSONString(resultMap);

	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		String jsonStr = rs.getString(columnName);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		ExtendedInfo entity = transferResult(jsonStr);
		return entity;
	}

	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonStr = cs.getString(columnIndex);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		ExtendedInfo entity = transferResult(jsonStr);
		return entity;
	}

	public Object getResult(ResultSet rs, int arg1) throws SQLException {
		String jsonStr = rs.getString(arg1);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		ExtendedInfo entity = transferResult(jsonStr);
		return entity;
	}

	private ExtendedInfo transferResult(String result) {
		if (StringUtils.isBlank(result)) {
			return null;
		}
		ExtendedInfo extendedInfo = new ExtendedInfo();
		try {
			Map<String, String> resultMap = JSONObject.parseObject(result, Map.class);
			// 兼容老数据
			String userId = resultMap.get(USER_ID);
			if (StringUtils.isNotBlank(userId)) {
				extendedInfo.setUserId(userId);
			}

			String userType = resultMap.get(USER_TYPE);
			if (StringUtils.isNotBlank(userType)) {
				extendedInfo.setUserType(userType);
			}
			String bankTradeId = resultMap.get(BANK_TRADE_ID);
			if (StringUtils.isNotBlank(bankTradeId)) {
				extendedInfo.setBankTradeId(bankTradeId);
			}
			String couponInfo = resultMap.get(COUPON_INFO);
			if (StringUtils.isNotBlank(couponInfo)) {
				extendedInfo.setCouponInfo(couponInfo);
			}
			String payerBankAccountNo = resultMap.get(PAYER_BANKACCOUNT_NO);
			if (StringUtils.isNotBlank(payerBankAccountNo)) {
				extendedInfo.setPayerBankAccountNo(CommonUtils.decrypt(payerBankAccountNo));
			}
			String reportMerchantNo = resultMap.get(REPORT_MERCHANT_NO);
			if (StringUtils.isNotBlank(reportMerchantNo)) {
				extendedInfo.setReportMerchantNo(reportMerchantNo);
			}			
			String accountExtendInfo = resultMap.get(ACCOUNT_EXTEND_INFO);
			if (StringUtils.isNotBlank(accountExtendInfo)) {
				extendedInfo.setAccountPayExtInfo((Map<String, String>)JSONObject.parse(accountExtendInfo));
			}
			String walletLevel = resultMap.get(WALLET_LEVEL);
			if (StringUtils.isNotBlank(walletLevel)) {
			    extendedInfo.setWalletLevel(walletLevel);
			}
			String extParam = resultMap.get(EXT_PARAM);
			if (StringUtils.isNotBlank(extParam)) {
				extendedInfo.setExtParam((Map<String, String>) JSONObject.parse(extParam));
			}
		} catch (Exception e) {
			logger.error("mybatis 解析扩展字段失败" , e);
		}
		return extendedInfo;
	}

}
