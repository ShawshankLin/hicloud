package cn.edu.cylg.cis.hicloud.dao.drive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.sqlbuilder.SqlBuilder;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;

@Repository
public class FileDao extends BaseDAO{
	
	/**
	 *  显示目录文件
	  * @param createId
	  * @param parentPath
	  * @return
	  * @throws Exception
	 */
	public List<File> listFiles(String createId,String parentPath) throws Exception{
		String hql = "from File f where f.createId=? and f.parentPath=? and f.status='active' order by f.isDir desc";
		return this.find(hql, new Object[]{createId,parentPath});
	}
	
	/**
	 * 搜索文件
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<File> findFilesByCondition(FileQuery query) throws Exception{		
		String xsql="select t from File t where 1=1 "
				+ "/~ and t.createId ={createId} ~/"
				+ "/~ and t.name like '%[name]%' ~/"
				+ "/~ and t.fileName like '%[fileName]%' ~/"
				+ "/~ and t.parentPath = {parentPath} ~/"
				+ "/~ and t.suffix in ([suffix]) ~/"
				+ "/~ and t.status = {status} ~/"
				+ "/~ and t.isDir = {isDir} ~/"
				+ "/~ and t.serialNum = {serialNum} ~/"
				+ "/~ and t.MD5 = {md5} ~/"
				+ "/~ and t.parentFile.id = {parentId} ~/";
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("createId", query.getCreateId());
		filters.put("name", query.getName());
		filters.put("fileName", query.getFileName());
		filters.put("parentPath", FuncUtil.isEmpty(query.getParentPath())?"":query.getParentPath().trim());
		if(query.getSuffix()!=null){
			filters.put("suffix",query.getSuffix().replaceAll("([^,]+)", "'$1'"));
		}else{
			filters.put("suffix",query.getSuffix());
		}
		filters.put("status", query.getStatus());
		filters.put("isDir", query.getIsDir());
		filters.put("serialNum", query.getSerialNum());
		filters.put("md5", query.getMd5());
		filters.put("parentId", query.getParentId());
		return this.find(SqlBuilder.build(xsql.toString(), filters));
	}
	
	
	/**
	 *  查找文件目录树
	  * @param rootId
	  * @return
	  * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<File> getFileTree(String createId, String rootId) throws Exception{
		String sql = "select * from drive_file f where f.CREATE_ID=? and FIND_IN_SET(f.ID, queryChildrenFile(?))";
		Query query = this.getCurrentSession().createSQLQuery(sql).addEntity("f",File.class);
		query.setParameter(0, createId);
		query.setParameter(1, rootId);
		return query.list();
	}
	
	/**
	 *  查找文件目录树
	  * @param rootId
	  * @return
	  * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<File> getDirTree(String createId,String parentId,Integer isDir) throws Exception{
		String hql="from File f where f.createId=? and f.isDir=?";
		if(FuncUtil.isEmpty(parentId)){
			hql+=" and f.parentFile.id is null";
		}else{
			hql+=" and f.parentFile.id=?";
		}
		Query query = this.getCurrentSession().createQuery(hql);
		query.setParameter(0, createId);
		query.setParameter(1, isDir);
		if(FuncUtil.notEmpty(parentId)){
			query.setParameter(2, parentId);
		}
		return query.list();
	}
	
	/**
	 *  查询文件最大版本数
	  * @param fileId
	  * @return
	  * @throws Exception
	 */
	public Integer getMaxFileVersion(String serialNum) throws Exception{
		String hql="select max(f.fileVersion) from File f where f.serialNum=?";
		return (Integer)this.getCurrentSession().createQuery(hql).setParameter(0, serialNum).uniqueResult();
	}
} 
