package cn.edu.cylg.cis.hicloud.controller.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

import com.google.gson.Gson;

@Controller
@RequestMapping("/user")
public class UserController implements WebConstants{

	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping
	public String index(ModelMap m) throws Exception{
		String userId = LoginHelper.getUserId();
		User user = this.userService.get(User.class, userId);
		m.addAttribute("user",user);
		return "/user/userInfo";
	}
	
	@RequestMapping("update")
	public String update(String id,String name, String email,
			String mobilePhone,String birthDay,Integer sex,String remark,
			HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.isEmpty(id)||FuncUtil.isEmpty(name)){
			throw new AppException("请求参数有误");	
		}
		try {
			User user=this.userService.get(User.class, id);
			if(user==null)
				throw new AppException("用户不存在");
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
		Gson gson = new Gson();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "保存个人资料成功");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
}
