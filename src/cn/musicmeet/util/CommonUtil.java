package cn.musicmeet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

public class CommonUtil {

	private final static Logger logger = Logger.getLogger(CommonUtil.class);

	public static String base64Encode(String data) {
		try {
			return Base64.encodeBase64String(data.getBytes(Charsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static byte[] base64Decode(String data) {
		return Base64.decodeBase64(data);
	}

	public static String sha512(String data) {
		try {
			return byte2hex(MessageDigest.getInstance("SHA-512").digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static String sha256(String data) {
		try {
			return byte2hex(MessageDigest.getInstance("SHA-256").digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static String sha1(String data) {
		try {
			return byte2hex(MessageDigest.getInstance("SHA-1").digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static String md5(String data) {
		try {
			return byte2hex(MessageDigest.getInstance("MD5").digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	// 获得指定长度的随机字符串
	public static String getRandString(int length) {
		try {
			byte[] bytes = new byte[length / 2];
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			secureRandom.nextBytes(bytes);
			return byte2hex(bytes);
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	// 获得指定长度的随机数字
	public static String getRandNumber(int length) {
		try {
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
			MersenneTwister mt = new MersenneTwister(secureRandom.nextLong());
			return String.valueOf(mt.nextLong((long) Math.pow(10, length) - 1) % ((long) Math.pow(10, length) - (long) Math.pow(10, length - 1)) + (long) Math.pow(10, length - 1));
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 将字符串转换成二进制流
	public static byte[] hex2byte(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	// 将二进制流转换成字符串
	public static String byte2hex(byte[] bytes) {
		BigInteger bi = new BigInteger(1, bytes);
		String hex = bi.toString(16);
		int paddingLength = (bytes.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		return hex;
	}

	// 在比较哈希值的时候，经过固定的时间才返回结果,避免攻击
	public static boolean slowEquals(String oldPwd, String newPwd) {
		char[] a = oldPwd.toCharArray();
		char[] b = newPwd.toCharArray();
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}

	public static boolean inArray(Object source, Object ext) {
		return inArray(source, ext, false);
	}

	public static boolean inArray(Object source, Object ext, boolean strict) {
		if ((source == null) || (ext == null)) {
			return false;
		}
		if ((source instanceof Collection)) {
			for (Object s : (Collection<?>) source) {
				if (s.toString().equals(ext.toString())) {
					if (strict) {
						if (s.getClass().getName().equals(ext.getClass().getName())) {
							return true;
						}
					} else {
						return true;
					}
				}
			}
		} else {
			Object[] arrayOfObject;
			int j = (arrayOfObject = (Object[]) source).length;
			for (int i = 0; i < j; i++) {
				Object s = arrayOfObject[i];
				if (s.toString().equals(ext.toString())) {
					if (strict) {
						if (s.getClass().getName().equals(ext.getClass().getName())) {
							return true;
						}
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getGMTTime(long time) {
		SimpleDateFormat format_en = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.US);
		TimeZone time_zone = TimeZone.getTimeZone("GMT");
		Calendar cal_en = Calendar.getInstance(Locale.US);
		cal_en.setTimeInMillis(time);
		format_en.setTimeZone(time_zone);
		return format_en.format(cal_en.getTime());
	}

	public static Date getDate(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime();
	}

	public static String getFormatDate(long time) {
		return getFormatDate(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getFormatDate(long time, String parttern) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(parttern);
		cal.setTimeInMillis(time);
		return format.format(cal.getTime());
	}

	public static Date getFormatDate(String time, String parttern) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(parttern);
			return format.parse(time);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getGenerateRandomId() {
		// 订单号规则，当前时间 yyyymmddHHmmss+6位随机数
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));// 得到年
		String month = String.valueOf(cal.get(Calendar.MONTH) + 1);// 得到月，因为从0开始的，所以要加1
		String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));// 得到天
		String hour = String.valueOf(cal.get(Calendar.HOUR));// 得到小时
		String minute = String.valueOf(cal.get(Calendar.MINUTE));// 得到分钟
		String second = String.valueOf(cal.get(Calendar.SECOND));// 得到秒
		String millsecond = String.valueOf(cal.get(Calendar.MILLISECOND));// 得到毫秒
		String random = String.valueOf(Math.round(Math.random() * 899999 + 100000));
		return year + month + day + hour + minute + second + millsecond + random;
	}

	public static String getGenerateWord() {
		// 将数字读入list
		List<String> list = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		// 打乱
		Collections.shuffle(list);
		// 重组成字符串
		return StringUtils.join(list, "").substring(0, 6);
	}

	public static String getRelativePath(String name) {
		return System.getProperties().getProperty("user.dir").concat(System.getProperties().getProperty("file.separator")).concat(name);
	}

	public static File getFileByPath(String path, boolean isAbsolute) {
		if (isAbsolute) {
			return new File(path);
		}
		return new File(System.getProperty("user.dir") + System.getProperty("file.separator") + path);
	}

	public static String nullToDefault(String value) {
		if (StringUtils.isBlank(value)) {
			return "";
		}
		return value;
	}

	public static long nullToDefault(Long value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static double nullToDefault(Double value, double defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static int nullToDefault(Integer value, int defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	public static boolean nullToDefault(Boolean value) {
		if (value == null) {
			return false;
		}
		return value;
	}

	public static String fileToMD5(File file) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// 通过通道读取文件
			FileInputStream in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			digest.update(byteBuffer);
			// 关闭流和通道
			ch.close();
			in.close();
			return byte2hex(digest.digest());
		} catch (NoSuchAlgorithmException | IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

	public static double toFixed(double value, int num) {
		if (value <= 0) {
			return 0.0;
		}
		if (num > 0) {
			BigDecimal b = new BigDecimal(value);
			return b.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return value;
	}

	public static String createLinkString(JSONObject params) {
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key).toString();
			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + "\"" + value + "\"";
			} else {
				prestr = prestr + key + "=" + "\"" + value + "\"" + "&";
			}
		}
		return prestr;
	}
}
