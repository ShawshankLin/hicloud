package cn.edu.cylg.cis.hicloud.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;

public class CustomSessionListener implements HttpSessionListener {

	private static final Log log = LogFactory.getLog(CustomSessionListener.class);
	//用户Session
	private static Map<String, HttpSession> sessionList = new HashMap<String, HttpSession>();
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();	
		log.info("创建会话"+session.getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		String userId=null;
		String userName=null;
		try {
			userId = LoginHelper.getUserId();
			userName=LoginHelper.getUserName();
		} catch (Exception e) {
			log.info("会话已超时");
			return;
		}
		log.info("销毁会话"+session.getId());
		//注销在线用户Session
		if(sessionList.containsKey(userId)) {
			sessionList.remove(userId);
		}
		log.info("用户【" + userName  + "】注销成功");
	}

	/**
	 * 添加用户
	 * @param key
	 * @param session
	 */
	public static void putSessionList(String userAccount, HttpSession session) {
		log.info("用户【" + userAccount  + "】登录成功");
		sessionList.put(userAccount, session);
	}
	
	/**
	 *  用户是否在线
	  * @param userId
	  * @return
	 */
	public static boolean isOnline(String userId){
		return sessionList.containsKey(userId);
	}

	
	public static Map<String, HttpSession> getSessionList(){
		return sessionList;
	}
}
