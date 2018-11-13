package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.facade.ncconfig.param.ConfigAuthPayParam;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.biz.APIPersonalMemberBiz;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.MerchantVerificationService;
import com.yeepay.g3.core.nccashier.service.NewOrderHandleService;
import com.yeepay.g3.core.nccashier.service.PersonalMemberService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigResult;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.PersonalMemberBanlancePayRequestInfo;
import com.yeepay.g3.core.nccashier.vo.ProductLevel;
import com.yeepay.g3.core.nccashier.vo.VerifyProductOpenRequestParam;
import com.yeepay.g3.facade.nccashier.dto.APIBasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.APIMemberBalancePayRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;

import java.util.Map;

@Service("apiPersonalMemberBiz")
public class APIPersonalMemberBizImpl extends APIBaseBiz implements APIPersonalMemberBiz {

	@Resource
	private NewOrderHandleService newOrderHandleService;

	@Resource
	private MerchantVerificationService merchantVerificationService;

	@Resource
	private PersonalMemberService personalMemberService;

	@Resource
	private NcConfigService ncConfigService;

	// 会员支付
	private static final ProductLevel productLevel = new ProductLevel(CashierVersionEnum.API, PayTool.GRHYZF,
			PayTypeEnum.GRHYZF);

	@Override
	public APIBasicResponseDTO balancePay(APIMemberBalancePayRequestDTO requestDTO) {
		APIBasicResponseDTO responseDTO = new APIBasicResponseDTO();
		try {
			// 参数校验
			validateBalancePayParam(requestDTO);
			// 反查订单
			OrderDetailInfoModel orderInfo = newOrderHandleService.queryOrder(requestDTO.getMerchantNo(),
					requestDTO.getToken(), requestDTO.getBizType(), TransactionTypeEnum.PREAUTH);
			// 商户配置校验
			VerifyProductOpenRequestParam requestParam = new VerifyProductOpenRequestParam();
			requestParam.setMerchantNo(orderInfo.getMerchantAccountCode());
			requestParam.setProductLevel(productLevel);
			requestParam.setTransactionType(orderInfo.getTransactionType());
			MerchantInNetConfigResult merchantInNetConfig = merchantVerificationService.verifyMerchantAuthority(requestParam);

			//存在付款商编，则跨商编支付
			if (StringUtils.isNotBlank(requestDTO.getPayMerchantNo())) {
				//是否授权跨商编支付
				ConfigAuthPayParam configAuthPayParam = new ConfigAuthPayParam();
				configAuthPayParam.setPayMerchantNo(requestDTO.getPayMerchantNo());
				configAuthPayParam.setParentMerchantNo(orderInfo.getParentMerchantNo());
				ncConfigService.hasAuthPay(configAuthPayParam);

			}

			// 进行会员支付
			this.balancePay(requestDTO, orderInfo, merchantInNetConfig);
		} catch (Throwable t) {
			String bizType = (requestDTO == null ? null : requestDTO.getBizType());
			handleException(bizType, responseDTO, t);
		}
		supplyResponse(responseDTO, requestDTO);
		return responseDTO;
	}

	private void balancePay(APIMemberBalancePayRequestDTO requestDTO, OrderDetailInfoModel orderInfo,
			MerchantInNetConfigResult merchantInNetConfig) {
		PersonalMemberBanlancePayRequestInfo requestInfo = buildPersonalMemberBanlancePayRequestInfo(requestDTO, orderInfo);
		personalMemberService.balancePay(requestInfo, orderInfo, merchantInNetConfig, productLevel);
	}

	/**
	 * 构造前置收银台会员余额支付特定的入参信息
	 * 
	 * @param requestDTO
	 * @return
	 */
	private PersonalMemberBanlancePayRequestInfo buildPersonalMemberBanlancePayRequestInfo(
			APIMemberBalancePayRequestDTO requestDTO, OrderDetailInfoModel orderInfo) {
		//判断是否存在付款商编，根据付款商编查询会员号
		String payMerchantNo = orderInfo.getMerchantAccountCode();
		if (StringUtils.isNotBlank(requestDTO.getPayMerchantNo())){
			payMerchantNo = requestDTO.getPayMerchantNo();
		}
		String memberNo = personalMemberService.queryValidMemberNo(payMerchantNo,requestDTO.getMerchantUserNo());
		PersonalMemberBanlancePayRequestInfo requestInfo = new PersonalMemberBanlancePayRequestInfo();

        String extParamJson = requestDTO.getExtInfo();
        if(StringUtils.isNotBlank(extParamJson)){
			Map<String,String> extParam = null;
            try {
                extParam = JSONObject.parseObject(extParamJson,Map.class);
				requestInfo.setExtParam(extParam);
            }catch (Exception e){
                throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg()+"，extInfo格式无效");
            }
        }
		requestInfo.setMemberNo(memberNo);
		requestInfo.setUserIp(requestDTO.getUserIp());
		//授权支付所属商编
		if (StringUtils.isNotBlank(requestDTO.getPayMerchantNo())) {
			requestInfo.setPayMerchantNo(payMerchantNo);
		}
		return requestInfo;
	}

	private void validateBalancePayParam(APIMemberBalancePayRequestDTO requestDTO) {
		if (requestDTO == null) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ", 请求入参不能为空");
		}
		requestDTO.validate();
		// 商编的转化 —— 将OPR：等前缀去掉
		requestDTO.setMerchantNo(CommonUtil.formatMerchantNo(requestDTO.getMerchantNo()));
		NcCashierLoggerFactory.TAG_LOCAL.set("[balancePay],token=" + requestDTO.getToken() + "]");
	}

}
