package com.yeepay.g3.core.payprocessor.biz;

import com.yeepay.g3.facade.payprocessor.dto.CflPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.CflPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPrePayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PassiveScanPayResponseDTO;

/**
 * FE支付业务处理
 * @author chronos.
 * @createDate 2016/11/9.
 */
public interface FePayBiz {

    OpenPayResponseDTO openPay(OpenPayRequestDTO requestDTO);

    NetPayResponseDTO netPay(NetPayRequestDTO requestDTO);

	OpenPrePayResponseDTO openPrePay(OpenPrePayRequestDTO openPrePayRequestDTO);

	CflPayResponseDTO cflPay(CflPayRequestDTO requestDTO);

	PassiveScanPayResponseDTO passiveScanPay(PassiveScanPayRequestDTO requestDTO);

}
