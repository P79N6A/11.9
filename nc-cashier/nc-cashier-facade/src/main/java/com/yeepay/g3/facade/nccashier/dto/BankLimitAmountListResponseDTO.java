package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.facade.nccashier.dto
 *
 * @author pengfei.chen
 * @since 16/11/10 10:28
 */
public class BankLimitAmountListResponseDTO extends BasicResponseDTO{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5349976116113718530L;

	public List<BankLimitAmountResponseDTO> getLimitAmountResponseDTOList() {
        return limitAmountResponseDTOList;
    }

    public void setLimitAmountResponseDTOList(List<BankLimitAmountResponseDTO> limitAmountResponseDTOList) {
        this.limitAmountResponseDTOList = limitAmountResponseDTOList;
    }

    private List<BankLimitAmountResponseDTO> limitAmountResponseDTOList;

}
