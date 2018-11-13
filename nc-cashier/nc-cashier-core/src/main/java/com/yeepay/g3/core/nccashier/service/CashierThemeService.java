package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.ThemeResult;
import com.yeepay.g3.facade.nccashier.dto.ThemeSettingDTO;
import com.yeepay.g3.utils.common.json.JSONException;
/**
 *	 主题定义与扩展接口
 * 
 */
public interface CashierThemeService {
	/**
	 *	@return String,包含需要显示或隐藏的DIV信息 
	 * @throws JSONException 
	 */
	
	 ThemeResult themeSet(ThemeSettingDTO resDTO) throws JSONException;

}
