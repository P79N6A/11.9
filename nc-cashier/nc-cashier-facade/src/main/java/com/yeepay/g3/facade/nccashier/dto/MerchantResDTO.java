package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.nccashier.dto.BasicResponseDTO;

import java.util.Date;

/**
 * Created by yp-pos-m-7118 on 17/10/25.
 */
public class MerchantResDTO extends BasicResponseDTO {
    private String merchantNo;
    private String customerNo;
    private String signName;
    private String name;
    private String shortName;
    private String source;
    private String dependence;
    private String type;
    private String bizcatCode;
    private String bizcatName;
    private String subBizcatCode;
    private String subBizcatName;
    private String province;
    private String city;
    private String address;
    private String postCode;
    private String sellerNo;
    private String status;
    private String signType;
    private Date createTime;
    private String auditStatus;
    private String activateStatus;


    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDependence() {
        return dependence;
    }

    public void setDependence(String dependence) {
        this.dependence = dependence;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBizcatCode() {
        return bizcatCode;
    }

    public void setBizcatCode(String bizcatCode) {
        this.bizcatCode = bizcatCode;
    }

    public String getBizcatName() {
        return bizcatName;
    }

    public void setBizcatName(String bizcatName) {
        this.bizcatName = bizcatName;
    }

    public String getSubBizcatCode() {
        return subBizcatCode;
    }

    public void setSubBizcatCode(String subBizcatCode) {
        this.subBizcatCode = subBizcatCode;
    }

    public String getSubBizcatName() {
        return subBizcatName;
    }

    public void setSubBizcatName(String subBizcatName) {
        this.subBizcatName = subBizcatName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getSellerNo() {
        return sellerNo;
    }

    public void setSellerNo(String sellerNo) {
        this.sellerNo = sellerNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getActivateStatus() {
        return activateStatus;
    }

    public void setActivateStatus(String activateStatus) {
        this.activateStatus = activateStatus;
    }


}
