package com.yeepay.g3.core.nccashier.vo;

import java.util.ArrayList;
import java.util.List;

import com.yeepay.g3.facade.nccashier.dto.SignRelationDTO;
import com.yeepay.g3.facade.nccashier.dto.SignRelationQueryResponseDTO;
import com.yeepay.g3.utils.common.CollectionUtils;

public class UserSignRelationCollection {

	private List<SignRelationInfo> usableSignRelationList;

	private List<SignRelationInfo> unusableSignRelationList;

	public List<SignRelationInfo> getUsableSignRelationList() {
		return usableSignRelationList;
	}

	public void setUsableSignRelationList(List<SignRelationInfo> usableSignRelationList) {
		this.usableSignRelationList = usableSignRelationList;
	}

	public List<SignRelationInfo> getUnusableSignRelationList() {
		return unusableSignRelationList;
	}

	public void setUnusableSignRelationList(List<SignRelationInfo> unusableSignRelationList) {
		this.unusableSignRelationList = unusableSignRelationList;
	}
	
	
}
