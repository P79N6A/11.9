package com.yeepay.g3.app.nccashier.wap.service.impl;

import com.yeepay.g3.app.nccashier.wap.service.BankInstallmentService;
import com.yeepay.g3.app.nccashier.wap.service.GuaranteeInstallmentService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardItemNecessary;
import com.yeepay.g3.app.nccashier.wap.vo.InstallmentBankResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.UrlInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentCardNoCheckResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPaymentResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.guarantee.GuaranteeInstallmentPrePayResponseVO;
import com.yeepay.g3.facade.nccashier.dto.CardBinInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.InstallmentRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CardTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class GuaranteeInstallmentServiceImpl implements GuaranteeInstallmentService {

    @Autowired
    private NcCashierService ncCashierService;

    @Resource
    private BankInstallmentService bankInstallmentService;



    @Override
    public GuaranteeInstallmentPrePayResponseVO prePay(RequestInfoDTO info, String bankCode, String period, String token) {
        GuaranteeInstallmentPrePayRequestDTO requestDTO = buildPrePayRequestDTO(info, bankCode, period, token);
        GuaranteeInstallmentPrePayResponseDTO guaranteeInstallmentPrePayResponseDTO = ncCashierService.guaranteeInstallmentPrePay(requestDTO);
        return transferPrePayResponseVO(guaranteeInstallmentPrePayResponseDTO);
    }

    @Override
    public GuaranteeInstallmentPaymentResponseVO requestPayment(RequestInfoDTO info, GuaranteeInstallmentPaymentRequestVO requestVO) {
        GuaranteeInstallmentPaymentRequestDTO requestDTO = buildPaymentRequestDTO(info, requestVO);
        GuaranteeInstallmentPaymentResponseDTO guaranteeInstallmentPrePayResponseDTO = ncCashierService.guaranteeInstallmentRequestPayment(requestDTO);
        return transferPaymentResponseVO(guaranteeInstallmentPrePayResponseDTO);
    }

    @Override
    public InstallmentBankResponseVO getSupportBankAndPeriods(long requestId) {
        InstallmentRouteResponseDTO response = ncCashierService.guaranteeInstallmentgetSupportBankAndPeriods(requestId);
        if (CollectionUtils.isEmpty(response.getUsableBankList())) {
            throw new CashierBusinessException(Errors.CASHIER_CONFIG_BANKS_NULL);
        }
        return bankInstallmentService.transferToBankInfoVOList(response.getUsableBankList());
    }

    @Override
    public GuaranteeInstallmentCardNoCheckResponseVO checkCardNo(RequestInfoDTO info, String cardNo, String bankCode) {
        GuaranteeInstallmentCardNoCheckResponseVO responseVO = new GuaranteeInstallmentCardNoCheckResponseVO();
        CardBinInfoDTO cardBinInfo = ncCashierService.getCardBinInfo(cardNo);
        if (ProcessStatusEnum.SUCCESS != cardBinInfo.getProcessStatusEnum()) {
            //卡bin查询不到此卡号
            throw new CashierBusinessException(cardBinInfo.getReturnCode(), cardBinInfo.getReturnMsg());
        }
        if(!cardBinInfo.getBank().equals(bankCode)){
            //卡号查到的银行与前端选择不符
            throw new CashierBusinessException(Errors.BANK_AND_CARD_INFO_MATCH);
        }
        //设置卡类型
        CardTypeEnum cardType = cardBinInfo.getCardTypeEnum();
        if(CardTypeEnum.CREDIT.equals(cardType)){
            responseVO.setCardType("信用卡");
        }else {
            throw new CashierBusinessException(Errors.PLZ_CREDIT_PAY);
        }
        return responseVO;
    }

    /**
     * 构建GuaranteeInstallmentPrePayRequestDTO入参
     *
     * @param info
     * @param bankCode
     * @param period
     * @param token
     * @return
     */
    private GuaranteeInstallmentPrePayRequestDTO buildPrePayRequestDTO(RequestInfoDTO info, String bankCode, String period, String token) {
        GuaranteeInstallmentPrePayRequestDTO requestDTO = new GuaranteeInstallmentPrePayRequestDTO();
        requestDTO.setRequestId(info.getPaymentRequestId());
        requestDTO.setBankCode(bankCode);
        requestDTO.setPeriod(Integer.parseInt(period));
        requestDTO.setToken(token);
        return requestDTO;
    }

    /**
     * 转换GuaranteeInstallmentPrePayResponseVO返回值
     *
     * @param responseDTO
     * @return
     */
    private GuaranteeInstallmentPrePayResponseVO transferPrePayResponseVO(GuaranteeInstallmentPrePayResponseDTO responseDTO) {
        GuaranteeInstallmentPrePayResponseVO responseVO = new GuaranteeInstallmentPrePayResponseVO();
        //银行、期数、金额、费率等
        responseVO.setBankCode(responseDTO.getBankCode());
        responseVO.setPeriod(Integer.toString(responseDTO.getPeriod()));
        responseVO.setOrderAmount(responseDTO.getOrderAmount().toString());
        //注：费率需乘以100，用于前端展示百分比数字(responseDTO中费率精度为4位小数，如0.0123、0.0200，乘100后需重新设置精度为2，以展示形如1.23%、2.00%的费率)
        responseVO.setServiceChargeRate(responseDTO.getServiceChargeRate().multiply(new BigDecimal(100)).setScale(2,RoundingMode.DOWN).toString());
        responseVO.setServiceCharge(responseDTO.getServiceCharge().toString());
        responseVO.setAmountPerPeriod(responseDTO.getAmountPerPeriod().toString());
        //补充项
        responseVO.setNeedSupply(responseDTO.getNeedSupply());
        CardItemNecessary cardItemNecessary = new CardItemNecessary();
        cardItemNecessary.setNeedOwner(responseDTO.isNeedOwner());
        cardItemNecessary.setNeedIdNo(responseDTO.isNeedIdNo());
        cardItemNecessary.setNeedAvlidDate(responseDTO.isNeedAvlidDate());
        cardItemNecessary.setNeedCvv(responseDTO.isNeedCvv());
        cardItemNecessary.setNeedPhoneNo(responseDTO.isNeedPhoneNo());
        cardItemNecessary.setNeedBankPWD(responseDTO.isNeedBankPWD());
        responseVO.setCardItemNecessary(cardItemNecessary);
        //其他业务数据
        responseVO.setPreRouteId(responseDTO.getPreRouteId());
        return responseVO;
    }

    /**
     * 构建GuaranteeInstallmentPaymentRequestDTO入参
     *
     * @param info
     * @param requestVO
     * @return
     */
    private GuaranteeInstallmentPaymentRequestDTO buildPaymentRequestDTO(RequestInfoDTO info, GuaranteeInstallmentPaymentRequestVO requestVO) {
        GuaranteeInstallmentPaymentRequestDTO requestDTO = new GuaranteeInstallmentPaymentRequestDTO();
        requestDTO.setRequestId(info.getPaymentRequestId());
        requestDTO.setBankCode(requestVO.getBankCode());
        requestDTO.setPeriod(Integer.parseInt(requestVO.getPeriod()));
        requestDTO.setPreRouteId(requestVO.getPreRouteId());
        requestDTO.setCardNo(requestVO.getCardNo());
        requestDTO.setIdNo(requestVO.getIdNo());
        requestDTO.setOwner(requestVO.getOwner());
        requestDTO.setPhoneNo(requestVO.getPhoneNo());
        requestDTO.setCvv(requestVO.getCvv());
        requestDTO.setAvlidDate(requestVO.getAvlidDate());
        requestDTO.setBankPWD(requestVO.getBankPWD());
        requestDTO.setToken(requestVO.getToken());
        return requestDTO;
    }

    /**
     * 转换GuaranteeInstallmentPaymentResponseVO返回值
     *
     * @param responseDTO
     * @return
     */
    private GuaranteeInstallmentPaymentResponseVO transferPaymentResponseVO(GuaranteeInstallmentPaymentResponseDTO responseDTO) {
        GuaranteeInstallmentPaymentResponseVO responseVO = new GuaranteeInstallmentPaymentResponseVO();
        UrlInfoVO urlInfoVO = new UrlInfoVO();
        urlInfoVO.setUrl(responseDTO.getPayUrl());
        urlInfoVO.setMethod(responseDTO.getMethod());
        urlInfoVO.setCharset(responseDTO.getEncoding());
        urlInfoVO.setParams(responseDTO.getParamMap());
        responseVO.setUrlInfo(urlInfoVO);
        return responseVO;
    }





}
