package cn.musicmeet.util;

import java.io.File;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class AvatarSessionListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		String avatar_id = (String) session.getAttribute("avatar_id");
		if (avatar_id != null) {
			File small_img = new File(getrealPath(session, avatar_id, "small"));
			if (small_img.exists()) {
				small_img.delete();
			}
			File middle_img = new File(getrealPath(session, avatar_id, "middle"));
			if (middle_img.exists()) {
				middle_img.delete();
			}
			File big_img = new File(getrealPath(session, avatar_id, "big"));
			if (big_img.exists()) {
				big_img.delete();
			}
		}
	}

	private String getrealPath(HttpSession session, String avatar_id, String size) {
		return (session.getServletContext().getRealPath("data") + "/avatar/" + avatar_id + "_" + size + ".jpeg").replace("/", System.getProperties().getProperty("file.separator"));
	}
}
