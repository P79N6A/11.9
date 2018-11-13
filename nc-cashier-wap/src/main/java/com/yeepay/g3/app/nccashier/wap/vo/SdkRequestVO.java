package com.yeepay.g3.app.nccashier.wap.vo;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.dubbo.common.utils.StringUtils;

import java.io.Serializable;

/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.vo
 *
 * @author pengfei.chen
 * @since 17/3/14 16:17
 */
public class SdkRequestVO implements Serializable {

    @NotBlank(message = "商户编号不能为空")
    private String merchantNo;

    @NotBlank(message = "订单token不能为空")
    private String token;

    @NotBlank(message = "timestamp不能为空")
    private String timestamp;

    private String directPayType;

    private String cardType;

    private String userNo;

    private String userType;

    @NotBlank(message = "签名sign不能为空")
    private String sign;

    /**
     * 扩展参数
     */
    private String ext;
    
    
    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirectPayType() {
        return directPayType ==null?"":directPayType;
    }

    public void setDirectPayType(String directPayType) {
        this.directPayType = directPayType;
    }

    public String getCardType() {
        return cardType==null?"":cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getUserNo() {
        return userNo ==null?"":userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserType() {
        return userType ==null?"":userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    
    public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getText(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("merchantNo="+getMerchantNo()).append("&token="+getToken()).append("&timestamp="+getTimestamp());
        stringBuilder.append("&directPayType="+getDirectPayType()).append("&cardType="+getCardType());
        stringBuilder.append("&userNo="+getUserNo()).append("&userType="+getUserType());

        return stringBuilder.toString();
    }
	
	/**
	 * 增加扩展参数后新的签名明文
	 * 
	 * @return
	 */
	public String getNewText() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getText()).append("&ext=").append(StringUtils.isBlank(ext) ? "" : ext);
		return stringBuilder.toString();
	}
}
