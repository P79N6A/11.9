package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.enumtype.SmsSendStatusEnum;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.ShareCardRedisSTO;
import com.yeepay.g3.facade.cwh.param.BaseInfo;
import com.yeepay.g3.facade.cwh.param.BindCardDTO;
import com.yeepay.g3.facade.cwh.param.BindLimitInfoResDTO;
import com.yeepay.g3.facade.foundation.dto.MerchantLimitResponseDto;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.ncmember.dto.*;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service.impl
 *
 * @author pengfei.chen
 * @since 16/12/30 15:17
 */
@Service("ncCashierUserCenterService")
public class NcCashierUserCenterServiceImpl extends NcCashierBaseService implements NcCashierUserCenterService {
    Logger logger = LoggerFactory.getLogger(NcCashierUserCenterServiceImpl.class);
    @Resource
    private PaymentRequestService paymentRequestService;
    @Resource
    private BankCardLimitInfoService bankCardLimitInfoService;
    @Resource
    private CashierBankCardService cashierBankCardService;
    @Resource
    private NcConfigService ncConfigService;
    @Override
    public MerchantConfigRespDTO registInfoFromUserCenter(String merchentNo) {
        MerchantConfigQueryDTO merchantConfigQueryDTO = buildMerchantConfigRespDTO(merchentNo);
        return userCenterService.queryUserRegisterInfo(merchantConfigQueryDTO);

    }

    @Override
    public GetSharableRespDTO queryShareBindCardList(GetSharableReqDTO getSharableRespDTO) {
        return userCenterService.queryShareBindBankList(getSharableRespDTO);
    }

    @Override
    public List<BaseInfo> filterShareBindCard(List<BaseInfo> list, PaymentRequest paymentRequest, String cusType) {
        //过滤掉不符合透传信息的
        filterBindBankByPassInfo(list, paymentRequest);
        //过滤不符合的收银台模版
        filterNoSupportCashierBanks(list,paymentRequest,cusType);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        try {
            //查询商户支持的银行卡列表以及限额,如果返回为空,或者异常，允许授权
            List<MerchantLimitResponseDto> limitResponseDtos = merchantConfigService.queryMerchantLimit(merchantConfigService.buildMerchantLimitRequestDto(paymentRequest));
            if (CollectionUtils.isEmpty(limitResponseDtos)) {
                return list;
            } else {
                for (BaseInfo baseInfo : list) {
                    boolean flg = isCanUser(limitResponseDtos,baseInfo.getBankCode(),CommonUtil.cardTypeTransferToStr(baseInfo.getBankCardType()),paymentRequest.getOrderAmount());
                    if(flg){
                        //只要有一张卡可用，则返回
                        return list;
                    }else {
                        continue;
                    }
                }
            }
        } catch (Throwable e) {
            logger.error("过滤共享卡超出限额卡时候出错,requestId:{}",paymentRequest.getId());
            return list;
        }
        return null;
    }

    private void filterBindBankByPassInfo(List<BaseInfo> list,PaymentRequest paymentRequest){
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Iterator<BaseInfo> infoIterator = list.iterator();
        while (infoIterator.hasNext()){
            BaseInfo baseInfo = infoIterator.next();
            //转化银行编码
            baseInfo.setBankCode(cashierBankCardService.changeBankCode(baseInfo.getBankCode()));
            //与透传卡号不符
            if (StringUtils.isNotBlank(paymentRequest.getCardNo()) && StringUtils.isNotBlank(baseInfo.getCardNo())
                    && !paymentRequest.getCardNo().equals(baseInfo.getCardNo())) {
                infoIterator.remove();
                continue;

            } else if (StringUtils.isNotBlank(paymentRequest.getBankCode())
                    && StringUtils.isNotBlank(baseInfo.getBankCode())
                    && !paymentRequest.getBankCode().equals(baseInfo.getBankCode())) {
                infoIterator.remove();
                continue;
            } else if (StringUtils.isNotEmpty(paymentRequest.getCardType()) && StringUtils.isNotEmpty(CommonUtil.cardTypeTransferToStr(baseInfo.getBankCardType()))
                    && !paymentRequest.getCardType().equals(CommonUtil.cardTypeTransferToStr(baseInfo.getBankCardType()))) {
                infoIterator.remove();
                continue;
            } else
            if (StringUtils.isNotEmpty(paymentRequest.getOwner()) && StringUtils.isNotEmpty(baseInfo.getOwner())
                    && !paymentRequest.getOwner().equals(baseInfo.getOwner())) {
                infoIterator.remove();
                continue;
            } else if (StringUtils.isNotEmpty(paymentRequest.getIdCard())
                    && StringUtils.isNotEmpty(baseInfo.getIdcard())
                    && !paymentRequest.getIdCard().equalsIgnoreCase(baseInfo.getIdcard())) {
                infoIterator.remove();
                continue;
            } else if (StringUtils.isNotEmpty(paymentRequest.getPhoneNo())
                    && !((StringUtils.isEmpty(baseInfo.getYbMobile())
                    && StringUtils.isEmpty(baseInfo.getBankMobile()))
                    || (StringUtils.isNotEmpty(baseInfo.getYbMobile())
                    && paymentRequest.getPhoneNo().equals(baseInfo.getYbMobile()))
                    || (StringUtils.isNotEmpty(baseInfo.getBankMobile())
                    && paymentRequest.getPhoneNo().equals(baseInfo.getBankMobile())))) {
                infoIterator.remove();
                continue;
            }
        }
    }



    @Override
    public RequestAuthorityRespDTO shareCardAuthCreateOrder(RequestAuthorityReqDTO requestAuthorityReqDTO) {
        return userCenterService.shareBankAuthCreateOrder(requestAuthorityReqDTO);
    }

    @Override
    public void shareCardAuthoritySendSms(ShareCardAuthoritySendSmsRequestDTO requestDTO,ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSmsResponseDTO) {
        PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        userCenterService.shareBankAuthSendSms(jsonObject.getLong("authorityNo"));
        if(StringUtils.isBlank(jsonObject.getString("smsStatus"))){
            jsonObject.put("smsStatus", SmsSendStatusEnum.SEND_SUCCESS.name());
            paymentRequest.setExtendInfo(jsonObject.toJSONString());
            logger.info("商户授权短信发送成功,扩展信息短信状态和请求授权号,requestId:{},merchant:{}",paymentRequest.getId(),paymentRequest.getMerchantNo());
            paymentRequestService.updatePayRequestExtendInfoById(paymentRequest);
        }
        shareCardAuthoritySendSmsResponseDTO.setSmsSendStatus(SmsSendStatusEnum.SEND_SUCCESS.name());
    }



    @Override
    public void shareCardAuthoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO requestDTO,ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirmResponseDTO) {
        PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(requestDTO.getRequestId());
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        if(!"SEND_SUCCESS".equals(jsonObject.getString("smsStatus"))){
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        jsonObject.put("smsStatus", "VALIDATE_SUCCESS");
        paymentRequest.setExtendInfo(jsonObject.toJSONString());
        ConfirmAuthorityRespDTO confirmAuthorityRespDTO = userCenterService.comfirmshareBankAuth(buildConfirmAuthorityReqDTO(paymentRequest, requestDTO.getValidateCode()));
        //从缓存中获取可共享的绑卡列表
        ShareCardRedisSTO shareCardRedisSTO = RedisTemplate.getTargetFromRedis(CommonUtil.SHARE_CARD_CACHE + paymentRequest.getId() + "", ShareCardRedisSTO.class);
        //把其他商户的放在该商户的前面
        if(shareCardRedisSTO!=null && CollectionUtils.isNotEmpty(shareCardRedisSTO.getList())){
            sortBindCardByMerchant(confirmAuthorityRespDTO.getBindList(), shareCardRedisSTO.getList());
        }
        //对返回的绑卡列表进行过滤
        cashierBankCardService.shareBindCardResultFilter(confirmAuthorityRespDTO.getBindList(),paymentRequest,shareCardAuthoritySmsConfirmResponseDTO, requestDTO.getCusType());
        paymentRequestService.updatePayRequestExtendInfoById(paymentRequest);
    }

    @Override
    public void sortBindCardByMerchant(List<BindCardDTO> bindCardDTOList, List<BaseInfo> baseInfoList) {
        if(CollectionUtils.isEmpty(bindCardDTOList) || CollectionUtils.isEmpty(bindCardDTOList)){
            return;
        }
        List<BindCardDTO> cardDTOList = new ArrayList<BindCardDTO>();
        for(BaseInfo baseInfo : baseInfoList){
            //转化bankCode
            for(BindCardDTO bindCardDTO : bindCardDTOList){
                //如过卡号相同
                if(baseInfo.getCardNo() == bindCardDTO.getCardNo()){
                    cardDTOList.add(bindCardDTO);
                }
            }
        }
        //首先移除掉可用卡的其他商户的共享的卡,
        bindCardDTOList.removeAll(cardDTOList);
        //把其他商户的共享的卡放在前面位置
        bindCardDTOList.addAll(0,cardDTOList);
    }

    @Override
    public BaseInfo validateNeedAuthorityRequest(PaymentRequest request, String cusType) {

        //1.判断开关是否打开
        if (!CommonUtil.allowAuthority()) {
            return null;
        }
        //获取同人限制值的身份证号
        String idCrdNo = this.getSamePersonIdCardNo(request);
        if (StringUtils.isBlank(idCrdNo)) {
            logger.info("该商户非同人,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
            return null;
        }
        //判断该商户是否在用户中心已经注册
        if (!isRegist(request.getMerchantNo())) {
            logger.info("该商户没有在用户中心注册,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
            return null;
        }
        GetSharableReqDTO getSharableReqDTO = buildGetSharableReqDTO(request, idCrdNo);
        //查询可共享的绑卡列表
        GetSharableRespDTO getSharableRespDTO = this.queryShareBindCardList(getSharableReqDTO);
        //如果可共享的卡 或者 推荐校验的卡的银行手机号或者易宝手机号为空，则不允许授权
        if (getSharableRespDTO == null
                || CollectionUtils.isEmpty(getSharableRespDTO.getNewSharableCard())
                || (StringUtils.isBlank(getSharableRespDTO.getToBeCheckCard().getBankMobile())
                && StringUtils.isBlank(getSharableRespDTO.getToBeCheckCard().getYbMobile()))) {
            logger.info("该商户获取可共享的的绑卡列表为空，或者有可能推荐校验的卡手机好为空,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
            return null;
        }
        //转化为三代统一配置的bankCode
        for(BaseInfo baseInfo : getSharableRespDTO.getNewSharableCard()){
            baseInfo.setBankCode(cashierBankCardService.changeBankCode(baseInfo.getBankCode()));
        }
        ShareCardRedisSTO shareCardRedisSTO = new ShareCardRedisSTO();
        shareCardRedisSTO.setList(getSharableRespDTO.getNewSharableCard());
        //把可共享的绑卡放入缓存中
        RedisTemplate.setCacheObjectSumValue(CommonUtil.SHARE_CARD_CACHE + request.getId(), shareCardRedisSTO, CommonUtil.getShareCardCacheTime());
        //过滤可共享的银行卡列表(过滤内容包括，是否在支持的银行卡列表中，是否全部超出限额);
        List<BaseInfo> list  = filterShareBindCard(getSharableRespDTO.getNewSharableCard(), request,cusType);
        //如果过滤后的可共享的绑卡列表为空，则不去授权
        if (CollectionUtils.isEmpty(list)) {
            logger.info("可共享的绑卡列表全部超出限额,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
            return null;
        }
        return getSharableRespDTO.getToBeCheckCard();

    }

    @Override
    public void authorityCreateOrderAndSendSms(PaymentRequest request,BaseInfo baseInfo,MerchantAuthorityResponseDTO merchantAuthorityResponseDTO) {
        //授权下单
        logger.info("商户授权请求下单,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
        RequestAuthorityRespDTO requestAuthorityRespDTO = this.shareCardAuthCreateOrder(buildRequestAuthorityReqDTO(request, baseInfo));
        //保存下单返回的authorityNo到 paymentRequest 的扩展信息里面
        JSONObject jsonObject = CommonUtil.parseJson(request.getExtendInfo());
        jsonObject.put("authorityNo", requestAuthorityRespDTO.getAuthorityNo() + "");
        //发送短信验证码
        logger.info("商户授权请求成功,请求发短信,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
        try {
            userCenterService.shareBankAuthSendSms(requestAuthorityRespDTO.getAuthorityNo());
            //发送成功后记录短信验证状态
            jsonObject.put("smsStatus", SmsSendStatusEnum.SEND_SUCCESS.name());
            request.setExtendInfo(jsonObject.toJSONString());
            logger.info("商户授权短信发送成功,扩展信息短信状态和请求授权号,requestId:{},merchant:{}",request.getId(),request.getMerchantNo());
            paymentRequestService.updatePayRequestExtendInfoById(request);
        }catch (Throwable e){
            logger.error("商户授权请求发送短信失败,requestId:{}",request.getId());
            merchantAuthorityResponseDTO.setSmsSendStatus(SmsSendStatusEnum.SEND_FAILED.name());
        }
    }


    private MerchantConfigQueryDTO buildMerchantConfigRespDTO(String merchantNo){
        MerchantConfigQueryDTO merchantConfigQueryDTO = new MerchantConfigQueryDTO();
        merchantConfigQueryDTO.setMerchantNo(merchantNo);
        return merchantConfigQueryDTO;
    }


    //判断该卡
    private boolean isCanUser(List<MerchantLimitResponseDto> limitResponseDtos,String bankCode,String cardType,BigDecimal orderAmount) {
        long amount = orderAmount.multiply(new BigDecimal(100)).longValue();
        /*for (MerchantLimitResponseDto merchantLimitResponseDto : limitResponseDtos) {
            if(merchantLimitResponseDto.getBankCode().equals(bankCode) && merchantLimitResponseDto.getDebit().equals(cardType)){
                //判断是否超出限额
                if ((merchantLimitResponseDto.getLimitOfBill() != -1)
                        && (amount > merchantLimitResponseDto.getLimitOfBill() || amount < merchantLimitResponseDto.getLimitMinOfBill())) {
                    return false;
                } else {
                    return true;
                }
            }else {
                continue;
            }
        }*/
        return true;
    }

    /*获取同仁限制值的身份证号,如果为空表示没有设置同人值*/
    private String getSamePersonIdCardNo(PaymentRequest paymentRequest){
        BindLimitInfoResDTO bindLimitInfoResDTO = bankCardLimitInfoService.getLimitInfoNoCache(paymentRequest);
        if(bindLimitInfoResDTO==null){
            return null;
        }else if(StringUtils.isNotBlank(bindLimitInfoResDTO.getUserNameLimit())
                && StringUtils.isNotBlank(bindLimitInfoResDTO.getIdentityNoLimit())
                && !Constant.MERCHANT_LIMIT_TYPE.equals(bindLimitInfoResDTO.getBindCardLimitType())) {
            return bindLimitInfoResDTO.getIdentityNoLimit();
        }else {
            return null;
        }
    }

    private boolean isRegist(String merchantNo){
        MerchantConfigRespDTO merchantConfigRespDTO = this.registInfoFromUserCenter(merchantNo);
        if(merchantConfigRespDTO == null){
            return false;
        }else if("1".equals(merchantConfigRespDTO.getStatus())){
            return true;
        }else {
            return false;
        }
    }


    private GetSharableReqDTO buildGetSharableReqDTO(PaymentRequest request,String idCardNo){
        GetSharableReqDTO getSharableReqDTO = new GetSharableReqDTO();
        getSharableReqDTO.setIdcardNo(idCardNo);
        MerchantUserDTO merchantUserDTO = new MerchantUserDTO();
        merchantUserDTO.setMerchantNo(request.getMerchantNo());
        merchantUserDTO.setIdentityType(request.getIdentityType());
        merchantUserDTO.setIdentityId(request.getIdentityId());
        getSharableReqDTO.setMerchantUser(merchantUserDTO);
        return getSharableReqDTO;
    }

    private RequestAuthorityReqDTO buildRequestAuthorityReqDTO(PaymentRequest paymentRequest,BaseInfo baseInfo){
        RequestAuthorityReqDTO requestAuthorityReqDTO = new RequestAuthorityReqDTO();
        requestAuthorityReqDTO.setRequestNo(paymentRequest.getMerchantOrderId());
        requestAuthorityReqDTO.setIdentityId(paymentRequest.getIdentityId());
        requestAuthorityReqDTO.setIdentityType(paymentRequest.getIdentityType());
        requestAuthorityReqDTO.setMerchantNo(paymentRequest.getMerchantNo());
        requestAuthorityReqDTO.setCardNo(baseInfo.getCardNo());
        requestAuthorityReqDTO.setPhone(baseInfo.getBankMobile() == null ? baseInfo.getYbMobile() : baseInfo.getBankMobile());
        requestAuthorityReqDTO.setIdcardNo(baseInfo.getIdcard());
        return requestAuthorityReqDTO;
    }


    private ConfirmAuthorityReqDTO buildConfirmAuthorityReqDTO(PaymentRequest paymentRequest,String validateCode){
        ConfirmAuthorityReqDTO confirmAuthorityReqDTO = new ConfirmAuthorityReqDTO();
        JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
        confirmAuthorityReqDTO.setAuthorityNo(jsonObject.getLong("authorityNo"));
        confirmAuthorityReqDTO.setValidateCode(validateCode);
        return confirmAuthorityReqDTO;
    }

    private void filterNoSupportCashierBanks(List<BaseInfo> list,PaymentRequest request, String cusType){
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        List<BankSupportDTO> supportCashierBanks = ncConfigService.getSupportBanks(request, cusType);
        // 支持的收銀台模板為空，將可用綁卡列表全部置為不可用，并標記不可用原因
        if (CollectionUtils.isEmpty(supportCashierBanks)) {
            // 监控日志
            logger.error("[monitor],event:nccashier_filterBindCardsByCashierTemplate, requestId:{},收银台模板支持银行列表为空", request.getId());
            // 這種情況下，首次也不可用 直接抛异常
            throw CommonUtil.handleException(Errors.CASHIER_CONFIG_BANKS_NULL);
        }
        Iterator<BaseInfo> infoIterator = list.iterator();
        while (infoIterator.hasNext()){
            BaseInfo baseInfo = infoIterator.next();
            boolean isSupport = false;
            for (BankSupportDTO supportCashierBank : supportCashierBanks) {
                boolean bankCode = supportCashierBank.getBankCode().equals(baseInfo.getBankCode());
                boolean cardType = supportCashierBank.getBanktype().equals(CommonUtil.cardTypeTransfer(baseInfo.getBankCardType()));
                //如果跟收银台模版相匹配的
                if (cardType && bankCode) {
                    isSupport = true;
                    //如果找到匹配的则跳出
                    break;
                }
            }
            //如果改共享卡没有找到支持的收银台模版，移除掉
            if(!isSupport){
                infoIterator.remove();
            }

        }
    }


}
