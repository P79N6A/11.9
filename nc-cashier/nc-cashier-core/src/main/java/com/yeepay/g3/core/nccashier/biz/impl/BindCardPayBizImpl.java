package com.yeepay.g3.core.nccashier.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.yeepay.g3.core.nccashier.biz.BindCardPayBiz;
import com.yeepay.g3.core.nccashier.biz.NcCashierCoreBiz;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.OrderPaymentService;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.FirstBindCardPayResponseDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.CashierBindPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPayResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.CashierPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedBankCardDTO;
import com.yeepay.g3.facade.nccashier.dto.NeedSurportDTO;
import com.yeepay.g3.facade.nccashier.dto.ProcessStatusEnum;
import com.yeepay.g3.facade.nccashier.enumtype.NCCashierOrderTypeEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.ncpay.dto.PaymentResponseDTO;
import com.yeepay.g3.facade.ncpay.enumtype.CardInfoTypeEnum;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.StringUtils;

@Component("bindCardPayBiz")
public class BindCardPayBizImpl extends NcCashierBaseBizImpl implements BindCardPayBiz {

	@Resource
	private OrderPaymentService orderPaymentService;


	@Resource
	private NcCashierCoreBiz ncCashierCoreBiz;

	@Override
	public FirstBindCardPayResponseDTO createPayment(FirstBindCardPayRequestDTO requestDTO) {
		// 参数校验
		FirstBindCardPayResponseDTO response = new FirstBindCardPayResponseDTO();
		try {
			validateBindCardFirstPay(requestDTO);
			CashierPaymentRequestDTO paymentRequestDto = buildCashierPaymentRequestDTO(requestDTO);
			CombinedPaymentDTO combinedPaymentDto = orderPaymentService.validateBindPayBusinInfo(paymentRequestDto);
			PaymentResponseDTO paymentResponseDTO = null;
			if (combinedPaymentDto.isNeedOrderRecord()) {
				orderPaymentService.doBindtPayCreatePayment(paymentRequestDto, combinedPaymentDto);
			}
			paymentResponseDTO = orderPaymentService.callNcPayOrder(paymentRequestDto, combinedPaymentDto,
					CardInfoTypeEnum.BIND);
			orderPaymentService.supplyBindPayOrderResult(paymentRequestDto, paymentResponseDTO, response,
					combinedPaymentDto.getPaymentRequest(), requestDTO.getNeedBankCardDTO());
			response.setRecordId(combinedPaymentDto.getPaymentRecord().getId());
			response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
		} catch (Throwable t) {
			handleException(response, t);
		}
		return response;
	}

	private CashierPaymentRequestDTO buildCashierPaymentRequestDTO(FirstBindCardPayRequestDTO requestDTO) {
		CashierPaymentRequestDTO cashierPaymentRequestDTO = new CashierPaymentRequestDTO();
		BeanUtils.copyProperties(requestDTO, cashierPaymentRequestDTO);
		cashierPaymentRequestDTO.setOrderType(NCCashierOrderTypeEnum.BIND);
		cashierPaymentRequestDTO.setPayTool(PayTool.BK_ZF.name());
		cashierPaymentRequestDTO.setBkFirst(true);
		return cashierPaymentRequestDTO;
	}

	private void validateBindCardFirstPay(FirstBindCardPayRequestDTO requestDTO) {
		BeanValidator.validate(requestDTO);
		NcCashierLoggerFactory.TAG_LOCAL.set(
				"[createPayment],支付请求ID=" + requestDTO.getRequestId() + ",支付记录ID=" + requestDTO.getRecordId() + "");
	}

	@Override
	public FirstBindCardPayResponseDTO firstPay(FirstBindCardPayRequestDTO requestDTO) {

		FirstBindCardPayResponseDTO responseDTO = createPayment(requestDTO);
		if (responseDTO == null || StringUtils.isNotBlank(responseDTO.getReturnCode())) {
			return responseDTO;
		}
		// needItemMock(responseDTO);
		if (responseDTO.getReqSmsSendTypeEnum() == null && !responseDTO.isLoseNeedItem()) {
			// 无需补充项也无需短验证，调用确认支付接口
			CashierBindPayRequestDTO request = buildCashierBindPayRequestDTO(requestDTO, responseDTO);
			CashierPayResponseDTO payResponse = ncCashierCoreBiz.bindPay(request);
			supplyFirstPayResponse(responseDTO, payResponse);
		}
		if(responseDTO.isLoseNeedItem()){
			RedisTemplate.setCacheObjectSumValue(Constant.BKZF_FIRST_REDIS_KEY + responseDTO.getRecordId(), Constant.BKZF_FIRST_PAY, Constant.FIVE_MINUTE);
		}
		return responseDTO;
	}

	private void supplyFirstPayResponse(FirstBindCardPayResponseDTO responseDTO, CashierPayResponseDTO payResponse) {
		if (payResponse != null && ProcessStatusEnum.SUCCESS.equals(payResponse.getProcessStatusEnum())) {
			responseDTO.setFinishPay(true);
		}else {
			responseDTO.setReturnCode(payResponse.getReturnCode());
			responseDTO.setReturnMsg(payResponse.getReturnMsg());
			responseDTO.setProcessStatusEnum(ProcessStatusEnum.FAILED);
		}
	}

	private CashierBindPayRequestDTO buildCashierBindPayRequestDTO(FirstBindCardPayRequestDTO requestDTO,
			FirstBindCardPayResponseDTO responseDTO) {
		CashierBindPayRequestDTO request = new CashierBindPayRequestDTO();
		request.setBindId(String.valueOf(requestDTO.getBindId()));
		request.setBkFirst(true);
		request.setNeedBankCardDTO(responseDTO.getNeedBankCardDto());
		request.setPaymentRecordId(responseDTO.getRecordId());
		request.setPaymentRequestId(requestDTO.getRequestId());
		request.setTokenId(requestDTO.getTokenId());
		return request;
	}

	/**
	 * mock测试数据-模拟有补充项的情况
	 * 
	 * @param responseDTO
	 */
	public void needItemMock(FirstBindCardPayResponseDTO responseDTO) {
		NeedBankCardDTO needBankCardDTO = responseDTO.getNeedBankCardDto();
		NeedSurportDTO needSurportDTO = needBankCardDTO.getNeedSurportDTO();
		if (needSurportDTO == null) {
			needSurportDTO = new NeedSurportDTO();
		}
		needSurportDTO.setIdnoIsNeed(true);
		needBankCardDTO.setNeedSurportDTO(needSurportDTO);
		responseDTO.setNeedBankCardDto(needBankCardDTO);
		responseDTO.setLoseNeedItem(true);
	}

}
