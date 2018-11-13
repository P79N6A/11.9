package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayResponseDTO;

public interface BindCardPayBiz {

	/**
	 * 绑卡支付首次支付下单，该接口不校验绑卡限制值与用户输入的限制值是否一致
	 * 
	 * @param requestDTO
	 */
	FirstBindCardPayResponseDTO createPayment(FirstBindCardPayRequestDTO requestDTO);

	/**
	 * 首次支付确认支付
	 * 
	 * @param requestDTO
	 * @return
	 */
	FirstBindCardPayResponseDTO firstPay(FirstBindCardPayRequestDTO requestDTO);

}
