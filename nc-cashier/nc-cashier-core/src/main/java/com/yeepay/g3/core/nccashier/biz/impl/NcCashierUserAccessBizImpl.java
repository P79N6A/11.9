package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.NcCashierUserAccessBiz;
import com.yeepay.g3.core.nccashier.dao.UserRequestInfoDao;
import com.yeepay.g3.core.nccashier.entity.ParamShowInfo;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UserAccount;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.UserRequestInfoService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.MerchantConfigInfo;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NcCashierUserAccessBizImpl extends NcCashierBaseBizImpl
		implements NcCashierUserAccessBiz {

	Logger logger = NcCashierLoggerFactory.getLogger(NcCashierUserAccessBizImpl.class);

	@Autowired
	private  UserRequestInfoDao userRequestInfoDao;

	@Autowired
	private UserRequestInfoService userRequestInfoService;

	@Override
	public RequestInfoDTO requestBaseInfo(String tokenId) {
		RequestInfoDTO response = new RequestInfoDTO();
		try {
			if (StringUtils.isBlank(tokenId)) {
				throw new IllegalArgumentException("tokenId is null");
			}
			NcCashierLoggerFactory.TAG_LOCAL
					.set("[查询用户访问记录|requestBaseInfo] - [tokenId=" + tokenId + "]");
			UserAccount userAccount = userProceeService.getUserAccountInfo(tokenId);
			UserRequestInfo userRequestInfo = userRequestInfoService.getUserRequestInfoByTokenId(tokenId);
			if (userAccount != null) {
				BeanUtils.copyProperties(userAccount.getParamShowInfo(), response);
				if(StringUtils.isNotEmpty(userAccount.getPaymentRecordId())){
					response.setPaymentRecordId(Long.valueOf(userAccount.getPaymentRecordId()));
				}
				response.setTradeTime(userAccount.getParamShowInfo().getCreateTime());
				response.setMerchantNo(userAccount.getMerchantNo());
				response.setParentMerchantNo(userAccount.getParamShowInfo().getParentMerchantNo());
				response.setPaymentRequestId(userAccount.getPaymentRequestId());
				response.setTradeSysNo(userAccount.getTradeSysNo());
				response.setOrderSysNo(userAccount.getOrderSysNo());
				response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
				if(userAccount.getParamShowInfo()!=null){
					response.setPaySysCode(userAccount.getParamShowInfo().getPaySysCode());
				}
				if(userRequestInfo != null) {
					if (StringUtils.isNotBlank(userRequestInfo.getCashierVersion())) {
						response.setCashierVersionEnum(CashierVersionEnum.valueOf(userRequestInfo.getCashierVersion()));
					}
					if(StringUtils.isNotBlank(userRequestInfo.getMerchantConfigInfo())){
						MerchantConfigInfo merchantConfigInfo = MerchantConfigInfo.getFromJson(userRequestInfo.getMerchantConfigInfo());
						if(merchantConfigInfo!=null){
							response.setPayTools(merchantConfigInfo.getPayTool());
						}
					}
					OrderProcessorRequestDTO urlParamInfo = userRequestInfoService.getUrlParamFromUserRequestInfo(userRequestInfo);
					if (urlParamInfo != null) {
						response.setUrlParamInfo(urlParamInfo);
					}
				}
				response.setIndustryCatalog(userAccount.getParamShowInfo().getIndustryCatalog());
				//非银行支付增加以下四个参数
				response.setGoodsKind(userAccount.getParamShowInfo().getGoodsKind());
				response.setGoodsDesc(userAccount.getParamShowInfo().getGoodsDesc());
				response.setGoodsExt(userAccount.getParamShowInfo().getGoodsExt());
				response.setCallBackUrl(userAccount.getParamShowInfo().getCallBackUrl());
			}else{
				throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
			}
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	@Override
	public UserAccessResponseDTO saveUserAccount(UserAccessDTO userAccessDTO) {
		UserAccessResponseDTO response = new UserAccessResponseDTO();
		try {
			BeanValidator.validate(userAccessDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[保存用户访问记录|saveUserAccess] - [paymentRequestId="
					+ userAccessDTO.getPaymentRequestId() + "]");
			PaymentRequest payRequest = paymentRequestService
					.findPayRequestById(Long.valueOf(userAccessDTO.getPaymentRequestId()));
			if (payRequest == null) {
				throw CommonUtil.handleException(Errors.PAY_REQUEST_NULL);
			}
			ParamShowInfo rInfo = paymentProcessService.makeRequestInfoDTO(payRequest);
			UserAccount uaccount = makeUserAccountInfo(userAccessDTO, payRequest, rInfo);
			userProceeService.saveUserAccount(uaccount);
			//保存用户请求信息
			if(StringUtils.isNotBlank(userAccessDTO.getMerchantConfigInfo())) {
				userRequestInfoDao.saveUserRequestInfo(buildUserRequestInfo(userAccessDTO));
			}
			response.setPaymentRequestId(uaccount.getPaymentRequestId());
			response.setUserAccessId(uaccount.getId());
			response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;

	}

	@Override
	public BasicResponseDTO clearRecordId(String tokenId) {
		BasicResponseDTO response = new BasicResponseDTO();
		try {
			if (StringUtils.isBlank(tokenId)) {
				throw new IllegalArgumentException("tokenId is null");
			}
			NcCashierLoggerFactory.TAG_LOCAL
					.set("[清空支付记录|clearRecordId] - [tokenId=" + tokenId + "]");

			UserAccount userAccount = userProceeService.getUserAccountInfo(tokenId);
			if (null != userAccount && StringUtils.isNotBlank(userAccount.getPaymentRecordId())) {
				userAccount.setPaymentRecordId(null);
				userAccount.setUpdateTime(new Date());
				userProceeService.updateUserAccount(userAccount);
			}
		} catch (Throwable e) {
			handleException(response, e);
		}
		return response;
	}

	private UserAccount makeUserAccountInfo(UserAccessDTO userAccessDTO, PaymentRequest payRequest,
			ParamShowInfo rInfo) {
		UserAccount userAccount = new UserAccount();
		userAccount.setTokenId(userAccessDTO.getTokenId());
		userAccount.setUserIp(userAccessDTO.getUserIp());
		userAccount.setUserUa(userAccessDTO.getUserUa());
		userAccount.setPaymentRequestId(Long.parseLong(userAccessDTO.getPaymentRequestId()));
		userAccount.setParamShowInfo(rInfo);
		userAccount.setMerchantNo(payRequest.getMerchantNo());
		userAccount.setTradeSysOrderId(payRequest.getTradeSysOrderId());
		userAccount.setTradeSysNo(payRequest.getTradeSysNo());
		userAccount.setOrderOrderId(payRequest.getOrderOrderId());
		userAccount.setOrderSysNo(payRequest.getOrderSysNo());
		userAccount.setCreateTime(new Date());
		userAccount.setUpdateTime(new Date());
		return userAccount;
	}

	private UserRequestInfo buildUserRequestInfo(UserAccessDTO userAccessDTO){
		UserRequestInfo userRequestInfo = new UserRequestInfo();
		userRequestInfo.setCashierVersion(userAccessDTO.getCashierVersionEnum().name());
		userRequestInfo.setMerchantConfigInfo(userAccessDTO.getMerchantConfigInfo());
		userRequestInfo.setUrlParamInfo(userAccessDTO.getUrlParamInfo());
		userRequestInfo.setTokenId(userAccessDTO.getTokenId());
		userRequestInfo.setCreateTime(new Date());
		userRequestInfo.setUpdateTime(new Date());

		return userRequestInfo;
	}


}
