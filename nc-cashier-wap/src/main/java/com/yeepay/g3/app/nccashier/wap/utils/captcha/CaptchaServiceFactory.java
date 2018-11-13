package com.yeepay.g3.app.nccashier.wap.utils.captcha;

import java.io.IOException;
import java.io.OutputStream;


import org.patchca.font.RandomFontFactory;
import org.patchca.service.CaptchaService;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.app.nccashier.wap.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 验证码处理器
 * 
 * @author duangduang
 * @date 2017-06-02
 */
public class CaptchaServiceFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaServiceFactory.class);

	private static final String DEFAULT_CHARACTERS = "abdefghmnqrtyABDEFGHMNRTY3456789"; // 自己设置！
	private static int DEFAULT_FONT_SIZE = 25;
	private static int DEFAULT_MIN_WORD_LENGTH = 4;
	private static int DEFAULT_MAX_WORD_LENGTH = 4;
	private static int DEFAULT_WIDTH = 80;
	private static int DEFAULT_HEIGHT = 35;

	private CaptchaServiceFactory() {
	}

	public static CaptchaService create(int fontSize, int minWordLen, int maxWordLen, String characters, int width,
			int height) {
		ConfigurableCaptchaService service = null;

		// 字体大小设置
		RandomFontFactory ff = new RandomFontFactory();
		ff.setMinSize(fontSize);
		ff.setMaxSize(fontSize);

		// 生成的单词设置
		RandomWordFactory rwf = new RandomWordFactory();
		rwf.setCharacters(characters);
		rwf.setMinLength(DEFAULT_MIN_WORD_LENGTH);
		rwf.setMaxLength(DEFAULT_MAX_WORD_LENGTH);

		// 处理器
		service = new CustomConfigurableCaptchaService();
		service.setFontFactory(ff);
		service.setWordFactory(rwf);

		// 生成图片大小（像素）
		service.setWidth(width);
		service.setHeight(height);
		return service;
	}

	public static CaptchaService create() {
		return create(DEFAULT_FONT_SIZE, DEFAULT_MIN_WORD_LENGTH, DEFAULT_MAX_WORD_LENGTH, DEFAULT_CHARACTERS,
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	/**
	 * 将验证码图片写入输出流，并返回验证码
	 * 
	 * @param outputStream
	 * @param imgFileFormat
	 * @return
	 * @throws IOException
	 */
	public static void writeCaptchaToOutputStream(OutputStream outputStream, String imgFileFormat, String token)
			throws IOException {
		if (StringUtils.isBlank(imgFileFormat)) {
			imgFileFormat = Constant.DEFAULT_IMG_FILE_FORMAT;
		}
		try {
			CaptchaService captchaService = CaptchaServiceFactory.create();
			String captcha = EncoderHelper.getChallangeAndWriteImage(captchaService, imgFileFormat, outputStream);
			boolean result = RedisTemplate.setCacheCoverOldValue(Constant.VALIDATE_CODE_REDIS_KEY+token, captcha, Constant.ONE_MINUTE);
			if(!result){
				LOGGER.warn("验证码缓存设置失败");
			}
		} catch (Throwable e) {
			LOGGER.error("获取会员支付页面验证码异常token="+token, e);
		} finally {
			outputStream.flush();
			outputStream.close();
		}
	}
}
