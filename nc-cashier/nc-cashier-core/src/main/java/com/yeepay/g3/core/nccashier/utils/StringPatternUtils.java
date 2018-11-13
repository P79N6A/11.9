package com.yeepay.g3.core.nccashier.utils;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-11-01 11:33
 **/

import java.util.regex.Pattern;

/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-11-01 11:33
 **/


public class StringPatternUtils {


    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
