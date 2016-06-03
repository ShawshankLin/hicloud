package cn.edu.cylg.cis.hicloud.core.base;

import java.io.Serializable;
import java.util.List;

/**
 * 业务逻辑基础类
 * @author Jeanzhou
 * @version
 *		1.0 2014年12月23日上午10:27:48
 */
public interface BaseService {

	/**
	 * 保存实体
	 * @param entity
	 * @return
	 */
	public <T extends BaseEntity> T save(T entity);
	
	/**
	 * 保存实体
	 * @param entities
	 * @return
	 */
	public <T extends BaseEntity> List<T> save(List<T> entities);
	
	/**
	 * 删除实体
	 * @param entity
	 */
	public <T extends Serializable> void delete(T entity);
	
	/**
	 * 删除实体
	 * @param entities
	 */
	public <T extends Serializable> void delete(List<T> entities);
	
	/**
	 * 更新实体
	 * @param entity
	 */
	public <T extends BaseEntity> void update(T entity);
	
	/**
	 * 更新实体
	 * @param entities
	 */
	public <T extends BaseEntity> void update(List<T> entities);
	
	/**
	 * 保存或更新实体
	 * @param entity
	 */
	public <T extends BaseEntity> void saveOrUpdate(T entity);
	
	/**
	 * 保存或更新实体
	 * @param entities
	 */
	public <T extends BaseEntity> void saveOrUpdate(List<T> entities);
	
	/**
	 * 查找
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T get(Class<T> clazz, Serializable id);
	
	/**
	 * 延迟加载
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T load(Class<T> clazz, Serializable id);
}
