package com.yeepay.g3.app.nccashier.wap.utils;


import com.yeepay.g3.app.nccashier.wap.vo.AccountPayRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.BindCardMerchantRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.CardInfoVO;
import com.yeepay.g3.facade.cwh.param.BankCardDetailDTO;
import com.yeepay.g3.facade.cwh.param.BaseInfo;
import com.yeepay.g3.facade.cwh.param.SensitiveInfo;
import com.yeepay.g3.facade.nccashier.dto.CardInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.encrypt.Base64;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * base64加解密工具类
 * Created by ruiyang.du on 2017/7/10.
 */
public class Base64Util {

    private static final Logger logger = LoggerFactory.getLogger(Base64Util.class);


    /**
     * base64字符串解密
     *
     * @param encoded 已转码串
     * @return
     */
    public static String decode(String encoded) {
        if (StringUtils.isEmpty(encoded)) {
            return "";
        }
        try {
            return Base64.decode(encoded);
        } catch (Exception e) {
            logger.warn("decode 失败，返回输入字符串，输入串为：" + encoded);
            return encoded;
        }
    }


    /**
     * base64字符串加密
     *
     * @param source 明文
     * @return
     */
    public static String encode(String source) {
        if (StringUtils.isEmpty(source)) {
            return "";
        }
        try {
            return Base64.encode(source);
        } catch (Exception e) {
            logger.warn("encode 失败，返回输入字符串，源字符为：" + source);
            return source;
        }
    }


    /**
     * base64解密CardInfoDTO中的敏感信息
     *
     * @param cardInfoDTO
     */
    public static void decryptCardInfoDTO(CardInfoDTO cardInfoDTO) {
        if (cardInfoDTO == null) {
            return;
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getName())) {
            cardInfoDTO.setName(decode(cardInfoDTO.getName()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getPhone())) {
            cardInfoDTO.setPhone(decode(cardInfoDTO.getPhone()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getIdno())) {
            cardInfoDTO.setIdno(decode(cardInfoDTO.getIdno()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getCardno())) {
            cardInfoDTO.setCardno(decode(cardInfoDTO.getCardno()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getCvv2())) {
            cardInfoDTO.setCvv2(decode(cardInfoDTO.getCvv2()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getValid())) {
            cardInfoDTO.setValid(decode(cardInfoDTO.getValid()));
        }
        if (StringUtils.isNotEmpty(cardInfoDTO.getPass())) {
            cardInfoDTO.setPass(decode(cardInfoDTO.getPass()));
        }
    }

    /**
     * base64解密CardInfoDTO中的敏感信息
     *
     * @param needBankCardDTO
     */
    public static void decryptNeedBankCardDTO(NeedBankCardDTO needBankCardDTO) {
        if (needBankCardDTO == null) {
            return;
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getCardno())) {
            needBankCardDTO.setCardno(decode(needBankCardDTO.getCardno()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getOwner())) {
            needBankCardDTO.setOwner(decode(needBankCardDTO.getOwner()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getIdno())) {
            needBankCardDTO.setIdno(decode(needBankCardDTO.getIdno()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getPhoneNo())) {
            needBankCardDTO.setPhoneNo(decode(needBankCardDTO.getPhoneNo()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getYpMobile())) {
            needBankCardDTO.setYpMobile(decode(needBankCardDTO.getYpMobile()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getCvv())) {
            needBankCardDTO.setCvv(decode(needBankCardDTO.getCvv()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getBankPWD())) {
            needBankCardDTO.setBankPWD(decode(needBankCardDTO.getBankPWD()));
        }
        if (StringUtils.isNotEmpty(needBankCardDTO.getAvlidDate())) {
            needBankCardDTO.setAvlidDate(decode(needBankCardDTO.getAvlidDate()));
        }
        if (needBankCardDTO.getBankCardDetailDTO() == null) {
            return;
        }
        BankCardDetailDTO bankCardDetailDTO = needBankCardDTO.getBankCardDetailDTO();
        if (bankCardDetailDTO.getBaseInfo() != null) {
            BaseInfo baseInfo = bankCardDetailDTO.getBaseInfo();
            if (StringUtils.isNotEmpty(baseInfo.getCardId())) {
                baseInfo.setCardId(decode(baseInfo.getCardId()));
            }
            if (StringUtils.isNotEmpty(baseInfo.getCardNo())) {
                baseInfo.setCardNo(decode(baseInfo.getCardNo()));
            }
            if (StringUtils.isNotEmpty(baseInfo.getIdcard())) {
                baseInfo.setIdcard(decode(baseInfo.getIdcard()));
            }
            if (StringUtils.isNotEmpty(baseInfo.getOwner())) {
                baseInfo.setOwner(decode(baseInfo.getOwner()));
            }
            if (StringUtils.isNotEmpty(baseInfo.getBankMobile())) {
                baseInfo.setBankMobile(decode(baseInfo.getBankMobile()));
            }
            if (StringUtils.isNotEmpty(baseInfo.getYbMobile())) {
                baseInfo.setYbMobile(decode(baseInfo.getYbMobile()));
            }
        }

        if (bankCardDetailDTO.getSensitiveInfo() != null) {
            SensitiveInfo sensitiveInfo = bankCardDetailDTO.getSensitiveInfo();
            if (StringUtils.isNotEmpty(sensitiveInfo.getCvv2())) {
                sensitiveInfo.setCvv2(decode(sensitiveInfo.getCvv2()));
            }
            if (StringUtils.isNotEmpty(sensitiveInfo.getExpireDate())) {
                sensitiveInfo.setExpireDate(decode(sensitiveInfo.getExpireDate()));
            }
            if (StringUtils.isNotEmpty(sensitiveInfo.getPin())) {
                sensitiveInfo.setPin(decode(sensitiveInfo.getPin()));
            }
        }
    }


    /**
     * base64解密AccountPayRequestVO中的敏感信息
     *
     * @param accountPayRequestVO
     */
    public static void decryptAccountPayRequestVO(AccountPayRequestVO accountPayRequestVO) {
        if (accountPayRequestVO == null) {
            return;
        }
        if (StringUtils.isNotEmpty(accountPayRequestVO.getUserAccount())) {
            accountPayRequestVO.setUserAccount(decode(accountPayRequestVO.getUserAccount()));
        }
        if (StringUtils.isNotEmpty(accountPayRequestVO.getTradePassword())) {
            accountPayRequestVO.setTradePassword(decode(accountPayRequestVO.getTradePassword()));
        }
    }
  
    /**
     * base64解密绑卡支付BindCardInfoVO中的敏感信息
     * @param bindCardInfoVO
     */
    public static void decryptBindCardInfoVO(BindCardInfoVO bindCardInfoVO){
    	if(bindCardInfoVO == null){
    		return;
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getCardno())){
    		bindCardInfoVO.setCardno(decode(bindCardInfoVO.getCardno()));
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getCvv())){
    		bindCardInfoVO.setCvv(decode(bindCardInfoVO.getCvv()));
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getIdno())){
    		bindCardInfoVO.setIdno(decode(bindCardInfoVO.getIdno()));
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getPhoneNo())){
    		bindCardInfoVO.setPhoneNo(decode(bindCardInfoVO.getPhoneNo()));
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getOwner())){
    		bindCardInfoVO.setOwner(decode(bindCardInfoVO.getOwner()));
    	}
    	if(StringUtils.isNotEmpty(bindCardInfoVO.getAvlidDate())){
    		bindCardInfoVO.setAvlidDate(decode(bindCardInfoVO.getAvlidDate()));
    	}
    }
    /**
     * 解密CardInfoVO中的敏感信息
     * @param cardInfo
     */
    public static void decryptCardInfoVO(CardInfoVO cardInfo){
    	if(StringUtils.isNotEmpty(cardInfo.getCardNo())){
    		cardInfo.setCardNo(decode(cardInfo.getCardNo()));
    	}
    	if(StringUtils.isNotEmpty(cardInfo.getCvv())){
    		cardInfo.setCvv(decode(cardInfo.getCvv()));
    	}
    	if(StringUtils.isNotEmpty(cardInfo.getIdNo())){
    		cardInfo.setIdNo(decode(cardInfo.getIdNo()));
    	}
    	if(StringUtils.isNotEmpty(cardInfo.getPhoneNo())){
    		cardInfo.setPhoneNo(decode(cardInfo.getPhoneNo()));
    	}
    	if(StringUtils.isNotEmpty(cardInfo.getOwner())){
    		cardInfo.setOwner(decode(cardInfo.getOwner()));
    	}
    	if(StringUtils.isNotEmpty(cardInfo.getAvlidDate())){
    		cardInfo.setAvlidDate(decode(cardInfo.getAvlidDate()));
    	}
    }
}
