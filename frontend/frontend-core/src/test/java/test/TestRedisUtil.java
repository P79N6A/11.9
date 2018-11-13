/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package test;

import com.yeepay.g3.core.frontend.entity.PayOrder;
import com.yeepay.g3.core.frontend.util.RedisUtil;
import com.yeepay.g3.facade.frontend.enumtype.OrderType;

/**
 * 类名称: TestRedisUtil <br>
 * 类描述: <br>
 *
 * @author: dongbo.jiao
 * @since: 17/10/18 18:22
 * @version: 1.0.0
 */

public class TestRedisUtil {

    public static void main(String[] args){

        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo(System.currentTimeMillis()+"");
        payOrder.setPayInterface("ICBCGC_ICBC_JD_ZF_5011");
        payOrder.setOrderType(OrderType.H5APP.name());
        RedisUtil.pushPayUrlToRedis(payOrder,"hfahfahfa");
    }
}
