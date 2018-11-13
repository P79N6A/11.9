package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncmember.dto.HiddenCode;
import com.yeepay.g3.facade.ncmember.dto.SignRelationQueryReqDTO;
import com.yeepay.g3.facade.ncmember.dto.SignRelationReqDTO;
import com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

public class CashierUserInfo {

    private static Logger logger = LoggerFactory.getLogger(CashierUserInfo.class);

    /**
     * 商编
     */
    private String merchantNo;

    /**
     * 用户类型类别，指“三代会员YIBAO”或“商户联名会员(外部用户)JOINLY”，见{@linkplain com.yeepay.g3.facade.ncpay.enumtype.MemberTypeEnum MemberTypeEnum}
     */
    private String type;

    /**
     * 原始用户号，包含：
     * 1，普通外部用户的用户号 identityId；
     * 2，易宝三代会员的外部用户号 userNo
     */
    private String userNo;

    /**
     * 用户类型标识，包含：
     * 1，外部用户的{@linkplain com.yeepay.g3.facade.cwh.enumtype.IdentityType IdentityType}；
     * 2，易宝三代会员标识“YIBAO”
     */
    private String userType;

    /**
     * 外部用户ID，1、针对外部用户，根据【identityId、identityType、merchantNo】获取到的外部用户id；
     * 2、针对易宝三代会员，根据【userNo、merchantNo】获取到的三代会员编号memberNo
     */
    private String externalUserId;

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    /**
     * 构建一个外部用户（非三代会员）
     * @param identityId
     * @param identityType
     * @param merchantNo
     * @return
     */
    public static CashierUserInfo buildOrignalExternalUser(String identityId,String identityType,String merchantNo){
        CashierUserInfo userInfo = new CashierUserInfo();
        userInfo.setType(MemberTypeEnum.JOINLY.name());
        userInfo.setUserNo(identityId);
        userInfo.setUserType(identityType);
        userInfo.setMerchantNo(merchantNo);
        return userInfo;
    }

    /**
     * 校验该外部用户是否已包含{merchantNo,userNo,userType}三条属性
     */
    public boolean formByIdentityId() {
        if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(userNo) || StringUtils.isBlank(userType)) {
            return false;
        }
        return true;
    }

    /**
     * 比较外部用户的三个属性{merchantNo,userNo,userType}是否一致
     *
     * @description 前提是，本对象和externalUser的三个属性都不为空
     * @param externalUser
     * @return
     */
    public boolean compare(CashierUserInfo externalUser) {
        if (externalUser == null) {
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
        }
        if (StringUtils.isBlank(merchantNo) || StringUtils.isBlank(externalUser.getMerchantNo())
                || StringUtils.isBlank(userNo) || StringUtils.isBlank(externalUser.getUserNo())
                || StringUtils.isBlank(userType) || StringUtils.isBlank(externalUser.getUserType())) {
            logger.warn("合法外部用户={}, 待比较的外部用户={}, 存在外部用户信息不完整，无法比较", this, externalUser);
            throw new CashierBusinessException(Errors.SYSTEM_EXCEPTION);
        }
        if (!merchantNo.equals(externalUser.getMerchantNo()) || !userNo.equals(externalUser.getUserNo())
                || !userType.equals(externalUser.getUserType())) {
            logger.warn("合法外部用户={}, 待比较的外部用户={}, 俩对象存在信息不一致", this, externalUser);
            return false;
        }
        return true;
    }

    /**
     * 补充SignRelationReqDTO对象中的外部用户信息
     *
     * @param signRelationReqDTO
     */
    public void supplySignRelationReqDTO(SignRelationReqDTO signRelationReqDTO) {
        if (signRelationReqDTO != null) {
            signRelationReqDTO.setIdentityId(userNo);
            signRelationReqDTO.setIdentityType(userType);
            signRelationReqDTO.setMerchantNo(merchantNo);
        }
    }

    /**
     * 补充该CashierUserInfo对象中的属性到订单信息
     * @param orderInfo
     */
    public void supplyOrderInfo(OrderDetailInfoModel orderInfo) {
        if (MemberTypeEnum.YIBAO.name().equals(type)) {
            orderInfo.setMemberNo(externalUserId);
            orderInfo.setMemberType(type);
        } else {
            orderInfo.setIdentityId(userNo);
            orderInfo.setIdentityType(IdentityType.valueOf(userType));
        }
    }

    /**
     * 构造SignRelationQueryReqDTO入参
     *
     * @return
     */
    public SignRelationQueryReqDTO buildSignRelationQueryReqDTO() {
        SignRelationQueryReqDTO signRelationQueryRequestDTO = new SignRelationQueryReqDTO();
        signRelationQueryRequestDTO.setIdentityId(userNo);
        signRelationQueryRequestDTO.setIdentityType(userType);
        signRelationQueryRequestDTO.setMerchantNo(merchantNo);
        return signRelationQueryRequestDTO;
    }

    @Override
    public String toString() {
        return "CashierUserInfo{" +
                "merchantNo='" + merchantNo + '\'' +
                ", type='" + type + '\'' +
                ", userNo='" + HiddenCode.hiddenIdentityId(userNo) + '\'' +
                ", userType='" + userType + '\'' +
                ", externalUserId='" + externalUserId + '\'' +
                '}';
    }
}
