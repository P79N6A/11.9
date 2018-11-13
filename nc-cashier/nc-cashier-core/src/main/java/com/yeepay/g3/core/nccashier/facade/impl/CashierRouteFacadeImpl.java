package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.biz.CashierRouteBiz;
import com.yeepay.g3.core.nccashier.gateway.service.YOPService;
import com.yeepay.g3.core.nccashier.interceptors.MethodInvokeLog;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.service.CashierRouteFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service("cashierRouteFacade")
public class CashierRouteFacadeImpl implements CashierRouteFacade {

	private static final Logger logger = LoggerFactory.getLogger(CashierRouteFacadeImpl.class);

	@Resource
	private CashierRouteBiz cashierRouteBiz;
	
	@Autowired
	private MethodInvokeLog methodInvokeLog;

	@Resource
	private YOPService yopService;

	@Override
	public CashierResultDTO receiptRequest(CashierRequestDTO cashierRequestDTO)
			throws CashierBusinessException {
		CashierResultDTO cashierResultDTO = null;
		try{
			validateRequest(cashierRequestDTO);// 校验必填项
			logger.info("[monitor],event:nccashier_receiptRequest_request,cashierRequestDTO:{}", cashierRequestDTO);
			NcCashierLoggerFactory.TAG_LOCAL.set("[下单|receiptRequest]—[tradeSysNo="+cashierRequestDTO.getTradeSysNo()+"merchantNo="+cashierRequestDTO.getMerchantNo()+"tradeSysOrderId="+cashierRequestDTO.getTradeSysOrderId()+"]");
			cashierResultDTO = cashierRouteBiz.receiptRequest(cashierRequestDTO);// 支付请求信息保存数据库
		}catch(CashierBusinessException e){
			methodInvokeLog.exceptionLog(e, "CashierRouteFacade.receiptRequest");
		}catch(Throwable e){//异常转换有问题不能抛IllegalArgumentException
			methodInvokeLog.exceptionLog(e, "CashierRouteFacade.receiptRequest");
		}
		logger.info("[monitor],event:nccashier_receiptRequest_response,cashierResultDTO:{}", cashierResultDTO);
		return cashierResultDTO;
	}


	private void validateRequest(CashierRequestDTO cashierRequestDTO) {
		BeanValidator.validate(cashierRequestDTO);
		if(cashierRequestDTO.getOrderAmount().compareTo(new BigDecimal(0))<=0){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "订单金额必须大于零");
		}
		if(null==cashierRequestDTO.getIndustryCatalog()&&StringUtils.isBlank(cashierRequestDTO.getGoodsCategoryCode())){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "商品类别码不能为空");
		}
		if(!"7".equals(cashierRequestDTO.getTradeSysNo())){
			if(StringUtils.isEmpty(cashierRequestDTO.getOrderOrderId())){
				throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "订单方订单号不能为空");
			}
			if(StringUtils.isEmpty(cashierRequestDTO.getOrderSysNo())){
				throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "订单方编码不能为空");
			}
		}
		if(StringUtils.isNotBlank(cashierRequestDTO.getMemberType())){
				if(StringUtils.isBlank(cashierRequestDTO.getMemberNo())){
					throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "商户类型存在时商户ID不能为空");	
				}
		}
	}


	@Override
	@Deprecated
	public String routeDecrypt(String merchantNo, String encRequestId) {
		return cashierRouteBiz.routeDecrypt(merchantNo, encRequestId);
	}

	@Override
	public String getURL(String cashierVer, String merchantNo, long id) {
		return cashierRouteBiz.getURL(cashierVer, merchantNo, id);
	}

	@Override
	public BussinessTypeResponseDTO routerPayWay(long requestId,String cusType) throws CashierBusinessException {
		return cashierRouteBiz.routerPayWay(requestId,cusType);
	}


	@Override
	public PayExtendInfo getPayExtendInfo(long requestId,String tokenId) {
		return cashierRouteBiz.getPayExtendInfo(requestId,tokenId);
	}

	@Override
	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId, String tokenId) {
		return cashierRouteBiz.getNewMerchantInNetConfig(requestId,tokenId);
	}


	@Override
	public NewOrderRequestResponseDTO newOrderRequest(String orderNo,int selectOrderSysNo) {
		return cashierRouteBiz.newOrderRequest(orderNo,selectOrderSysNo);
	}


	@Override
	public boolean yopVerify(String appKey, String plaintext, String signature) {
		return yopService.verify(appKey, plaintext, signature);
	}


	@Override
	public String yopSign(String signContent) {
		return yopService.sign(signContent);
	}

	@Override
	public OrderProcessResponseDTO orderProcessorRequest(OrderProcessorRequestDTO orderProcessorRequestDTO) {
		return cashierRouteBiz.orderProcessRequest(orderProcessorRequestDTO);
	}

}
