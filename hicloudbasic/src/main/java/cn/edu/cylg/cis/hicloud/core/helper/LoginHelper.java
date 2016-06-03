package cn.edu.cylg.cis.hicloud.core.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import cn.edu.cylg.cis.hicloud.core.constants.CoreConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.User;
/**
 * 登陆辅助类
 * @author  Shawshank
 * @version 1.0
 * 2016年4月16日 创建文件
 */
public class LoginHelper{
	
	public static Subject getSubject(){
		return SecurityUtils.getSubject(); 
	}
	
	public static Session getSession(){
		Subject currentUser = getSubject();
		return currentUser.getSession();
	}
	
	public static void login(User user,String loginType,UsernamePasswordToken token){
		Session session = getSession();
		session.setAttribute(CoreConstants.LOGIN_USER, user);
		session.setAttribute(CoreConstants.LOGIN_TYPE, loginType);
		getSubject().login(token);
	}
	
	public static boolean hasLogin(){
		Session session = getSession();
		return session.getAttribute(CoreConstants.LOGIN_USER)!=null;
	}
	
	public static void logout(){
		getSubject().logout();
	}
	
	public static User getCurrentUser() throws Exception{
		Session session=getSession();
		return (User)session.getAttribute(CoreConstants.LOGIN_USER);
	}

	public static String getUserId() throws Exception{
		Session session = getSession();
		String loginType=(String)session.getAttribute(CoreConstants.LOGIN_TYPE);
		if(loginType==null){
			throw new AppException("请先登录");
		}
		if(loginType.equals(LoginType.USER)){
			Object object=(Object)session.getAttribute(CoreConstants.LOGIN_USER);
			if(object!=null){
				if(object instanceof User){
					User user=(User)object;
					return user.getId();
				}
			}
		}
		return null;
	}

	public static String getUserName() throws Exception{
		Session session = getSession();
		String loginType=(String)session.getAttribute(CoreConstants.LOGIN_TYPE);
		if(loginType==null){
			throw new AppException("请先登录");
		}
		if(loginType.equals(LoginType.USER)){
			Object object=(Object)session.getAttribute(CoreConstants.LOGIN_USER);
			if(object!=null){
				if(object instanceof User){
					User user=(User)object;
					return user.getUserName();
				}
			}
		}
		return null;
	}
	
	public static String getName() throws Exception{
		Session session = getSession();
		String loginType=(String)session.getAttribute(CoreConstants.LOGIN_TYPE);
		if(loginType==null){
			throw new AppException("请先登录");
		}
		if(loginType.equals(LoginType.USER)){
			Object object=(Object)session.getAttribute(CoreConstants.LOGIN_USER);
			if(object!=null){
				if(object instanceof User){
					User user=(User)object;
					return user.getName();
				}
			}
		}
		return null;
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {   
        String header = request.getHeader("X-Requested-With");   
        if (header != null && "XMLHttpRequest".equals(header))   
            return true;   
        else  
            return false;   
    } 
	
	public static boolean isPhoneRequest(HttpServletRequest request) { 
		String userAgent = request.getHeader("user-agent");
		if(userAgent.matches(".*Android.*")) {
			return true;
		}else if(userAgent.matches(".*iPhone.*")) {
			return true;
		}else if(userAgent.matches(".*iPad.*")) {
			return true;
		}
		return false;
	}
	
	public static String getServername(HttpServletRequest request){
		return request.getRequestURL().toString().replace(request.getRequestURI(), "");
	}
}

