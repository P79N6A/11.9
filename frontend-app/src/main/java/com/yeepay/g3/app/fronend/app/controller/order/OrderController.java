package com.yeepay.g3.app.fronend.app.controller.order;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.dto.OperationResponse;
import com.yeepay.g3.app.fronend.app.enumtype.PaySystemEnum;
import com.yeepay.g3.app.fronend.app.utils.AjaxCallBack;
import com.yeepay.g3.facade.frontend.dto.*;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mingzong.liu
 * @createDate 16/7/18.
 */
@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

    @RequestMapping("queryOrder")
    public String queryOrder(HttpServletRequest request, Model model) throws Exception {
        logger.info("order query start...");
        String initQuery = String.valueOf(request.getParameterMap().get("initQuery"));
        if (StringUtils.isNotBlank(initQuery) && !"null".equals(initQuery)){//如果是第一次请求的话,初始化查询时间
            String nowDate = DateUtils.getShortDateStr() + " 00:00:00";
            String endDate = DateUtils.getShortDateStr(DateUtils.addDay(new Date(),1)) + " 00:00:00";
            request.getParameterMap().put("dateStart", nowDate);
            request.getParameterMap().put("dateEnd", endDate);
        }
        initQueryParams(model);
        initUser(request,model);
        return "order/queryOrder";
    }

    /**
     * 批量补单
     * @param params
     * @return
     */
    @RequestMapping("batchRepair")
    public String batchRepair(String params, HttpServletResponse response) throws IOException {
        AjaxCallBack callBack = ajaxValidate(params);
        if (!callBack.getStatus()){
            return outPutJson(callBack,response);
        }
        Map<PaySystemEnum, List<BasicOperationDTO>> operateMap = parseRequest(params);
        OperationResponse opResponse = repairHandler(operateMap);
        callBack.setDetail(opResponse);
        return outPutJson(callBack,response);
    }

    /**
     * 批量补单处理
     * @param operateMap
     * @return
     */
    private OperationResponse repairHandler(Map<PaySystemEnum, List<BasicOperationDTO>> operateMap) {
        OperationResponse response = new OperationResponse();
        for (PaySystemEnum sys : operateMap.keySet()) {
            List<BasicOperationDTO> orders = operateMap.get(sys);
            if (orders == null || orders.size() < 1) {
                continue;
            }
            try {
                if (PaySystemEnum.FE.equals(sys)) {
                    repairFe(orders, response);
                }
                if (PaySystemEnum.NET.equals(sys)) {
                    repairNet(orders, response);
                }
                if (PaySystemEnum.CFL.equals(sys)) {
                    repairInstallment(orders, response);
                }
            } catch (Throwable th) {
                logger.error("[补单失败]", th);
                List<String> errorList = new ArrayList<String>();
                errorList.add("[" + sys.name() + "] - [补单失败] - " + th.getMessage());
                response.getErrorList().addAll(errorList);
            }
        }
        return response;
    }

    /**
     * 分期补单
     *
     * @param orderNos
     * @return
     */
    private void repairInstallment(List orderNos, OperationResponse response) {
        ImOperationRequestDTO requestDTO = buildImRequest(orderNos);
        ImOperationResponseDTO responseDTO = imOpFacade.repairOrders(requestDTO);
        logger.info("[分期补单返回结果] " + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    /**
     * FE补单
     *
     * @param orderNos
     * @return
     */
    private void repairFe(List orderNos, OperationResponse response) {
        FeOperationRequestDTO requestDTO = buildFeOpRequest(orderNos);
        FeOperationResponseDTO responseDTO = feOpFacade.repairOrders(requestDTO);
        logger.info("[FE补单返回结果] " + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    /**
     * 网银补单
     *
     * @param orderNos
     * @return
     */
    private void repairNet(List orderNos, OperationResponse response) {
        NetOperationRequestDTO requestDTO = buildNetOpRequest(orderNos);
        NetOperationResponseDTO responseDTO = netOpFacade.repairOrders(requestDTO);
        logger.info("[网银补单返回结果] " + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    private void reNotifyFe(List orders, OperationResponse response) {
        FeOperationResponseDTO responseDTO = feOpFacade.reNotifyOrders(buildFeOpRequest(orders));
        logger.info("[FE补发通知返回结果]" + response.toString());
        updateResponse(response, responseDTO);
    }

    private void reNotifyNet(List orders, OperationResponse response) {
        NetOperationResponseDTO responseDTO = netOpFacade.reNotifyOrders(buildNetOpRequest(orders));
        logger.info("[网银补发通知返回结果]" + response.toString());
        updateResponse(response, responseDTO);
    }

    /**
     * 分期补发通知
     */
    private void reNotifyInstallment(List orders, OperationResponse response) {
        ImOperationResponseDTO responseDTO = imOpFacade.reNotifyOrders(buildImRequest(orders));
        logger.info("[分期补发通知返回结果]" + responseDTO.toString());
        updateResponse(response, responseDTO);
    }

    /**
     * 批量补发通知
     * @param params
     * @return
     */
    @RequestMapping("batchReNotify")
    public String batchReNotify(String params,HttpServletResponse response) throws IOException {
        AjaxCallBack callBack = ajaxValidate(params);
        if (!callBack.getStatus()){
            return outPutJson(callBack,response);
        }
        Map<PaySystemEnum, List<BasicOperationDTO>> operateMap = parseRequest(params);
        OperationResponse opResponse = reNotifyHandler(operateMap);
        callBack.setDetail(opResponse);
        return outPutJson(callBack,response);
    }

    /**
     * 补发通知处理
     * @param operateMap
     * @return
     */
    private OperationResponse reNotifyHandler(Map<PaySystemEnum, List<BasicOperationDTO>> operateMap) {
        OperationResponse response = new OperationResponse();
        for (PaySystemEnum sys : operateMap.keySet()) {
            List<BasicOperationDTO> orders = operateMap.get(sys);
            if (orders == null || orders.size() < 1) {
                continue;
            }
            try {
                if (PaySystemEnum.FE.equals(sys)) {
                    reNotifyFe(orders, response);
                }
                if (PaySystemEnum.NET.equals(sys)) {
                    reNotifyNet(orders, response);
                }
                if (PaySystemEnum.CFL.equals(sys)) {
                    reNotifyInstallment(orders, response);
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
