package cn.edu.cylg.cis.hicloud.service.group;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;

public interface FriendService extends BaseService{

	/**
	  * 根据条件查找好友
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<Friend> findFriendByCondition(FriendQuery query) throws Exception;

	/**
	  * 发送好友消息
	  * @param friendName
	  * @param contentPath
	  * @throws Exception
	 */
	public void sendFriend(String friendName,String contentPath) throws Exception;
	
	/**
	 *  接受好友消息
	  * @param id
	  * @param contentPath
	  * @throws Exception
	 */
	public void  receiveFriend(String receiveId, String noticeId ,Integer status,String contentPath) throws Exception;

	/**
	  * 删除好友
	  * @param id
	  * @throws Exception
	 */
	public void removeFriend(String id) throws Exception;

}
