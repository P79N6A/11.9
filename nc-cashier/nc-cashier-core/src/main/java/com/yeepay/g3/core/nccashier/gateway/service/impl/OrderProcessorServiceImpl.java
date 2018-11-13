/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.enumtype.TransactionTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.OrderProcessorService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.OrderDetailInfoModel;
import com.yeepay.g3.core.nccashier.vo.TransparentCardInfo;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.enumtype.CurrencyEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.opr.dto.order.OrderDTO;
import com.yeepay.g3.facade.opr.enumtype.OrderStatusEnum;

import org.springframework.stereotype.Service;

/**
 * @author zhen.tan
 *
 */
@Service
public class OrderProcessorServiceImpl extends NcCashierBaseService implements OrderProcessorService {

	@Override
	public OrderDetailInfoModel getOrderDetailInfo(String token) {
		OrderDTO dto = orderFacade.queryOrderByToken(token);
		if(dto != null){
			if("OPR00000".equals(dto.getCode())){
				if(dto.getStatus() == OrderStatusEnum.CLOSE){
					throw CommonUtil.handleException(Errors.OPR_CANCELED_ERROR);
				}
				if(dto.getStatus() == OrderStatusEnum.CANCEL){
					throw CommonUtil.handleException(Errors.OPR_CANCELED_ERROR);
				}
				if(dto.getStatus() == OrderStatusEnum.SUCCESS){
					throw CommonUtil.handleException(Errors.THRANS_FINISHED);
				}
				OrderDetailInfoModel infoModel = new OrderDetailInfoModel();
				infoModel.setOrderSysNo("DS");
				infoModel.setUniqueOrderNo(dto.getUniqueOrderNo());
				infoModel.setMerchantAccountCode(dto.getMerchantNo());
				infoModel.setMerchantName(dto.getMerchantName());
				infoModel.setMerchantOrderId(dto.getOrderId());
				infoModel.setOrderAmount(dto.getOrderAmount());
				infoModel.setFrontCallBackUrl(dto.getRedirectUrl());
				infoModel.setOrderExpDate(dto.getTimeoutExpress());
				infoModel.setOrderTime(dto.getRequestDate());
				infoModel.setCurrency(CurrencyEnum.CNY);
				//DS业务方映射ncpay业务方 modified by yangmin.peng
				infoModel.setProductOrderCode(CommonUtil.getBizMappingConfigDsToNcpay(dto.getBizSystemNo()));
				String paymentParamExt = dto.getPaymentParamExt();
				JSONObject jsonObject = CommonUtil.parseJson(paymentParamExt);
				infoModel.setNotBankPaycallBackUrl(jsonObject.getString("callBackUrl"));
				TransparentCardInfo cardInfo = new TransparentCardInfo();
				cardInfo.setCardNo(jsonObject.getString("bankCardNo"));
				cardInfo.setIdcard(jsonObject.getString("idCardNo"));
				cardInfo.setOwner(jsonObject.getString("cardName"));
				infoModel.setCardInfo(cardInfo);
				infoModel.setOrderType(jsonObject.getString("orderType"));
				infoModel.setAreaCode(jsonObject.getString("areaCode"));
				infoModel.setGroupTag(jsonObject.getString("groupTag"));
				infoModel.setBindId(jsonObject.getString("bindId"));
				// 账户支付所需的扣款商编（目前只有前置收银台取这个值）
				infoModel.setAccountPayMerchantNo(jsonObject.getString("accountPayMerchantNo"));
				JSONObject goodsInfoJson  =  CommonUtil.parseJson(dto.getGoodsParamExt());
				infoModel.setProductName(goodsInfoJson.getString("goodsName"));
				infoModel.setProductKind(goodsInfoJson.getString("goodsKind"));
				infoModel.setProductDesc(goodsInfoJson.getString("goodsDesc"));
				infoModel.setOrderExtendInfo(goodsInfoJson.getString("goodsExt"));
				infoModel.setSaleProductCode(dto.getSalesProductCode());
				infoModel.setParentMerchantNo(dto.getParentMerchantNo());
				infoModel.setTradeRiskInfo(dto.getRiskParamExt());//add by xueping.ni 
				if(dto.getTimeoutExpressType()!=null) {
					infoModel.setOrderExpDateType(dto.getTimeoutExpressType().name());
				}
				JSONObject riskParamExt  =  CommonUtil.parseJson(dto.getRiskParamExt());
				infoModel.setReffer(riskParamExt.getString("referer"));
				//modified by yangming.peng 20170527支持大算用户手续费：收银台写死收费项DSBZB
				infoModel.setCallFeeItem(Constant.DS_USER_FEE_ITEM);
				infoModel.setSignedMerchantAccountCode(dto.getCompareMerchantNo());
				infoModel.buildOtherParamExt(dto.getInfoParamExt());
				infoModel.setTransactionType(TransactionTypeEnum.valueByName(dto.getTradeType()));
				if(TransactionTypeEnum.PREAUTH == infoModel.getTransactionType()){
					// 预授权状态
					infoModel.setCurrentPreauthStatus(dto.getPreAuthStatus()==null?null:dto.getPreAuthStatus().name());
					// 预授权发起金额
					infoModel.setPreauthAmount(dto.getPreAuthAmount());
				}
				// modify by meiling.zhuang 20180830 获取银行卡分期补贴的计费项
				infoModel.setProductVersion((dto.getProductVersion()==null)?null:dto.getProductVersion().name());
				return infoModel;
			}else{
				throw CommonUtil.handleException(SysCodeEnum.OPR.name(), dto.getCode(), dto.getMessage());
			}
		}else{
			throw CommonUtil.handleException(Errors.SYSTEM_INPUT_EXCEPTION);
		}
	}
	
}
