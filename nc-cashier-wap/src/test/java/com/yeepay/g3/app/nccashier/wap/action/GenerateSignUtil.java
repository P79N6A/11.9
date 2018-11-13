package com.yeepay.g3.app.nccashier.wap.action;


import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

//import com.yeepay.g3.facade.yop.ca.dto.DigitalSignatureDTO;
//import com.yeepay.g3.facade.yop.ca.enums.CertTypeEnum;
//import com.yeepay.g3.facade.yop.ca.enums.DigestAlgEnum;
//import com.yeepay.g3.frame.yop.ca.rsa.RSAKeyUtils;

public class GenerateSignUtil {

//	public static String generateSignInfo(String plainText, String appKey, String priKey){
//		DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
////		digitalSignatureDTO.setAppKey("OPR:10040007800");
////		digitalSignatureDTO.setAppKey("OPR:10040040287");
//		digitalSignatureDTO.setAppKey("OPR:" + appKey);
//		digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
//		digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
//		digitalSignatureDTO.setPlainText(plainText);
//		//私钥
////		String priKey7800 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVHL17lfZ+2nZ3eHuO0soTOHU a4X7wuVPrl1hy9DLQuLqdbwfg10gltRfBnPx3ZkrRdR4m5LY7T6yxdFY5Kej3hNwbrch/pvZwoQo 8F2eHigvrCRjxudmHvQHLdtFRPtimPs4VG3GYl11/e1lPDcnOoOLlPF4lyKmsEKPwK/DK+I+jeCP o2jB+pAxFPVWXY6mWrs91sgpdoOmSCijPDsxfhfmVvFQvLUdXr/SuKjvH75keAZ9oUVNjPC7QJHR o3JYoU9iUfiu5TJwwx7XKCjqMuXvb+iV34/L1MHfZdjo1QU+hvQSTonP1+DSVTOTE3uP6ufiC4nw wyXpGAPMY8YRAgMBAAECggEAMGhy9uO59MAhf0o+7MXaDW/zmsYqnCDME8BraBdjThr+7EoJtkmy hWO4a4TyO5NmFKDtUIp8akhWH8LezKQGbblweqL9oWBD/roEB2EqwmM47YdQ3NQ1S1hRkLm3K5I7 CPe6e4b3YUcnqw/tBF6IItBYnJafx3fEdZ51oBJMVvNW10Eu/ZCdVn+wpMIlGzH8qn3pNrkpP2EW 19OSissnjVJ1E7v5TDGZBQVSHkcbQ7xr9oAjOscZcm6TtTPWKlsNvXayyZBVSnsszRPgmVaIZL+L CJGbcQV6HtOwqIFqmUfWNg0Adf4++K5SJWnEzdMP+SaaJyJnYGjklEkf9mQNxQKBgQDvc8YHreju RfGLNmaCUdr0G5iTSi7KQCymMmSRqOd1upD1v3zGCM4a+zVlGu1aQCmba+qWxXIeuygcwn01g01G ukrq4dP85/mnqisI4OG607NCRILbuupphk/tdYWbdI7o/Qave2slaczLlRpAgc/gYqcwCitljmnf KPvX/KU7gwKBgQC3K4A+UfrrMwKvgYxtcP6lCuG8mswBuDRkPC6LZrJc22S3HWw7LXVWwUi8SZdG oCUwDPFzfrHZsl7965SLKB7zk/1+VmSWt8ZueOMwOg7UPOfbMBg52PC1rOtu5JY4Bm1aZsHAIYDK tKKYUaazFyC23ZidNLQhMXINGN8aIscf2wKBgQCEh86WV4IxxxKem5h3DrkiHNgAxbFKDeTog8G4 AQVC2uT6r2Zu8VaqBloSQKoYJqUgucUYd+Xm7m2QJXFJmge+WsO2ZxF+zCIY042IF3e4gQ2ZYvQO i9DMYSOB6WbumL+0Yr89hxDRn1JTZ44lH/QfXFrusuI8Dmu4sSVa8SG+4wKBgAiVSkIhV0+0KTkO KgVq2RPkyaUr38lo11OnGks/+bWuNi76evre63OwRPdFv4f4syVoRdwyoKTh3d+qLWDD9YdWdPd5 lucVH4BHu+WjotRBMmAsBcaYKtdojfO5VGy1qGQnEoctSrq08jWPBe+4crj+80rSkGpJxd1lP/ca kBgnAoGBAMTq2u3BJ3tpT8YSlOA1ojtFOpKVzdYrKi+1piFjyNgEcTpf1f37x8SxYzVYz9gGor5A oAp8DzyJz/9RsSLJJmAV0lliiNbgBDjFOJvJaUy2Gx8KWD9AHW6vtMNnsssCyrrRHHk1HcojT6Uk fw/rSIouLd9rSkID/5AYUAwnNUai";
////		String priKey287 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQcp9lZjnjLrfxKMUeM3M9uhRs enziyPS3rTa1CBNrhdFSFJW2OLlqemFGD7toowuT15R53ccK921d5Xsi8ikhy4OUiYivJoA9Zztm r+uYRoN7WCFbuZzqlC8W0xjKZaCmAlEkMqARdbgrUrgewr3nAAMa6gNh40Rpq2Qplf9cuXwHfW16 ZMcm01RSMvWPa/QtMusKSgW6WZo3CBekV/o9CVSh9qYurbnlG1POtERS6YtcGAtcjrD3uuPamYFQ xYK2w+eg8q6lkert+2TlTPqTtmnaCAtlGnAC1RFAHRGoT+4Yvw4alDM3G1DUUbeiMmzazEQ4h1uH 3CVypeDMwX0fAgMBAAECggEAXR8oF2zB4O4bc4M/IOs2XhL6W7zTijjXWxp17FtaebT5bxsKMUF8 d2KSF2LJBPon2pXeiHoreaxte10X9z16uujC2R2ZWqFNh0hoCRlcnvzGgtwcFVAiEzCY8vQARWsj GCLiQJ3Kh7cGlhdrz5joaGWfmthGefLUBfOSTSUATbvd96mFPX/RDnvpQ1YR53caz6mPPm+Y/SkY vxKIRrW4KJrnWUc4XGEPtwlMSFhqwzTaLfIFps85Q6kJMvT7iD3oao8sb2QXXWz8jO3yMbXoFYuz F7uF5QVedCNC4lCbaXrQlm2vtz3fodg8iXw5WFXPsUOVgaVPFkP0ky5VAoKYEQKBgQD51REDagTf +HBBXvmxU8kazEg55nDfsvZxKoWnH3sRlYB3n3T6CJhKeLiuRGyWVWE6Bos7a4BNzbaLtXbGUiUd GsHplrEw56pVNIJp7i5+go9fJ9xOcBu1jn3Fok9PZxeX/SmGhiWSrysY+YKjyI28dFgd29ds7Vw0 PVoCOr8CNQKBgQCUA4dS3P+hrkD/r72dlZWgWFKW0assz389Nne05TEQwIdAed5nef6gwa4izCnG 6G/RccjZVyXfTJCGK6qWEyJZnAKg61a3yqanqQAwo7hUCAQ/A6m5AM2BvlyPh7oqRBc6h+67WxpN ZqA158VahV2HrRJZo7Ie+4Zoh9dNSmFsgwKBgEmZjnCODC+bNh7cBv1JrKY7Zk/AZqJQS6/dEhDB AnWUsIsNK935KDxQQj/8omzLbGA2y0/PBLZnEw8nf30/d/WSC2xwW3UH2rNNS9o/M+1LM0eaK5nk BxW+i6jsfybqPRYmm9Qosur6tdyerPDpRXAuakMpn9ZUOuSc6mZbvie9AoGAL/aYwnRU7hqee3yC cG9JpkUYtkDJPGMc80QUNG1Uof4RlgYn2NZCeetpN2N7YjZuDavWjv9biWmxJ5k81RbsOaeBfo3k cvhbWtc79YcIM0rJvXW9aPLmpVV/fw9Xd1zLPi9QYCwccNqBrhYl8Lho349o891E2h9dpb/nN+eT fGsCgYEA6Vf17+MTBko2U0sZFZ/quBORRNN+MVQz5F+iCai6knbvMC36h4T3LlXPiAwOVM3y2EHU dal5mEmgi0mmneJswiLa0Pg3O1Qftbr3IXzCb801Cco4XNlJaw30rEygRrqFcWyoaU07XJWZAcro azt2M8/2T53JecfxwiI8wBCbQ4U=";
//		PrivateKey privateKey =null;
//		try {
////			privateKey = RSAKeyUtils.string2PrivateKey(priKey);
//			privateKey = RSAKeyUtils.string2PrivateKey(priKey);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (InvalidKeySpecException e) {
//			e.printStackTrace();
//		}
//		String signInfo = DigitalEnvelopeUtils.sign0(digitalSignatureDTO,privateKey);
//		return signInfo;
//	}
//	public static void main(String[] args) {
//		String trxInfo = "10040018749order_processor_token1487659807329000ZFBasd";
//		String sign = generateSignInfo(trxInfo);
//		System.out.println("生成的签名为"+sign);
//	}

}
