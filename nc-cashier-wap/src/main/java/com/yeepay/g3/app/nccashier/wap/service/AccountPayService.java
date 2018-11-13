package com.yeepay.g3.app.nccashier.wap.service;

import org.springframework.validation.BindingResult;

import com.yeepay.g3.app.nccashier.wap.vo.AccountPayRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.AccountPayResponseVO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

/**
 * 账户支付wap服务层
 * 
 * @author duangduang
 * @date 2017-06-01
 */
public interface AccountPayService {

	/**
	 * 账户支付支付下单接口
	 * 
	 * @param param
	 * @return
	 */
	AccountPayResponseVO accountPay(AccountPayRequestVO param, RequestInfoDTO requestInfo);

	/**
	 * 校验验证码
	 * 
	 * @param captcha
	 * @param token
	 * @return
	 */
	boolean validateCaptcha(String captcha, String token);

	/**
	 * 校验会员支付入参
	 * 
	 * @param param
	 * @return
	 */
	RequestInfoDTO validateAccountPayParam(AccountPayRequestVO param, BindingResult bindingResult);
	
	void checkExpireTime(String expireTime);
}
