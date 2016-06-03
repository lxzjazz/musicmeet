package cn.musicmeet.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

public class ImageUtil {

	private final static Logger logger = Logger.getLogger(ImageUtil.class);

	public static String cutImage(String sourcePath, String targetPath, int x, int y, int width, int height) throws IOException {
		File file = new File(sourcePath);
		if (!file.exists()) {
			throw new IOException("SourcePath Error: " + sourcePath);
		}
		if (null == targetPath || targetPath.isEmpty())
			targetPath = sourcePath;
		String formatName = getImageFormatName(file);
		if (null == formatName)
			return targetPath;
		formatName = formatName.toLowerCase();
		BufferedImage image = ImageIO.read(file);
		image = image.getSubimage(x, y, width, height);
		ImageIO.write(image, formatName, new File(targetPath));
		return targetPath;
	}

	public static String zoom(String sourcePath, String targetPath, int width, int height) throws IOException {
		File file = new File(sourcePath);
		if (!file.exists())
			throw new IOException("SourcePath Error: " + sourcePath);
		if (null == targetPath || targetPath.isEmpty())
			targetPath = sourcePath;
		String formatName = getImageFormatName(file);
		if (null == formatName)
			return targetPath;
		formatName = formatName.toLowerCase();
		BufferedImage image = ImageIO.read(file);
		BufferedImage zoomImage = zoom(image, width, height);
		ImageIO.write(zoomImage, formatName, new File(targetPath));
		return targetPath;
	}

	private static BufferedImage zoom(BufferedImage sourceImage, int width, int height) {
		BufferedImage zoomImage = null;
		zoomImage = new BufferedImage(width, height, sourceImage.getType());
		Image image = sourceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		Graphics gc = zoomImage.getGraphics();
		gc.setColor(Color.WHITE);
		gc.drawImage(image, 0, 0, null);
		return zoomImage;
	}

	private static String getImageFormatName(File file) throws IOException {
		String formatName = null;
		ImageInputStream iis = ImageIO.createImageInputStream(file);
		Iterator<ImageReader> imageReader = ImageIO.getImageReaders(iis);
		if (imageReader.hasNext()) {
			ImageReader reader = imageReader.next();
			formatName = reader.getFormatName();
		}
		iis.flush();
		iis.close();
		return formatName;
	}

	public static boolean postImage(String imagePath, String type, String id, String name, String file) {
		try {
			String realPath = imagePath + type + "/" + id + "/" + name + ".jpg";
			// 文件分隔符
			String separator = System.getProperties().getProperty("file.separator");
			// 根据不同的操作系统，设置文件分隔符
			realPath = realPath.replace("/", separator);

			// 获取文件目录
			File dir = new File(realPath.substring(0, realPath.lastIndexOf(separator)));

			// 如果目录不存在
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// 新建文件
			File savefile = new File(realPath);

			// 将文件读成字节数组并写入服务器
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savefile));
			out.write(CommonUtil.base64Decode(file));

			// 关闭流
			out.flush();
			out.close();

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
}
