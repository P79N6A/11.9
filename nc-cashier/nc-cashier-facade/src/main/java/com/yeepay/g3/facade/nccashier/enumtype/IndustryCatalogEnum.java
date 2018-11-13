package com.yeepay.g3.facade.nccashier.enumtype;

public enum IndustryCatalogEnum {
/**
 * 虚拟产品
 */
 VIRTUAL_PRODUCT("7993"), 
 /**
  * 公缴费
  */
 PUBLIC_PAYMENT("4900"),
 /**
  * 电信和手机充值
  */
 TELECOM_PHONE_RECHARGE("4814"),
 /**
  * 公益事业
  */
 PUBLIC_CAREER("8398"),
 
 /**
  * 电商
  */
 ONLINE_RETAILERS("3101"),
 /**
  * 彩票业务
  */
 LOTTERY_BUSINESS("7995"),
 /**
  * 行政教育
  */
 ADMINISTRATIVE_EDUCATION("8299"),
 /**
  * 线下服务业
  */
 UNDER_LINE_SERVICE("9499"),
 /**
  * 保险行业
  */
 INSURANCE_INDUSTRY("6300"),
 /**
  * 金融、基金
  */
 FINANCIAL_FUND("3002"),
 /**
  * 航旅
  */
 AIR_TRAVEL("4511"),
 /**
  * 其他
  */
 OTHER("5969"),
 /**
  * 百货商店
  */
 DEPARTMENT_STORE("5311"),
 /**
  * 大型景区售票
  */
 LARGE_SCENIC_SPOT_TICKET("4733"),
 /**
  * 航空公司
  */
 AIRLINE_COMPANY("4511"),
 /**
  * 综合业务
  */
 INTEGRATED_SERVICES("0002"),
 /**
  * 三代会员充值
  */
 TG_MEMBER_RECHARGE("0001"),
 /**
  * 收款宝路由专用
  */
 SKB_ROUTE_USE("3333");
 
  private  String value;

   
   private IndustryCatalogEnum(String value) {
        this.value = value;
    }   
    public String getValue() { 
        return value;
    }
}
