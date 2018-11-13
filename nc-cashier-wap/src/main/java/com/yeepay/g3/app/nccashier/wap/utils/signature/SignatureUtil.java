package com.yeepay.g3.app.nccashier.wap.utils.signature;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import com.yeepay.g3.utils.common.StringUtils;

public class SignatureUtil {
   

    /**
     *  rsa内容签名
     * 
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws AlipayApiException
     */
    public static String rsaSign(String content, String privateKey, String charset,
                                 String signType) throws Exception {

        if (SignatureConstants.SIGN_TYPE_RSA.equals(signType)) {

            return rsaSign(content, privateKey, charset);
        } else if (SignatureConstants.SIGN_TYPE_RSA2.equals(signType)) {

            return rsa256Sign(content, privateKey, charset);
        } else {
            throw new Exception("Sign Type is Not Support : signType=" + signType);
        }

    }

    /**
     * sha256WithRsa 加签
     * 
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws Exception 
     * @throws AlipayApiException
     */
    public static String rsa256Sign(String content, String privateKey,
                                    String charset) throws Exception {

        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SignatureConstants.SIGN_TYPE_RSA,
                new ByteArrayInputStream(privateKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                .getInstance(SignatureConstants.SIGN_SHA256RSA_ALGORITHMS);

            signature.initSign(priKey);

            if (StringUtils.isBlank(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (Exception e) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, e);
        }

    }

    /**
     * sha1WithRsa 加签
     * 
     * @param content
     * @param privateKey
     * @param charset
     * @return
     * @throws AlipayApiException
     */
    public static String rsaSign(String content, String privateKey,
                                 String charset) throws Exception{
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(SignatureConstants.SIGN_TYPE_RSA,
                new ByteArrayInputStream(privateKey.getBytes()));

            java.security.Signature signature = java.security.Signature
                .getInstance(SignatureConstants.SIGN_ALGORITHMS);

            signature.initSign(priKey);

            if (StringUtils.isBlank(charset)) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();

            return new String(Base64.encodeBase64(signed));
        } catch (InvalidKeySpecException ie) {
            throw new Exception("RSA私钥格式不正确，请检查是否正确配置了PKCS8格式的私钥", ie);
        } catch (Exception e) {
            throw new Exception("RSAcontent = " + content + "; charset = " + charset, e);
        }
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm,
                                                    InputStream ins) throws Exception {
        if (ins == null || StringUtils.isBlank(algorithm)) {
            return null;
        }

        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = StreamUtil.readText(ins).getBytes();

        encodedKey = Base64.decodeBase64(encodedKey);

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
    }

   

}
