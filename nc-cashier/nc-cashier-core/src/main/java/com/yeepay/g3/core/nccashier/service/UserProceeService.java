package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.UserAccount;
/**
 * 用户信息缓存服务
 * Created by xiewei on 15-10-21.
 */
public interface UserProceeService {

    /**
     * 保存用户信息:1.保存到redis 2.保存到数据库
     * @param userAccount
     * @return
     */
    public long saveUserAccount(UserAccount userAccount);

    /**
     * 获取用户缓存信息:1.先从redis获取 2.reidis获取失败再从数据库中获取
     * @param tokenId
     * @return
     */
	public UserAccount getUserAccountInfo(String tokenId);
	
	/**
	 * 根据tokenId更新支付记录id
	 * 
	 * @param tokenId
	 * @param recordId
	 */
	public void getAndUpdatePaymentRecordId(String tokenId, String recordId);
	
	public void updateUserAccount(UserAccount userAccount);

}
