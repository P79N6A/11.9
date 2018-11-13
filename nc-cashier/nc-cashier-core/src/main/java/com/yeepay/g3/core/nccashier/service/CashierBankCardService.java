/**
 * 
 */
package com.yeepay.g3.core.nccashier.service;

import java.math.BigDecimal;
import java.util.List;

import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.core.nccashier.vo.InstallmentBankInfo;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.result.CardBinDTO;

/**
 * @author zhen.tan
 * @since：2016年5月25日 下午6:26:58
 *
 */
public interface CashierBankCardService {

	/**
	 * 获取卡必填项信息
	 * @param response
	 * @param requestDto
	 * @throws CashierBusinessException
	 */
	public void getCardValidates(CardValidateResponseDTO response,CardValidateRequestDTO requestDto)
			throws CashierBusinessException;
	
	/**
	 * 校验并获取银行支持列表
	 * @param requestId
	 * @param response
	 */
	public void validateAndGetSupportBankList(long requestId,SupportBanksResponseDTO response, String cusType);
	/**
	 * 获取支付业务类型
	 * @param paymentRequest
	 * @param 
	 * @return
	 */
	public void getPayType(PaymentRequest paymentRequest,BussinessTypeResponseDTO bussinessTypeResponseDTO,String cusType);

	/**
	 * 获取银行卡信息
	 * @param bankCardResquestDTO
	 * @param bankCardReponseDTO
	 */
	public void getBankCardInfo(long requestId,
			BankCardReponseDTO bankCardReponseDTO,String cusType);

	/**
	 * 校验卡信息必填项
	 * @param response
	 * @param requestDto
	 * @throws CashierBusinessException
	 */
	public boolean validateCardNeed(CardInfoDTO cardInfo, PaymentRequest payRequest,
			PaymentRecord record) throws CashierBusinessException;
	
	
	
	/**
	 * 获取透传信息
	 * @param paymentRequest
	 * @return
	 */
	public PassCardInfoDTO getPassCardInfo(PaymentRequest paymentRequest);
	

	/**
	 * 获取卡bin信息
	 * @param cardno
	 * @return
	 */
	public CardBinDTO getCardBinInfo(String cardno);

	/**
	 * 授权成功后对返回的结果进行排序
	 * @param list
	 * @param paymentRequest
	 */
	public void shareBindCardResultFilter(List<BindCardDTO> list,PaymentRequest paymentRequest,ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO, String cusType);

	public String changeBankCode(String bankCode);
	
	/**
	 * 校验绑卡限制信息
	 * 
	 * @param person
	 * @param bindLimitInfoResDTO
	 */
	void checkBindLimitInfo(Person person, BindLimitInfoResDTO bindLimitInfoResDTO);
	
	/**
	 * 获取非空cardBin，若为空则抛异常
	 * @param cardno
	 * @return
	 */
	CardInfo getNonNullCardBin(String cardno);
	
	/**
	 * 获取卡bin信息
	 * 
	 * @param cardno
	 * @return
	 */
	CardInfo getCardBin(String cardno);
	
	/**
	 * 过滤银行卡分期的银行
	 * 
	 * @param merchantNo
	 * @param orderAmount
	 * @return
	 */
	InstallmentBanksResponseDTO filterSupportInstallmentBankList(String merchantNo, BigDecimal orderAmount);
	
	List<InstallmentBankInfo> getSupportBankList(String merchantNo);
	
	List<InstallmentBankInfo> getNonNullInstallmentBankList(String merchantNo);
	
	/**
	 * 1，获取配置中心配置的银行卡分期的银行及期数信息，请注意其他信息不一定有； 
	 * 2，若银行卡分期支持的银行及期数为空，则抛异常；
	 * 
	 * @param merchantNo
	 * @return
	 */
	MerchantInNetConfigResult getInstallmentBankConfig(String merchantNo);
	/**
	 * 预授权交易：获取已绑卡信息，不处理透传信息
	 * @param requestId
	 * @param bankCardReponseDTO
	 * @param cusType
	 */
	void getBankCardInfo4Preauth(long requestId, BankCardReponseDTO bankCardReponseDTO, String cusType);


	/**
	 * 透传绑卡id校验
	 * @param bindId
	 * @param userNo
	 * @param userType
	 * @param merchantNo
     * @return
     */
	public String validatePassBindId(long requestId);


	}
