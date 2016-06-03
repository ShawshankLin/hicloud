package cn.edu.cylg.cis.hicloud.dao.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.Role;
import cn.edu.cylg.cis.hicloud.entity.UserRole;
import cn.edu.cylg.cis.hicloud.vo.query.RoleQuery;
import cn.edu.cylg.cis.hicloud.vo.query.UserRoleQuery;
@Repository
public class RoleDao extends BaseDAO{

	public List<Role> findRoleByCondition(RoleQuery query) throws Exception{
		String hql="select t from Role t where 1=1 "
				+ "/~ and t.id = {id} ~/"
				+ "/~ and t.roleName like '%[roleName]%' ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("id", query.getId());
		filters.put("roleName", query.getRoleName());
		return this.find(SqlBuilder.build(hql, filters));
	}
	
	public List<UserRole> findUserRoleByCondition(UserRoleQuery query) throws Exception{
		String hql="select t from UserRole t where 1=1 "
				+ "/~ and t.id = {id} ~/"
				+ "/~ and t.role.id = {roleId} ~/"
				+ "/~ and t.user.id = {userId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("id", query.getId());
		filters.put("roleId", query.getRoleId());
		filters.put("userId", query.getUserId());
		return this.find(SqlBuilder.build(hql, filters));
	}
	
}
