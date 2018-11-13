package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.app.nccashier.wap.service.AccountPayService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.utils.BeanValidate;
import com.yeepay.g3.app.nccashier.wap.utils.RedisTemplate;
import com.yeepay.g3.app.nccashier.wap.vo.AccountPayRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.AccountPayResponseVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.CashierAccountPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.AccountPayValidateResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.enumtype.TimeTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;


/**
 * 账户支付服务实现类
 * 
 * @author duangduang
 * @date 2017-06-01
 */
@Service("accountPayService")
public class AccountPayServiceImpl implements AccountPayService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccountPayServiceImpl.class);
	
	@Autowired
	private NcCashierService ncCashierService;

	@Autowired
	private NewWapPayService newWapPayService;

	@Override
	public AccountPayResponseVO accountPay(AccountPayRequestVO param, RequestInfoDTO requestInfo) {
		// 权限和交易密码校验
		AccountPayValidateRequestDTO validateDTO = buildAccountPayValidateRequestDTO(param);
		AccountPayValidateResponseDTO responseDTO = ncCashierService.accountPayValidate(validateDTO);

		// 商户账户信息校验成功，去支付
		if (ProcessStatusEnum.SUCCESS.equals(responseDTO.getProcessStatusEnum())) {
			CashierAccountPayRequestDTO payDTO = buildAccountPayRequestDTO(param, requestInfo, responseDTO.getDebitCustomerNo());
			ncCashierService.accountPay(payDTO);
			return new AccountPayResponseVO(Constant.SUCCESS);
		}

		// 校验失败，返回密码剩余重试次数和冻结时间
		return buildAccountPayResponseVO(responseDTO);

	}

	/**
	 * 构造请求core 账户支付支付接口的入参
	 * 
	 * @param param
	 * @param requestInfo
	 * @return
	 */
	private CashierAccountPayRequestDTO buildAccountPayRequestDTO(AccountPayRequestVO param, RequestInfoDTO requestInfo, String debitCustomerNo) {
		CashierAccountPayRequestDTO requestDTO = new CashierAccountPayRequestDTO();
		requestDTO.setRecordId(requestInfo.getPaymentRecordId());
		requestDTO.setRequestId(requestInfo.getPaymentRequestId());
		requestDTO.setTokenId(param.getToken());
		requestDTO.setUserAccount(param.getUserAccount());
		requestDTO.setDebitCustomerNo(debitCustomerNo);
		return requestDTO;
	}

	/**
	 * 构造请求core 账户支付校验接口的入参
	 * 
	 * @param param
	 * @return
	 */
	private AccountPayValidateRequestDTO buildAccountPayValidateRequestDTO(AccountPayRequestVO param) {
		AccountPayValidateRequestDTO requestDTO = new AccountPayValidateRequestDTO();
		BeanUtils.copyProperties(param, requestDTO);
		return requestDTO;
	}

	/**
	 * 返回给页面
	 * 
	 * @param responseDTO
	 * @return
	 */
	private AccountPayResponseVO buildAccountPayResponseVO(AccountPayValidateResponseDTO responseDTO) {
		AccountPayResponseVO responseVO = new AccountPayResponseVO();
		BeanUtils.copyProperties(responseDTO, responseVO);
		responseVO.setErrorcode(responseDTO.getReturnCode());
		responseVO.setErrormsg(responseDTO.getReturnMsg());
		if (responseDTO.getFrozenTime()!=null && responseDTO.getFrozenTime() > 0) {
			String timeType = responseDTO.getFrozenTimeType() == null ? TimeTypeEnum.MINUTE.getDesc()
					: responseDTO.getFrozenTimeType().getDesc();
			responseVO.setFrozenTime(responseDTO.getFrozenTime() + timeType);
		}
		responseVO.setBizStatus(Constant.FAIL);
		return responseVO;
	}

	@Override
	public boolean validateCaptcha(String captcha, String token) {
		String captchaInRedis = RedisTemplate.getTargetFromRedis(Constant.VALIDATE_CODE_REDIS_KEY + token,
				String.class);
		if (StringUtils.isBlank(captchaInRedis)) {
			return false;
		}
		RedisTemplate.delCacheObject(Constant.VALIDATE_CODE_REDIS_KEY + token);
		if (captchaInRedis.equalsIgnoreCase(captcha)) {
			return true;
		}
		return false;
	}

	@Override
	public RequestInfoDTO validateAccountPayParam(AccountPayRequestVO param, BindingResult bindingResult) {
		// 入参非空校验
		BeanValidate.validate(param, bindingResult);
		// 校验验证码
		boolean result = validateCaptcha(param.getCaptcha(), param.getToken());
		if(!result){
			throw new CashierBusinessException(Errors.ACCOUNT_PAY_CAPTCHA_ERROR);
		}
		// token校验
		return newWapPayService.validateRequestInfoDTO(param.getToken());
	}

	@Override
	public void checkExpireTime(String expireTime) {
		if(StringUtils.isBlank(expireTime)){
			return;
		}
		// 有效期
		Date validateDate;
		try {
			validateDate = DateUtils.parseDate(expireTime, "yyyy-MM-dd HH:mm");
		} catch (ParseException e) {
			LOGGER.error("转换有效期异常,expireTime=" + expireTime, e);
			return;
		}
		long currentTime = new Date().getTime();
		long validateTime = validateDate.getTime();
		if(validateTime < currentTime){
			throw new CashierBusinessException(Errors.THRANS_EXP_DATE);
		};
	}
	
	

}
