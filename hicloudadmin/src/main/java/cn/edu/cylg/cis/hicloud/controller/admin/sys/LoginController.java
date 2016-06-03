package cn.edu.cylg.cis.hicloud.controller.admin.sys;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.CoreConstants;
import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.helper.LoginType;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
@Controller("AdminLoginCtrl")
@RequestMapping("/admin")
public class LoginController implements WebConstants{
	
	protected static final Log log = LogFactory.getLog(LoginController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping
	public String tologin() throws Exception{
		return "/admin/login/login";
	}
	
	//登陆
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestParam(value="userName",required=true)String userName,
			@RequestParam(value="password",required=true)String password,HttpServletRequest request,ModelMap m) throws Exception{
		log.info("开始登陆");
		Map<String,String> result=new HashMap<String,String>();
		String errInfo="";
		String loginType = "";
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		if(FuncUtil.notEmpty(userName)&&FuncUtil.notEmpty(password)){
			String encryptPWD = new SimpleHash("SHA-1", userName,password).toString();	//密码加密
			User user = null;
			if(StringUtils.equals(CoreConstants.SUPERADMIN,userName)){
				user=new User();
				user.setUserName(userName);
				user.setPassword(CoreConstants.SUPERADMIN_PASSWORD);
				loginType = LoginType.SUPER_ADMIN;
			}/*else{
				user=userService.findUserByUserName(userName);
				loginType = LoginType.USER;
			}*/
			if(user!=null){
				if(StringUtils.equals(encryptPWD,user.getPassword())){
					session.setAttribute(CoreConstants.LOGIN_USER, user);
					session.setAttribute(CoreConstants.LOGIN_TYPE, loginType);
					UsernamePasswordToken token = new UsernamePasswordToken(userName, password); 
					//shiro加入身份验证
					Subject subject = SecurityUtils.getSubject(); 
				    try { 
				        subject.login(token); 
				    } catch (AuthenticationException e) { 
				    	errInfo = "身份验证失败！";
				    }
				}else{
					log.error("密码不正确");
					errInfo="请输入正确的帐号或密码";
				}
			}else{
				log.error("用户名不正确");
				errInfo="请输入正确的帐号或密码";
			}
		}else{
			errInfo="请输入帐号密码";
		}
		String status="";
		String message="";
		if(FuncUtil.isEmpty(errInfo)){
			status=MessageType.SUCCESS;
			message="正在为您跳转首页";
		}else{
			status=MessageType.ERROR;
			message=errInfo;
		}
		result.put(MSG_STATUS, status);
		result.put(MSG_INFO, message);
		result.put(MSG_TARGET, request.getContextPath()+"/admin/index");
		return result;
	}
	
	//跳转首页
	@RequestMapping(value="/index")
	public String forward() throws Exception{
		//return "/admin/portal/index";
		return "redirect:/admin/user/list";
	}
	
	@RequestMapping(value="/logout")
	public String logout(){
		//shiro管理的session
		Subject currentUser = SecurityUtils.getSubject();  
		Session session = currentUser.getSession();
		session.removeAttribute(CoreConstants.LOGIN_USER);
		session.removeAttribute(CoreConstants.LOGIN_TYPE);
		//shiro销毁登录
		Subject subject = SecurityUtils.getSubject(); 
		subject.logout();
		return "redirect:/login/login";
	}
	
	@RequestMapping("/403")  
    public String unauthorizedRole(){  
        return "/error/403";  
    }
	
}