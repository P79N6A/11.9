package com.yeepay.g3.facade.nccashier.dto;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/1/4 11:38
 */
public class ShareCardAuthoritySendSmsResponseDTO extends BasicResponseDTO {

    //短信发送状态状态
    private String smsSendStatus;

    public String getSmsSendStatus() {
        return smsSendStatus;
    }

    public void setSmsSendStatus(String smsSendStatus) {
        this.smsSendStatus = smsSendStatus;
    }
}
