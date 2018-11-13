package com.yeepay.g3.app.fronend.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import com.caucho.hessian.client.HessianProxyFactory;
import com.yeepay.g3.app.frame.utils.PermissionUtils;
import com.yeepay.g3.app.fronend.app.dto.OperationResponse;
import com.yeepay.g3.app.fronend.app.enumtype.PaySystemEnum;
import com.yeepay.g3.app.fronend.app.utils.AjaxCallBack;
import com.yeepay.g3.app.fronend.app.utils.FrontEndConfigUtils;
import com.yeepay.g3.app.fronend.app.utils.MemberQueryUtil;
import com.yeepay.g3.facade.employee.dto.sso.DataDictDTO;
import com.yeepay.g3.facade.employee.user.dto.UserDTO;
import com.yeepay.g3.facade.frontend.dto.BasicOperationDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FeOperationResponseDTO;
import com.yeepay.g3.facade.frontend.dto.ImOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.ImOperationResponseDTO;
import com.yeepay.g3.facade.frontend.dto.NetOperationRequestDTO;
import com.yeepay.g3.facade.frontend.dto.NetOperationResponseDTO;
import com.yeepay.g3.facade.frontend.facade.FrontendOperationFacade;
import com.yeepay.g3.facade.frontend.installment.facade.InstallmentOperationFacade;
import com.yeepay.g3.facade.frontend.netpay.facade.NetPayOperationFacade;
import com.yeepay.g3.utils.common.json.JSONObject;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;


/**
 * Created by lewis on 16/4/25.
 */
public class BaseController {
    protected Logger logger= LoggerFactory.getLogger(this.getClass());

    protected PermissionUtils permissionUtils=new PermissionUtils();

    protected static final String SPLIT_COMMA = ",";
    protected static final String SPLIT_LINE = "-";

    protected InstallmentOperationFacade imOpFacade = RemoteServiceFactory.getService(InstallmentOperationFacade.class);
    protected FrontendOperationFacade feOpFacade = RemoteServiceFactory.getService(FrontendOperationFacade.class);
    protected NetPayOperationFacade netOpFacade = RemoteServiceFactory.getService(NetPayOperationFacade.class);

    protected AjaxCallBack ajaxValidate(String params){
        AjaxCallBack callBack=new AjaxCallBack();
        if (StringUtils.isBlank(params)){
            callBack.setStatus(false);
            callBack.setDetail("操作失败:参数不能为空");
            return callBack;
        }
        callBack.setStatus(true);
        return  callBack;
    }

    protected String outPutJson(Object params,HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        JSONObject json = (JSONObject) JSONObject.wrap(params);
        response.getWriter().write(json.toString());
        return null;
    }

    public PermissionUtils getPermissionUtils() {
        return permissionUtils;
    }

    public void setPermissionUtils(PermissionUtils permissionUtils) {
        this.permissionUtils = permissionUtils;
    }

    public <T>T getRemoteService(Class<T> T){
        return (T) RemoteServiceFactory.getService(T.getClass());
    }

    /**
     * 获取指定的facadeService
     * @param  c
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object getFacadeService(Class c,String url) throws Exception{
        HessianProxyFactory factory = new HessianProxyFactory();
        try {
            Object obj= factory.create(c, url);
            return obj;
        } catch (Throwable ta) {
            ta.printStackTrace();
            throw new Exception("系统调用异常");
        }
    }

    /**
     * 初始化页面
     * @param model
     */
    protected void initQueryParams(Model model){
        long queryStart = System.currentTimeMillis();
        model.addAttribute("requestSystemMap", MemberQueryUtil.getFrontRequestSystem());//业务方
        model.addAttribute("payStatusMap", MemberQueryUtil.getFrontPayStatus());//支付状态
        model.addAttribute("orderTypeMap",MemberQueryUtil.getFrontOrderType());//支付方式
        model.addAttribute("notifyStatusMap",MemberQueryUtil.getFrontNotifyStatus());//通知状态
        model.addAttribute("bankMap",MemberQueryUtil.getFrontBankList());//银行卡
        model.addAttribute("cardTypeMap",MemberQueryUtil.getFrontCardType());//卡种
        model.addAttribute("refundStatusMap",MemberQueryUtil.getFrontRefundStatus());//退款状态
        model.addAttribute("refundTypeMap",MemberQueryUtil.getFrontRefundType());//退款类型
        model.addAttribute("platformMap",MemberQueryUtil.getFrontPlatform());//平台类型
        model.addAttribute("paymentProductMap", MemberQueryUtil.getFrontPayProduct());
        model.addAttribute("orderSystemMap", MemberQueryUtil.getFrontOrderSystem());
        long queryEnd = System.currentTimeMillis();
        model.addAttribute("querySpent",queryEnd-queryStart);//查询耗时
    }

    /**
     * 获取登陆用户,并判断是否显示成本
     * @param request
     * @param model
     */
    protected void initUser(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
//        if (session == null) {
//            session = ServletActionContext.getRequest().getSession(true);
//        }
        Object obj = session.getAttribute(DataDictDTO.SESSION_USERINFO);
        if (obj == null) {
            return;
        }
        UserDTO userDto = (UserDTO) obj;
        if (userDto == null){
            return;
        }
        logger.info("登陆用户:" + userDto.toString());
        //显示银行成本
        List<String> users = (List) FrontEndConfigUtils.getSysConfig(FrontEndConfigUtils.FE_BANK_COST_USER);
        if (users == null || users.size()<1)
            return;
        for (String user : users){
            if (user.equals(userDto.getLoginName())){
                model.addAttribute("showCost",true);
                break;
            }
        }
    }

    /**
     * 组装FE运营请求参数
     * @param operationDTOs
     * @return
     */
    protected FeOperationRequestDTO buildFeOpRequest(List<BasicOperationDTO> operationDTOs) {
        FeOperationRequestDTO requestDTO = new FeOperationRequestDTO();
        requestDTO.setOrderNos(operationDTOs);
        return requestDTO;
    }

    /**
     * 网银运营请求参数
     * @param operationDTOs
     * @return
     */
    protected NetOperationRequestDTO buildNetOpRequest(List<BasicOperationDTO> operationDTOs) {
        NetOperationRequestDTO requestDTO = new NetOperationRequestDTO();
        requestDTO.setOrderNos(operationDTOs);
        return requestDTO;
    }

    /**
     * 分期运营请求参数
     * @param operationDTOs
     * @return
     */
    protected ImOperationRequestDTO buildImRequest(List<BasicOperationDTO> operationDTOs) {
        ImOperationRequestDTO requestDTO = new ImOperationRequestDTO();
        requestDTO.setOrderNos(operationDTOs);
        return requestDTO;
    }

    protected Map<PaySystemEnum, List<BasicOperationDTO>> parseRequest(String params) {
        Map<PaySystemEnum, List<BasicOperationDTO>> map = new HashMap<PaySystemEnum, List<BasicOperationDTO>>();
        String[] paramArray = params.split(SPLIT_COMMA);
        for (String param : paramArray){
            String[] values = param.split(SPLIT_LINE);
            if (values == null || values.length != 3)
                continue;
            BasicOperationDTO operationDTO = new BasicOperationDTO(values[0], values[1], values[2]);
            PaySystemEnum currentSys = PaySystemEnum.getPaySystem(values[2]);
            for (PaySystemEnum sys : PaySystemEnum.values()) {
                if (sys.equals(currentSys)) {
                    List<BasicOperationDTO> orders = map.get(sys);
                    if (orders == null) {
                        orders = new ArrayList<BasicOperationDTO>();;
                    }
                    orders.add(operationDTO);
                    map.put(sys, orders);
                    break;
                }
            }
        }
        return map;
    }

    protected void updateResponse(OperationResponse response, Object object) {
        if (object instanceof FeOperationResponseDTO) {
            FeOperationResponseDTO responseDTO = (FeOperationResponseDTO) object;
            if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
                throw new IllegalArgumentException(responseDTO.getResponseMsg());
            }
            response.setSuccess(responseDTO.getSuccess() + response.getSuccess());
            response.setIgnore(responseDTO.getIgnore() + response.getIgnore());
            response.getErrorList().addAll(responseDTO.getErrorList());
        }
        if (object instanceof NetOperationResponseDTO) {
            NetOperationResponseDTO responseDTO = (NetOperationResponseDTO) object;
            if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
                throw new IllegalArgumentException(responseDTO.getResponseMsg());
            }
            response.setSuccess(responseDTO.getSuccess() + response.getSuccess());
            response.setIgnore(responseDTO.getIgnore() + response.getIgnore());
            response.getErrorList().addAll(responseDTO.getErrorList());
        }
        if (object instanceof ImOperationResponseDTO) {
            ImOperationResponseDTO responseDTO = (ImOperationResponseDTO) object;
            if (StringUtils.isNotBlank(responseDTO.getResponseCode())) {
                throw new IllegalArgumentException(responseDTO.getResponseMsg());
            }
            response.setSuccess(responseDTO.getSuccess() + response.getSuccess());
            response.setIgnore(responseDTO.getIgnore() + response.getIgnore());
            response.getErrorList().addAll(responseDTO.getErrorList());
        }
    }
}
