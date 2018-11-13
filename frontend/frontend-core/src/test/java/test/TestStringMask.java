/**
 * Copyright: Copyright (c)2017
 * Company: 易宝支付(YeePay)
 */
package test;

import com.yeepay.g3.facade.bankchannel.dto.OpenPayRequestDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名称: TestStringMask <br>
 * 类描述: <br>
 *
 * @author: dongbo.jiao
 * @since: 18/3/22 11:56
 * @version: 1.0.0
 */

public class TestStringMask {

    public static void main(String[] args){
        OpenPayRequestDTO openPayRequestDTO = new OpenPayRequestDTO();
        openPayRequestDTO.setUserIdCard("12313213131");
        openPayRequestDTO.setUserTrueName("lalal");
        System.out.println(ReflectionToStringBuilder.toStringExclude(openPayRequestDTO,"userIdCard","userTrueName"));
    }
}
