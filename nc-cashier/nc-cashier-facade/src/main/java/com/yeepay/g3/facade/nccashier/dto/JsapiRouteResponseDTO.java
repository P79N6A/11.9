package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.util.HiddenCode;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 16/12/12 17:29
 */
public class JsapiRouteResponseDTO extends BasicResponseDTO {

    /**
     * 公众号ID
     */
    private String appId;
    /**
     * 公众号应用密钥
     */
    private String appSecret;
    /**
     *  调用预路由状态（1：成功；0：失败）
     */
    private String dealStatus;
    /**
     * 场景类型扩展（jsapiH5：微信内部H5通道；normal：正常通道）
     */
    private String sceneTypeExt;
    
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getSceneTypeExt() {
		return sceneTypeExt;
	}

	public void setSceneTypeExt(String sceneTypeExt) {
		this.sceneTypeExt = sceneTypeExt;
	}

    @Override
    public String toString() {
        return "JsapiRouteResponseDTO{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + HiddenCode.hiddenName(appSecret) + '\'' +
                ", dealStatus='" + dealStatus + '\'' +
                ", sceneTypeExt='" + sceneTypeExt + '\'' +
                '}';
    }
}
