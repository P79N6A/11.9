package com.yeepay.g3.facade.nccashier.dto;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/1/4 11:38
 */
public class MerchantAuthorityResponseDTO extends BasicResponseDTO {
    //短信发送状态
    private String smsSendStatus;

    private Long requestId;

    private String phoneLater;

    private String idCardNoLater;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getSmsSendStatus() {
        return smsSendStatus;
    }

    public void setSmsSendStatus(String smsSendStatus) {
        this.smsSendStatus = smsSendStatus;
    }

    public String getPhoneLater() {
        return phoneLater;
    }

    public void setPhoneLater(String phoneLater) {
        this.phoneLater = phoneLater;
    }

    public String getIdCardNoLater() {
        return idCardNoLater;
    }

    public void setIdCardNoLater(String idCardNoLater) {
        this.idCardNoLater = idCardNoLater;
    }
}
