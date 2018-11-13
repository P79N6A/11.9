/**
 *
 */
package com.yeepay.g3.app.fronend.app.controller.mpay;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.enumtype.Source;
import com.yeepay.g3.app.fronend.app.utils.ConstantUtil;
import com.yeepay.g3.app.fronend.app.utils.MemberQueryUtil;
import com.yeepay.mpay.api.WeChatManagementFacade;
import com.yeepay.mpay.api.dto.CustomerConfigReqDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

//import com.yeepay.g3.app.fronend.app.enumtype.Source;
//import com.yeepay.g3.app.fronend.app.utils.ConstantUtil;
//import com.yeepay.g3.app.fronend.app.utils.MemberQueryUtil;
//import com.yeepay.mpay.api.WeChatManagementFacade;
//import com.yeepay.mpay.api.dto.CustomerConfigReqDTO;

/**
 * @author TML
 */

@Controller
@RequestMapping(value = "/customerConfig")
public class CustomerConfigController extends BaseController {

    private static final Log logger = LogFactory.getLog(CustomerConfigController.class);


    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/query")
    public String query(Model model, HttpServletRequest request) {

        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paraName = (String) params.nextElement();
            model.addAttribute(paraName, request.getParameter(paraName));
        }
        model.addAttribute("sourceMap", MemberQueryUtil.getMpaySource());

        return "mpay/order/queryCustomerConfig";
    }


    @RequestMapping(value = "/add")
    public ModelAndView add() {
        ModelAndView rv = new ModelAndView("mpay/order/addCustomerConfig");
        ModelMap model = rv.getModelMap();
        model.addAttribute("sourceMap", MemberQueryUtil.getMpaySource());
        return rv;
    }


    @RequestMapping(value = "/edit")
    public ModelAndView edit(@RequestParam("customerNumber") String customerNumber, Model modela) {
        try {
            WeChatManagementFacade weChatManagementFacade = (WeChatManagementFacade) getFacadeService(WeChatManagementFacade.class, ConstantUtil.getSysConfigValue(ConstantUtil.MPAYURLKEY));
            CustomerConfigReqDTO customerConfigReqDTO = weChatManagementFacade.queryOnlineDirectCustomer(customerNumber);
            int sourceValue = customerConfigReqDTO.getSource() != null ? customerConfigReqDTO.getSource().intValue() : 0;
            List<String> sourceList = Source.getSourceList(sourceValue);
            Map<String, Source> sourceMap = MemberQueryUtil.getMpaySource();
            for (String str : sourceList) {
                if (sourceMap.get(str) != null) {
                    sourceMap.get(str).setSelect(true);
                }
            }

            ModelAndView mo = new ModelAndView("mpay/order/editCustomerConfig");
            // 设置属性
            ModelMap model = mo.getModelMap();
            model.addAttribute("sourceMap", sourceMap);
            model.addAttribute("customerConfigReqDTO", customerConfigReqDTO);
            return mo;
        } catch (Exception e) {
            logger.error("customerNumber: " + customerNumber, e);
            return null;
        }
    }


    /**
     * 修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public String update(@ModelAttribute("param") CustomerConfigReqDTO param, @RequestParam("sourceStr") String sourceStr, HttpServletResponse response) throws IOException {
        try {
            WeChatManagementFacade weChatManagementFacade = null;
//	        msg = this.validateParams(param.getSmsTempId(), param.getSmsTempContent(), param.getRemark());

            Long source = Source.getSourceArr(sourceStr);
            param.setSource(source);
            weChatManagementFacade = (WeChatManagementFacade) getFacadeService(WeChatManagementFacade.class, ConstantUtil.getSysConfigValue(ConstantUtil.MPAYURLKEY));
            weChatManagementFacade.updateOnlineDirectCustomer(param);
            return "操作成功";
        } catch (Exception e) {
            logger.error("customerNumber: " + param.getCustomerNumber(), e);
            return e.getMessage();
        }
    }


    /**
     * 保存
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public String save(@ModelAttribute("param") CustomerConfigReqDTO param, @RequestParam("sourceStr") String sourceStr) {
        String msg = null;
        try {
            Long source = Source.getSourceArr(sourceStr);
            param.setSource(source);
            WeChatManagementFacade weChatManagementFacade = (WeChatManagementFacade) getFacadeService(WeChatManagementFacade.class, ConstantUtil.getSysConfigValue(ConstantUtil.MPAYURLKEY));
            weChatManagementFacade.addOnlineDirectCustomer(param);
            msg = "操作成功";
            return msg;
        } catch (Exception e) {
            logger.error("customerNumber: " + param.getCustomerNumber(), e);
            msg = e.getMessage();
        }
        return msg;
    }


}
