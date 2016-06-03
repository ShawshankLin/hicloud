package cn.edu.cylg.cis.hicloud.controller.sys;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.NoticeQuery;

@Controller
@RequestMapping("/notice")
public class NoticeController implements WebConstants{

	@Resource(name="noticeService")
	private NoticeService noticeService; 
	
	
	@RequestMapping
	public String index(ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
		NoticeQuery query=new NoticeQuery();
		query.setReceiverId(userId);
		query.setStatus(0);
		List<Notice> notices=this.noticeService.findNoticeByCondition(query);
		m.addAttribute("notices",notices);
		return "/sys/notice-box";
	}
	
	@RequestMapping("readNotice")
	public String readNotice(String noticeId) throws Exception{
		if(FuncUtil.isEmpty(noticeId))
			throw new AppException("请求参数有误");
		Notice notice=this.noticeService.get(Notice.class, noticeId);
		if(notice==null)
			throw new AppException("消息不存在");
		notice.setStatus(1);
		noticeService.update(notice);
		return "redirect:/notice";
	}
}
