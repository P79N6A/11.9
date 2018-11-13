/** 
 * Copyright: Copyright (c)2014
 * Company: 易宝支付(YeePay) 
 */
package com.yeepay.g3.app.nccashier.wap.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/**
 * Description
 * PackageName: com.yeepay.g3.app.nccashier.wap.utils
 *
 * @author pengfei.chen
 * @since 16/8/3 16:05
 */
public final class QrCodeUtil {

	private static final Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

	private static final int DEFAULT_COLOR_FRONT = 0xFF263E22;

	private static final int DEFAULT_COLOR_BACK = 0xFFFFFFFF;

	private static final Color DEFAULT_BORDER_COLOR = Color.WHITE;

	private static final int DEFAULT_BORDER = 3;

	private static final int DEFAULT_LOGOPART = 2;

	/**
	 * 生成二维码
	 * <br>注：由于zxing类库生成的二维码带有不可控宽度的白边，此方法统一去除了白边，
	 * 返回二维码尺寸可能小于入参，建议前端重新统一设置二维码图片尺寸
	 * @param context
	 * @param width
	 * @param height
	 * @param logoImage
	 * @return
	 * @throws IOException
	 * @throws WriterException
	 */
	public static BufferedImage encodeImage(String context, int width, int height, InputStream logoImage) throws IOException, WriterException {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1); //此参数未起作用
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		BitMatrix matrix = multiFormatWriter.encode(context, BarcodeFormat.QR_CODE, width, height, hints);
		BitMatrix pureMatrix = deleteWhite(matrix);
		BufferedImage image = new BufferedImage(pureMatrix.getWidth(), pureMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < pureMatrix.getWidth(); x++) {
			for (int y = 0; y < pureMatrix.getHeight(); y++) {
				image.setRGB(x, y, pureMatrix.get(x, y) ? DEFAULT_COLOR_FRONT : DEFAULT_COLOR_BACK);
			}
		}
		if (logoImage == null) {
			return image;
		}
		setLogoImage(image, ImageIO.read(logoImage));
		return image;
	}

	/**
	 * 添加logo图片
	 * 
	 * @param image
	 * @param logo
	 */
	private static void setLogoImage(BufferedImage image, BufferedImage logo) {
		Graphics2D g = image.createGraphics();
		int widthLogo = (int) (logo.getWidth());
		int heightLogo = (int) (logo.getHeight());
		int x = (image.getWidth() - widthLogo) / DEFAULT_LOGOPART;
		int y = (image.getHeight() - heightLogo) / DEFAULT_LOGOPART;
		g.drawImage(logo, x, y, widthLogo, heightLogo, null);
		g.drawRoundRect(x, y, widthLogo, heightLogo, 16, 16);
		g.setStroke(new BasicStroke(DEFAULT_BORDER));
		g.setColor(DEFAULT_BORDER_COLOR);
		g.drawRect(x, y, widthLogo, heightLogo);
		g.dispose();
	}

	/**
	 * 去除生成的二维码白色的边缘部分
	 * @param matrix
	 * @return
	 */
	private static BitMatrix deleteWhite(BitMatrix matrix) {
		try {
			int[] rec = matrix.getEnclosingRectangle(); //二维码有效部分的位置和尺寸信息
			int resLeft = rec[0];
			int resTop = rec[1];
			int resWidth = rec[2];
			int resHeight = rec[3];
			if (0 == resLeft && 0 == resTop) {
				//左偏移和上偏移均为0，即没有白边，直接返回
				return matrix;
			}
			BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
			resMatrix.clear();
			for (int i = 0; i < resWidth; i++) {
				for (int j = 0; j < resHeight; j++) {
					if (matrix.get(i + resLeft, j + resTop))
						resMatrix.set(i, j); //重新按有效尺寸绘制无白边的二维码
				}
			}
			return resMatrix;
		} catch (Exception e) {
			//去除二维码白边异常，避免影响业务逻辑
			logger.warn("deleteWhite() 二维码去除白边异常，返回原二维码. ",e);
			return matrix;
		}
	}

}
