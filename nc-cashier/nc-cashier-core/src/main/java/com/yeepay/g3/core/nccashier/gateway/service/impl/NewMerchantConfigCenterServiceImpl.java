package com.yeepay.g3.core.nccashier.gateway.service.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-09-03 15:48
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.entity.MerchantProductInfo;
import com.yeepay.g3.core.nccashier.gateway.service.NewMerchantConfigCenterService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.ConfigCenterUtils;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.configcenter.constants.GeneralSettingType;
import com.yeepay.g3.facade.configcenter.dto.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.CollectionUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yeepay.g3.core.nccashier.utils.ConfigCenterUtils.NC_CASHIER_SYS_NAME;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-03 15:48
 **/

@Service
public class NewMerchantConfigCenterServiceImpl extends NcCashierBaseService implements NewMerchantConfigCenterService {

    private static Logger logger = LoggerFactory.getLogger(MerchantConfigCenterServiceImpl.class);



    @Override
    public MerchantProductRespDto getMerchantInNetConfig(String merchantNo,String bizSystem) {
        MerchantProductRespDto respDto = null;
        try{
            if(StringUtils.isNotBlank(merchantNo) && StringUtils.isNotBlank(bizSystem)){
                respDto = merchantConfigurationQueryFacade.queryMerchantConfigProduct(buildMerchantProductReqDto(merchantNo,bizSystem));
            }
        }catch(Throwable t){
            logger.error("调用配置中心新接口获取产品开通配置信息异常", t);
        }
        if(respDto==null){
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
        }
        return respDto;
    }

    @Override
    public MerchantSettingRespDto getPayScene(String merchantNo,  String baseproductCode, String marketingproductCode) {
        MerchantSettingRespDto respDto = null;
        try{
            if(StringUtils.isNotBlank(merchantNo)){
                respDto = merchantConfigurationQueryFacade.queryMerchantGeneralSetting(bulidMerchantSettingReqDto(merchantNo,baseproductCode,marketingproductCode));
            }
        }catch(Throwable t){
            logger.error("调用配置中心新接口获取支付场景", t);
        }
        if(respDto==null){
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
        }
        return respDto;
    }

    @Override
    public List<MerchantProductInfo> getAllMerchantInNetConfig(String merchantNo, String bizSystem,String token) {
        List<MerchantProductInfo> list = null;
        //先从redis获取
        list = RedisTemplate.getTargetFromRedis(CommonUtil.LOAD_NEW_CONFIG_REDIS + merchantNo +"_"+token,new ArrayList<MerchantProductInfo>().getClass());
        if(CollectionUtils.isEmpty(list)){
            MerchantProductRespDto configResult = getMerchantInNetConfig(merchantNo,bizSystem);

            if(configResult!=null && CollectionUtils.isNotEmpty(configResult.getMerchantProductList())) {
                list = new ArrayList<MerchantProductInfo>();
                for (MerchantProductBaseRespDto dto : configResult.getMerchantProductList()) {
                    MerchantSettingRespDto setttingDto = getPayScene(merchantNo, dto.getBaseproductCode(), dto.getMarketingproductCode());
                    MerchantProductInfo merchantProductInfo = new MerchantProductInfo();
                    merchantProductInfo.setBase(dto.getBaseproductCode());
                    merchantProductInfo.setBiz(dto.getBusinessuserCode());
                    merchantProductInfo.setMar(dto.getMarketingproductCode());
                    merchantProductInfo.setSce(getPaySceneByMerchantSettingRespDto(setttingDto));
                    list.add(merchantProductInfo);
                }
                RedisTemplate.setCacheObjectSumValue(CommonUtil.LOAD_NEW_CONFIG_REDIS + merchantNo +"_"+token,JSONUtils.toJsonString(list),50*1000);
            }
            else
                return null;
        }
        return list;
    }



    String getPaySceneByMerchantSettingRespDto(MerchantSettingRespDto setttingDto){
        if(setttingDto != null){
            for(MerchantSettingBaseRespDto setting : setttingDto.getMerchantSettingList()){
                if(GeneralSettingType.SCENE_SETTING.name().equals(setting.getGeneralSettingCode())){
                    List<MerchantSettingBaseRespDto.MerchantSettingAttributeDto> list = setting.getMerchantSettingAttributeDtoList();
                    if(CollectionUtils.isNotEmpty(list))
                        for(MerchantSettingBaseRespDto.MerchantSettingAttributeDto dto : list){
                            if(StringUtils.isNotBlank(dto.getAttributeValue()) && "SCENE_SETTING".equals(dto.getAttributeName())){
                                JSONObject jsonObject = JSON.parseObject(dto.getAttributeValue());
                                if(jsonObject.get("status").equals("ENABLE"))
                                    return jsonObject.get("code").toString();
                            }
                    }
                }
                return "";
            }
        }
        return "";
    }


    MerchantProductReqDto buildMerchantProductReqDto(String merchantNo,String bizSystem){
        MerchantProductReqDto dto = new MerchantProductReqDto();
        dto.setMerchantNo(merchantNo);
        dto.setBusinessuserCode(bizSystem);
        dto.setSystem(NC_CASHIER_SYS_NAME);
        dto.setUid(ConfigCenterUtils.getUidByBizSystem(NC_CASHIER_SYS_NAME));
        return dto;
    }

    MerchantSettingReqDto bulidMerchantSettingReqDto(String merchantNo, String baseproductCode, String marketingproductCode){
        MerchantSettingReqDto dto = new MerchantSettingReqDto();
        dto.setMerchantNo(merchantNo);
        dto.setSystem(NC_CASHIER_SYS_NAME);
        dto.setUid(ConfigCenterUtils.getUidByBizSystem(NC_CASHIER_SYS_NAME));
        dto.setMarketingproductCode(marketingproductCode);
        dto.setBaseproductCode(baseproductCode);
        dto.setGeneralSettingType(GeneralSettingType.SCENE_SETTING);    //支付场景
        return dto;
    }


}
