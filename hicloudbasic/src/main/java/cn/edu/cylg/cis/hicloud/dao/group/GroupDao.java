package cn.edu.cylg.cis.hicloud.dao.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.GroupNotice;
import cn.edu.cylg.cis.hicloud.entity.GroupUser;
import cn.edu.cylg.cis.hicloud.vo.query.GroupFileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupUserQuery;

@Repository
public class GroupDao extends BaseDAO{

	public List<Group> findGroupByCondition(GroupQuery query) throws Exception{		
		String xsql="select t from Group t where 1=1 "
				+ "/~ and t.groupNo ={groupNo} ~/"
				+ "/~ and t.groupName  = {groupName} ~/"
				+ "/~ and t.createId ={createId} ~/"
				+ "/~ and t.space.id = {spaceId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("groupNo", query.getGroupNo());
		filters.put("groupName", query.getGroupName());
		filters.put("createId", query.getCreateId());
		filters.put("spaceId", query.getSpaceId());
		return this.find(SqlBuilder.build(xsql, filters));
	}
	
	public String getGroupNo() throws Exception{
		String sql="select max(p.GROUP_NO) from group_group p";
		String maxGroupNo=(String)this.getCurrentSession().createSQLQuery(sql).uniqueResult();
		return maxGroupNo;
	}
	
	public List<GroupFile> listFiles(String groupId, String parentPath) throws Exception{
		String hql = "from GroupFile t where t.group.id=? and t.parentPath=? order by t.isDir desc";
		return this.find(hql, new Object[]{groupId,parentPath});
	}
	
	
	public List<GroupUser> findGroupUserByCondition(GroupUserQuery query) throws Exception{		
		String xsql="select t from GroupUser t where 1=1 "
				+ "/~ and t.group.id = {groupId} ~/"
				+ "/~ and t.user.id = {userId} ~/"
				+ "/~ and t.isConfirm = {isConfirm} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("groupId", query.getGroupId());
		filters.put("userId", query.getUserId());
		filters.put("isConfirm", query.getIsConfirm());
		return this.find(SqlBuilder.build(xsql, filters));
	}
	
	public List<GroupFile> findGroupFileByCondition(GroupFileQuery query) throws Exception{		
		String xsql="select t from GroupFile t where 1=1 "
				+ "/~ and t.group.id = {groupId} ~/"
				+ "/~ and t.createId = {createId} ~/"
				+ "/~ and t.name like %[name]% ~/"
				+ "/~ and t.fileName = {fileName} ~/"
				+ "/~ and t.isDir = {isDir} ~/"
				+ "/~ and t.parentId = {parentId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("groupId", query.getGroupId());
		filters.put("createId", query.getCreateId());
		filters.put("name", query.getName());
		filters.put("fileName", query.getFileName());
		filters.put("isDir", query.getIsDir());
		filters.put("parentId", query.getParentId());
		return this.find(SqlBuilder.build(xsql, filters));
	}
	
	
	@SuppressWarnings("unchecked")
	public List<GroupNotice> findGroupNoticeByGroupId(String groupId) throws Exception{
		String hql="select t from GroupNotice t where 1=1 and t.group.id=? order by t.createDate desc";
		Query query=this.getCurrentSession().createQuery(hql);
		query.setParameter(0, groupId);
		query.setMaxResults(5);  
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<GroupFile> getFileTree(String groupId, String rootId) throws Exception{
		String sql = "select * from group_groupfile f where f.GROUP_ID=? and FIND_IN_SET(f.ID, queryChildrenGroupFile(?))";
		Query query = this.getCurrentSession().createSQLQuery(sql).addEntity("f",GroupFile.class);
		query.setParameter(0, groupId);
		query.setParameter(1, rootId);
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Group> findAllGroup() throws Exception{
		String hql="from Group";
		Query query=this.getCurrentSession().createQuery(hql);
		return query.list();
	}
}
