/**
 * 
 */
package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantInfoService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.APIMerchantScanService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.ExtendInfoFromPayRequest;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.APIMerchantScanPayDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.facade.payprocessor.enumtype.ProcessStatus;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yp-tc-m-2768
 *
 */
@Service("apiMerchantScanService")
public class APIMerchantScanServiceImpl extends NcCashierBaseService implements APIMerchantScanService {
	@Resource
	private PayProcessorService payProcessorService;
	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private PaymentProcessService paymentProcessService;
	@Override
	public void callPP2CreateOrder(APIMerchantScanPayDTO request,
			PaymentRequest paymentRequest, PaymentRecord record) {
		PassiveScanPayRequestDTO passiveSanrequest = new PassiveScanPayRequestDTO();
		buildBasicRequestDTO(paymentRequest,passiveSanrequest);
		buildMerchantScanPay(request,paymentRequest,record,passiveSanrequest);
		PassiveScanPayResponseDTO response = payProcessorService.merchantScanPay(passiveSanrequest);
		updateRecordInfo(response,record);
		
	}
	private void updateRecordInfo(PassiveScanPayResponseDTO response,
			PaymentRecord record) {
		if(null !=response){
			if(response.getProcessStatus() == ProcessStatus.FAILED){
				record.setErrorCode(response.getResponseCode());
				record.setErrorMsg(response.getResponseMsg());
			}else{
				record.setPaymentOrderNo(response.getRecordNo());
				record.setBankChannelNo(response.getBankId());
				record.setBankCode(response.getBankId());
				record.setTradeSerialNo(response.getBankTrxId());
				record.setBankOrderNo(response.getBankOrderNo());
				record.setState(PayRecordStatusEnum.SUCCESS);
				paymentProcessService.updateRecord(record);
			}
		}
	}
	private void buildMerchantScanPay(APIMerchantScanPayDTO request,
			PaymentRequest paymentRequest,PaymentRecord record,
			PassiveScanPayRequestDTO passiveSanrequest) {
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		String riskInfo = buildTradeRiskInfoByTooluseripAndRequest(request.getUserIp(),paymentRequest);
		passiveSanrequest.setMerchantStoreNo(request.getStoreCode());
		passiveSanrequest.setMerchantTerminalId(request.getDeviceSn());
		passiveSanrequest.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
		passiveSanrequest.setBasicProductCode(CommonUtil.getBasicProductCode(record.getPayTool(),paymentRequest.getTradeSysNo()));
//		modify by xueping.ni 来客需求修改所有商户等级为V类
		passiveSanrequest.setCustomerLevel(Constant.CUSTOMER_LEVEL_V);
		passiveSanrequest.setDeviceInfo(null);
		passiveSanrequest.setGoodsInfo(riskInfo);
		passiveSanrequest.setPayBusinessType(PayBusinessType.DC);
		passiveSanrequest.setPayEmpowerNo(request.getCode());
		passiveSanrequest.setRetailProductCode(jsonObject.getString("saleProductCode"));
		passiveSanrequest.setPayOrderType(PayOrderType.PASSIVESCAN);
		passiveSanrequest.setPayerIp(request.getUserIp());
		passiveSanrequest.setPayProduct(record.getPayTool());
		if(PayTypeEnum.ALIPAY_SCAN.name().equals(record.getPayType())){
			passiveSanrequest.setPlatformType(PlatformType.ALIPAY);
		}else if(PayTypeEnum.WECHAT_SCAN.name().equals(record.getPayType())){
			passiveSanrequest.setPlatformType(PlatformType.WECHAT);
		}else if(PayTypeEnum.UPOP_PASSIVE_SCAN.name().equals(record.getPayType())){
			passiveSanrequest.setPlatformType(PlatformType.OPEN_UPOP);
		}else{
			passiveSanrequest.setPlatformType(null);
		}
		ExtendInfoFromPayRequest extendInfoFromPayRequest = ExtendInfoFromPayRequest.getFromJson(paymentRequest.getExtendInfo());
		if(StringUtils.isNotBlank(extendInfoFromPayRequest.getAreaCode())){
			Map<String, String> extParam = new HashMap<String, String>();
			extParam.put(Constant.AREA_CODE,extendInfoFromPayRequest.getAreaCode());//区域编码，放在extParam传入PP
			passiveSanrequest.setExtParam(extParam);
		}
	}


}
