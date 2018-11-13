package com.yeepay.g3.app.nccashier.wap.service;

import com.yeepay.g3.app.nccashier.wap.vo.EBankPayRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.EBankPayResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.EBankSupportBanksVO;
import com.yeepay.g3.app.nccashier.wap.vo.PayResultQueryStateListenVO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;

/**
 * pc网银支付相关请求的处理service
 * @author duangduang
 * @since  2016-11-08
 */
public interface  EBankPayService {
	
	/**
	 * 查询该商家支持的银行列表（收银台模板）
	 * @param requestInfo	
	 * @return
	 */
	EBankSupportBanksVO querySupportBankList(RequestInfoDTO requestInfo);
	
	/**
	 * 网银首页展示请求处理
	 * @param token
	 * @return
	 */
	EBankSupportBanksVO ebankIndexShow(String token);
	
	
	/**
	 * 网银确认支付请求处理
	 * @param param
	 * @return
	 */
	EBankPayResponseVO ebankPay(EBankPayRequestVO param);


	/**
	 * 获取充值支持网银银行列表
	 * @param token
	 * @param type	net/remit   网银/代付汇入
     * @return
     */
	EBankSupportBanksVO getLoadSupportEBanks(String token, String type);

	EBankSupportBanksVO queryLoadSupportBankList(RequestInfoDTO requestInfo,String token,String type);




	}
