package com.yeepay.g3.app.nccashier.wap.utils.captcha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.patchca.background.BackgroundFactory;
import org.patchca.color.ColorFactory;
import org.patchca.color.SingleColorFactory;
import org.patchca.filter.ConfigurableFilterFactory;
import org.patchca.filter.FilterFactory;
import org.patchca.filter.library.AbstractImageOp;
import org.patchca.filter.library.WobbleImageOp;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.Captcha;
import org.patchca.service.ConfigurableCaptchaService;

/**
 * 自定义验证码配置工厂
 * @author duangduang
 *
 */
public class CustomConfigurableCaptchaService extends ConfigurableCaptchaService {

	private static final Random RANDOM = new Random();

	private List<SingleColorFactory> colorList = new ArrayList<SingleColorFactory>(); // 为了性能

	public CustomConfigurableCaptchaService() {
		colorList.add(new SingleColorFactory(Color.blue));
		colorList.add(new SingleColorFactory(Color.black));
		colorList.add(new SingleColorFactory(Color.red));
		colorList.add(new SingleColorFactory(Color.pink));
		colorList.add(new SingleColorFactory(Color.orange));
		colorList.add(new SingleColorFactory(Color.green));
		colorList.add(new SingleColorFactory(Color.magenta));
		colorList.add(new SingleColorFactory(Color.cyan));
	}

	/**
	 * 设置验证码颜色
	 * 
	 * @return
	 */
	public ColorFactory nextColorFactory() {
		int index = RANDOM.nextInt(colorList.size());
		return colorList.get(index);
	}

	/**
	 * 设置干扰线
	 * 
	 * @return
	 */
	public FilterFactory nextFilterFactory(boolean isRandom) {
		ConfigurableFilterFactory filterFactory = new ConfigurableFilterFactory();
		List<BufferedImageOp> filters = new ArrayList<BufferedImageOp>();
		WobbleImageOp wobbleImageOp = new WobbleImageOp();
		wobbleImageOp.setEdgeMode(AbstractImageOp.EDGE_MIRROR);
		wobbleImageOp.setxAmplitude(2.0);
		wobbleImageOp.setyAmplitude(1.0);
		filters.add(wobbleImageOp);
		filterFactory.setFilters(filters);
		return filterFactory;
	}

	@Override
	public Captcha getCaptcha() {
		config();
		return super.getCaptcha();
	}

	/**
	 * 配置字体、颜色、背景工厂
	 */
	private void config() {
		// 背景
		BackgroundFactory backgroundFactory = new CustomCaptchaBackgroundFactory();
		this.setBackgroundFactory(backgroundFactory);

		// 颜色
		ColorFactory cf = nextColorFactory();
		this.setColorFactory(cf);

		// 滤镜（干扰线）
		FilterFactory filterFactory = this.nextFilterFactory(false);
		this.setFilterFactory(filterFactory);
		if (filterFactory instanceof CurvesRippleFilterFactory) {
			((CurvesRippleFilterFactory) filterFactory).setColorFactory(cf);
		}

	}

	/**
	 * 验证码背景工厂
	 * 
	 * @author duangduang
	 * @date 2017-06-02
	 */
	private class CustomCaptchaBackgroundFactory implements BackgroundFactory {

		private Random random = new Random();

		public void fillBackground(BufferedImage image) {
			Graphics graphics = image.getGraphics();

			// 验证码图片的宽高
			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();

			// 填充为白色背景
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, imgWidth, imgHeight);

			// 画100个噪点(颜色及位置随机)
			for (int i = 0; i < 100; i++) {
				// 随机颜色
				int rInt = random.nextInt(255);
				int gInt = random.nextInt(255);
				int bInt = random.nextInt(255);

				graphics.setColor(new Color(rInt, gInt, bInt));

				// 随机位置
				int xInt = random.nextInt(imgWidth - 3);
				int yInt = random.nextInt(imgHeight - 2);

				// 随机旋转角度
				int sAngleInt = random.nextInt(360);
				int eAngleInt = random.nextInt(360);

				// 随机大小
				int wInt = random.nextInt(6);
				int hInt = random.nextInt(6);

				graphics.fillArc(xInt, yInt, wInt, hInt, sAngleInt, eAngleInt);

				// 画5条干扰线
				if (i % 20 == 0) {
					int xInt2 = random.nextInt(imgWidth);
					int yInt2 = random.nextInt(imgHeight);
					graphics.drawLine(xInt, yInt, xInt2, yInt2);
				}
			}
		}

	}

}
