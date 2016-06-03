package cn.edu.cylg.cis.hicloud.controller.api.sys;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.core.helper.LoginType;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

@Controller("ApiLoginCtrl")
@RequestMapping("/api")
public class LoginController implements WebConstants{

	protected static final Log log = LogFactory.getLog(LoginController.class);
	
	@Resource(name="userService")
	private UserService userService;
	
	//登陆
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public Object login(String userName,String password,
			HttpServletRequest request,HttpServletResponse response,ModelMap m) throws Exception{
		log.info("开始登陆");
		Map<String,Object> result=new HashMap<String,Object>();
		String errInfo="";
		String loginType = "";
		User user=null;
		if(FuncUtil.notEmpty(userName)&&FuncUtil.notEmpty(password)){
			String encryptPWD = new SimpleHash("SHA-1", userName,password).toString();	//密码加密
			user=userService.findUserByUserName(userName);
			if(user!=null){
				if(StringUtils.equals(encryptPWD,user.getPassword())){
					loginType = LoginType.USER;
					UsernamePasswordToken token = new UsernamePasswordToken(userName, password); 
				    try { 
				        LoginHelper.login(user,loginType,token); 
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
			message="登陆成功";
		}else{
			status=MessageType.ERROR;
			message=errInfo;
			user=null;
		}
		result.put(MSG_STATUS, status);
		result.put(MSG_INFO, message);
		result.put(MSG_DATA, user);
		return result;
	}
	
}

