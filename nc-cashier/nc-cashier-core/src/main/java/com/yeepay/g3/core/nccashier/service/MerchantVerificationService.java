/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.dto.MerchantProductDTO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;

import java.util.List;
import java.util.Map;

/**
 * 商户权限校验服务
 * 
 * @author xueping.ni
 *
 */
public interface MerchantVerificationService {

	/**
	 * 校验产品开通（未开通抛异常）
	 * 
	 * @param verifyProductOpenRequestParam
	 * @return
	 */
	MerchantInNetConfigResult verifyMerchantAuthority(VerifyProductOpenRequestParam verifyProductOpenRequestParam);

	/**
	 * 校验产品开通（未开通不抛异常）
	 * 
	 * @param verifyProductOpenRequestParam
	 * @return
	 */
	MerchantInNetConfigResult verifyMerchantAuthorityWithoutNotOpenError(
			VerifyProductOpenRequestParam verifyProductOpenRequestParam);
	
	PayExtendInfo getMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest);
	
	PayExtendInfo getMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest, PayExtendInfo info);
	
	void setPayExtendsInfoByJson(PayExtendInfo info, String merchantConfigInfo, String extendInfo,String bizSys);


	List<MerchantProductDTO> getNewMerchantInNetConfig(String tokenId, PaymentRequest paymentRequest);



}
