package cn.edu.cylg.cis.hicloud.controller.admin.basic;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;

@Controller("AdminFileCtrl")
@RequestMapping("/admin/file")
public class FileController implements WebConstants{

	@Resource(name="fileService")
	private FileService fileService;
	
	/** 文件显示 */
	@RequestMapping("list")
	public String list(FileQuery query,ModelMap m) throws Exception{
		List<File> files=fileService.searchFiles(query);
		m.addAttribute("files",files);
		return "/admin/basic/basic-file-list";
	}
}
