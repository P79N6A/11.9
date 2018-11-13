package com.yeepay.g3.facade.nccashier.dto;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/2/20 11:08
 */
public class OrderProcessResponseDTO extends BasicResponseDTO{   
	private static final long serialVersionUID = 6449639602364905562L;

	private long requestId;

    private String merchantNo;

    private String uniqueOrderNo;
    private String encodeRequestId;

    private String configInfo;
    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getUniqueOrderNo() {
        return uniqueOrderNo;
    }

    public void setUniqueOrderNo(String uniqueOrderNo) {
        this.uniqueOrderNo = uniqueOrderNo;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getEncodeRequestId() {
        return encodeRequestId;
    }

    public void setEncodeRequestId(String encodeRequestId) {
        this.encodeRequestId = encodeRequestId;
    }

    public String getConfigInfo() {
        return configInfo;
    }

    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo;
    }
}
