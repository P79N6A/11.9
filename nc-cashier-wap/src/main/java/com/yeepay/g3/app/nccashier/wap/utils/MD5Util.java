package com.yeepay.g3.app.nccashier.wap.utils;

import com.yeepay.g3.utils.common.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * 获取md5 32位小写加密串
     * @param source
     * @return
     */
    public static String encryptMD5(String source){
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(source.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();// 加密
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 验证text的MD5加密串是否sign一致
     * @param text
     * @param sign
     * @return
     */
    public static boolean signVerify(String text,String sign) {
        if (StringUtils.isBlank(sign)) {
            return false;
        }
        String signFromText = encryptMD5(text);
        return sign.equals(signFromText);
    }

}
