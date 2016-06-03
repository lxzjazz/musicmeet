package cn.musicmeet.controllers.avatar;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;
import net.sf.json.JSONObject;

import org.springframework.web.multipart.MultipartFile;

import cn.musicmeet.beans.User;
import cn.musicmeet.controllers.LoginRequired;
import cn.musicmeet.util.ImageCutterUtil;

@Path("")
public class AvatarController {
	// 支持上传的图片类型
	private final static String[] IMGS = { "jpg", "png", "jpeg" };

	@SuppressWarnings("unchecked")
	@LoginRequired
	@Post("upload")
	public JSONObject post_avatar_upload(JSONObject json, Invocation inv, MultipartFile file, @Param("x") int x, @Param("y") int y, @Param("width") int width, @Param("height") int height) {
		HttpServletRequest request = inv.getRequest();
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute(User.SGLOBAL);
		String avatar_id = (String) sGlobal.get(User.AVATAR_ID);
		return upload_avatar(avatar_id, json, inv, file, x, y, width, height);
	}

	@Post("reg/upload")
	public JSONObject post_avatar_reg_upload(JSONObject json, Invocation inv, MultipartFile file, @Param("x") int x, @Param("y") int y, @Param("width") int width, @Param("height") int height) {
		HttpSession session = inv.getRequest().getSession();
		String avatar_id = (String) session.getAttribute("avatar_id");
		if (avatar_id == null) {
			avatar_id = UUID.randomUUID().toString().replace("-", "");
			session.setAttribute("avatar_id", avatar_id);
		}
		return upload_avatar(avatar_id, json, inv, file, x, y, width, height);
	}

	@SuppressWarnings("unchecked")
	@LoginRequired
	@Post("reset")
	public JSONObject post_avatar_reset(JSONObject json, Invocation inv) {

		HttpServletRequest request = inv.getRequest();
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute(User.SGLOBAL);

		String avatar_id = (String) sGlobal.get(User.AVATAR_ID);

		try {
			File small_img = new File(getrealPath(inv, avatar_id, "small"));
			if (small_img.exists()) {
				small_img.delete();
			}
			File middle_img = new File(getrealPath(inv, avatar_id, "middle"));
			if (middle_img.exists()) {
				middle_img.delete();
			}
			File big_img = new File(getrealPath(inv, avatar_id, "big"));
			if (big_img.exists()) {
				big_img.delete();
			}
			json.element("success", "头像重置成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "服务器异常，头像重置失败");
		}
		return json;
	}

	@Post("reg/reset")
	public JSONObject post_avatar_reset(JSONObject json, Invocation inv, @Param("avatar_id") String avatar_id) {
		try {
			File small_img = new File(getrealPath(inv, avatar_id, "small"));
			if (small_img.exists()) {
				small_img.delete();
			}
			File middle_img = new File(getrealPath(inv, avatar_id, "middle"));
			if (middle_img.exists()) {
				middle_img.delete();
			}
			File big_img = new File(getrealPath(inv, avatar_id, "big"));
			if (big_img.exists()) {
				big_img.delete();
			}
			json.element("success", "头像重置成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "服务器异常，头像重置失败");
		}
		return json;
	}

	@Get
	public String get_avatar(Invocation inv, @Param("size") String size) throws IOException {
		HttpServletResponse response = inv.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("image/jpeg");

		// 设置默认大小
		if (size == null || size != null && !size.equals("small") && !size.equals("middle") && !size.equals("big")) {
			size = "middle";
		}

		// 默认图片
		String realPath = inv.getServletContext().getRealPath("data") + "/avatar/user_noavatar_" + size + ".jpeg";

		// 根据不同的操作系统，设置文件操作符
		realPath = realPath.replace("/", System.getProperties().getProperty("file.separator"));

		// 非常关键，response默认已经打开了getWriter(),会与getOutputStream()冲突，所以重置一下
		response.reset();

		// 将文件以流的方式读入缓存的字节数组，然后将缓存的字节数组写入到response的输出流中
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(realPath));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		byte[] data = new byte[1024];
		while ((fis.read(data)) != -1) {
			out.write(data);
			data = new byte[1024];
		}

		// 关闭打开的输入输出流
		fis.close();
		out.flush();
		out.close();

		return "";
	}

	@Get("{avatar_id}")
	public String get_avatar_nickname(Invocation inv, @Param("avatar_id") String avatar_id, @Param("size") String size) throws IOException {
		HttpServletResponse response = inv.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("image/jpeg");

		// 设置默认大小
		if (size == null || size != null && !size.equals("small") && !size.equals("middle") && !size.equals("big")) {
			size = "middle";
		}

		String realPath = getrealPath(inv, avatar_id, size);

		// 判断图片文件是否存在，不存在就将realPath改为默认
		File file = new File(realPath);
		if (!file.exists() || file.isDirectory()) {
			realPath = inv.getServletContext().getRealPath("data") + "/avatar/user_noavatar_" + size + ".jpeg";
		}

		// 根据不同的操作系统，设置文件操作符
		realPath = realPath.replace("/", System.getProperties().getProperty("file.separator"));

		// 非常关键，response默认已经打开了getWriter(),会与getOutputStream()冲突，所以重置一下
		response.reset();

		// 将文件以流的方式读入缓存的字节数组，然后将缓存的字节数组写入到response的输出流中
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(realPath));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		byte[] data = new byte[1024];
		while ((fis.read(data)) != -1) {
			out.write(data);
			data = new byte[1024];
		}

		// 关闭打开的输入输出流
		fis.close();
		out.flush();
		out.close();

		return "";
	}

	private JSONObject upload_avatar(String avatar_id, JSONObject json, Invocation inv, MultipartFile file, @Param("x") int x, @Param("y") int y, @Param("width") int width, @Param("height") int height) {

		if (file == null || file.isEmpty()) {
			json.element("error", "找不到文件");
			return json;
		}

		try {
			// 获取文件名
			String fileName = file.getOriginalFilename();

			// 获取文件拓展名
			String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

			// 判断格式是否支持，否则返回格式不支持
			if (!imgs_check(IMGS, fileExtension)) {
				json.element("error", "图片格式不支持");
				return json;
			}

			if (file.getSize() > 1048576) {
				json.element("error", "图片文件过大");
				return json;
			}

			String tempPath = getrealPath(inv, avatar_id, "temp");

			// 根据不同的操作系统，设置文件操作符
			tempPath = tempPath.replace("/", System.getProperties().getProperty("file.separator"));

			// 获取文件目录
			File dir = new File(tempPath.substring(0, tempPath.lastIndexOf(System.getProperties().getProperty("file.separator"))));

			// 如果目录不存在
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// 新建文件
			File savefile = new File(tempPath);

			// 将文件读成字节数组并写入服务器
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savefile));
			out.write(file.getBytes());

			// 关闭流
			out.flush();
			out.close();
			String tempPath_cut = getrealPath(inv, avatar_id, "cut");

			String realPath_big = getrealPath(inv, avatar_id, "big");

			String realPath_middle = getrealPath(inv, avatar_id, "middle");

			String realPath_small = getrealPath(inv, avatar_id, "small");

			// 判断图片源大小比例，是否超出300x300像素
			BufferedImage image = ImageIO.read(savefile);
			double destWidth = image.getWidth();
			double destHeight = image.getHeight();
			if (destWidth > 300 || destHeight > 300) {
				if (destWidth >= destHeight) {
					double b = destWidth / 300;
					x = Double.valueOf(x * b).intValue();
					y = Double.valueOf(y * b).intValue();
					width = Double.valueOf(width * b).intValue();
					height = Double.valueOf(height * b).intValue();
				} else {
					double b = destHeight / 300;
					x = Double.valueOf(x * b).intValue();
					y = Double.valueOf(y * b).intValue();
					width = Double.valueOf(width * b).intValue();
					height = Double.valueOf(height * b).intValue();
				}
			}
			ImageCutterUtil.cutImage(tempPath, tempPath_cut, x, y, width, height);

			// 图片大小压缩
			ImageCutterUtil.zoom(tempPath_cut, realPath_big, 200, 200);
			ImageCutterUtil.zoom(tempPath_cut, realPath_middle, 90, 90);
			ImageCutterUtil.zoom(tempPath_cut, realPath_small, 30, 30);

			// 删除缓存文件
			File tempFile = new File(tempPath);
			File cutFile = new File(tempPath_cut);
			if (tempFile.exists()) {
				tempFile.delete();
			}
			if (cutFile.exists()) {
				cutFile.delete();
			}
			json.element("success", "头像上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			json.element("error", "服务器异常,头像上传失败");
		}
		return json;
	}

	private String getrealPath(Invocation inv, String avatar_id, String size) {
		return inv.getServletContext().getRealPath("data") + "/avatar/" + avatar_id + "_" + size + ".jpeg";
	}

	private boolean imgs_check(String[] imgs, String fileExtension) {
		for (String str : imgs) {
			if (str.equalsIgnoreCase(fileExtension)) {
				return true;
			}
		}
		return false;
	}
}
