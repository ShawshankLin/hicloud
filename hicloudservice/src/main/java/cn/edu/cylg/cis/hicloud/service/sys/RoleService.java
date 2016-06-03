package cn.edu.cylg.cis.hicloud.service.sys;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.Role;
import cn.edu.cylg.cis.hicloud.entity.UserRole;
import cn.edu.cylg.cis.hicloud.vo.query.RoleQuery;
import cn.edu.cylg.cis.hicloud.vo.query.UserRoleQuery;

public interface RoleService extends BaseService{

	
	public List<Role> findRoleByCondition(RoleQuery query) throws Exception;
	
	public void saveUserRole(String userIds,String roleId) throws Exception;
	
	public List<UserRole> findUserRoleByCondition(UserRoleQuery query) throws Exception;
}
