package com.yeepay.g3.app.nccashier.wap.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.lang.StringUtils;

public class DataUtil {

	private DataUtil() {
		// default
	}

	// private static String urlRegx =
	// "(http://|https://){1}[a-zA-Z0-9\\.\\-/:\\?=\\%\\$\\~\\&\\+;_,#]+";

	private static Pattern p = Pattern.compile("[\\u4E00-\\u9FA5]");

	public static final String SYS_DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DEFAULTBASE64CHARSET = "iso-8859-1";

	public static final String DEFAULTCHARSET = "UTF-8";

	private static final Pattern emailPattern = Pattern.compile("(\\w+(.\\w+)*)@(\\w+(.\\w+)*)");

	private static final Pattern mobileNumberPattern =
			Pattern.compile("(15[0-9]|13[0-9]|18[0-9])\\d{8}");


	public static boolean isOutOfLength(String value, int minLen, int maxLen) {
		if (value == null) {
			return true;
		}
		if (value.length() > maxLen) {
			return true;
		}
		if (value.length() < minLen) {
			return true;
		}
		return false;
	}

	public static boolean checkOutOfLength(String value, int len) {
		if (value == null) {
			return false;
		}
		if (value.length() > len) {
			return true;
		}
		return false;
	}


	public static boolean isChinese(char ch) {
		Matcher m = p.matcher(String.valueOf(ch));
		return m.find() ? true : false;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		}
		if (obj instanceof Object[]) {
			if (((Object[]) obj).length == 0) {
				return true;
			}
		}
		if (obj instanceof String) {
			if (((String) obj).trim().isEmpty()) {
				return true;
			}
		}
		if (obj instanceof Map) {
			if (((Map) obj).isEmpty()) {
				return true;
			}
		}
		if (obj instanceof Collection) {
			if (((Collection) obj).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmpty(String value) {
		if (value != null && value.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isLegalEmail(String value) {
		if (value == null || value.trim().length() == 0) {
			return false;
		}
		if (value.length() > 50) {
			return false;
		}
		if (value.indexOf('.') == -1) {
			return false;
		}
		if (value.indexOf('@') != value.lastIndexOf('@')) {
			return false;
		}
		if (value.indexOf(',') != -1) {
			return false;
		}
		return emailPattern.matcher(value).matches();
	}

	public static boolean isLegalMobile(long value) {
		String v = value + "";
		if (isEmpty(v)) {
			return false;
		}
		return mobileNumberPattern.matcher(v).matches();
	}

	public static boolean isLegalMobile(String value) {
		if (isEmpty(value)) {
			return false;
		}
		return mobileNumberPattern.matcher(value).matches();
	}

	public static String limitLength(String str, int len) {
		if (str == null) {
			return null;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	public static String limitHtmlRow(String html, int len) {
		return DataUtil.toHtmlRow(DataUtil.limitLength(DataUtil.toTextRow(html), len));
	}

	public static String limitTextRow(String html, int len) {
		return DataUtil.limitLength(DataUtil.toTextRow(html), len);
	}

	/**
	 * 识别英文的获取特定长度的字符串
	 * 
	 * @param s
	 * @param len
	 * @return 2010-8-5
	 */
	public static String limitLengthEx(String s, int len) {
		// 逻辑为转换为字符数组遇到英文字符，长度+1，然后判断是否超出总长度，如果超出长度，就取到当前判断长度的位置的字符串
		if (isEmpty(s)) {
			return null;
		}
		if (s.length() <= len) {
			return s;
		}
		double _len = len;
		char[] ch = s.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (isEnChar(ch[i])) {
				_len = _len + 0.5;
				if (_len >= ch.length) {
					break;
				}
			}
		}
		if (_len >= ch.length) {
			return s;
		}
		return s.substring(0, (int) _len);
	}

	private static boolean isEnChar(char ch) {
		if (ch >= 'A' && ch <= 'z') {
			return true;
		}
		return false;
	}

	public static String subString(String str, int len) {
		if (str == null) {
			return null;
		}
		if (str.length() <= len) {
			return str;
		}
		return str.substring(0, len);
	}

	/**
	 * 过滤换行符
	 * 
	 * @param str
	 * @return
	 */
	public static String toRowValue(String str) {
		if (str != null) {
			return str.replaceAll("<br/>", "").replaceAll("<br>", "").replaceAll("\n", "")
					.replaceAll("\r", "").replaceAll("&nbsp;", " ").replaceAll("\"", "&quot;");
		}
		return null;
	}

	/**
	 * 把html数据转为转义字符，并消除换行
	 * 
	 * @param str
	 * @return
	 */
	public static String toHtmlRow(String str) {
		if (str == null) {
			return null;
		}
		String v = str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\n", "").replaceAll("\r", "").replaceAll("　", " ")
				.replaceAll(" +", " ").replaceAll("\"", "&quot;").replaceAll("'", "&#39;");
		return v;
	}

	/**
	 * 过滤html数据中的特殊字符，转为转义字符
	 * 
	 * @param str
	 * @return
	 */
	public static String filterHtmlRowValue(String str) {
		if (str == null) {
			return null;
		}
		String v = str// .replaceAll("&", "&amp;")//str中可能存在转义过的符号
				.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\s+", "&nbsp;")
				.replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
		return v;
	}

	// private static String getLenSpace(int len) {
	// StringBuilder sb = new StringBuilder();
	// for (int i = 0; i < len; i++) {
	// sb.append("&nbsp;");
	// }
	// return sb.toString();
	// }

	/**
	 * 把html转义字符转为普通文本 保留换行
	 * 
	 * @param str
	 * @return
	 */
	public static String toText(String str) {
		String v = null;
		if (str != null) {
			v = str.replaceAll("\r", "").replaceAll("\n", "").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">").replaceAll("<br/>", "\n").replaceAll("&amp;", "&")
					.replaceAll("&nbsp;", " ").replaceAll("&quot;", "\"").replaceAll("&#39;", "'");
		}
		return v;
	}

	/**
	 * 把html转义字符转为普通文本 去掉换行
	 * 
	 * @param str
	 * @return
	 */
	public static String toTextRow(String str) {
		String v = null;
		if (str != null) {
			v = str.replaceAll("\r", "").replaceAll("\n", "").replaceAll("&lt;", "<")
					.replaceAll("&gt;", ">").replaceAll("<br/>", "").replaceAll("&amp;", "&")
					.replaceAll("&nbsp;", " ").replaceAll("&quot;", "\"").replaceAll("&#39;", "'");
		}
		return v;
	}

	public static String urlDecoder(String value) {
		if (value != null) {
			try {
				return URLDecoder.decode(value, DEFAULTCHARSET);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}

	public static String urlDecoder(String value, String charset) {
		if (value != null) {
			try {
				return URLDecoder.decode(value, charset);
			} catch (UnsupportedEncodingException e) {
				System.out.println(e);
			}
		}
		return "";
	}

	public static String urlEncoder(String value) {
		if (value != null) {
			try {
				return URLEncoder.encode(value, DEFAULTCHARSET);
			} catch (UnsupportedEncodingException e) {
				System.out.println(e);
			}
		}
		return "";
	}

	public static String urlEncoder(String value, String charset) {
		if (value != null) {
			try {
				return URLEncoder.encode(value, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		return "";
	}

	public static Date parseTime(String v, String pattern) {
		if (DataUtil.isEmpty(v)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(v);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseTime(String timeString) {
		return parseTime(timeString, SYS_DEFAULT_TIME_FORMAT);
	}

	public static String getFormatTimeData(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String getFormatTimeData(Date date, String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date);
	}

	public static String getFormatTimeData(Date date) {
		return getFormatTimeData(date, SYS_DEFAULT_TIME_FORMAT);
	}

	public static String getRandom(int len) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}

	public static int getRandomNumber(int maxNumber) {
		Random r = new Random();
		return r.nextInt(maxNumber);
	}

	/**
	 * 如果end=-1,无法取结果
	 * 
	 * @param list
	 * @param begin
	 * @param size
	 * @return
	 */
	public static int getSelectedListEnd(List<?> list, int begin, int size) {
		int end = 0;
		if (begin > list.size() - 1) {
			return -1;
		}
		end = begin + size;
		if (end > list.size() - 1) {
			end = list.size();
		}
		return end;
	}

	public static long parseIpNumber(String ip) {
		String[] ips = ip.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ips.length; i++) {
			if (ips[i].length() < 3) {
				int zeron = 3 - ips[i].length();
				for (int k = 0; k < zeron; k++) {
					sb.append("0");
				}
			}
			sb.append(ips[i]);
		}
		try {
			return Long.parseLong(sb.toString());
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static boolean isInElements(Object obj, Object[] objs) {
		for (Object o : objs) {
			if (o.equals(obj)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 过滤http
	 * 
	 * @param v
	 * @return
	 */
	public static String filterHttp(String v) {
		if (v == null) {
			return null;
		}
		if (v.toLowerCase().startsWith("http://")) {
			return v.substring(7);
		}
		return v;
	}

	public static boolean endWithInterpunction(String s) {
		if (s != null) {
			char ch = s.charAt(s.length() - 1);
			if (ch == ',' || ch == '，' || ch == '.' || ch == '。' || ch == '!' || ch == '！'
					|| ch == '?' || ch == '？') {
				return true;
			}
		}
		return false;
	}

	public static String formatUrl(String content) {
		if (content == null) {
			return null;
		}
		return content.replaceAll(" www.", " http://www.");
	}

	public static String replaceRirstUrl(String s, String url, String replaceStr) {
		String tmpurl = url.replaceAll("\\?", "\\\\?").replaceAll("\\$", "\\\\$");
		return s.replaceFirst(tmpurl, replaceStr);
	}

	public static int countString(String source, String v) {
		String str = " " + source;
		if (str.endsWith(v)) {
			return str.split(v).length;
		}
		return str.split(v).length - 1;
	}

	public static String filterZoneName(String name) {
		int idx = name.indexOf("省");
		String v = name;
		if (idx != -1) {
			v = v.substring(idx + 1);
		}
		idx = v.indexOf("市");
		if (idx != -1) {
			v = v.replaceAll("市", "");
		}
		idx = v.indexOf("区");
		if (idx != -1) {
			v = v.replaceAll("区", "");
		}
		return v;
		// return name.replaceAll("省", "").replaceAll("市", "").replaceAll("区",
		// "");
	}

	public static Date createDate(int year, int month, int date) {
		if (year == 0 || month == 0 || date == 0) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DATE, date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// 中国移动：
	// 2G号段：134、135、136、137、138、139、150、151、152、155、157、158、159；
	// 3G号段：187、188；
	// 中国联通：
	// 2G号段：130|131|132|156|185|186
	// 3G号段：185、186；
	// 中国电信：
	// 2G号段：133|153|180|189
	// 3G号段：180、189；
	private static final Pattern mobilePattern = Pattern
			.compile("(134|135|136|137|138|139|150|151|152|155|157|158|159|187|188)[0-9]{8}");

	private static final Pattern unicomPattern =
			Pattern.compile("(130|131|132|156|185|186)[0-9]{8}");

	private static final Pattern telecomPattern = Pattern.compile("(133|153|180|189)[0-9]{8}");

	/**
	 * 判断是否是移动号码
	 * 
	 * @param mobile
	 * @return 2010-4-11
	 */
	public static boolean isCmpMobile(String mobile) {
		return mobilePattern.matcher(mobile).matches();
	}

	public static String getMobileType(String mobile) {
		if (isCmpMobile(mobile)) {
			return "mobile";
		}
		if (isCmpUnicom(mobile)) {
			return "unicom";
		}
		return "telecom";
	}

	/**
	 * 判断是不是联通号码
	 * 
	 * @param mobile
	 * @return 2010-4-11
	 */
	public static boolean isCmpUnicom(String mobile) {
		return unicomPattern.matcher(mobile).matches();
	}

	/**
	 * 判断是否是电信号码
	 * 
	 * @param mobile
	 * @return 2010-4-11
	 */
	public static boolean isCmpTelecom(String mobile) {
		return telecomPattern.matcher(mobile).matches();
	}

	/**
	 * 把正则表达式特殊字符编码后的的值
	 * 
	 * @param s
	 * @return 2010-5-7
	 */
	public static String getFilterRegValue(String s) {
		if (isEmpty(s)) {
			return s;
		}
		return s.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]").replaceAll("\\{", "\\\\{")
				.replaceAll("\\}", "\\\\}").replaceAll("\\*", "\\\\*").replaceAll("\\?", "\\\\?")
				.replaceAll("\\+", "\\\\+").replaceAll("\\.", "\\\\.").replaceAll("\\(", "\\\\(")
				.replaceAll("\\)", "\\\\)").replaceAll("\\^", "\\\\^").replaceAll("\\|", "\\\\|")
				.replaceAll("\\$", "\\\\$");
	}

	public static String filerUnicodeString(String value) {
		char[] ss = value.toCharArray();
		for (int i = 0; i < ss.length; ++i) {
			if (ss[i] > 0xFFFD) {
				ss[i] = ' ';
			} else if (ss[i] < 0x20 && ss[i] != 't' & ss[i] != 'n' & ss[i] != 'r') {
				ss[i] = ' ';
			}
		}
		return new String(ss);
	}

	public static boolean validateIp(String ip) {
		if (ip == null) {
			return false;
		}
		String[] s = ip.split("\\.");
		if (s.length != 4) {
			return false;
		}
		for (int i = 0; i < s.length; i++) {
			int a = Integer.parseInt(s[i]);
			if (a < 0 || a > 255) {
				return false;
			}
			if (i == 3 && a == 0) {
				return false;
			}
		}
		return true;
	}

	public static String getLegalIp(String ip) {
		if (DataUtil.validateIp(ip)) {
			return ip;
		}
		return null;
	}

	public static boolean isSameDay(Date a, Date b) {
		Calendar cal_a = Calendar.getInstance();
		Calendar cal_b = Calendar.getInstance();
		cal_a.setTime(a);
		cal_b.setTime(b);
		if (cal_a.get(Calendar.DATE) == cal_b.get(Calendar.DATE)
				&& cal_a.get(Calendar.MONTH) == cal_b.get(Calendar.MONTH)
				&& cal_a.get(Calendar.YEAR) == cal_b.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}

	public static <T> List<T> subList(List<T> list, int begin, int size) {
		if (list.size() - 1 < begin) {
			return new ArrayList<T>();
		}
		if (list.size() <= size) {
			return list.subList(begin, begin + list.size());
		}
		return list.subList(begin, begin + size);
	}

	public static String getFmtTime(Date date) {
		long v = System.currentTimeMillis() - date.getTime();
		long sec = v / 1000;
		long min = sec / 60;
		long h = min / 60;
		if (sec == 0) {
			return "1秒前";
		}
		StringBuilder sb = new StringBuilder();
		if (h > 0) {
			if (h < 24) {
				sb.append(h).append("小时前");
				return sb.toString();
			}
			long day = h / 24;
			if (day < 30 && day > 0) {
				sb.append(day).append("天前");
				return sb.toString();
			}
			long month = day / 30;
			if (month < 12 && month > 0) {
				sb.append(month).append("个月前");
				return sb.toString();
			}
			sb.append(month / 12).append("年前");
			return sb.toString();
		}
		if (min > 0) {
			sb.append(min).append("分前");
			return sb.toString();
		}
		sb.append(sec).append("秒前");
		return sb.toString();
	}

	/**
	 * 按照中文汉字进行排序
	 * 
	 * @param array
	 * @return
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static String[] getSortedByName(String[] array) {
		Comparator cmp = Collator.getInstance(Locale.CHINA);
		String[] arr = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			arr[i] = array[i];
		}
		Arrays.sort(arr, cmp);
		return arr;
	}

	/**
	 * 只保留日期数据，时间数据为0
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 获得某个日期的结束时间 HH:mm:ss:ms 为23:59:59:0
	 * 
	 * @param date
	 * @return
	 */
	public static Date getEndDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static int getRandomPageBegin(int count, int size) {
		int ncount = count;
		Random r = new Random();
		int begin = 0;
		if (ncount > size) {
			ncount = ncount - size + 1;
			while ((begin = r.nextInt(ncount)) < 0) {
			}
		}
		return begin;
	}

	/**
	 * 复制文件
	 * 
	 * @param source 原文家路径
	 * @param distm 要复制到的目标路径 2010-5-4
	 */
	public static void copyFile(String source, String dist, String fileName) throws IOException {
		copyFile(new File(source), dist, fileName);
	}

	public static void mkdir(String path) {
		File f = new File(path);
		if (!f.exists() || !f.isDirectory()) {
			f.mkdirs();
		}
	}

	public static boolean isFileDirectory(String path) {
		File f = new File(path);
		if (!f.exists()) {
			return false;
		}
		return f.isDirectory();
	}

	/**
	 * 复制文件
	 * 
	 * @param source 原文家路径
	 * @param distm 要复制到的目标路径 2010-5-4
	 */
	public static void copyFile(File file, String dist, String fileName) throws IOException {
		File f = new File(dist);
		if (!f.exists() || !f.isDirectory()) {
			f.mkdirs();
		}
		BufferedOutputStream buffos = null;
		BufferedInputStream buffis = null;
		byte[] by = new byte[1024];
		try {
			buffos = new BufferedOutputStream(new FileOutputStream(dist + "/" + fileName));
			buffis = new BufferedInputStream(new FileInputStream(file));
			int len = -1;
			while ((len = buffis.read(by)) != -1) {
				buffos.write(by, 0, len);
			}
			buffos.flush();
		} catch (FileNotFoundException e) {
		} finally {
			try {
				if (buffos != null) {
					buffos.close();
				}
			} catch (IOException e) {
			}
			try {
				if (buffis != null) {
					buffis.close();
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 查看文件大小是否大于指定大小
	 * 
	 * @param file
	 * @param size 单位为k
	 * @return true:文件大小超过指定大小,false:文件大小在指定大小之内 2010-5-10
	 */
	public static boolean isBigger(File file, long size) {
		long fileSize = file.length();
		BigDecimal decimal =
				new BigDecimal(fileSize).divide(new BigDecimal(1024), 0, BigDecimal.ROUND_HALF_UP);
		long amount = decimal.longValue() - size;
		if (amount > 0) {
			return true;
		}
		return false;
	}

	public static final int FILE_SIZE_TYPE_K = 0;

	public static final int FILE_SIZE_TYPE_M = 1;

	public static long getFileSize(File file, int sizeType) {
		long fileSize = file.length();
		if (sizeType == FILE_SIZE_TYPE_K) {
			BigDecimal decimal = new BigDecimal(fileSize).divide(new BigDecimal(1024), 0,
					BigDecimal.ROUND_HALF_UP);
			return decimal.longValue();
		}
		if (sizeType == FILE_SIZE_TYPE_M) {
			BigDecimal decimal = new BigDecimal(fileSize).divide(new BigDecimal(1024 * 1024), 0,
					BigDecimal.ROUND_HALF_UP);
			return decimal.longValue();
		}
		return fileSize;
	}

	public static boolean isImage(File input) throws IOException {
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(input);
			Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
			if (readers.hasNext()) {
				return true;
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			return false;
		} finally {
			if (iis != null) {
				try {
					iis.close();
				} catch (IOException e) {
					throw e;
				}
				iis = null;
			}
		}
		return false;
	}

	public static void deleteFile(File file) {
		if (file == null) {
			return;
		}
		if (file.exists()) {
			file.delete();
		}
		File parent = file.getParentFile();
		if (parent != null) {
			parent.delete();
		}
	}

	/**
	 * 验证2个字符串是否相等，当字符串都为null时，返回不相等
	 * 
	 * @param s1
	 * @param s2
	 * @return 2010-7-12
	 */
	public static boolean eqNotNull(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return false;
		}
		if (s1.equals(s2)) {
			return true;
		}
		return false;
	}

	public static String fmtDouble(double number, String pattern) {
		DecimalFormat fmt = new DecimalFormat(pattern);
		return fmt.format(number);
	}


	public static String getDistanceTime(Date date) {
		long v = System.currentTimeMillis() - date.getTime();
		long sec = v / 1000;
		long min = sec / 60;
		long h = min / 60;
		if (sec == 0) {
			return "1秒前";
		}
		StringBuilder sb = new StringBuilder();
		if (h > 0) {
			if (h < 24) {
				sb.append(h).append("小时前");
				return sb.toString();
			}
			// long day = h / 24;
			// if (day < 30 && day > 0) {
			// sb.append(day).append("天前");
			// return sb.toString();
			// }
			// long month = day / 30;
			// if (month < 12 && month > 0) {
			// sb.append(month).append("个月前");
			// return sb.toString();
			// }
			// sb.append(month / 12).append("年前");
			// return sb.toString();
			SimpleDateFormat _fmt0 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return _fmt0.format(date);
		}
		if (min > 0) {
			sb.append(min).append("分钟前");
			return sb.toString();
		}
		sb.append(sec).append("秒前");
		// sb.append("刚刚");
		return sb.toString();
	}

	/**
	 * 验证Email格式
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmail(String value) {
		if (value == null || value.trim().length() == 0) {
			return false;
		}
		if (value.indexOf('.') == -1) {
			return false;
		}
		if (value.indexOf('@') != value.lastIndexOf('@')) {
			return false;
		}
		if (value.indexOf(',') != -1) {
			return false;
		}
		if (value.length() > 50) {
			return false;
		}
		return emailPattern.matcher(value).matches();
	}

	/**
	 * 验证手机号码格式 匹配格式： 11位手机号码 ， 方法一："^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"; 方法二：
	 * "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isMobile(String value) {
		if (isEmpty(value)) {
			return false;
		}
		// String regex = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		String regex = "^1\\d{10}$";
		return value.matches(regex);
	}

	/**
	 * 验证邮政编码格式 匹配格式： 3~12位邮政编码 ， "^[a-zA-Z0-9]{3,12}$"
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPostcode(String value) {
		String regex = "^[a-zA-Z0-9-]{3,12}$";
		return value.matches(regex);
	}

	/**
	 * 验证邮政编码格式(CHINA)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPostcodeChina(String value) {
		String regex = "^\\d{6}$";
		return value.matches(regex);
	}

	/**
	 * 验证身份证号码格式 匹配格式： "^\d{14}(\d{1}|\d{4}|(\d{3}[xX]))$"
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isCertificateSFZ(String value) {
		String regex = "^\\d{14}(\\d{1}|\\d{4}|(\\d{3}[xX]))$";
		return value.matches(regex);
	}

	/**
	 * 验证军官证号码格式 匹配格式： "^\d{6,8}$"
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isCertificateJGZ(String value) {
		String regex = "^\\d{6,8}$";
		return value.matches(regex);
	}

	/**
	 * 验证护照号码格式 匹配格式： "(P\\d{7})|(G\\d{8})"
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isCertificateHZ(String value) {
		String regex = "(P\\d{7})|(G\\d{8})";
		return value.matches(regex);
	}

	/**
	 * 验证网址格式
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isSiteAddress(String value) {
		String regex =
				"((http|https|ftp):(\\/\\/|\\\\)((\\w)+[.]){1,}(net|com|cn|org|cc|tv|[0-9]{1,3})(((\\/[\\~]*|\\[\\~]*)(\\w)+)|[.](\\w)+)*(((([?](\\w)+){1}[=]*))*((\\w)+){1}([\\&](\\w)+[\\=](\\w)+)*)*)";
		if (!value.matches(regex)) {
			return false;
		}
		if (value.length() > 255) {
			return false;
		}
		return true;
	}

	/**
	 * 验证数字格式 (只针对于广告位的长,宽,大小)
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumber2(String value) {
		String regex = "\\d+[.]?\\d+";
		if (!value.matches(regex)) {
			return false;
		}
		if (Double.parseDouble(value) <= 0 || Double.parseDouble(value) > 1024) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 将无小数的long金额转换为有小数的string
	 * 
	 * @author liyu
	 * @modifytime 2012-10-20 下午06:08:31
	 * @version 1.0
	 * 
	 * @param money
	 * @return
	 */
	public static String changeLongMoneyToString(Long money) {
		String t2 = String.valueOf(money);
		if (t2.length() > 2) {
			t2 = t2.substring(0, t2.length() - 2) + "." + t2.substring(t2.length() - 2);
		} else if (t2.length() == 2) {
			t2 = "0." + t2;
		} else if (t2.length() == 1) {
			t2 = "0.0" + t2;
		} else {
			t2 = "0.0";
		}
		return t2;
	}

	/**
	 * 将double类型的金额数据转换成long整型 会保留原double类型的两位小数，之后还有数值的会四舍五入处理
	 * 
	 * @author liyu
	 * @modifytime 2012-9-3 上午10:56:24
	 * @version 1.0
	 * 
	 * @param money
	 * @return
	 */
	public static long double2Long(double money) {
		String s = money + "";
		if (s.indexOf(".") == -1) { // 不包含小数点
			return Long.parseLong(s + "00");
		} else {
			BigDecimal tmp = new BigDecimal(money * 100).setScale(0, BigDecimal.ROUND_HALF_UP);
			money = tmp.longValue();
			String moneyStr = String.valueOf(money);
			if (moneyStr.indexOf("E") == -1) {
				s = money + "";
				s = s.substring(0, s.indexOf("."));
			} else {
				int i = moneyStr.substring(moneyStr.indexOf(".") + 1, moneyStr.indexOf("E"))
						.length();
				int j = Integer.parseInt(moneyStr.substring(moneyStr.indexOf("E") + 1));
				int x = j - i;
				s = moneyStr.substring(0, moneyStr.indexOf("E")).replace(".", "");
				for (int y = 0; y < x; y++) {
					s = s + "0";
				}
			}
		}
		return Long.parseLong(s);
	}

	/**
	 * 检验值长度是否在区间块之间
	 * 
	 * @param value 验证值
	 * @param start 左区间值
	 * @param end 右区间值
	 * @return
	 * @author da.zhang
	 * @date 2013-3-4 下午5:55:32
	 */
	public static boolean checkBlock(String value, int start, int end) {
		if (isEmpty(value)) {
			return false;
		}
		if (start < 0) {
			return false;
		}
		if (end < 0) {
			return false;
		}
		if (start > end) {
			return false;
		}
		if (start == end) {
			if (value.length() == start) {
				return true;
			} else {
				return false;
			}
		}
		if (value.length() >= start && value.length() <= end) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 简单验证银行卡号格式 匹配格式： ^\\d{15,19}$ java正则格式 /\d{15,19}$/ js正则格式
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isBankCardNum(String value) {
		if (isEmpty(value)) {
			return false;
		}
		String regex = "^\\d{14,19}$";
		return value.matches(regex);
	}

	/**
	 * 验证信用卡有效期格式
	 * 
	 * @param validthru 信用卡有效期月日
	 * @return boolean
	 * @author da.zhang
	 * @date 2013-6-27
	 */
	public static boolean isValidthru(String validthru) {
		if (isEmpty(validthru)) {
			return false;
		}
		String regex =
				"^((([0][13578])|([1][02]))(([0][1-9])|([12][0-9])|([3][01])))|((([0][469])|(11))(([0][1-9])|([12][0-9])|(30)))|((02)(([0][1-9])|([12][0-9])))$";
		return validthru.matches(regex);
	}

	/**
	 * 验证n位数字格式
	 * 
	 * @param value
	 * 
	 * @param count 共n位
	 * @return
	 */
	public static boolean isNumCount(String value, int count) {
		if (isEmpty(value)) {
			return false;
		}
		String regex = "^\\d{" + count + "}$";
		return value.matches(regex);
	}

	/**
	 * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
	 * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数） 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
	 * 3、出生日期码（第七位至十四位） 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
	 * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） （1）十七位数字本体码加权求和公式 S
	 * = Sum(Ai * Wi), i = 0, , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8
	 * 4 2 1 6 3 7 9 10 5 8 4 2 （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码:
	 * 1 0 X 9 8 7 6 5 4 3 2
	 */
	/**
	 * 功能：身份证的有效验证
	 * 
	 * @param IDStr 身份证号
	 * @return 有效：返回"" 无效：返回String信息
	 * @throws ParseException
	 */
	@SuppressWarnings({"rawtypes"})
	public static String IDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
		String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8",
				"4", "2"};
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime()
				- s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
			errorInfo = "身份证生日不在有效范围。";
			return errorInfo;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static Hashtable GetAreaCode() {
		Hashtable hashtable = new Hashtable();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern.compile(
				"^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 过滤非法字符，进行HTML编码 （防挂马攻击）
	 * 
	 * @param src 非法字符
	 * @return String
	 * @author da.zhang
	 * @date 2013-7-4
	 */
	public static String killXss(String src) {
		src = src.replace("<", "&#60;");
		src = src.replace(">", "&#62;");
		src = src.replace("\"", "&#34;");
		src = src.replace("'", "&#39;");
		return src;
	}

	/**
	 * 验证中文格式 （条件：必须为长度<=20的“中文”，中间允许包含“·”“.”）
	 * 
	 * @param value
	 * 
	 * @return
	 */
	public static boolean isChinese(String value) {
		if (isEmpty(value)) {
			return false;
		}
		if (value.length() > 20) {
			return false;
		}
		String regex = "^[\u4E00-\u9FA5·.]+$";
		return value.matches(regex);
	}

	/**
	 * 处理超过一定长度的字符串 （例如： maxLength为5，src为"奇虎360"，fill为"*"， 则返回"奇虎360"
	 * maxLength为5，src为"奇虎360子公司"，fill为"*"， 则返回"奇虎360*" maxLength为5，src为"奇虎"，fill为"*"， 则返回"奇虎" ）
	 * 
	 * @param src 原字符串
	 * @param maxLength 最大长度
	 * @param fill 填充的字符串
	 * @return 处理后的字符串
	 */
	public static String proccessStrTooLong(String src, int maxLength, String fill) {
		if (StringUtils.isEmpty(src)) {
			return src;
		}

		if (src.length() <= maxLength) {
			return src;
		}

		src = src.substring(0, maxLength) + fill;

		return src;
	}

	/**
	 * 获得可显手机号（隐藏中间位）
	 * 
	 * @param mobile 手机号
	 * @param first 显示前几位
	 * @param last 显示后几位
	 * @return String
	 * @author da.zhang
	 * @date 2013-8-1
	 */
	public static String getDisplayMobile(String mobile, int first, int last) {

		if (StringUtils.isEmpty(mobile)) {
			return null;
		}
		if ((first + last) > mobile.length()) {
			return mobile;
		}
		StringBuffer stb = new StringBuffer();
		stb.append(mobile.substring(0, first));
		int hiddenNumber = mobile.length() - first - last;
		for (int i = 0; i < hiddenNumber; i++) {
			stb.append("*");
		}
		stb.append(mobile.substring(mobile.length() - last));

		return stb.toString();
	}

	/*
	 * public static void main(String[] args) { System.out.println(isMobile("13311074046"));
	 * System.out.println(isMobile("13911361730")); System.out.println(isMobile("14526268034")); try
	 * { System.out.println(IDCardValidate("110101198511121332")); } catch (ParseException e) {
	 * e.printStackTrace(); }
	 * 
	 * System.out.println(isChinese(".中国·人"));
	 * 
	 * }
	 */
	/**
	 * 字段长度截取
	 * 
	 * @param text 目标字符串
	 * @param length 截取最后长度
	 * @param encode 采用的编码方式
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String sublaststring(String text, int length, String encode)
			throws UnsupportedEncodingException {
		if (text == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		int currentLength = 0;
		char[] c = text.toCharArray();
		List<Character> list = new ArrayList<Character>();
		for (int i = c.length; i > 0; i--) {
			char one = c[i - 1];
			currentLength += String.valueOf(one).getBytes(encode).length;
			if (currentLength <= length) {
				// sb.append(c);
				list.add(one);
			} else {
				break;
			}
		}
		for (int i = 0; i < list.size(); i++) {
			char x = (Character) list.get(list.size() - 1 - i);
			sb.append(x);
		}
		return sb.toString();

	}
}
