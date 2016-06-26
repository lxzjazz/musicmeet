package cn.musicmeet.controllers.chat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.musicmeet.controllers.LoginRequired;
import net.paoding.rose.web.Invocation;
import net.paoding.rose.web.annotation.Param;
import net.paoding.rose.web.annotation.Path;
import net.paoding.rose.web.annotation.rest.Get;
import net.paoding.rose.web.annotation.rest.Post;

@Path("")
public class ChatController {
	@SuppressWarnings("unchecked")
	@Get
	public String get(Invocation inv, HttpServletRequest request) {
		Map<String, Object> sGlobal = (Map<String, Object>) request.getAttribute("sGlobal");
		if (sGlobal != null) {
			return "r:" + request.getContextPath() + "/#chat";
		}
		inv.addModel("title", "休闲聊天");
		return "chat";
	}

	@LoginRequired
	@Post
	public String post(Invocation inv) {
		inv.addModel("title", "休闲聊天");
		return "chat";
	}

	@Post("upload/audio")
	public void uploadAudio(Invocation inv, HttpServletRequest request, @Param("audioId") String audioId) throws IOException {
		// 计算路径
		String audioPath = getRealPath(inv, audioId);

		// 根据不同的操作系统，设置文件操作符
		audioPath = audioPath.replace("/", System.getProperties().getProperty("file.separator")).replace("\\", System.getProperties().getProperty("file.separator"));

		// 获取文件目录
		File dir = new File(audioPath.substring(0, audioPath.lastIndexOf(System.getProperties().getProperty("file.separator"))));

		// 如果目录不存在
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// 新建文件
		File audiofile = new File(audioPath);

		// 将文件读成字节数组并写入服务器
		BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(audiofile));
		byte[] data = new byte[1024];
		while ((bis.read(data)) != -1) {
			out.write(data);
			data = new byte[1024];
		}
		// 关闭流
		bis.close();
		out.flush();
		out.close();
	}

	@Get("download/audio")
	public String downloadAudio(Invocation inv, HttpServletRequest request, HttpServletResponse response, @Param("audioId") String audioId) throws IOException {
		// 计算路径
		String audioPath = getRealPath(inv, audioId);

		// 根据不同的操作系统，设置文件操作符
		audioPath = audioPath.replace("/", System.getProperties().getProperty("file.separator")).replace("\\", System.getProperties().getProperty("file.separator"));

		// 新建文件
		File audiofile = new File(audioPath);

		// 非常关键，response默认已经打开了getWriter(),会与getOutputStream()冲突，所以重置一下
		response.reset();
		// 设置响应字符集和类型
		response.setCharacterEncoding("UTF-8");
		response.setContentType("audio/mpeg");
		response.setContentLength((int) audiofile.length());
		response.setStatus(200);

		if (!audiofile.exists() || !audiofile.isFile()) {
			response.setStatus(404);
			return "";
		}

		// 将文件以流的方式读入缓存的字节数组，然后将缓存的字节数组写入到response的输出流中
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(audiofile));
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

	private String getRealPath(Invocation inv, String audioId) {
		return inv.getServletContext().getRealPath("data") + "/chat/" + audioId + ".mp3";
	}
}
