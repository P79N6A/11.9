package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

/**
 * 主题请求参数
 * 
 */

public class ThemeSettingDTO implements Serializable{
	
	private static final long serialVersionUID = -1473958940139519335L;
	private String customNumber;
	private String themeCode;

	public String getCustomNumber() {
		return customNumber;
	}

	public void setCustomNumber(String customNumber) {
		this.customNumber = customNumber;
	}

	public String getThemeCode() {
		return themeCode;
	}

	public void setThemeCode(String themeCode) {
		this.themeCode = themeCode;
	}


	
}
