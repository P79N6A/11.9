package com.yeepay.g3.core.nccashier.biz;

import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteCancelResDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteReqDTO;
import com.yeepay.g3.facade.nccashier.dto.api.APIPreauthCompleteResDTO;

/**
 * 预授权biz层声明，<include> 预授权下单、预授权发短验证、预授权确认支付、预授权撤销、预授权完成、预授权完成撤销三个功能
 * 
 * @author duangduang
 *
 */
public interface APIPreauthBiz {

	/**
	 * 预授权下单
	 * @param requestDTO
	 * @return
	 */
	APIPreauthPaymentResponseDTO preAuthFirstRequest(APIPreauthFirstRequestDTO requestDTO);


	/**
	 * 预授权绑卡下单
	 * @param requestDTO
	 * @return
	 */
	APIPreauthPaymentResponseDTO preAuthBindRequest(APIPreauthBindRequestDTO requestDTO);

	/**
	 * 预授权发短验
	 * @param requestDTO
	 * @return
	 */
	APIBasicResponseDTO preauthSmsSend(APIPreauthSmsSendRequestDTO requestDTO);

	/**
	 * 预授权确认支付
	 * @param requestDTO
	 * @return
	 */
	APIPreauthConfirmResponseDTO preAuthOrderConfirm(APIPreauthConfirmRequestDTO requestDTO);

	/**
	 * 预授权撤销
	 *
	 * @param requestDTO
	 * @return
	 */
	APIPreauthResponseDTO preauthCancel(APIBasicRequestDTO requestDTO);

	/**
	 * 预授权完成接口
	 *
	 * @param requestDTO
	 * @return
	 */
	APIPreauthCompleteResponseDTO preauthComplete(APIBasicRequestDTO requestDTO);

	/**
	 * 预授权完成撤销接口
	 *
	 * @param requestDTO
	 * @return
	 */
	APIPreauthResponseDTO preauthCompleteCancel(APIBasicRequestDTO requestDTO);
	
	/**
	 * 预授权完成
	 * @param reqDTO
	 * @return
	 */
	APIPreauthCompleteResDTO complete(APIPreauthCompleteReqDTO reqDTO);
	
	/**
	 * 预授权撤销
	 * 
	 * @param reqDTO
	 * @return
	 */
	APIPreauthCancelResDTO cancle(APIPreauthCancelReqDTO reqDTO);
	
	/**
	 * 预授权完成撤销
	 * 
	 * @param reqDTO
	 * @return
	 */
	APIPreauthCompleteCancelResDTO completeCancle(APIPreauthCompleteCancelReqDTO reqDTO);
}
