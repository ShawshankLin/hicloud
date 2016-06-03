package cn.edu.cylg.cis.hicloud.service.group;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.GroupNotice;
import cn.edu.cylg.cis.hicloud.entity.GroupUser;
import cn.edu.cylg.cis.hicloud.vo.query.GroupFileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupUserQuery;

public interface GroupService extends BaseService{

	/**
	 *  根据条件查找分组
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<Group> findGroupByCondition(GroupQuery query) throws Exception;
	
	/**
	 *  获取群组号
	  * @return
	  * @throws Exception
	 */
	public String getGroupNo() throws Exception;
	
	/**
	 *  创建文件夹
	  * @param path
	  * @param ref
	  * @throws Exception
	 */
	public GroupFile createFile(String userId, String dirName, String path, Group group) throws Exception;
	
	/**
	  *  创建文件
	  * @param name
	  * @param path
	  * @param ref
	  * @param isDir
	  * @throws Exception
	 */
	public GroupFile createFile(String userId, String name, String path, Group group, File ref, Integer isDir) throws Exception;
	
	
	/**
	 *  显示文件
	  * @param parentPath
	  * @return
	  * @throws Exception
	 */
	public List<GroupFile> listFiles(String groupId , String parentPath) throws Exception;

	/**
	 *  分组用户查询
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<GroupUser> findGroupUserByCondition(GroupUserQuery query) throws Exception;
	
	/**
	  *  发送群组邀请
	  * @param groupId
	  * @param friendName
	  * @param contentPath
	  * @throws Exception
	 */
	public void sendGroup(String groupId,String friendName,String contentPath)throws Exception;
	
	/**
	  *  接受群组
	  * @param receiveId
	  * @param noticeId
	  * @param contentPath
	  * @throws Exception
	 */
	public void receiveGroup(String receiveId,String noticeId,Integer status,String contentPath) throws Exception;
	
	/**
	 * 
	  * 创建群组
	  * @param group
	  * @param friends
	  * @throws Exception
	 */
	public Group createGroup(Group group, String friends, String contentPath) throws Exception;

	/**
	 *  查找群组文件
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<GroupFile> findGroupFileByCondition(GroupFileQuery query) throws Exception;

	/**
	  * 查找群组公告
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<GroupNotice> findGroupNoticeByGroupId(String groupId) throws Exception;
	
	/**
	  * 删除群组文件
	  * @param userId
	  * @param fileId
	  * @throws Exception
	 */
	public void deleteFiles(String groupId,String userId, String fileId) throws Exception;
	
	/**
	  * 查找文件树
	  * @param groupId
	  * @param userId
	  * @param fileId
	  * @return
	  * @throws Exception
	 */
	public List<GroupFile> getFileTree(String groupId,String fileId) throws Exception;
	
	/**
	  * 分配群组存储空间
	  * @param groupIds
	  * @param spaceId
	  * @throws Exception
	 */
	public void saveGroupSpace(String groupIds,String spaceId) throws Exception;
	
	
	/**
	 *  查找全部群组
	  * @return
	  * @throws Exception
	 */
	public List<Group> findAllGroup() throws Exception;
}
