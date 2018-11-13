/**
 *
 */
package com.yeepay.g3.app.fronend.app.controller.mpay;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.utils.MemberQueryUtil;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * WX支付订单管理
 *
 * @author TML
 */
@Controller
@RequestMapping(value = "/mpayOrder")
public class MpayOrderController extends BaseController {

    private static final Log logger = LogFactory.getLog(MpayOrderController.class);


    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    public String query(Model model, HttpServletRequest request) {
        logger.debug("开始进入mpay订单查询");

        model.addAttribute("orderStatusMap", MemberQueryUtil.getMpayOrderStatus());
        model.addAttribute("orderTypeMap", MemberQueryUtil.getMpayOrderType());
        model.addAttribute("sourceMap", MemberQueryUtil.getMpaySourceDesc());

        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paraName = (String) params.nextElement();
            model.addAttribute(paraName, request.getParameter(paraName));
        }

        return "mpay/order/queryMpayOrder";
    }


    /**
     * 获取指定的facadeService
     *
     * @param c
     * @return
     * @author meiling.zhuang
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object getFacadeService(Class c) {
        return RemoteServiceFactory.getService(c);
    }
}
