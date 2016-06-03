package cn.edu.cylg.cis.hicloud.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.cylg.cis.hicloud.core.constants.CoreConstants;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;

public class AppInterceptor extends HandlerInterceptorAdapter{

	private static final Log log = LogFactory.getLog(AppInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(!(handler instanceof HandlerMethod)){
			return true;
		}
		log.info("进入app拦截器");
		String errInfo="";
		String requestUrl=request.getRequestURI();
		if(requestUrl.substring(requestUrl.lastIndexOf("/")+1,requestUrl.length()).equals("login")){
			
		}else{
			if(requestUrl.indexOf("api")!=-1){
				String token=null;
				Map<String,String[]> paramMap=request.getParameterMap();
				if(paramMap.containsKey("token")){
					token=paramMap.get("token")[0];
				}
				if(FuncUtil.notEmpty(token)){
					if(!CoreConstants.APP_KEY.equals(token)){
						errInfo="请求参数有误";
					}
				}else{
					errInfo="请求参数有误";
				}
				if(FuncUtil.notEmpty(errInfo)){
					response.getWriter().print("{\"status\":\"error\",\"message\":\""+errInfo+"\"}");
					return false;
				}
			}
		}
		return true;
	}
}
