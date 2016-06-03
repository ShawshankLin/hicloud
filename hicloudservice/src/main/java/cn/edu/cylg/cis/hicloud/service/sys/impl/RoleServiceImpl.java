package cn.edu.cylg.cis.hicloud.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.dao.sys.RoleDao;
import cn.edu.cylg.cis.hicloud.entity.Role;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.entity.UserRole;
import cn.edu.cylg.cis.hicloud.service.sys.RoleService;
import cn.edu.cylg.cis.hicloud.vo.query.RoleQuery;
import cn.edu.cylg.cis.hicloud.vo.query.UserRoleQuery;
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl implements RoleService{

	@Resource
	private RoleDao roleDao;
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return roleDao;
	}

	@Override
	public List<Role> findRoleByCondition(RoleQuery query) throws Exception {
		// TODO Auto-generated method stub
		return roleDao.findRoleByCondition(query);
	}

	@Override
	public void saveUserRole(String userIds, String roleId) throws Exception {
		Role role=this.get(Role.class, roleId);
		if(role==null)
			throw new AppException("角色不存在");
		String[] userIdss=userIds.split(",");
		if(userIdss!=null&&userIdss.length>0){
			UserRoleQuery query=new UserRoleQuery();
			query.setRoleId(roleId);
			List<UserRole> delList=this.roleDao.findUserRoleByCondition(query);
			this.roleDao.delete(delList);
			log.info("删除角色相关用户");
			List<UserRole> userRoles=new ArrayList<UserRole>();
			for(int i=0;i<userIdss.length;i++){
				User user=this.get(User.class, userIdss[i]);
				if(user==null)
					throw new AppException("用户"+userIdss[i]+"不存在");
				UserRole userRole=new UserRole();
				userRole.setUser(user);
				userRole.setRole(role);
				userRoles.add(userRole);
			}
			this.save(userRoles);
		}
	}

	@Override
	public List<UserRole> findUserRoleByCondition(UserRoleQuery query) throws Exception {
		return roleDao.findUserRoleByCondition(query);
	}
	
}
