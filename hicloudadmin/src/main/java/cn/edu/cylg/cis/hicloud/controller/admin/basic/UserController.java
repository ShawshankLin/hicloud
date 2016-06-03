package cn.edu.cylg.cis.hicloud.controller.admin.basic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.plugins.tree.TreeNode;
import cn.edu.cylg.cis.hicloud.entity.Role;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.entity.UserRole;
import cn.edu.cylg.cis.hicloud.service.sys.RoleService;
import cn.edu.cylg.cis.hicloud.service.sys.SpaceService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FileDownload;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.PathUtil;
import cn.edu.cylg.cis.hicloud.vo.query.UserQuery;
import cn.edu.cylg.cis.hicloud.vo.query.UserRoleQuery;

import com.google.gson.Gson;

@Controller("AdminUserCtrl")
@RequestMapping("/admin/user")
public class UserController implements WebConstants{
	
	protected static final Log log = LogFactory.getLog(UserController.class);

	@Resource(name="userService")
	private UserService userService;
	@Resource(name="roleService")
	private RoleService roleService;
	@Resource(name="spaceService")
	private SpaceService spaceService;
	
	/** 用户显示 */
	@RequestMapping("list")
	public String list(UserQuery query,ModelMap m) throws Exception{
		List<User> users=userService.findUserByCondition(query);
		m.addAttribute("result",users);
		return "/admin/basic/basic-user-list";
	}
	
	/** 用户添加*/
	@RequestMapping("save")
	public String save(String isPage,
			String id, String userName,String password,String name, String email,
			String mobilePhone,String birthDay,String sex,String remark,
			HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			return "/admin/basic/basic-user-add";
		}
		if(FuncUtil.isEmpty(userName)||FuncUtil.isEmpty(name)){
			throw new AppException("请求参数有误");	
		}
		try {
			if(FuncUtil.notEmpty(id)){
				User user=this.userService.get(User.class, id);
				if(user==null)
					throw new AppException("用户不存在");
				String encryptPWD = new SimpleHash("SHA-1",userName,password).toString();	//密码加密
				user.setPassword(encryptPWD);
				user.setUserName(userName);
				user.setName(name);
				user.setEmail(email);
				user.setMobilePhone(mobilePhone);
				if(FuncUtil.notEmpty(birthDay)){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date date=sdf.parse(birthDay);
					user.setBirthDay(date);
				}
				user.setRemark(remark);
				userService.update(user);
			}else{
				User checkUser=this.userService.findUserByUserName(userName);
				if(checkUser!=null){
					Gson gson = new Gson();
					Map<String, Object> result = new HashMap<String, Object>();
					result.put(MSG_STATUS, MessageType.ERROR);
					result.put(MSG_INFO, "用户名已存在");
					m.put(AJAX_MSG,gson.toJson(result));
					return AJAX_URL;
				}
				User user=new User();
				user.setUserName(userName);
				user.setName(name);
				user.setEmail(email);
				user.setMobilePhone(mobilePhone);
				if(FuncUtil.notEmpty(birthDay)){
					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
					Date date=sdf.parse(birthDay);
					user.setBirthDay(date);
				}
				user.setRemark(remark);
				String encryptPWD = new SimpleHash("SHA-1",userName,password).toString();	//密码加密
				user.setPassword(encryptPWD);
				user.setPhoto("/resources/images/user/"+new Random().nextInt(9)+".png");
				List<Space> spaces=this.spaceService.findAllSpace();//分配低配空间
				if(spaces!=null&&spaces.size()>0){
					user.setSpace(spaces.get(0));
					log.info("分配用户存储空间"+spaces.get(0).getSpaceName()+":"+spaces.get(0).getSpaceSize());
				}
				user.setStatus(1);
				userService.save(user);
			}
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		Gson gson = new Gson();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加用户成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/user/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
	/** 用户删除*/
	@RequestMapping("delete")
	public String delete(String id,ModelMap m)throws Exception{
		if(FuncUtil.notEmpty(id)){
			Gson gson=new Gson();
			Map<String,String> result=new HashMap<String,String>();
			String errInfo="";
			try {
				User user=userService.get(User.class, id);
				userService.delete(user);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				errInfo="删除失败";
			}
			String status="";
			String message="";
			if(FuncUtil.isEmpty(errInfo)){
				status=MessageType.SUCCESS;
				message="删除成功";
			}else{
				status=MessageType.ERROR;
				message=errInfo;
			}
			result.put(MSG_STATUS, status);
			result.put(MSG_INFO, message);
			m.put(AJAX_MSG,gson.toJson(result));
		}
		return AJAX_URL;
	}
	
	/** 修改用户状态*/
	@RequestMapping("changeStatus")
	public String changeStatus(String id,Integer status,ModelMap m)throws Exception{
		if(FuncUtil.isEmpty(id)||FuncUtil.isEmpty(status)){
			throw new AppException("请求参数有误");
		}
		User user=userService.get(User.class, id);
		user.setStatus(status);
		userService.update(user);
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");
		Gson gson=new Gson();
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
	
	/**用户编辑*/
	@RequestMapping("edit")
	public String edit(String id,ModelMap m) throws Exception{
		if(FuncUtil.notEmpty(id)){
			User user=userService.get(User.class, id);
			m.addAttribute("user", user);
			return "/admin/basic/basic-user-add";
		}
		return "redirect:/admin/basic/basic-user-list";
	}
	
	/** 用户导入*/
	@RequestMapping(value="import",method = RequestMethod.POST)
	public String importUser(@RequestParam("file")MultipartFile file,ModelMap m,HttpServletRequest request) throws Exception{
		//上传文件
		String fileName = file.getOriginalFilename();
		if(FuncUtil.isEmpty(fileName)){
			throw new AppException("请选择要上传的文件");
		}
		userService.importUser(fileName, file.getInputStream());
		log.info("导入用户报表结束");
		return "redirect:/admin/user/list";
	}
	
	@RequestMapping("download") //下载交易明细模板
	public void download(HttpServletResponse response) throws Exception{
		StringBuilder filePath=new StringBuilder().append(PathUtil.getClasspath()).append("/resources/file/user.xlsx");
		FileDownload.fileDownload(response, filePath.toString(), "用户报表模板.xlsx");
	}
	
	@RequestMapping("getUserInfosByRoleId")
	@ResponseBody
	public Object getUserInfosByRoleId(String roleId) throws Exception{
		if(FuncUtil.isEmpty(roleId))
			throw new AppException("请求参数有误");
		Role role=this.roleService.get(Role.class, roleId);
		if(role==null)
			throw new AppException("角色不存在");
		List<User> users=this.userService.findAllUser();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		nodes.add(new TreeNode("0","0","菜单目录",false,true,"0"));
		UserRoleQuery query=new UserRoleQuery();
		query.setRoleId(roleId);
		List<UserRole> urList= this.roleService.findUserRoleByCondition(query);
		for(int i=0;i<users.size();i++){
			User user=users.get(i);
			boolean flat=false;
			for(int j=0;j<urList.size();j++){
				if(urList.get(j).getUser().getId().equals(user.getId())){
					nodes.add(new TreeNode(user.getId(),"0",user.getUserName()+"-"+user.getName(),true,true,user.getId()));
					flat=true;
					break;
				}
			}
			if(flat)continue;
			nodes.add(new TreeNode(user.getId(),"0",user.getUserName()+"-"+user.getName(),false,true,user.getId()));
		}
		return nodes;
	}
	
	@RequestMapping("getUserInfosBySpaceId")
	@ResponseBody
	public Object getUserInfosBySpaceId(String spaceId) throws Exception{
		if(FuncUtil.isEmpty(spaceId))
			throw new AppException("请求参数有误");
		Space space=this.spaceService.get(Space.class, spaceId);
		if(space==null)
			throw new AppException("存储空间不存在");
		List<User> users=this.userService.findAllUser();
		List<TreeNode> nodes=new ArrayList<TreeNode>();
		nodes.add(new TreeNode("0","0","菜单目录",false,true,"0"));
		UserQuery query=new UserQuery();
		query.setSpaceId(spaceId);
		List<User> uList= this.userService.findUserByCondition(query);
		for(int i=0;i<users.size();i++){
			User user=users.get(i);
			boolean flat=false;
			for(int j=0;j<uList.size();j++){
				if(uList.get(j).getId().equals(user.getId())){
					nodes.add(new TreeNode(user.getId(),"0",user.getUserName()+"-"+user.getName(),true,true,user.getId()));
					flat=true;
					break;
				}
			}
			if(flat)continue;
			nodes.add(new TreeNode(user.getId(),"0",user.getUserName()+"-"+user.getName(),false,true,user.getId()));
		}
		return nodes;
	}
	
	
	/** 分配用户空间*/
	@RequestMapping("distributeSpace")
	public String distributeSpace(String isPage,String userIds,String spaceId,ModelMap m,HttpServletRequest request) throws Exception{
		if(FuncUtil.notEmpty(isPage)&&StringUtils.equals("true", isPage)){
			if(FuncUtil.isEmpty(spaceId))
				throw new AppException("请求参数有误");
			Space space=this.spaceService.get(Space.class, spaceId);
			m.addAttribute("space",space);
			return "/admin/sys/sys-userspace-distribute";
		}
		if(FuncUtil.isEmpty(userIds)||FuncUtil.isEmpty(spaceId))
			throw new AppException("请求参数有误");
		this.userService.saveUserSpace(userIds, spaceId);
		Gson gson=new Gson();
		Map<String,String> result=new HashMap<String,String>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");
		result.put(MSG_TARGET, request.getContextPath()+"/admin/user/list");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
}
