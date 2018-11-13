package com.yeepay.g3.core.nccashier.service.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-10-18 11:42
 **/

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.OrderAction;
import com.yeepay.g3.core.nccashier.enumtype.SynTypeEnum;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.ClfEasyService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.PaymentProcessService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.vo.CardInfo;
import com.yeepay.g3.core.nccashier.vo.PersonHoldCard;
import com.yeepay.g3.core.nccashier.vo.RecordCondition;
import com.yeepay.g3.facade.cwh.enumtype.IdcardType;
import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayRecordStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.common.NCPayParamMode;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.facade.payprocessor.dto.*;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-10-18 11:42
 **/

@Service("clfEasyService")
public class ClfEasyServiceImpl extends NcCashierBaseService implements ClfEasyService {

    @Resource
    private PaymentProcessService paymentProcessService;


    @Resource
    private PayProcessorService payProcessorService;


    /**
     * 通过condition获取Record
     * @param paymentRequest
     * @param recordId
     * @param payTool
     * @param orderAction
     * @param recordPayTypes
     * @return
     */
    public PaymentRecord getPaymentRecord(PaymentRequest paymentRequest, String recordId,PayTool payTool,OrderAction orderAction,String recordPayTypes[]){
        RecordCondition condition = buildRecordCondition(paymentRequest, recordId, orderAction, recordPayTypes,payTool);
        PaymentRecord paymentRecord = paymentProcessService.getNonNullPaymentRecord(recordId, condition);
        return paymentRecord;
    }

	private RecordCondition buildOrderRecordCondit(String cardNo, String period, String tradeSysOrderId, String tradeSysNo) {
		RecordCondition recordCondition = new RecordCondition();
		recordCondition.setCardN0(cardNo);
		recordCondition.setPeriod(period);
		recordCondition.setPayTool(PayTool.ZF_FQY.name());
		recordCondition.setTradeSysOrderId(tradeSysOrderId);
		recordCondition.setTradeSysNo(tradeSysNo);
		return recordCondition;
	}
	
	private PersonHoldCard buildPersonHoldCard(CardInfo cardInfo) {
		PersonHoldCard person = new PersonHoldCard();
		person.setCard(cardInfo);
		return person;
	}
	
    public PaymentRecord getRecordToOrder(CardInfo cardInfo, String period, String token, PaymentRequest paymentRequest){
		RecordCondition condition = buildOrderRecordCondit(cardInfo.getCardNo(), period, paymentRequest.getTradeSysOrderId(), paymentRequest.getTradeSysNo());
		PersonHoldCard person = buildPersonHoldCard(cardInfo);
		PaymentRecord paymentRecord = paymentProcessService.createPaymentRecord(paymentRequest, condition, person,
				com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum.ZF_FQY_NORMAL.name(), null, token, cardInfo.getTmpId(),null);
		return paymentRecord;
	}
    
    public NeedSurportDTO order(PaymentRequest paymentRequest, PaymentRecord paymentRecord, CardInfoDTO cardInfo){
    	//构建调用PP入参，调用PP下单接口
        String riskInfo = buildTradeRiskInfoByUseripAndRequest(paymentRequest.getUserIp(), paymentRequest);
        NcCflEasyRequestDTO ncCflEasyRequestDTO = buildNcCflEasyRequestDTO(paymentRequest, paymentRecord, CardInfoTypeEnum.TEMP ,riskInfo, cardInfo);
        NcCflEasyResponseDTO ncCflEasyResponseDTO = payProcessorService.cflEasyCreatePayment(ncCflEasyRequestDTO);
        //更新record，并处理返回结果
        paymentProcessService.updateRecordNo(paymentRecord.getId(), ncCflEasyResponseDTO.getSmsType().name(), ncCflEasyResponseDTO.getRecordNo(),
                PayRecordStatusEnum.ORDERED, ncCflEasyResponseDTO.getNeedItem(), "NONE");
        paymentRecord.setSmsVerifyType(ncCflEasyResponseDTO.getSmsType().name());
        //处理补充项（取款密码）、是否需短验
        return handlerSMSTypeAndNeedItem(ncCflEasyResponseDTO.getNeedItem());
      
    }

    @Override
    public NcCflEasyConfirmResponseDTO confirmPay(CardInfoDTO cardInfoDTO, String verifyCode, PaymentRequest paymentRequest, PaymentRecord paymentRecord, SynTypeEnum synTypeEnum) {
        PayRecordStatusEnum origPayRecordStatusEnum = paymentRecord.getState();
        try {
            paymentProcessService.avoidRepeatPayWithException(paymentRecord, new PayRecordStatusEnum[]{PayRecordStatusEnum.ORDERED, PayRecordStatusEnum.SMS_SEND});
            NcCflEasyConfirmRequestDTO confirmDTO = new NcCflEasyConfirmRequestDTO();
            confirmDTO.setRecordNo(paymentRecord.getPaymentOrderNo());
            confirmDTO.setSmsCode(verifyCode);
            NcCflEasyConfirmResponseDTO responseDTO = payProcessorService.clfEasyConfirmPay(confirmDTO);
            return responseDTO;
        } catch (CashierBusinessException e) {
            String errorCode = e.getDefineCode();
            if ("N400094".equals(errorCode) || "N400091".equals(errorCode)) {
                paymentProcessService.recoverRecordToObjStatus(paymentRecord.getId(), origPayRecordStatusEnum, PayRecordStatusEnum.PAYING);
            }
            throw e;
        }
    }

    /**
	 * 下单后处理短验类型、补充项、提交补充项场景等
	 *
     * @param smsType
	 * @param needItemNum
	 * @param responseDTO
	 *            当cardInfoTypeEnum=TEMP时，需APIYJZFFirstPaymentResponseDTO；
	 *            当cardInfoTypeEnum=BIND时，需APIYJZFBindPaymentResponseDTO
	 */
	private NeedSurportDTO handlerSMSTypeAndNeedItem(int needItemNum) {
		if (0 == needItemNum) {
			return null;
		}
		NeedSurportDTO needSurportDTO = new NeedSurportDTO();
		NCPayParamMode nCPayParamMode = new NCPayParamMode(needItemNum);
		needSurportDTO.setIdnoIsNeed(nCPayParamMode.needIdCardNumber());
		needSurportDTO.setOwnerIsNeed(nCPayParamMode.needUserName());
		needSurportDTO.setPhoneNoIsNeed(nCPayParamMode.needBankMobilePhone());
		needSurportDTO.setCvvIsNeed(nCPayParamMode.needCvv());
		needSurportDTO.setAvlidDateIsNeed(nCPayParamMode.needAvlidDate());
		needSurportDTO.setBankPWDIsNeed(nCPayParamMode.needBankPWD());
		return needSurportDTO;
    }
    
    /**
     * 构造pp下单请求
     */
    private NcCflEasyRequestDTO buildNcCflEasyRequestDTO(PaymentRequest paymentRequest,PaymentRecord payRecord,
                                                           CardInfoTypeEnum type,String riskInfo,CardInfoDTO cardInfo) throws CashierBusinessException {
        NcCflEasyRequestDTO param = new NcCflEasyRequestDTO();
        param.setGoodsInfo(riskInfo);
        buildBasicRequestDTO(paymentRequest,param);
        param.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));
        param.setPayProduct(PayTool.ZF_FQY.name());
        param.setBizType(Long.valueOf(paymentRequest.getOrderSysNo()));
        param.setPayScene(paymentRequest.getBizModeCode());
        param.setPayOrderType(PayOrderType.CFL_EASY);
        param.setMemberType(MemberTypeEnum.valueOf(payRecord.getMemberType()));
        param.setMemberNO(payRecord.getMemberNo());
        param.setCardInfoType(type);
        param.setCardInfoId(StringUtils.isNotBlank(payRecord.getBindId()) ? Long.parseLong(payRecord.getBindId()) : null);
        param.setPayTool(payRecord.getPayProductCode());
        param.setCflCount(payRecord.getPeriod());
        if (CardInfoTypeEnum.TEMP.equals(type) && cardInfo != null) {
        		cardInfo.setIdType(IdcardType.ID.name());
            param.setBankCardInfoDTO(cardInfo.transferPayProcessBankCardInfoDTO());
        }
		/*设置零售产品码和基础产品码*/
        param.setBasicProductCode(CommonUtil.getBasicProductCode(param.getPayProduct(), paymentRequest.getTradeSysNo()));
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        param.setRetailProductCode(jsonObject.getString("saleProductCode"));
        return param;
    }

}
