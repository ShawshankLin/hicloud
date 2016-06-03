package cn.edu.cylg.cis.hicloud.service.group.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.dao.group.FriendDao;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.drive.ShareService;
import cn.edu.cylg.cis.hicloud.service.group.FriendService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.GoEasyUtils;
import cn.edu.cylg.cis.hicloud.utils.GsonBuilderUtil;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

import com.google.gson.Gson;

@Service("friendService")
public class FriendServiceImpl extends BaseServiceImpl implements FriendService,WebConstants{

	@Autowired
	private FriendDao friendDao;
	@Resource
	private UserService userService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
	@Resource(name="shareService")
	private ShareService shareService;
	
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return friendDao;
	}

	@Override
	public List<Friend> findFriendByCondition(FriendQuery query) throws Exception {
		// TODO Auto-generated method stub
		return this.friendDao.findFriendByCondition(query);
	}

	

	@Override
	public void sendFriend(String friendName,String contentPath) throws Exception {
		User to = this.userService.findUserByUserName(friendName);
		if(to==null){
			throw new AppException("添加好友不存在");
		}
		String userId = LoginHelper.getUserId();
		String name = LoginHelper.getName();
		if(to.getId().equals(userId)){
			throw new AppException("添加好友不能为自己");
		}
		FriendQuery query =new FriendQuery();
		query.setCreateId(userId);
		query.setFriendId(to.getId());
		query.setIsConfirm(1);
		List<Friend> friends=this.findFriendByCondition(query);
		if(friends!=null&&friends.size()>0){
			throw new AppException(to.getUserName()+"已是你的好友");
		}
		Gson gson=GsonBuilderUtil.create();
		String content=name+"请求添加您为好友。";
		StringBuilder target=new StringBuilder();
		target.append("<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveFriendRQ?receiveId=");		
		String getuiTarget="";
		query.setCreateId(userId);
		query.setFriendId(to.getId());
		query.setIsConfirm(0);
		friends=this.findFriendByCondition(query);
		if(friends!=null&&friends.size()>0){//已入库，重新发送好友请求通知
			log.info("重新发送好友请求通知");
			Friend friend=friends.get(0);	
			target.append(friend.getId()+"&status=1'\">接受</button><button class=\"am-btn am-btn-danger am-btn-xs am-margin-left-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveFriendRQ?receiveId="+friend.getId()+"&status=-1'\">拒绝</button>").toString();
			getuiTarget="/api/friend/receiveFriendRQ?receiveId="+friend.getId();
		}else{//未入库，发送好友请求通知
			Friend friend=new Friend();
			friend.setFriend(to);
			friend.setIsConfirm(0);
			this.save(friend);
			Notice notice=new Notice();
			notice.setReceiver(to);
			notice.setContent(content);
			notice.setType(MessageType.FRIEND_SEND);
			notice.setStatus(0);
			this.noticeService.save(notice);
			target.append(friend.getId()+"&noticeId="+notice.getId()+"&status=1'\">接受</button><button class=\"am-btn am-btn-danger am-btn-xs am-margin-left-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveFriendRQ?receiveId="+friend.getId()+"&noticeId="+notice.getId()+"&status=-1'\">拒绝</button>");
			getuiTarget="/api/friend/receiveFriendRQ?receiveId="+friend.getId();
			notice.setTarget(target.toString());
			this.noticeService.update(notice);
		}
		Map<String,String> msgMap=new HashMap<String, String>();
		msgMap.put(MSG_INFO, content);
		msgMap.put(MSG_TYPE, MessageType.FRIEND_SEND);
		msgMap.put(MSG_TARGET, target.toString());
		log.info("发送通知给用户"+to.getId());
		log.info("通知报文"+ gson.toJson(msgMap));
		GoEasyUtils.publishMsg(to.getId(), gson.toJson(msgMap));
		log.info("发送GoEasy成功");
		msgMap.put(MSG_TARGET, getuiTarget.toString());
		log.info("通知报文"+ gson.toJson(msgMap));
		PushInteface.pushToSinglebytran(to.getId(), gson.toJson(msgMap));
		log.info("发送个推成功");
	}
	
	@Override
	public void  receiveFriend(String receiveId, String noticeId ,Integer status,String contentPath) throws Exception {
		String userId=LoginHelper.getUserId();
		String name = LoginHelper.getName();
		Friend friend=this.get(Friend.class, receiveId);
		if(friend.getCreateId().equals(userId))
			throw new AppException("不能添加自己为好友");
		String content=null;
		if(status==1){
			content=name+"接受了您的添加请求并添加您为好友";
		}else if(status==-1){
			content=name+"拒绝了您的好友请求";
		}else{
			throw new AppException("无效的请求状态");
		}
		friend.setIsConfirm(status);
		log.info("更新好友发送请求状态");
		this.update(friend);
		if(FuncUtil.notEmpty(noticeId)){
			Notice sendNotice=this.get(Notice.class, noticeId);
			sendNotice.setStatus(1);
			log.info("更新对方发送通知状态");
			this.update(sendNotice);
		}
		User user=this.userService.get(User.class, friend.getCreateId());
		Friend my=new Friend();
		my.setFriend(user);
		my.setIsConfirm(status);
		this.save(my);
		log.info("更新我的好友成功");
		Notice receiveNotice=new Notice();//添加消息记录
		receiveNotice.setReceiver(user);
		receiveNotice.setContent(content);
		receiveNotice.setType(MessageType.FRIEND_RECEIVE);
		receiveNotice.setStatus(0);
		this.noticeService.save(receiveNotice);
		String target="<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+contentPath+"/notice/readNotice?noticeId="+receiveNotice.getId()+"'\">标记为已读</button>";
		receiveNotice.setTarget(target);
		this.noticeService.update(receiveNotice);
		log.info("发送回执消息");
		Map<String,String> msgMap=new HashMap<String, String>();
		Gson gson=GsonBuilderUtil.create();
		msgMap.put(MSG_INFO,content );
		log.info("发送通知给用户"+friend.getCreateId());
		log.info("通知报文"+ gson.toJson(msgMap));
		PushInteface.pushToSinglebytran(friend.getCreateId(), gson.toJson(msgMap));
		log.info("发送个推成功");
		msgMap.put(MSG_TARGET, target);
		log.info("发送通知给用户"+friend.getCreateId());
		log.info("通知报文"+ gson.toJson(msgMap));
		GoEasyUtils.publishMsg(friend.getCreateId(),gson.toJson(msgMap));
		log.info("发送GoEasy成功");
	}

	@Override
	public void removeFriend(String id) throws Exception {
		Friend friend=this.friendDao.get(Friend.class, id);
		if(friend==null)
			throw new AppException("好友不存在");
		FriendQuery query=new FriendQuery();
		query.setFriendId(friend.getFriend().getId());
		query.setIsConfirm(1);
		List<Friend> friends=this.friendDao.findFriendByCondition(query);
		query.setFriendId(friend.getCreateId());
		query.setIsConfirm(1);
		List<Friend> friendss=this.findFriendByCondition(query);
		friends.addAll(friendss);
		if(friends!=null&&friends.size()>0){
			this.friendDao.delete(friends);
			log.info("删除好友成功");
		}else{
			throw new AppException("不存在好友关系");
		}
		ShareQuery squery=new ShareQuery();
		String userId=LoginHelper.getUserId();
		squery.setCreateId(userId);
		squery.setToId(friend.getFriend().getId());
		squery.setType(3);
		List<Share> shares=this.shareService.findShareByCondition(squery);
		squery.setCreateId(friend.getFriend().getId());
		squery.setToId(userId);
		squery.setType(3);
		List<Share> sharess=this.shareService.findShareByCondition(squery);
		shares.addAll(sharess);
		this.shareService.delete(shares);
		log.info("删除好友相关分享记录成功");
	}

}
