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
import cn.edu.cylg.cis.hicloud.entity.Role;
import cn.edu.cylg.cis.hicloud.service.sys.RoleService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.RoleQuery;

import com.google.gson.Gson;
@Controller("AdminRoleCtrl")
@RequestMapping("/admin/role")
public class RoleController implements WebConstants{

	protected static final Log log = LogFactory.getLog(RoleController.class);
	
	@Resource(name="roleService")
	private RoleService roleService;
	
	
	@RequestMapping("list")
	public String list(RoleQuery query,ModelMap m) throws Exception{
		List<Role> roles=roleService.findRoleByCondition(query);
		m.addAttribute("result",roles);
		return "/admin/sys/sys-role-list";
	}
	
	/** 用户添加*/
	@RequestMapping("save")
	public String save(String isPage,String id, String roleName,String description,
			HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			return "/admin/sys/sys-role-add";
		}
		if(FuncUtil.isEmpty(roleName)){
			throw new AppException("请求参数有误");	
		}
		try {
			if(FuncUtil.notEmpty(id)){
				Role role=this.roleService.get(Role.class, id);
				role.setRoleName(roleName);
				role.setDescription(description);
				roleService.update(role);
			}else{
				Role role=new Role();
				role.setRoleName(roleName);
				role.setDescription(description);
				roleService.save(role);
			}
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加角色成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/role/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
	@RequestMapping("edit")
	public String edit(String id,ModelMap m) throws Exception{
		if(FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		Role role=roleService.get(Role.class, id);
		m.addAttribute("role", role);
		return "/admin/sys/sys-role-add";
	}
	
	/** 角色删除*/
	@RequestMapping("delete")
	@ResponseBody
	public Object delete(String id)throws Exception{
		if(FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		try {
			Role role=roleService.get(Role.class, id);
			roleService.delete(role);
		} catch (Exception e) {
			throw new AppException(e);
		}
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");		
		return result;
	}
	
	/** 分配用户角色*/
	@RequestMapping("distribute")
	public String distribute(String isPage,String userIds,String roleId,ModelMap m,HttpServletRequest request) throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			if(FuncUtil.isEmpty(roleId))
				throw new AppException("请求参数有误");
			Role role=this.roleService.get(Role.class, roleId);
			m.addAttribute("role",role);
			return "/admin/sys/sys-role-distribute";
		}
		if(FuncUtil.isEmpty(userIds)||FuncUtil.isEmpty(roleId))
			throw new AppException("请求参数有误");
		this.roleService.saveUserRole(userIds, roleId);
		Gson gson=new Gson();
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "分配成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/role/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
}
