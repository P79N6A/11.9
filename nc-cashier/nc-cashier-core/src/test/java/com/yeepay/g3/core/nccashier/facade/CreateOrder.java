package com.yeepay.g3.core.nccashier.facade;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.caucho.hessian.client.HessianProxyFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.opr.dto.order.ext.GoodsParamExtDTO;
import com.yeepay.g3.facade.opr.dto.order.ext.IndustryParamExtDTO;
import com.yeepay.g3.facade.opr.dto.order.ext.PaymentParamExtDTO;
import com.yeepay.g3.facade.opr.dto.order.ext.RiskControlParamExtDTO;
import com.yeepay.g3.facade.opr.dto.yop.order.YopCreateOrderReqDTO;
import com.yeepay.g3.facade.opr.dto.yop.order.YopCreateOrderResDTO;
import com.yeepay.g3.facade.opr.enumtype.ProductVersionEnum;
import com.yeepay.g3.facade.opr.facade.yop.YopOrderFacade;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.utils.JsonUtils;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.json.JSONUtils;

public class CreateOrder extends BaseTest {
	
	@Autowired
    private YopOrderFacade yopOrderFacade;

    @Test
    public void testYopCreateOrder() throws SocketException, JsonProcessingException, MalformedURLException {
    	 	String url = "http://10.151.32.27:30134/opr-hessian/hessian/YopOrderFacade";
         HessianProxyFactory factory = new HessianProxyFactory();
         YopOrderFacade facade = (YopOrderFacade) factory.create(YopOrderFacade.class, url);

     
         YopCreateOrderReqDTO request = new YopCreateOrderReqDTO();
         request.setProductVersion("DSBZB");//营销产品码 Y
         request.setAppKey("OPR:10040039448");

         request.setBizSystemNo("G2NET"); //业务方标志  Y
         request.setParentMerchantNo("10040039448");//父商编
         request.setMerchantNo("10040039448");//收单商
//         request.setOrderId("CustomerRequestId1515059102961");//请求号
         request.setOrderId("CustomerRequestId" + System.currentTimeMillis());
         request.setOrderAmount("2");//订单金额
         request.setTimeoutExpress(1440);//订单有效期 分钟
         request.setTimeoutExpressType("MINUTE");//DAY HOUR MINUTE SECOND
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         request.setRequestDate(sdf.format(new Date()));

         request.setRedirectUrl("http://www.RedirectUrl.com");
         request.setNotifyUrl("http://com.NotifyUrl.3g1");
         request.setAssureType("REALTIME");// REALTIME/ASSURE  实时/担保
//       request.seta("assurePeriod", 0);//担保周期（一期无）int（单位：天）
         request.setSalesProductCode("DSBZB");
//         request.setCsUrl("");
         request.setCsUrl("http://www.CsUrl.com");
         request.setFundProcessType("REAL_TIME");//REAL_TIME|DELAY_SETTLE|REAL_TIME_DIVIDE|SPLIT_ACCOUNT_IN
//         request.setD0Url("http://www.D0Url.com");
//         request.setSettleProcessType("D0");
//         request.setSettleProcessType(null);
         //实时分账通知地址
//         request.setDivideNotifyUrl("http：//www.EEeUrlDivDivideUrlDivideUrlideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivDivideUrlDivideUrlideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUrlDivideUr.com");
//         request.setDivideNotifyUrl("http://www.DivideUrl.com");
         //实时分账分账明细
//         List<DivideAndSplitAccountDetailDTO> divideAndSplitAccountDetailDTOList = Lists.newArrayList();
//         DivideAndSplitAccountDetailDTO divideDTO1 = new DivideAndSplitAccountDetailDTO();
//         divideDTO1.setAmount(new BigDecimal("0.05"));
//         divideDTO1.setProportion(new Double("0.5"));
//         divideDTO1.setLedgerNo("10040028946");
//         divideDTO1.setLedgerName("实时分账测试1");
//         divideAndSplitAccountDetailDTOList.add(divideDTO1);

//         DivideAndSplitAccountDetailDTO divideDTO2 = new DivideAndSplitAccountDetailDTO();
//         divideDTO2.setAmount(new BigDecimal("0.07"));
//         divideDTO2.setProportion(new Double("0.25"));
//         divideDTO2.setLedgerNo("10040040287");
//         divideDTO2.setLedgerName("实时分账测试2");
//          divideAndSplitAccountDetailDTOList.add(divideDTO2);

//         DivideAndSplitAccountDetailDTO divideDTO3 = new DivideAndSplitAccountDetailDTO();
//         divideDTO3.setAmount(new BigDecimal("0.01"));
//         divideDTO3.setProportion(new Double("0.25"));
//         divideDTO3.setLedgerNo("10040040286");
//         divideDTO3.setLedgerName("实时分账测试2");
//         divideAndSplitAccountDetailDTOList.add(divideDTO3);
//         request.setDivideDetail(JSONUtils.toJsonString(divideAndSplitAccountDetailDTOList));
//         request.setDivideDetail("[{\"ledgerName\":\"实时分账测试1\",\"proportion\":0.5,\"ledgerNo\":\"10040040286\"},{\"ledgerName\":\"1实时实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账\",\"proportion\":0.01,\"ledgerNo\":\"10040040287\"}]");
//         request.setDivideDetail("1实时实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1实时分账测试1000");

         //商品扩展信息
         Map<String, String> goodsExt = new HashMap<String, String>();
         goodsExt.put("goodsDesc", "Luna Mini2");
         goodsExt.put("goodsName", "洗脸仪");
         goodsExt.put("goodsKind", "33");
         request.setGoodsParamExt(JsonUtils.toJsonString(goodsExt));

         //支付扩展信息

         Map<String, String> paymentParamExt = new HashMap<String, String>();
//         paymentParamExt.put("paymentProduct", "EANK");//支付产品：传值必须是对应的枚举值NCPAY SCCANPAY EANK EWALLET CFL
//         paymentParamExt.put("directBankId", "");
//         paymentParamExt.put("cardName", "测试员");
//         paymentParamExt.put("idCardNo", "513722197906074286");
//         paymentParamExt.put("bankCardNo", "6212263602015645309");
//         paymentParamExt.put("mobilePhoneNo", "15810627677");
//         paymentParamExt.put("payCardType", "");
//         paymentParamExt.put("userIp", "192.168.10.1");
//         paymentParamExt.put("productType", "VIRTUAL");//开通分期支付必传 VIRTUAL:虚拟 SUBSTANCE:实体
//         paymentParamExt.put("orderType", "ONLINE");//开通分期支付必传 线上：ONLINE 线下：OFFLINE
         request.setPaymentParamExt(JsonUtils.toJsonString(paymentParamExt));

         request.setMemo("标准版测试");

         //账务扩展参数
         Map<String, String> infoParamExt = new HashMap<String, String>();
         infoParamExt.put("historyType","SALES");
         infoParamExt.put("historyDesc",request.getOrderId());
         infoParamExt.put("settleable","false");
         infoParamExt.put("bizType","TEST");
         infoParamExt.put("trxId",request.getOrderId().substring(17));
//         request.setInfoParamExt(JsonUtils.toJsonString(infoParamExt));

         //行业扩展信息
         Map<String, String> industryParamExt = new HashMap<String, String>();
         industryParamExt.put("bizSource", "行业来源");//行业来源
         industryParamExt.put("bizEntity", "经营主体");//经营主体
         request.setIndustryParamExt(JsonUtils.toJsonString(industryParamExt));

         //风控扩展信息
         Map<String, String> riskParamExt = new HashMap<String, String>();
         riskParamExt.put("registTime", "2017-02-08 12:00:00");
//         request.setRiskParamExt("refer","http://www.yeepay.com");
//         request.setRiskParamExt(JsonUtils.toJsonString(riskParamExt));

         System.out.println("请求参数" + JsonUtils.toJsonPrettyString(request));

         YopCreateOrderResDTO response = facade.createOrder(request);
         System.out.println("返回参数" + JsonUtils.toJsonPrettyString(response));

//         ScriptDto orderDto = new ScriptDto();
//         orderDto.setContent(response.getToken());
//         orderDto.setScriptId(1l);
//         setVal("orderDto",orderDto, VariableScope.GLOBAL);
//         ScriptOrderDto orderDto = new ScriptOrderDto();
//         orderDto.setToken(response.getToken());
//         orderDto.setOrderId(response.getOrderId());
//         orderDto.setUniqueOrderNo(response.getUniqueOrderNo());
//         setVal("orderDto",orderDto, VariableScope.GLOBAL);

    }

//    @Test
//    public void testYopQueryOrder() {
//        YopQueryOrderReqDTO queryOrderReqDTO = new YopQueryOrderReqDTO();
//        queryOrderReqDTO.setAppKey("OPR:10040007800");
//        queryOrderReqDTO.setBizSystemNo("DS");
//        queryOrderReqDTO.setParentMerchantNo("10040007800");
//        queryOrderReqDTO.setMerchantNo("10040007800");
//        queryOrderReqDTO.setOrderId("1487266585596");
//        // queryOrderReqDto.setUniqueOrderNo("1001201702160000000000001250");
//
//        System.out.println(queryOrderReqDTO);
//        YopQueryOrderResDTO resDTO = yopOrderFacade.queryOrder(queryOrderReqDTO);
//        System.out.println(resDTO);
//        assertEquals(BizResponseCodeConstant.SUCCESS, resDTO.getCode());
//        assertEquals(resDTO.getOrderId(), queryOrderReqDTO.getOrderId());
//
//    }

//    @Test
//    public void testYopBatchQueryOrder() {
//        YopBatchQueryOrderReqDTO batchQueryOrderReqDTO = new YopBatchQueryOrderReqDTO();
//        batchQueryOrderReqDTO.setProductVersion("DSBZB");
//        batchQueryOrderReqDTO.setAppKey("OPR:10040007800");
//        batchQueryOrderReqDTO.setBizSystemNo("DS");
//        batchQueryOrderReqDTO.setParentMerchantNo("10040007800");
//        batchQueryOrderReqDTO.setMerchantNo("10040007800");
//        batchQueryOrderReqDTO.setPageNo("9999999999999");
//        batchQueryOrderReqDTO.setPageSize("20");
//        batchQueryOrderReqDTO.setRequestDateBegin("2017-01-01 00:00:00");
//        batchQueryOrderReqDTO.setRequestDateEnd("2017-03-01 00:00:00");
//        // batchQueryOrderReqDto.setStatus("SUCCESS");// PROCESSING CLOSED
//        // FAILED
//        // SUCCESS
//        YopBatchQueryOrderResDTO batchQueryOrderResDTO = yopOrderFacade.batchQueryOrder(batchQueryOrderReqDTO);
//        System.out.println(JSONUtils.toJsonString(batchQueryOrderResDTO));
//        assertEquals(BizResponseCodeConstant.SUCCESS, batchQueryOrderResDTO.getCode());
//    }

//    @Test
//    public void testCallBack() throws IOException {
//        CustBasedNotifyFacade custBasedNotifyFacade = RemoteServiceFactory.getService(CustBasedNotifyFacade.class);
//        OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
//        orderInfoEntity.setNotifyUrl("http://172.18.162.212:8080/opr-hessian/callBackTest/callBackTest.do");
//        orderInfoEntity.setParentMerchantNo("10040007799");
//        orderInfoEntity.setMerchantNo("10040007799");
//        orderInfoEntity.setOrderId("3333");
//        Map<String, Object> notifyMap = Maps.newHashMap();
//        OrderPaymentEntity pay = new OrderPaymentEntity();
//        notifyMap.put("parentMerchantNo", orderInfoEntity.getParentMerchantNo());
//        notifyMap.put("merchantNo", orderInfoEntity.getMerchantNo());
//        notifyMap.put("requestId", orderInfoEntity.getOrderId());
//        notifyMap.put("bankOrderId", pay.getBankOrderId());
//        notifyMap.put("uniqueOrderNo", orderInfoEntity.getUniqueOrderNo());
//        notifyMap.put("status", "SUCCESS");
//        notifyMap.put("orderAmount", orderInfoEntity.getOrderAmount());
//        notifyMap.put("payAmount", pay.getPayAmount());
//        notifyMap.put("requestDate", orderInfoEntity.getRequestDate());
//        notifyMap.put("successDate", pay.getPaySuccessDate());
//        custBasedNotifyFacade.sendNotification(Constant.OPR_TRADE_SUCCESS_RULECODE, orderInfoEntity.getNotifyUrl(), CustomerIdentificationEnum.APP_KEY, "OPR:10040007799-10", orderInfoEntity.getOrderId(), true, false, notifyMap);
//
//    }

    private static PaymentParamExtDTO assemblePaymentParamExt() {
        PaymentParamExtDTO paymentParamExt = new PaymentParamExtDTO();
//        paymentParamExt.setBankCardNo("6214830104643936");
//        paymentParamExt.setCardName("赵振宽");
//        paymentParamExt.setIdCardNo("411402198808304559");
        return paymentParamExt;
    }

    private static GoodsParamExtDTO assmbleGoodsInfoExt() {
        GoodsParamExtDTO goodsParamExt = new GoodsParamExtDTO();
        goodsParamExt.setGoodsDesc("无人机");
        goodsParamExt.setGoodsName("大疆无人机");
        return goodsParamExt;
    }

    public static void main(String[] args) {
        // ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 60,
        // TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(1000));
        // for(int i=0;i<5;i++){
        // executor.execute(
        // new Runnable() {
        //
        // @Override
        // public void run() {
        // yopCreateOrder();
        // }
        // });
        // }

		 yopCreateOrder();
//		queryOrder();
//        batchQueryOrder();
    }

    private static void yopCreateOrder() {
        String BASE_URL = "http://172.17.102.175:8064/yop-center/";
//        String secretKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQcp9lZjnjLrfxKMUeM3M9uhRs enziyPS3rTa1CBNrhdFSFJW2OLlqemFGD7toowuT15R53ccK921d5Xsi8ikhy4OUiYivJoA9Zztm r+uYRoN7WCFbuZzqlC8W0xjKZaCmAlEkMqARdbgrUrgewr3nAAMa6gNh40Rpq2Qplf9cuXwHfW16 ZMcm01RSMvWPa/QtMusKSgW6WZo3CBekV/o9CVSh9qYurbnlG1POtERS6YtcGAtcjrD3uuPamYFQ xYK2w+eg8q6lkert+2TlTPqTtmnaCAtlGnAC1RFAHRGoT+4Yvw4alDM3G1DUUbeiMmzazEQ4h1uH 3CVypeDMwX0fAgMBAAECggEAXR8oF2zB4O4bc4M/IOs2XhL6W7zTijjXWxp17FtaebT5bxsKMUF8 d2KSF2LJBPon2pXeiHoreaxte10X9z16uujC2R2ZWqFNh0hoCRlcnvzGgtwcFVAiEzCY8vQARWsj GCLiQJ3Kh7cGlhdrz5joaGWfmthGefLUBfOSTSUATbvd96mFPX/RDnvpQ1YR53caz6mPPm+Y/SkY vxKIRrW4KJrnWUc4XGEPtwlMSFhqwzTaLfIFps85Q6kJMvT7iD3oao8sb2QXXWz8jO3yMbXoFYuz F7uF5QVedCNC4lCbaXrQlm2vtz3fodg8iXw5WFXPsUOVgaVPFkP0ky5VAoKYEQKBgQD51REDagTf +HBBXvmxU8kazEg55nDfsvZxKoWnH3sRlYB3n3T6CJhKeLiuRGyWVWE6Bos7a4BNzbaLtXbGUiUd GsHplrEw56pVNIJp7i5+go9fJ9xOcBu1jn3Fok9PZxeX/SmGhiWSrysY+YKjyI28dFgd29ds7Vw0 PVoCOr8CNQKBgQCUA4dS3P+hrkD/r72dlZWgWFKW0assz389Nne05TEQwIdAed5nef6gwa4izCnG 6G/RccjZVyXfTJCGK6qWEyJZnAKg61a3yqanqQAwo7hUCAQ/A6m5AM2BvlyPh7oqRBc6h+67WxpN ZqA158VahV2HrRJZo7Ie+4Zoh9dNSmFsgwKBgEmZjnCODC+bNh7cBv1JrKY7Zk/AZqJQS6/dEhDB AnWUsIsNK935KDxQQj/8omzLbGA2y0/PBLZnEw8nf30/d/WSC2xwW3UH2rNNS9o/M+1LM0eaK5nk BxW+i6jsfybqPRYmm9Qosur6tdyerPDpRXAuakMpn9ZUOuSc6mZbvie9AoGAL/aYwnRU7hqee3yC cG9JpkUYtkDJPGMc80QUNG1Uof4RlgYn2NZCeetpN2N7YjZuDavWjv9biWmxJ5k81RbsOaeBfo3k cvhbWtc79YcIM0rJvXW9aPLmpVV/fw9Xd1zLPi9QYCwccNqBrhYl8Lho349o891E2h9dpb/nN+eT fGsCgYEA6Vf17+MTBko2U0sZFZ/quBORRNN+MVQz5F+iCai6knbvMC36h4T3LlXPiAwOVM3y2EHU dal5mEmgi0mmneJswiLa0Pg3O1Qftbr3IXzCb801Cco4XNlJaw30rEygRrqFcWyoaU07XJWZAcro azt2M8/2T53JecfxwiI8wBCbQ4U=";
        String secretKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVHL17lfZ+2nZ3eHuO0soTOHU a4X7wuVPrl1hy9DLQuLqdbwfg10gltRfBnPx3ZkrRdR4m5LY7T6yxdFY5Kej3hNwbrch/pvZwoQo 8F2eHigvrCRjxudmHvQHLdtFRPtimPs4VG3GYl11/e1lPDcnOoOLlPF4lyKmsEKPwK/DK+I+jeCP o2jB+pAxFPVWXY6mWrs91sgpdoOmSCijPDsxfhfmVvFQvLUdXr/SuKjvH75keAZ9oUVNjPC7QJHR o3JYoU9iUfiu5TJwwx7XKCjqMuXvb+iV34/L1MHfZdjo1QU+hvQSTonP1+DSVTOTE3uP6ufiC4nw wyXpGAPMY8YRAgMBAAECggEAMGhy9uO59MAhf0o+7MXaDW/zmsYqnCDME8BraBdjThr+7EoJtkmy hWO4a4TyO5NmFKDtUIp8akhWH8LezKQGbblweqL9oWBD/roEB2EqwmM47YdQ3NQ1S1hRkLm3K5I7 CPe6e4b3YUcnqw/tBF6IItBYnJafx3fEdZ51oBJMVvNW10Eu/ZCdVn+wpMIlGzH8qn3pNrkpP2EW 19OSissnjVJ1E7v5TDGZBQVSHkcbQ7xr9oAjOscZcm6TtTPWKlsNvXayyZBVSnsszRPgmVaIZL+L CJGbcQV6HtOwqIFqmUfWNg0Adf4++K5SJWnEzdMP+SaaJyJnYGjklEkf9mQNxQKBgQDvc8YHreju RfGLNmaCUdr0G5iTSi7KQCymMmSRqOd1upD1v3zGCM4a+zVlGu1aQCmba+qWxXIeuygcwn01g01G ukrq4dP85/mnqisI4OG607NCRILbuupphk/tdYWbdI7o/Qave2slaczLlRpAgc/gYqcwCitljmnf KPvX/KU7gwKBgQC3K4A+UfrrMwKvgYxtcP6lCuG8mswBuDRkPC6LZrJc22S3HWw7LXVWwUi8SZdG oCUwDPFzfrHZsl7965SLKB7zk/1+VmSWt8ZueOMwOg7UPOfbMBg52PC1rOtu5JY4Bm1aZsHAIYDK tKKYUaazFyC23ZidNLQhMXINGN8aIscf2wKBgQCEh86WV4IxxxKem5h3DrkiHNgAxbFKDeTog8G4 AQVC2uT6r2Zu8VaqBloSQKoYJqUgucUYd+Xm7m2QJXFJmge+WsO2ZxF+zCIY042IF3e4gQ2ZYvQO i9DMYSOB6WbumL+0Yr89hxDRn1JTZ44lH/QfXFrusuI8Dmu4sSVa8SG+4wKBgAiVSkIhV0+0KTkO KgVq2RPkyaUr38lo11OnGks/+bWuNi76evre63OwRPdFv4f4syVoRdwyoKTh3d+qLWDD9YdWdPd5 lucVH4BHu+WjotRBMmAsBcaYKtdojfO5VGy1qGQnEoctSrq08jWPBe+4crj+80rSkGpJxd1lP/ca kBgnAoGBAMTq2u3BJ3tpT8YSlOA1ojtFOpKVzdYrKi+1piFjyNgEcTpf1f37x8SxYzVYz9gGor5A oAp8DzyJz/9RsSLJJmAV0lliiNbgBDjFOJvJaUy2Gx8KWD9AHW6vtMNnsssCyrrRHHk1HcojT6Uk fw/rSIouLd9rSkID/5AYUAwnNUai";
        YopRequest request = new YopRequest("OPR:10040007800", secretKey, BASE_URL);
//        setProductVersion(ProductVersionEnum.DSBZB.name());
//        request.addParam("productVersion", "标准版");
		request.addParam("version", "v1.0");
        request.addParam("parentMerchantNo", "10040007800");
        request.addParam("merchantNo", "10040007800");
        request.addParam("orderId", System.currentTimeMillis() + "");
        request.addParam("orderAmount", "0.03");
        request.addParam("timeoutExpress", 30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        request.addParam("requestDate", sdf.format(new Date()));
        request.addParam("redirectUrl", "https://www.baidu.com/");
        request.addParam("notifyUrl", "https://www.baidu.com/");
        // request.addParam("assureType", "REALTIME");// REALTIME/ASSURE
        // request.addParam("assurePeriod", 10);
        PaymentParamExtDTO paymentParamExt = assemblePaymentParamExt();
        request.addParam("paymentParamExt", JSONUtils.toJsonString(paymentParamExt));

        GoodsParamExtDTO goodsParamExt = assmbleGoodsInfoExt();
        request.addParam("goodsParamExt", JSONUtils.toJsonString(goodsParamExt));

        request.addParam("memo", "accountMemo111");
        request.addParam("industryParamExt", "");
        request.addParam("riskControlParamExt", "");

        System.out.println(request.toString());
        YopResponse response = YopClient3.postRsa("/rest/v1.0/std/trade/order", request);
        System.out.println(response.toString());
    }

//    private static void queryOrder() {
//        String BASE_URL = "http://172.17.102.175:8064/yop-center/";
//        String secretKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQcp9lZjnjLrfxKMUeM3M9uhRs enziyPS3rTa1CBNrhdFSFJW2OLlqemFGD7toowuT15R53ccK921d5Xsi8ikhy4OUiYivJoA9Zztm r+uYRoN7WCFbuZzqlC8W0xjKZaCmAlEkMqARdbgrUrgewr3nAAMa6gNh40Rpq2Qplf9cuXwHfW16 ZMcm01RSMvWPa/QtMusKSgW6WZo3CBekV/o9CVSh9qYurbnlG1POtERS6YtcGAtcjrD3uuPamYFQ xYK2w+eg8q6lkert+2TlTPqTtmnaCAtlGnAC1RFAHRGoT+4Yvw4alDM3G1DUUbeiMmzazEQ4h1uH 3CVypeDMwX0fAgMBAAECggEAXR8oF2zB4O4bc4M/IOs2XhL6W7zTijjXWxp17FtaebT5bxsKMUF8 d2KSF2LJBPon2pXeiHoreaxte10X9z16uujC2R2ZWqFNh0hoCRlcnvzGgtwcFVAiEzCY8vQARWsj GCLiQJ3Kh7cGlhdrz5joaGWfmthGefLUBfOSTSUATbvd96mFPX/RDnvpQ1YR53caz6mPPm+Y/SkY vxKIRrW4KJrnWUc4XGEPtwlMSFhqwzTaLfIFps85Q6kJMvT7iD3oao8sb2QXXWz8jO3yMbXoFYuz F7uF5QVedCNC4lCbaXrQlm2vtz3fodg8iXw5WFXPsUOVgaVPFkP0ky5VAoKYEQKBgQD51REDagTf +HBBXvmxU8kazEg55nDfsvZxKoWnH3sRlYB3n3T6CJhKeLiuRGyWVWE6Bos7a4BNzbaLtXbGUiUd GsHplrEw56pVNIJp7i5+go9fJ9xOcBu1jn3Fok9PZxeX/SmGhiWSrysY+YKjyI28dFgd29ds7Vw0 PVoCOr8CNQKBgQCUA4dS3P+hrkD/r72dlZWgWFKW0assz389Nne05TEQwIdAed5nef6gwa4izCnG 6G/RccjZVyXfTJCGK6qWEyJZnAKg61a3yqanqQAwo7hUCAQ/A6m5AM2BvlyPh7oqRBc6h+67WxpN ZqA158VahV2HrRJZo7Ie+4Zoh9dNSmFsgwKBgEmZjnCODC+bNh7cBv1JrKY7Zk/AZqJQS6/dEhDB AnWUsIsNK935KDxQQj/8omzLbGA2y0/PBLZnEw8nf30/d/WSC2xwW3UH2rNNS9o/M+1LM0eaK5nk BxW+i6jsfybqPRYmm9Qosur6tdyerPDpRXAuakMpn9ZUOuSc6mZbvie9AoGAL/aYwnRU7hqee3yC cG9JpkUYtkDJPGMc80QUNG1Uof4RlgYn2NZCeetpN2N7YjZuDavWjv9biWmxJ5k81RbsOaeBfo3k cvhbWtc79YcIM0rJvXW9aPLmpVV/fw9Xd1zLPi9QYCwccNqBrhYl8Lho349o891E2h9dpb/nN+eT fGsCgYEA6Vf17+MTBko2U0sZFZ/quBORRNN+MVQz5F+iCai6knbvMC36h4T3LlXPiAwOVM3y2EHU dal5mEmgi0mmneJswiLa0Pg3O1Qftbr3IXzCb801Cco4XNlJaw30rEygRrqFcWyoaU07XJWZAcro azt2M8/2T53JecfxwiI8wBCbQ4U=";
//        YopRequest request = new YopRequest("OPR:10040040287", secretKey, BASE_URL);
//        request.addParam("parentMerchantNo", "10040040286");
//        request.addParam("merchantNo", "10040040287");
//        request.addParam("orderId", "CustomerRequestId1487926476464");
//        System.out.println(request.toQueryString());
//        YopResponse response = YopClient3.postRsa("/rest/v1.0/sys/trade/orderquery", request);
//        System.out.println(response.toString());
//    }
//
//    private static void batchQueryOrder() {
//        String BASE_URL = "http://10.151.30.80:18064/yop-center/";
//        String secretKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC656dj7GkdEhoJc9vl7/nAOlph A00KqWnOceHBXCcbh+KDWTN7ssrVwhdlj/StOmb9xaZbR2CgkeB188R4tGjP+hEx193yn3TE/GbC 0/dlJsImjKh1D+EGOT5kTXNclfyyp82o/mK3PDyyOcZ0ZsuppuRGWYgzEyDFjIaXp1iEMzJ1Zk+x uelqN4GXkAbQUdTv+P3iEhw3mQIoh7f4xs15kZfqSs50vwzx2bO7M9BnUIrRihj1Q+9MdHUIEBBN 17voBq3amvQsO8rUOpkX7t9gB1e4G4WNFxLuPkKUBWDx3yOTeArC8kdk03fvwbYCy5EnNNFVRbbs zEN9MtMmueD9AgMBAAECggEAcxAN8uwlwenU8aDL7YCRBpBl93dqxHeLVbayPrGWudIr1qThlvN4 /6Ofd7zp6oMbyHsXcPY+PFvSDYtmltCp/e97Cm5Uivszjdm9AZAwq6ryt3y8cGS4KXawPFuZ+5Oy n+HCMJl6+EczLp122U43b1f5DH4t5ROPh059xXsWeTtmF91BOYt8iaETs/bkP0TJOBXWEJFuV33h NKQqXeuoY3Sk9QfP9IQuJJi7sTvWVS4z9HHr2HdvokfzfzbSU+nV35bYIT9IHJf8ddHPNGZLPH2Q k5UOI1OpFoE/5x4Hh2HzrogyMERrxqbjraYTSeo4XczwzFVVoZAyQ61/JIOzQQKBgQDgfhLrclg0 Rs1/yyuk+LN+HISF4rDJD/Y5lIi4kWlCMysukH1yK8+GQG2feZ9OGqxVz3YdhzbAqFxprNLerQVS AwoBWw8QcE+r3NzX47h+msc/BtLw3G2p2i1GPKn8q22F5HW5alygr8KcqKsXoKdpd49tlX3N6DKo I3OP3i3NtQKBgQDVIxK3HBXrxn+pKcnW2FxVjSt1/W3YJH/+wxfwtTuFAvulYKznwtmOH+XTzX4t SzQ75RGoci4tGDZP3RIdCj0WzXEsAlKvpDdkiQxg4j/Y5CigHYiUVRPg/MCT7He2UjyAJoBDgsAW SSGXxmK3Tpn05lHo1hBo5EdzjghKNlGTKQKBgFHZxd4wr7ZHxm98zWn1/8PQdAU41QpKFpWB8aOR 6RXpACqXJhX1oVGqtg16U7ovgXw8Cy3S+gSenisthykHXrQ7RW+fGsv8lx/rhF32unL2zgTN4SVO yXEvimhcgAIPIvy5gq7a9/DihIuH9/HCcqfCnvnukpL7wVSKsHzZHkwtAoGBALmvfLaaeV6X6NBv 37CVfpvqCVsaF4CfDAPf5BOJUPlox+QkfX7Mes0fbRNBjBPZ2P/1aZTfJOuUlHyvlQBH00Dyaw50 V6U95CRqYaZmdkl3PQ2BrWPNWyxAB2bYNNNagesDn+sxFjhULjgUVhxtFiQOJH49QL6hYr6BlydQ e3FBAoGAUHNCzVUSIOrRh+EYpcgohJELiy+xxkQFBKM/fzPo2OMW46XHLY5zevuTozCX9Xj7W5zu rP7i5vmTKCLbaQUrKlbKOqFSzerYni9cnhL5HC87ciBHq2kyF3tM1vOKXhaYe9UlzbKaQ6xNd3kL T4TP4gveAHw1uMrjsxm7gEmgsEI=";
//        YopRequest request = new YopRequest("OPR:10040039448", secretKey, BASE_URL);
//        request.addParam("parentMerchantNo", "10040039448");// 必填
//        request.addParam("merchantNo", "10040039448");// 必填
////        request.addParam("status", "SUCCESS");// 订单状态 SUCCESS
//        request.addParam("requestDateBegin", "2017-02-04 00:00:00");// 请求时间
//        request.addParam("requestDateEnd", "2017-03-05 00:00:00");// 请求时间
//        request.addParam("pageNo", "1");// 页码
//        request.addParam("pageSize", "20");// 页容
//
//        System.out.println(request.toQueryString());
//        YopResponse response = YopClient3.postRsa("/rest/v2.0/opr/batch_query_order", request);
//        System.out.println(response.toString());
//
//    }

}
