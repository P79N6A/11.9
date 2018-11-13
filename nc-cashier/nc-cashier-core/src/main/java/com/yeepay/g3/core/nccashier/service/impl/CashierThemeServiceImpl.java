package com.yeepay.g3.core.nccashier.service.impl;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.service.CashierThemeService;
import com.yeepay.g3.facade.nccashier.dto.ThemeResult;
import com.yeepay.g3.facade.nccashier.dto.ThemeSettingDTO;
import com.yeepay.g3.utils.common.json.JSONException;

@Service
public class CashierThemeServiceImpl implements CashierThemeService {

	@Override
	public ThemeResult themeSet(ThemeSettingDTO resDTO) throws JSONException  {

		ThemeResult theme=new ThemeResult();
		if ("SKB_THEME_CUSTOM".equals(resDTO.getThemeCode())) {
			theme.setShowBottomInfo(false);
			theme.setShowMerchantName(false);
		}
		return theme;

	}

	

}
