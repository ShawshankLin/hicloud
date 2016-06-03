package cn.edu.cylg.cis.hicloud.controller.login;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.CustomSessionListener;
import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.core.helper.LoginType;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

/**
 * 登陆 Controller
 * @author  Shawshank
 * @version 1.0
 * 2016年1月19日 创建文件
 */
@Controller
public class LoginController implements WebConstants{

	protected static final Log log = LogFactory.getLog(LoginController.class);
	
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
	
	//登陆
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public Object login(@RequestParam("userName")String userName,
			@RequestParam("password")String password,
			HttpServletRequest request,HttpSession session,ModelMap m) throws Exception{
		log.info("开始登陆");
		Map<String,String> result=new HashMap<String,String>();
		String loginType = "";
		if(FuncUtil.isEmpty(userName)){
			throw new AppException("用户名不允许为空");
		}
		if(FuncUtil.isEmpty(password)){
			throw new AppException("密码不允许为空");
		}
		String encryptPWD = new SimpleHash("SHA-1", userName,password).toString();	//密码加密
		User user=this.userService.findUserByUserName(userName);
		if(user!=null){
			if(StringUtils.equals(encryptPWD,user.getPassword())){
				loginType = LoginType.USER;
				UsernamePasswordToken token = new UsernamePasswordToken(userName, password); 
			    try { 
			    	LoginHelper.login(user,loginType,token);  
			    } catch (AuthenticationException e) { 
			    	throw new AppException("身份验证失败");
			    }
			}else{
				throw new AppException("请输入正确的帐号或密码");
			}
		}else{
			throw new AppException("请输入正确的帐号或密码");
		}
		CustomSessionListener.putSessionList(user.getId(), session);
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "正在为您跳转首页");
		result.put(MSG_TARGET, request.getContextPath()+"/index");
		return result;
	}
	
	//跳转首页
	@RequestMapping("index")
	public String forward(@CookieValue(value="STYLE")String style,ModelMap m,HttpServletResponse response) throws Exception{
		if(style==null){
			Cookie cookie=new Cookie("STYLE", DEFAULT_VIEW);
			cookie.setMaxAge(7*24*60*60);
			response.addCookie(cookie);
		}
		return "redirect:/myfile";
	}
	
	@RequestMapping("/logout")
	public String logout(){
		LoginHelper.logout();
		return "redirect:/login/login";
	}
	
	@RequestMapping("/403")  
    public String unauthorizedRole(){  
        return "/error/403";  
    }
	
}
