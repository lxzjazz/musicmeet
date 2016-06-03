package cn.musicmeet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.Charsets;
import org.apache.commons.lang3.StringUtils;

/**
 * Gzip压缩工具类
 * 
 * @author TKJ
 *
 */
public class GzipUtil {

	/**
	 * 压缩
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(String str) {
		if (StringUtils.isNotBlank(str)) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPOutputStream gzip = new GZIPOutputStream(out);
				gzip.write(str.getBytes(Charsets.UTF_8.name()));
				gzip.flush();
				gzip.close();
				out.flush();
				out.close();
				return out.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 解压
	 * 
	 * @param strByte
	 * @return
	 */
	public static String uncompress(byte[] strByte) {
		if (strByte != null) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(strByte));
				byte[] buffer = new byte[1];
				int n;
				while ((n = gunzip.read(buffer)) > -1) {
					out.write(buffer, 0, n);
				}
				return out.toString(Charsets.UTF_8.name());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static byte[] encodeAndCompress(String resultData) throws UnsupportedEncodingException {
		return compress(URLEncoder.encode(resultData, Charsets.UTF_8.name()).replace("+", "%20"));
	}

	public static String uncompressAndDecode(byte[] resultData) throws UnsupportedEncodingException {
		return URLDecoder.decode(uncompress(resultData), Charsets.UTF_8.name()).replace("%20", "+");
	}
}
