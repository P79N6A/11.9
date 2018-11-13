package com.yeepay.g3.facade.nccashier.dto;

import java.util.Map;



public class MarketActivityResponseDTO extends BasicResponseDTO{

	private static final long serialVersionUID = 1L;
	
	private Map<String, ActivityInfoOfPayProductDTO> activities;
	
	public MarketActivityResponseDTO(){
		
	}

	public Map<String, ActivityInfoOfPayProductDTO> getActivities() {
		return activities;
	}

	public void setActivities(Map<String, ActivityInfoOfPayProductDTO> activities) {
		this.activities = activities;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("MarketActivityResponseDTO[");
		str.append("activities=" + activities);
		str.append("]");
		return str.toString();
	}
	
}
