package cn.edu.cylg.cis.hicloud.service.sys;

import java.io.InputStream;
import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.vo.query.UserQuery;



/**
 * UserService 接口
 * @author  Shawshank
 * @version 1.0
 * 2016年1月20日 创建文件
 */
public interface UserService extends BaseService{

	/** 根据用户名查找用户 */
	public User findUserByUserName(String userName) throws Exception;
	
	/** 根据条件查找用户list*/
	public List<User> findUserByCondition(UserQuery query) throws Exception;
	
	/** 导入用户*/
	public void importUser(String fileName,InputStream is) throws Exception;
	
	/** 查找全部用户*/
	public List<User> findAllUser() throws Exception;
	
	/** 分配用户存储空间*/
	public void saveUserSpace(String userIds,String spaceId) throws Exception;
}
