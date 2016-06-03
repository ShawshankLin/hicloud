package cn.edu.cylg.cis.hicloud.dao.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.vo.query.NoticeQuery;

@Repository("noticeDao")
public class NoticeDao extends BaseDAO{

	public List<Notice> findNoticeByCondition(NoticeQuery query) throws Exception{
		String hql="select t from Notice t where 1=1 "
				+ "/~ and t.createId ={createId} ~/"
				+ "/~ and t.receiver.id = {receiver} ~/"
				+ "/~ and t.status = {status} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("createId", query.getCreateId());
		filters.put("receiver", query.getReceiverId());
		filters.put("status", query.getStatus());
		return this.find(SqlBuilder.build(hql, filters));
	}
	
	
}
