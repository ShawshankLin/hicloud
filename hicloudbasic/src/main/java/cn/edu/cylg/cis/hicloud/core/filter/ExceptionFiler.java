package cn.edu.cylg.cis.hicloud.core.filter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;
/**
 * 异常拦截器
 * @author  Shawshank
 * @version 1.0
 * 2016年4月16日 创建文件
 */
public class ExceptionFiler implements Filter,WebConstants {  
	
	private static final Log log = LogFactory.getLog(ExceptionFiler.class);
    
	private String errorUrl;
	private String ajaxUrl;

	
    public void destroy() {  
          
    }  
  
    public void doFilter(ServletRequest request, ServletResponse response,  
            FilterChain chain) throws IOException, ServletException {           
        try{
 			chain.doFilter(request, response);
 		}catch(Exception e){
 			log.error("捕获到异常"+e.getMessage(),e);
 			e.printStackTrace();
 			String errorMsg = "未知错误";
 			if(e.getCause() instanceof AppException){
 				errorMsg = e.getCause().getMessage();
 			}
 			if(LoginHelper.isAjaxRequest((HttpServletRequest)request)){//ajax请求
 				Map<String,Object> result=new HashMap<String,Object>();
 				result.put(MSG_STATUS, MessageType.ERROR);
 				result.put(MSG_INFO, errorMsg);
 				request.setAttribute(AJAX_MSG, new Gson().toJson(result));
 				RequestDispatcher dispathcher = request.getRequestDispatcher(ajaxUrl);
 				dispathcher.forward(request, response);
 			}else if(LoginHelper.isPhoneRequest((HttpServletRequest)request)){//phone请求
 				Map<String,Object> result=new HashMap<String,Object>();
 				Gson gson=new Gson();
 				result.put(MSG_STATUS, MessageType.ERROR);
 				result.put(MSG_INFO, errorMsg);
 				String userId=request.getParameter("userId");
 				if(FuncUtil.notEmpty(userId)){
 					log.info("发送个推到用户"+userId);
 					log.info("异常通知报文"+gson.toJson(result));
 					PushInteface.pushToSinglebytran(userId, gson.toJson(result));
 					log.info("发送个推成功");
 				}else{
 					log.info("无法识别当前请求用户，发送异常失败");
 				}
 			}else{//pc请求
 				request.setAttribute(EXCEPTION_MSG, errorMsg);
 				RequestDispatcher dispathcher = request.getRequestDispatcher(errorUrl);
 				dispathcher.forward(request, response);
 			}
 		} 
    }
    
    public void init(FilterConfig config) throws ServletException {  
    	errorUrl = config.getInitParameter("errorUrl");
		ajaxUrl = config.getInitParameter("ajaxurl");
    }  
  
}  
