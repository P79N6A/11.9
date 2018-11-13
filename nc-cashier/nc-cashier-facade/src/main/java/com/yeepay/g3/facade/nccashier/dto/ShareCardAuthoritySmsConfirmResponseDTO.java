package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 17/1/4 11:38
 */
public class ShareCardAuthoritySmsConfirmResponseDTO extends BasicResponseDTO {

    private boolean isChangeCard =true;
    private String SmsValidateStatus;
    private List<BankCardDTO> bankCardDTOList;
    public String getSmsValidateStatus() {
        return SmsValidateStatus;
    }

    public void setSmsValidateStatus(String smsValidateStatus) {
        SmsValidateStatus = smsValidateStatus;
    }

    public List<BankCardDTO> getBankCardDTOList() {
        return bankCardDTOList;
    }

    public void setBankCardDTOList(List<BankCardDTO> bankCardDTOList) {
        this.bankCardDTOList = bankCardDTOList;
    }

    public boolean isChangeCard() {
        return isChangeCard;
    }

    public void setIsChangeCard(boolean isChangeCard) {
        this.isChangeCard = isChangeCard;
    }
}
