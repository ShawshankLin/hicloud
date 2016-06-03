package cn.edu.cylg.cis.hicloud.dao.drive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;
@Repository
public class ShareDao extends BaseDAO{

	/**
	 *  根据条件查找分享
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<Share> findShareByCondition(ShareQuery query) throws Exception{		
		String xsql="select t from Share t where 1=1 "
				+ "/~ and t.createId= {createId} ~/"
				+ "/~ and t.file.id ={fileId} ~/"
				+ "/~ and t.type  = {type} ~/"
				+ "/~ and t.to.id = {toId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("createId", query.getCreateId());
		filters.put("fileId", query.getFileId());
		filters.put("type", query.getType());
		filters.put("toId", query.getToId());
		return this.find(SqlBuilder.build(xsql, filters));
	}
	
	
	public List<Map<String,String>> findShareForIndex(ShareQuery query) throws Exception{
		StringBuilder xsql=new StringBuilder();
		xsql.append("select new map(s.id as id,f.name as name,s.type as type,s.extractionCode as extractionCode,s.hot as hot,f.fileSize as fileSize,f.suffix as suffix,s.createDate as createDate ");
		if(FuncUtil.notEmpty(query.getToId())){
			xsql.append(", s.to.id as userId, s.to.name as userName ");
		}
		xsql.append(" ) ");
		xsql.append(" from Share as s join s.file as f where 1=1 ").append("/~ and s.createId = {createId} ~/");
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("createId", query.getCreateId());
		return this.find(SqlBuilder.build(xsql.toString(), filters));
	}
	
}
