package com.yeepay.g3.core.nccashier.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.dao.UserProcessDao;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.UserProceeService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * Created by xiewei on 15-10-21.
 */
@Service("userProceeService")
public class UserProcessServiceImpl implements UserProceeService {

	private static Logger logger = NcCashierLoggerFactory.getLogger(UserProcessServiceImpl.class);

	@Resource
	private UserProcessDao userProcessDao;

	@Override
	public long saveUserAccount(UserAccount userAccount) {
		long id = userProcessDao.saveUserAccount(userAccount);
		setRequsetInfoIntoRedis(userAccount.getTokenId(), userAccount);
		return id;
	}

	@Override
	public UserAccount getUserAccountInfo(String tokenId) {
		UserAccount userAccount = getUserAccountFromRedis(tokenId);
		if (userAccount == null) {
			userAccount = getUserAccountBytoken(tokenId);
		}
		return userAccount;
	}

	private UserAccount getUserAccountBytoken(String tokenId) {
		Map map = new HashMap();
		map.put("tokenId", tokenId);
		UserAccount userAccount = userProcessDao.getUserAccountBytoken(map);
		if(userAccount != null){
			setRequsetInfoIntoRedis(tokenId, userAccount);
		}
		
		return userAccount;
	}


	private void setRequsetInfoIntoRedis(String tokenId, UserAccount userAccount) {
		try {
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_USERACCOUNT_REDIS_KEY + tokenId,
					userAccount, Constant.NCCASHIER_USERACCOUNT_REDIS_TIME_LIMIT);
		} catch (Exception e) {
			logger.error("更新redis用户缓存信息失败,userAccount:"+userAccount.toString(),e);
		}
	}
	
	private UserAccount getUserAccountFromRedis(String tokenId){
		try {
			UserAccount userAccount = RedisTemplate.getTargetFromRedis(
					Constant.NCCASHIER_USERACCOUNT_REDIS_KEY + tokenId, UserAccount.class);
			return userAccount;
		} catch (Exception e) {
			logger.error("获取redis用户缓存信息失败,tokenId:{},错误信息:{}", tokenId, e);
			return null;
		}
	}

	public void updateUserAccount(UserAccount userAccount) {		
		try {
			userProcessDao.updateUserAccountRecordId(userAccount);
		} catch (Exception e) {
			// 更新失败发生的概率极低
			logger.error("更新数据库用户缓存信息失败,userAccount:"+userAccount.toString(), e);
			throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
		}
		
		setRequsetInfoIntoRedis(userAccount.getTokenId(),userAccount);
	}

	@Override
	public void getAndUpdatePaymentRecordId(String tokenId, String recordId) {
		UserAccount userAccount = getUserAccountInfo(tokenId);
		userAccount.setPaymentRecordId(recordId);
		userAccount.setUpdateTime(new Date());
		updateUserAccount(userAccount);
	}
}
