package com.yeepay.g3.facade.nccashier.dto;

import java.util.List;

public class InstallmentBankCardResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 可用签约
	 */
	private List<SignRelationDTO> usableSignRelationList;
	
	private List<SignRelationDTO> unusableSignRelationList;

}
