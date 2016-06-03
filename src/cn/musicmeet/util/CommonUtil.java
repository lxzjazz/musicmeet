package cn.musicmeet.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class CommonUtil {
	// 浏览器Agent
	private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36";

	public static String sha512(String data) throws NoSuchAlgorithmException {
		return byte2hex(MessageDigest.getInstance("SHA-512").digest(data.getBytes()));
	}

	public static String sha256(String data) throws NoSuchAlgorithmException {
		return byte2hex(MessageDigest.getInstance("SHA-256").digest(data.getBytes()));
	}

	public static String sha1(String data) throws NoSuchAlgorithmException {
		return byte2hex(MessageDigest.getInstance("SHA-1").digest(data.getBytes()));
	}

	public static String md5(String data) throws NoSuchAlgorithmException {
		return byte2hex(MessageDigest.getInstance("MD5").digest(data.getBytes()));
	}

	// 获得指定长度的随机字符串
	public static String getRandSalt(int length) throws NoSuchAlgorithmException, NoSuchProviderException {
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "SUN");
		byte[] bytes = secureRandom.generateSeed(length / 2);
		secureRandom.nextBytes(bytes);
		return byte2hex(bytes);
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

	public static String encodeUrl(String url) throws UnsupportedEncodingException {
		// 转义半角和全角空格
		url = url.replace(" ", "%20").replace(" ", "%20").replace("　", "%20");
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p.matcher(url);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), "UTF-8"));
		}
		m.appendTail(b);
		return b.toString();
	}

	// 判断一个url是否可以访问
	public static boolean isCorrectUrl(String linkUrl) {
		if (StringUtils.isNotBlank(get(linkUrl))) {
			return true;
		}
		return false;
	}

	public static String get(String url) {
		return get(url, 0);
	}

	public static String get(String url, int count) {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpGet request = null;
		HttpEntity entity = null;
		try {
			// 创建http连接
			client = HttpClientBuilder.create().setUserAgent(USER_AGENT).disableRedirectHandling().build();
			// 设置请求延时
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build();
			// 新建GET请求
			request = new HttpGet(url);
			request.setConfig(config);
			// 开始请求
			response = client.execute(request);
			// 根据状态码进行判断
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败重试3次
			if (count < 3) {
				get(url, ++count);
			}
		} finally {
			try {
				if (request != null) {
					request.abort();
				}
				if (response != null) {
					response.close();
				}
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String post(String url, List<BasicNameValuePair> inputs) {
		return post(url, inputs, 0);
	}

	public static String post(String url, List<BasicNameValuePair> inputs, int count) {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpPost request = null;
		HttpEntity entity = null;
		try {
			// 创建http连接
			client = HttpClientBuilder.create().setUserAgent(USER_AGENT).disableRedirectHandling().build();
			// 设置请求延时
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build();
			// 新建POST请求
			request = new HttpPost(url);
			request.setConfig(config);
			// 设置参数
			request.setEntity(new UrlEncodedFormEntity(inputs, "UTF-8"));
			// 开始请求
			response = client.execute(request);
			// 根据状态码进行判断
			if (response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败重试3次
			if (count < 3) {
				post(url, inputs, ++count);
			}
		} finally {
			try {
				if (request != null) {
					request.abort();
				}
				if (response != null) {
					response.close();
				}
				if (entity != null) {
					EntityUtils.consume(entity);
				}
				if (client != null) {
					client.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
