package cn.musicmeet.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageCutterUtil {

	public static String cutImage(String sourcePath, String targetPath, int x, int y, int width, int height) throws IOException {
		File file = new File(sourcePath);
		if (!file.exists()) {
			throw new IOException("图片路径不正确:" + sourcePath);
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
			throw new IOException("图片路径不正确:" + sourcePath);
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
}
