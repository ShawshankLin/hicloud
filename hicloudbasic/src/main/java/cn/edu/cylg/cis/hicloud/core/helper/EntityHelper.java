package cn.edu.cylg.cis.hicloud.core.helper;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
import cn.edu.cylg.cis.hicloud.core.constants.CoreConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.User;


public class EntityHelper {

	/**
	 * 获取当前登录人信息
	 * @return
	 */
	public static Object getLoginUser() {
		Object object =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getAttribute(CoreConstants.LOGIN_USER);
		if(object == null) {
			throw new AppException("请先登录");
		}
		return object;
	}
	
	/**
	 * 设置实体创建信息
	 * @param entity
	 */
	public static void setEntityInfo(BaseEntity entity) {
		if(entity.getId() == null || "".equals(entity.getId())) {
			setAddEntityInfo(entity);
		}else {
			setModifyEntityInfo(entity);
		}
	}
	
	/**
	 * 设置创建者信息
	 */
	private static void setAddEntityInfo(BaseEntity baseEntity) {
		baseEntity.setCreateDate(new Date());
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(request == null) {
			return;
		}
		HttpSession session = request.getSession();
		String loginType = (String)session.getAttribute(CoreConstants.LOGIN_TYPE);
		if(loginType != null) {
			//超级管理员
			if(LoginType.SUPER_ADMIN.equals(loginType)) {
				baseEntity.setCreateName(CoreConstants.SUPERADMIN);
			}else if(LoginType.USER.equals(loginType)){
				Object object = session.getAttribute(CoreConstants.LOGIN_USER);
				if(object != null) {
					if(object instanceof User){
						User user = (User)object;
						baseEntity.setCreateId(user.getId());
						baseEntity.setCreateName(user.getUserName());
					}
				}
			}
		}
	}
	
	/**
	 * 设置修改者信息
	 * @param baseEntity
	 */
	private static void setModifyEntityInfo(BaseEntity baseEntity) {
		baseEntity.setModifyDate(new Date());
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(request == null) {
			return;
		}
		HttpSession session = request.getSession();
		String loginType = (String)session.getAttribute(CoreConstants.LOGIN_TYPE);
		if(loginType != null) {
			//超级管理员
			if(LoginType.SUPER_ADMIN.equals(loginType)) {
				baseEntity.setModifyName(CoreConstants.SUPERADMIN);
			}else if(LoginType.USER.equals(loginType)){
				Object object = session.getAttribute(CoreConstants.LOGIN_USER);
				if(object != null) {
					if(object instanceof User){
						User user = (User)object;
						baseEntity.setModifyId(user.getId());
						baseEntity.setModifyName(user.getUserName());
					}
				}
			}
		}
	}
}
