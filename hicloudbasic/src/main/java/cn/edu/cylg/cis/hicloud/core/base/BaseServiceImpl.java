package cn.edu.cylg.cis.hicloud.core.base;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 基本业务逻辑实现类
 * @author Jeanzhou
 * @version
 *		1.0 2014年12月23日上午10:50:34
 */
public abstract class BaseServiceImpl implements BaseService {

	protected static final Log log = LogFactory.getLog(BaseServiceImpl.class);
	
	public abstract BaseDAO getBaseDao();
	
	@Override
	public <T extends BaseEntity> T save(T entity) {
		return this.getBaseDao().save(entity);
	}
	
	@Override
	public <T extends BaseEntity> List<T> save(List<T> entities) {
		return this.getBaseDao().save(entities);
	}
	
	@Override
	public <T extends Serializable> void delete(T entity) {
		this.getBaseDao().delete(entity);
	}
	
	@Override
	public <T extends Serializable> void delete(List<T> entities) {
		this.getBaseDao().delete(entities);
	}
	
	@Override
	public <T extends BaseEntity> void update(T entity) {
		this.getBaseDao().update(entity);
	}
	
	@Override
	public <T extends BaseEntity> void update(List<T> entities) {
		this.getBaseDao().update(entities);
	}
	
	@Override
	public <T extends BaseEntity> void saveOrUpdate(T entity) {
		this.getBaseDao().saveOrUpdate(entity);
	}
	
	@Override
	public <T extends BaseEntity> void saveOrUpdate(List<T> entities) {
		this.getBaseDao().saveOrUpdate(entities);
	}

	@Override
	public <T> T get(Class<T> clazz, Serializable id) {
		return this.getBaseDao().get(clazz, id);
	}

	@Override
	public <T> T load(Class<T> clazz, Serializable id) {
		return this.getBaseDao().load(clazz, id);
	}
}
