package cn.edu.cylg.cis.hicloud.controller.admin.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.service.sys.SpaceService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

import com.google.gson.Gson;
@Controller("AdminSpaceCtrl")
@RequestMapping("/admin/space")
public class SpaceController implements WebConstants{

	protected static final Log log = LogFactory.getLog(RoleController.class);
	
	@Resource(name="spaceService")
	private SpaceService spaceService;
	
	@RequestMapping("list")
	public String list(ModelMap m) throws Exception{
		List<Space> spaces=this.spaceService.findAllSpace();
		m.addAttribute("result",spaces);
		return "/admin/sys/sys-space-list";
	}
	
	/** 存储空间添加*/
	@RequestMapping("save")
	public String save(String isPage,String id, String spaceName,Integer spaceSize,
			HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			return "/admin/sys/sys-space-add";
		}
		if(FuncUtil.isEmpty(spaceName)){
			throw new AppException("请求参数有误");	
		}
		try {
			if(FuncUtil.notEmpty(id)){
				Space space=this.spaceService.get(Space.class, id);
				space.setSpaceName(spaceName);
				space.setSpaceSize(spaceSize*1024*1024*1024l);
				spaceService.update(space);
			}else{
				Space space=new Space();
				space.setSpaceName(spaceName);
				space.setSpaceSize(spaceSize*1024*1024*1024l);
				spaceService.save(space);
			}
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/space/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
	
	@RequestMapping("edit")
	public String edit(String id,ModelMap m) throws Exception{
		if(FuncUtil.isEmpty(id)){
			throw new AppException("请求参数有误");
		}
		Space space=spaceService.get(Space.class, id);
		m.addAttribute("space", space);
		return "/admin/sys/sys-space-add";
	}
	
	/** 角色删除*/
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(String id)throws Exception{
		if(FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		try {
			Space space=spaceService.get(Space.class, id);
			if(space==null)
				throw new AppException("存储空间不存在");
			spaceService.delete(space);
		} catch (Exception e) {
			throw new AppException(e);
		}
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");		
		return result;
	}
	
	
	
}
