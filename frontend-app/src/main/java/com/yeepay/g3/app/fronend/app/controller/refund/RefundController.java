package com.yeepay.g3.app.fronend.app.controller.refund;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.dto.OperationResponse;
import com.yeepay.g3.app.fronend.app.enumtype.PaySystemEnum;
import com.yeepay.g3.app.fronend.app.utils.AjaxCallBack;
import com.yeepay.g3.facade.frontend.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 16/8/11.
 */
@Controller
@RequestMapping("refund")
public class RefundController extends BaseController {

    @RequestMapping("batchRefund")
    public String batchRefund(String params, HttpServletResponse response) throws IOException {
        AjaxCallBack callBack = ajaxValidate(params);
        if (!callBack.getStatus()){
            return outPutJson(callBack,response);
        }
        Map<PaySystemEnum, List<BasicOperationDTO>> operateMap = parseRequest(params);
        OperationResponse opResponse = refundHandler(operateMap);
        callBack.setDetail(opResponse);
        return outPutJson(callBack,response);
    }

    /**
     * 补发分期退款到退款中心
     * @param orderNos
     */
    private void refundInstallment(List orderNos, OperationResponse response) {
        ImOperationRequestDTO requestDTO = buildImRequest(orderNos);
        ImOperationResponseDTO responseDTO = imOpFacade.refundOrders(requestDTO);
        logger.info("[分期发送到退款中心]" + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    private void refundFe(List orderNos, OperationResponse response) {
        FeOperationRequestDTO requestDTO = buildFeOpRequest(orderNos);
        FeOperationResponseDTO responseDTO = feOpFacade.refundOrders(requestDTO);
        logger.info("[FE发送到退款中心]" + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    private OperationResponse refundHandler(Map<PaySystemEnum, List<BasicOperationDTO>> operateMap) {
        OperationResponse response = new OperationResponse();
        for (PaySystemEnum sys : operateMap.keySet()) {
            List<BasicOperationDTO> orders = operateMap.get(sys);
            if (orders == null || orders.size() < 1) {
                continue;
            }
            try {
                if (PaySystemEnum.FE.equals(sys)) {
                    refundFe(orders, response);
                }
                if (PaySystemEnum.CFL.equals(sys)) {
                    refundInstallment(orders, response);
                }
            } catch (Throwable th) {
                List<String> errorList = new ArrayList<String>();
                errorList.add("[" + sys.name() + "] - [补单失败] - " + th);
                response.getErrorList().addAll(errorList);
            }
        }
        return response;
    }
}
