package cn.edu.cylg.cis.hicloud.dao.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.vo.query.UserQuery;

/**
 * 用户dao
 * @author  Shawshank
 * @version 1.0
 * 2016年2月6日 创建文件
 */
@Repository
public class UserDao extends BaseDAO{
	
	
	@SuppressWarnings("unchecked")
	public List<User> findAllUser() throws Exception{
		String hql="from User";
		Query query=this.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	public List<User> findUserByCondition(UserQuery query) throws Exception{
		String hql="select t from User t where 1=1 "
				+ "/~ and t.id ={id} ~/"
				+ "/~ and t.userName= {userName} ~/"
				+ "/~ and t.email = {email} ~/"
				+ "/~ and t.mobilePhone={mobilePhone} ~/"
				+ "/~ and t.space.id = {spaceId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("id", query.getId());
		filters.put("userName", query.getUserName());
		filters.put("email", query.getEmail());
		filters.put("mobilePhone", query.getMobilePhone());
		filters.put("spaceId", query.getSpaceId());
		return this.find(SqlBuilder.build(hql, filters));
	}
	
	
	/** 根据用户名查找 */
	@SuppressWarnings("unchecked")
	public User findUserByUserName(String userName) throws Exception{
		String hql="from User u where u.userName=:userName";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter("userName", userName);
		List<User> list=query.list();
		return list == null || list.isEmpty() ? null : (User)list.get(0);
	}
	
}
