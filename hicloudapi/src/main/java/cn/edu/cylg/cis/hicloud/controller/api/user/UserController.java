package cn.edu.cylg.cis.hicloud.controller.api.user;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

@Controller("ApiUserCtrl")
@RequestMapping("/api/user")
public class UserController implements WebConstants{
	
	private static final Log log = LogFactory.getLog(UserController.class);

	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("getUserInfo")
	@ResponseBody
	public Object getUserInfo(String userId,ModelMap m) throws Exception{
		log.info("获取用户信息开始");
		Long startTime = System.currentTimeMillis();
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		User user = this.userService.get(User.class, userId);
		if(user==null)
			throw new AppException("用户不存在");
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "获取用户信息成功");
		result.put(MSG_DATA, user);
		Long endTime = System.currentTimeMillis();
		log.info("获取用户信息结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	@RequestMapping("updateUserInfo")
	@ResponseBody
	public Object updateUserInfo(String userId,String name, String email,
			String mobilePhone,String birthDay,Integer sex,String remark,
			HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(name)){
			throw new AppException("请求参数有误");	
		}
		User user=this.userService.get(User.class, userId);
		if(user==null)
			throw new AppException("用户不存在");
		try {
			user.setName(name);
			user.setEmail(email);
			user.setMobilePhone(mobilePhone);
			if(FuncUtil.notEmpty(birthDay)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date=sdf.parse(birthDay);
				user.setBirthDay(date);
			}
			user.setSex(sex);
			user.setRemark(remark);
			userService.update(user);
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "保存个人资料成功");
		result.put(MSG_DATA, user);
		return result;
	}
}
