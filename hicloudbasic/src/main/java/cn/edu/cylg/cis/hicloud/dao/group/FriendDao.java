package cn.edu.cylg.cis.hicloud.dao.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;

@Repository
public class FriendDao extends BaseDAO{

	public List<Friend> findFriendByCondition(FriendQuery query) throws Exception{		
		String xsql="select t from Friend t where 1=1 "
				+ "/~ and t.id = {id} ~/"
				+ "/~ and t.createId ={createId} ~/"
				+ "/~ and t.friend.id = {friendId} ~/"
				+ "/~ and t.isConfirm = {isConfirm} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("id", query.getId());
		filters.put("createId", query.getCreateId());
		filters.put("friendId", query.getFriendId());
		filters.put("isConfirm", query.getIsConfirm());
		return this.find(SqlBuilder.build(xsql, filters));
	}
}
