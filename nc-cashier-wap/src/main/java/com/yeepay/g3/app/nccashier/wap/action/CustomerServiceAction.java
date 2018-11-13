package com.yeepay.g3.app.nccashier.wap.action;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NewWapPayService;
import com.yeepay.g3.app.nccashier.wap.vo.CustomerServiceResponseVO;
import com.yeepay.g3.facade.nccashier.dto.PayExtendInfo;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.BeanUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Controller
@RequestMapping(value = "/helper", method = {RequestMethod.POST, RequestMethod.GET})
public class CustomerServiceAction extends WapBaseAction{
	
	private static final Logger Log = LoggerFactory.getLogger(CustomerServiceAction.class);
	
	@Autowired
	private NewWapPayService newWapPayService;
	
	@Autowired
	private NcCashierService ncCashierService;

	@RequestMapping(value = "/customerservice/{token}")
	public @ResponseBody void customerService(@PathVariable("token") String token, String errMsg, HttpServletResponse response){
		CustomerServiceResponseVO resData = getCustomerServiceInitMessage(token, errMsg);
		String initMsg = resData.getCustomerServiceInitMessage();
		Log.info("传给客服的订单信息:{}" , initMsg);
		// 客服不支持加引号
		ajaxResultWrite(response, initMsg);
	}
	
	protected void ajaxResultWrite(HttpServletResponse response, String data) {
		PrintWriter out = null;
		try{
			out = response.getWriter();
			out.println(data);
			out.flush();
		}catch (IOException e){
			Log.error("httpResponse writes error,e:{}",e);
		}
		finally{
			if (out!=null) {
				out.close();
			}
		}
	}

	
	
	/**
	 * 获取想给客服系统的订单信息
	 * 
	 * @param token
	 * @param errMsg
	 * @return
	 */
	private CustomerServiceResponseVO getCustomerServiceInitMessage(String token, String errMsg) {
		CustomerServiceResponseVO resData = new CustomerServiceResponseVO();

		try {
			RequestInfoDTO requestInfo = newWapPayService.validateRequestInfoDTO(token);
			BeanUtils.copyProperties(requestInfo, resData);
			resData.setErrMsg(errMsg);

			PayExtendInfo extendInfo = ncCashierService.getPayExtendInfo(requestInfo.getPaymentRequestId(), token);
			if (extendInfo != null) {
				resData.buildCashierVersion(extendInfo.getCashierVersion());
				resData.buildPayTool(extendInfo);
			}

		} catch (Throwable t) {
			Log.error("获取订单信息异常token=" + token, t);
			if (t instanceof CashierBusinessException) {
				resData.setErrMsg(((CashierBusinessException) t).getMessage());
			} else {
				resData.setErrMsg(Errors.SYSTEM_EXCEPTION.getMsg());
			}
		}

		return resData;
	}
}
