package cn.edu.cylg.cis.hicloud.dao.sys;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.entity.Space;
/**
 * 存储空间dao
 * @author  Shawshank
 * @version 1.0
 * 2016年2月6日 创建文件
 */
@Repository
public class SpaceDao extends BaseDAO{

	@SuppressWarnings("unchecked")
	public List<Space> findAllSpace() throws Exception{
		String hql="from Space";
		Query query=this.getCurrentSession().createQuery(hql);
		return query.list();
	}
	
}
