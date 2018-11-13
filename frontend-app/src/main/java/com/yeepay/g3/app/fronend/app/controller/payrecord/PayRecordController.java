package com.yeepay.g3.app.fronend.app.controller.payrecord;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chronos.
 * @createDate 16/8/5.
 */
@Controller
@RequestMapping("payRecord")
public class PayRecordController extends BaseController {

    @RequestMapping("queryDetail")
    public String queryDetail(String orderNo, HttpServletRequest request){
        if (StringUtils.isBlank(orderNo))
            return "order/queryOrder";
        request.getParameterMap().put("orderNo",orderNo);
        return "payrecord/detail";
    }

}
