package com.yeepay.g3.facade.payprocessor.facade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.frontend.enumtype.BankAccountType;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import com.yeepay.g3.facade.payprocessor.dto.BasicRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OpenPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;

/**
 * @author chronos.
 * @createDate 2016/11/15.
 */
public class PayOrderFacadeTestDeploy extends BaseTest {
//    @Autowired
//    private PayOrderFacade payOrderFacade;

    protected  PayOrderFacade payOrderFacade = RemoteFacadeProxyFactory.getService(PayOrderFacade.class, ExternalSystem.PP);

    @Test
    public void openRequest() throws Exception {
        OpenPayRequestDTO requestDTO = new OpenPayRequestDTO();
        baseSet(requestDTO);
        requestDTO.setCustomerLevel("V");
        requestDTO.setPlatformType(PlatformType.WECHAT);
        requestDTO.setPayBusinessType(PayBusinessType.DC);
        requestDTO.setPayOrderType(PayOrderType.ACTIVESCAN);
        requestDTO.setPageCallBack("http://www.baidu.com");
        requestDTO.setPayProduct("NCPAY");
        requestDTO.setRetailProductCode("3011001006003A");
        requestDTO.setBasicProductCode("3011001006003");
        requestDTO.setAppId("wx123456789");
        requestDTO.setOpenId("och7GjvYlkgK9kZSDa1bKQsEKRBs");
        requestDTO.setBankTotalCost(new BigDecimal(100));
        requestDTO.setReportMerchantNo("11111");
        requestDTO.setPayInterface("BJCCB_NET_OPEN_C6612");
        Map<String, String> extParam = new HashMap<String, String>();
        String specifyChannelCodes = "AAAAAA|BBBBBB|CCCCCC";
        extParam.put("specifyChannelCodes", specifyChannelCodes);
//		requestDTO.setExtParam(extParam);
        System.out.println(requestDTO.toString());
        OpenPayResponseDTO responseDTO = payOrderFacade.openRequest(requestDTO);
        System.out.println(responseDTO.toString());
    }
    @Ignore
    @Test
    public void onlineRequest() throws Exception {
        NetPayRequestDTO requestDTO = new NetPayRequestDTO();
        baseSet(requestDTO);
        requestDTO.setBankAccountType(BankAccountType.INDIVIDUAL);
        requestDTO.setBankId("BOC");
        requestDTO.setCustomerLevel("A");
        requestDTO.setPayBusinessType(PayBusinessType.DC);
        requestDTO.setPageCallBack("http://www.baidu.com");
        requestDTO.setPayOrderType(PayOrderType.NET);
        System.out.println(requestDTO.toString());
        NetPayResponseDTO netPayResponseDTO = payOrderFacade.onlineRequest(requestDTO);
        System.out.println(netPayResponseDTO.toString());
    }

    @Test
    public void ncRequest() throws Exception {

    }

    /**
     * 基础参数设置
     *
     * @param requestDTO
     */
    private void baseSet(BasicRequestDTO requestDTO) {
        requestDTO.setCustomerNumber("10040007800");
        requestDTO.setOutTradeNo("TEST" + RandomUtils.nextInt());
        requestDTO.setAmount(new BigDecimal("0.01"));
        requestDTO.setCustomerName("CET4");
        requestDTO.setRequestSystem("NCCASHIER");
        requestDTO.setOrderNo("order" + RandomUtils.nextInt());
        requestDTO.setOrderSystem("NCCASHIER");
        requestDTO.setRequestSysId("req" + RandomUtils.nextInt());
        requestDTO.setCashierVersion(CashierVersion.WEB);
        //requestDTO.setPayScene("123");
        requestDTO.setProductName("DOCKER服务器");
        requestDTO.setPayerIp("172.168.123.13");
        requestDTO.setOrderSystem("DS");
//        requestDTO.setDealUniqueSerialNo("TEST1004007708" + RandomUtils.nextInt());
        requestDTO.setDealUniqueSerialNo("deal" + System.currentTimeMillis());
        requestDTO.setRiskProduction("NCCASHIER");
       

    }
}
