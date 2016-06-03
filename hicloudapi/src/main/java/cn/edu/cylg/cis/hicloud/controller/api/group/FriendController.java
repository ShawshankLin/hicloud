package cn.edu.cylg.cis.hicloud.controller.api.group;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.service.group.FriendService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;
/**
 * 
 * @author  Shawshank
 * @version 1.0
 * 2016年5月7日 创建文件
 */
@Controller("ApiFriendCtrl")
@RequestMapping("/api/friend")
public class FriendController implements WebConstants{

protected static final Log log = LogFactory.getLog(FriendController.class);

	
	@Resource(name="friendService")
	private FriendService friendService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
	
	
	/** 显示好友列表*/
	@RequestMapping
	@ResponseBody
	public Object index(String userId) throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		log.info("加载好友列表开始");
		Long startTime = System.currentTimeMillis();
		FriendQuery query=new FriendQuery();
		query.setCreateId(userId);
		query.setIsConfirm(1);
		List<Friend> friends=friendService.findFriendByCondition(query);  
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "好友列表加载成功");
		result.put(MSG_DATA, friends);
		Long endTime = System.currentTimeMillis();
		log.info("加载好友列表结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	/** 添加好友*/
	@RequestMapping("sendFriendRQ")
	@ResponseBody
	public Object sendFriendRQ(String userId,String friendName,HttpServletRequest request) throws Exception{
		log.info("添加好友开始");
		Long startTime = System.currentTimeMillis();
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		try {
			String contentPath=request.getContextPath();
			this.friendService.sendFriend(friendName, contentPath);
		} catch (AppException e) {
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "已发送好友请求，等待对方确认");
		Long endTime = System.currentTimeMillis();
		log.info("添加好友结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	@RequestMapping("receiveFriendRQ")
	@ResponseBody
	public Object receiveFriendRQ(String userId,String receiveId,String noticeId,Integer status,HttpServletRequest request) throws Exception{
		log.info("接受好友开始");
		Long startTime = System.currentTimeMillis();
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(receiveId)||FuncUtil.isEmpty(status)){
			throw new AppException("请求参数有误");
		}
		try {
			String contentPath=request.getContextPath();
			this.friendService.receiveFriend(receiveId, noticeId , status, contentPath);
		} catch (AppException e) {
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "已发送好友请求，等待对方确认");
		Long endTime = System.currentTimeMillis();
		log.info("添加好友结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	
	/**
	 *  删除好友
	  * @param friendId
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/removeFriend")
	@ResponseBody
	public Object removeFriend(String userId,String id,HttpServletRequest request) throws Exception{
		log.info("删除好友开始");
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(id)){
			throw new AppException("请求参数有误");
		}
		Friend friend=this.friendService.get(Friend.class, id);
		if(friend==null)
			throw new AppException("好友不存在");
		FriendQuery query=new FriendQuery();
		query.setFriendId(friend.getFriend().getId());
		query.setIsConfirm(1);
		List<Friend> friends=this.friendService.findFriendByCondition(query);
		query.setFriendId(friend.getCreateId());
		query.setIsConfirm(1);
		List<Friend> friendss=this.friendService.findFriendByCondition(query);
		friends.addAll(friendss);
		if(friends!=null&&friends.size()>0){
			this.friendService.delete(friends);
			log.info("删除好友成功");
		}else{
			throw new AppException("不存在好友关系");
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除好友成功");
		log.info("删除好友结束");
		return result;
	}
}
