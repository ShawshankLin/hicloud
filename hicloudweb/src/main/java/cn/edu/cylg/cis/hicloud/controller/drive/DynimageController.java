package cn.edu.cylg.cis.hicloud.controller.drive;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.helper.HdfsHelper;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.PropUtil;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;

@Controller
@RequestMapping("dynimage")
public class DynimageController implements WebConstants{
	
	private static final String dynimageDir = PropUtil.readValue("cahcePath");
	
	private static final String notfoundimage = "/404.jpg";
	
	@RequestMapping("download")
	public String download(String location, HttpServletRequest request,
			HttpServletResponse resp, HttpSession session, ModelMap m) {
		String contentType = null;
		if (!FuncUtil.isEmpty(location)) {
			if (location.toLowerCase().endsWith("jpg") || location.toLowerCase().endsWith("jpeg")) {
				contentType = MediaType.IMAGE_JPEG_VALUE;
			} else if (location.toLowerCase().endsWith("png")) {
				contentType = MediaType.IMAGE_PNG_VALUE;
			} else if (location.toLowerCase().endsWith("gif")) {
				contentType = MediaType.IMAGE_GIF_VALUE;
			} else {
				m.put(AJAX_MSG, "不支持的资源类型");
				return AJAX_URL;
			}
			InputStream in = null;
			OutputStream out = null;
			try {
				String imagePath = dynimageDir +File.separator+ UuidUtil.get32UUID();
				File file = new File(imagePath);
				if(!HdfsHelper.exists(location)){
					imagePath = dynimageDir + notfoundimage;
					file = new File(imagePath);
				}else{
					HdfsHelper.download(location, imagePath);
				}
				in = new FileInputStream(file);
				out = resp.getOutputStream();
				resp.reset();
				resp.setContentType(contentType);
				byte[] buffer = new byte[1024];
				int bytesRead = -1;
				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.flush();
				//file.delete();
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					} 
				}
			}
		}
		m.put(AJAX_MSG, "参数传递错误");
		return AJAX_URL;
	}
}
