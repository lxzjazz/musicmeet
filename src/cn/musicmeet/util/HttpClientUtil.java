package cn.musicmeet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;

public class HttpClientUtil {

	private final static Logger logger = Logger.getLogger(HttpClientUtil.class);

	private final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36";

	public static String get(String url) {
		return get(url, Charsets.UTF_8.name(), false, 3, 0);
	}

	public static String get(String url, String charset) {
		return get(url, charset, false, 3, 0);
	}

	public static String get(String url, int retryTotal) {
		return get(url, Charsets.UTF_8.name(), false, retryTotal, 0);
	}

	public static String get(String url, boolean isGzip) {
		return get(url, Charsets.UTF_8.name(), isGzip, 3, 0);
	}

	public static String get(String url, String charset, boolean isGzip, int retryTotal) {
		return get(url, charset, isGzip, retryTotal, 0);
	}

	private static String get(String url, String charset, boolean isGzip, int retryTotal, int count) {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpGet request = null;
		HttpEntity entity = null;
		try {
			// 格式化指定编码
			if (StringUtils.isNotBlank(charset)) {
				try {
					charset = Charset.forName(charset.toLowerCase()).name();
				} catch (Exception e) {
					charset = Charsets.UTF_8.name();
				}
			} else {
				charset = Charsets.UTF_8.name();
			}
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
			int statusCode = response.getStatusLine().getStatusCode();
			// 正常
			if (statusCode == HttpStatus.SC_OK) {
				entity = response.getEntity();
				if (isGzip) {
					return GzipUtil.uncompressAndDecode(getBytesFromInputStream(entity.getContent()));
				}
				// 通过编码返回字符串
				return new String(getBytesFromInputStream(entity.getContent()), charset);
			}
			// 重定向
			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				String redirectUrl = getRelativeUrl(url, response.getFirstHeader("location").getValue());
				// 如果不是死循环跳转
				if (!redirectUrl.equalsIgnoreCase(url) && count < retryTotal) {
					return get(redirectUrl, charset, isGzip, retryTotal, ++count);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败重试3次
			if (count < retryTotal) {
				get(url, charset, isGzip, retryTotal, ++count);
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
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public static String post(String url, List<BasicNameValuePair> inputs) {
		return post(url, inputs, Charsets.UTF_8.name(), false, 3, 0);
	}

	public static String post(String url, List<BasicNameValuePair> inputs, String charset) {
		return post(url, inputs, charset, false, 3, 0);
	}

	public static String post(String url, List<BasicNameValuePair> inputs, boolean isGzip) {
		return post(url, inputs, Charsets.UTF_8.name(), isGzip, 3, 0);
	}

	public static String post(String url, List<BasicNameValuePair> inputs, int retryTotal) {
		return post(url, inputs, Charsets.UTF_8.name(), false, retryTotal, 0);
	}

	public static String post(String url, List<BasicNameValuePair> inputs, boolean isGzip, int retryTotal) {
		return post(url, inputs, Charsets.UTF_8.name(), isGzip, retryTotal, 0);
	}

	private static String post(String url, List<BasicNameValuePair> inputs, String charset, boolean isGzip, int retryTotal, int count) {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpPost request = null;
		HttpEntity entity = null;
		try {
			// 格式化指定编码
			if (StringUtils.isNotBlank(charset)) {
				try {
					charset = Charset.forName(charset.toLowerCase()).name();
				} catch (Exception e) {
					charset = Charsets.UTF_8.name();
				}
			} else {
				charset = Charsets.UTF_8.name();
			}
			// 创建http连接
			client = HttpClientBuilder.create().setUserAgent(USER_AGENT).disableRedirectHandling().build();
			// 设置请求延时
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build();
			// 新建POST请求
			request = new HttpPost(url);
			request.setConfig(config);
			// 设置参数
			request.setEntity(new UrlEncodedFormEntity(inputs, Charsets.UTF_8.name()));
			// 开始请求
			response = client.execute(request);
			// 根据状态码进行判断
			int statusCode = response.getStatusLine().getStatusCode();
			// 正常
			if (statusCode == HttpStatus.SC_OK) {
				entity = response.getEntity();
				if (isGzip) {
					return GzipUtil.uncompressAndDecode(getBytesFromInputStream(entity.getContent()));
				}
				// 通过编码返回字符串
				return new String(getBytesFromInputStream(entity.getContent()), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败重试3次
			if (count < retryTotal) {
				post(url, inputs, charset, isGzip, retryTotal, ++count);
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
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public static String postXML(String url, String xml) {
		return postXML(url, xml, Charsets.UTF_8.name(), false, 3, 0);
	}

	public static String postXML(String url, String xml, String charset) {
		return postXML(url, xml, charset, false, 3, 0);
	}

	public static String postXML(String url, String xml, boolean isGzip) {
		return postXML(url, xml, Charsets.UTF_8.name(), isGzip, 3, 0);
	}

	public static String postXML(String url, String xml, int retryCount) {
		return postXML(url, xml, Charsets.UTF_8.name(), false, retryCount, 0);
	}

	public static String postXML(String url, String xml, boolean isGzip, int retryCount) {
		return postXML(url, xml, Charsets.UTF_8.name(), isGzip, retryCount, 0);
	}

	private static String postXML(String url, String xml, String charset, boolean isGzip, int retryTotal, int count) {
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		HttpPost request = null;
		HttpEntity entity = null;
		try {
			// 格式化指定编码
			if (StringUtils.isNotBlank(charset)) {
				try {
					charset = Charset.forName(charset.toLowerCase()).name();
				} catch (Exception e) {
					charset = Charsets.UTF_8.name();
				}
			} else {
				charset = Charsets.UTF_8.name();
			}
			// 创建http连接
			client = HttpClientBuilder.create().setUserAgent(USER_AGENT).disableRedirectHandling().build();
			// 设置请求延时
			RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).setConnectionRequestTimeout(5000).setConnectTimeout(5000).setSocketTimeout(5000).build();
			// 新建POST请求
			request = new HttpPost(url);
			request.setConfig(config);
			// 设置参数
			StringEntity stringEntity = new StringEntity(xml, charset);
			// stringEntity.setContentType("text/xml");
			request.setEntity(stringEntity);
			// 开始请求
			response = client.execute(request);
			// 根据状态码进行判断
			int statusCode = response.getStatusLine().getStatusCode();
			// 正常
			if (statusCode == HttpStatus.SC_OK) {
				entity = response.getEntity();
				if (isGzip) {
					return GzipUtil.uncompressAndDecode(getBytesFromInputStream(entity.getContent()));
				}
				// 通过编码返回字符串
				return new String(getBytesFromInputStream(entity.getContent()), charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 失败重试3次
			if (count < retryTotal) {
				postXML(url, xml, charset, isGzip, retryTotal, ++count);
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
				logger.error(e.getMessage(), e);
			}
		}
		return "";
	}

	public static String encodeUrl(String url, String charset) throws UnsupportedEncodingException {
		// 格式化指定编码
		if (StringUtils.isNotBlank(charset)) {
			try {
				charset = Charset.forName(charset.toLowerCase()).name();
			} catch (Exception e) {
				charset = Charsets.UTF_8.name();
			}
		} else {
			charset = Charsets.UTF_8.name();
		}
		// 转义半角和全角空格
		url = url.replace(" ", "%20").replace(" ", "%20").replace("　", "%20");
		Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+");
		Matcher m = p.matcher(url);
		StringBuffer b = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
		}
		m.appendTail(b);
		return b.toString();
	}

	public static JSONObject parseXMLToJSON(String xml) {
		return parseXMLToJSON(xml, "UTF-8");
	}

	public static JSONObject parseXMLToJSON(String xml, String charset) {
		JSONObject result = null;
		if (StringUtils.isBlank(xml) || StringUtils.isBlank(charset)) {
			return result;
		}
		try {
			result = new JSONObject();
			SAXReader reader = new SAXReader();
			InputStream inputStream = new ByteArrayInputStream(xml.getBytes(charset));
			Document doc = reader.read(inputStream);
			Iterator<?> it = doc.getRootElement().elementIterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				List<?> children = e.elements();
				if (children.isEmpty()) {
					result.put(e.getName(), e.getStringValue());
				} else {
					// 不考虑多层嵌套的情况
					continue;
				}
			}
			inputStream.close();
		} catch (IOException | DocumentException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	private static String getBaseUrl(String url) {
		return "http://".concat(url.replace("http://", "").split("/")[0]).concat("/");
	}

	private static String getRelativeUrl(String baseUrl, String url) {
		if (StringUtils.isNotBlank(url)) {
			if (url.startsWith("/")) {
				return getBaseUrl(baseUrl).concat(url.substring(1));
			} else if (url.startsWith("http://") || url.startsWith("https://")) {
				return url;
			}
			return baseUrl.substring(0, baseUrl.lastIndexOf("/")).concat("/").concat(url);
		}
		return baseUrl;
	}

	private static byte[] getBytesFromInputStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1];
		int len;
		while ((len = in.read(buffer)) > -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close();
		in.close();
		return out.toByteArray();
	}
}
