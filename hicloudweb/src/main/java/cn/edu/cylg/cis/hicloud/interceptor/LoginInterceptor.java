package cn.edu.cylg.cis.hicloud.interceptor;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;

/**
 * 通用session拦截器
 * @author  Shawshank
 * @version 1.0
 * 2016年4月3日 创建文件
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{
	
	private static final Log log = LogFactory.getLog(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof HandlerMethod)){
			return true;
		}
		//log.info("进入session超时拦截器");
		String requestUrl=request.getRequestURI();
		String actionName = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		if(!(actionName.equals("login")||actionName.equals("admin"))){
			if(!LoginHelper.hasLogin()){
				log.info("session已超时");
				if(LoginHelper.isPhoneRequest(request)){
					Map<String, String[]> paramMap=request.getParameterMap();
					if(paramMap.containsKey("userId")){
						String userId = paramMap.get("userId")[0];
						log.info("发送个推通知到终端设备>>>>>>>>>>>>>"+userId);
						PushInteface.pushToSingle(userId, "999");
					}
				}else{
					log.info("跳转登陆页..........");
					if(LoginHelper.isAjaxRequest((HttpServletRequest)request)){
						request.setAttribute("jsonString", "登录超时，请登录");
						RequestDispatcher dispathcher = request.getRequestDispatcher("/WEB-INF/views/vnb/main/ajax.jsp");
						dispathcher.forward(request, response);
					}else{
						response.sendRedirect(request.getContextPath()+"/");
						return false;
					}
				}
			}
		}
		return true;
	}

}
