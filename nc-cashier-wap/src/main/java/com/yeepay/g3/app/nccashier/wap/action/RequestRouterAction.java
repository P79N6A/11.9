package com.yeepay.g3.app.nccashier.wap.action;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.ExceptionUtil;
import com.yeepay.g3.facade.nccashier.dto.CheckRefferRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.NewOrderRequestResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.facade.nccashier.dto.UserAccessDTO;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 统一收银台支付请求总路由
 * @author zhen.tan
 *
 */
@Controller
@RequestMapping(value = "/request")
public class RequestRouterAction extends WapBaseAction {
	
	 
	@Resource
	private NcCashierService ncCashierService;
	 
	@RequestMapping(value = "/router")
	public Object router(String uniqueOrderNo,String hmac, HttpServletRequest request){
		String userToken = null;
		NewOrderRequestResponseDTO response = null;
		UserAccessDTO userAccess = null;
		try {
			userToken = UUID.randomUUID().toString();
			String bizType = request.getParameter("bizType");
			int selectOrderSysNo =  validateBizType(bizType);
			
			//大算订单加YOP验签
			if(selectOrderSysNo == 0){
				boolean success = ncCashierService.yopVerify(null, uniqueOrderNo, hmac);
				if(!success){
					throw new CashierBusinessException(Errors.SECURITY_ERROR.getCode(), Errors.SECURITY_ERROR.getMsg());
				}
			}
			RedirectView view = null;
            //获取用户IP
            String ypip = getUserIp(request);
            // 获取用户终端UA
            String ua = getUserUA(request);
          
            if (ua.length() > 300) {
                ua = ua.substring(0, 300);
            }
            
            response = ncCashierService.newOrderRequest(uniqueOrderNo,selectOrderSysNo);

			userAccess = new UserAccessDTO();
            userAccess.setPaymentRequestId(response.getRequestId()+"");
            userAccess.setUserIp(ypip);
            userAccess.setUserUa(ua);
            userAccess.setTokenId(userToken);
            userAccess.setMerchantNo(response.getMerchantAccountCode());
            ncCashierService.saveUserAccess(userAccess);
           
            String reffer = request.getHeader("referer");
            if(StringUtils.isNotBlank(reffer) && !CommonUtil.regexRealm(reffer)){
            	 CheckRefferRequestDTO requestDTO = new CheckRefferRequestDTO();
                 requestDTO.setBizOrderId(response.getUniqueOrderNo());
                 requestDTO.setMerchantAccountCode(response.getMerchantAccountCode());
                 requestDTO.setReffer(reffer);
                 ncCashierService.checkReffer(requestDTO);
            }
            
			if(response.getVersion() == CashierVersionEnum.PC){
				view = new RedirectView(CommonUtil.getPreUrl(response.getMerchantAccountCode(),request)+newpcctx + "/request/"+userToken);
			}else{
				view = new RedirectView(CommonUtil.getPreUrl(response.getMerchantAccountCode(),request)+wapctx + "/request/"+response.getMerchantAccountCode()+"/"+response.getEncodeRequestId()+"?token="+ userToken);
			}
			
			return view;
		} catch (Throwable e) {
			// 跳转到出错页面
			RequestInfoDTO info = getErrorOrderInfo(userAccess);
			if (response!=null && CashierVersionEnum.WAP.equals(response.getVersion())) {
				CashierBusinessException ex = ExceptionUtil.handleException(e, SysCodeEnum.NCCASHIER_WAP);
				return createErrorMV(userToken, ex.getDefineCode(), ex.getMessage(), info);
			} else {
				return createNewPcErrorRV(userToken, false, "router_error", e, SysCodeEnum.NCCASHIER_PC, info);
			}
		}
	}
	 
	private int validateBizType(String bizType){
		int selectOrderSysNo = 0;
		if(StringUtils.isNotBlank(bizType)){
			selectOrderSysNo = Integer.parseInt(bizType);
			if(selectOrderSysNo!=2 && selectOrderSysNo!=0){
				throw new CashierBusinessException(Errors.ORDER_SYS_NO.getCode(),Errors.ORDER_SYS_NO.getMsg());
			}
		}
		return selectOrderSysNo;
	}
	
}
