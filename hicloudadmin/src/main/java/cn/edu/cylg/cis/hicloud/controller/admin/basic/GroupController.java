package cn.edu.cylg.cis.hicloud.controller.admin.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.plugins.tree.TreeNode;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.service.group.GroupService;
import cn.edu.cylg.cis.hicloud.service.sys.SpaceService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.GroupQuery;

import com.google.gson.Gson;

/**
 * 群组管理
 * @author  Shawshank
 * @version 1.0
 * 2016年5月10日 创建文件
 */
@Controller("AdminGroupCtrl")
@RequestMapping("/admin/group")
public class GroupController implements WebConstants{

	@Resource(name="groupService")
	private GroupService groupService;
	@Resource(name="spaceService")
	private SpaceService spaceService;
	
	/** 文件显示 */
	@RequestMapping("list")
	public String list(GroupQuery query,ModelMap m) throws Exception{
		List<Group> groups=groupService.findGroupByCondition(query);
		m.addAttribute("groups",groups);
		return "/admin/basic/basic-group-list";
	}
	
	
	@RequestMapping("getGroupInfosBySpaceId")
	@ResponseBody
	public Object getGroupInfosBySpaceId(String spaceId) throws Exception{
		if(FuncUtil.isEmpty(spaceId))
			throw new AppException("请求参数有误");
		Space space=this.spaceService.get(Space.class, spaceId);
		if(space==null)
			throw new AppException("存储空间不存在");
		List<Group> groups=this.groupService.findAllGroup();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		nodes.add(new TreeNode("0","0","菜单目录",false,true,"0"));
		GroupQuery query=new GroupQuery();
		query.setSpaceId(spaceId);
		List<Group> gList= this.groupService.findGroupByCondition(query);
		for(int i=0;i<groups.size();i++){
			Group group=groups.get(i);
			boolean flat=false;
			for(int j=0;j<gList.size();j++){
				if(gList.get(j).getId().equals(group.getId())){
					nodes.add(new TreeNode(group.getId(),"0",group.getGroupNo()+"-"+group.getGroupName(),true,true,group.getId()));
					flat=true;
					break;
				}
			}
			if(flat)continue;
			nodes.add(new TreeNode(group.getId(),"0",group.getGroupNo()+"-"+group.getGroupName(),false,true,group.getId()));
		}
		return nodes;
	}


	@RequestMapping("distributeSpace")
	public String distributeSpace(String isPage,String groupIds,String spaceId,ModelMap m,HttpServletRequest request) throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			Space space=this.spaceService.get(Space.class, spaceId);
			m.addAttribute("space",space);
			return "/admin/sys/sys-groupspace-distribute";
		}
		if(FuncUtil.isEmpty(groupIds)||FuncUtil.isEmpty(spaceId))
			throw new AppException("请求参数有误");
		this.groupService.saveGroupSpace(groupIds, spaceId);
		Gson gson=new Gson();
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "分配成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/group/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
}
