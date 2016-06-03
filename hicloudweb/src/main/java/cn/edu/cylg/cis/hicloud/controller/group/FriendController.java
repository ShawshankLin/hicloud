package cn.edu.cylg.cis.hicloud.controller.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.service.group.FriendService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;
@Controller
@RequestMapping("/friend")
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
	public String index(ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
		FriendQuery query=new FriendQuery();
		query.setCreateId(userId);
		query.setIsConfirm(1);
		List<Friend> friends=friendService.findFriendByCondition(query);  
		m.addAttribute("friends",friends);
		return "/group/friend-info-index";
	}
	
	/** 添加好友*/
	@RequestMapping("sendFriendRQ")
	@ResponseBody
	public Object sendFriendRQ(String friendName,HttpServletRequest request) throws Exception{
		log.info("添加好友开始");
		if(FuncUtil.isEmpty(friendName))
			throw new AppException("请求参数有误");
		try {
			String contentPath=request.getContextPath();
			this.friendService.sendFriend(friendName,contentPath);
		} catch (AppException e) {
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "已发送好友请求，等待对方确认");
		log.info("添加好友结束");
		return result;
	}
	
	/**
	 * 
	  * 好友接受
	  * @param receiveId 添加记录id
	  * @param noticeId  通知记录id
	  * @param status    好友操作状态
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("receiveFriendRQ")
	public String receiveFriendRQ(String receiveId,String noticeId,Integer status,HttpServletRequest request) throws Exception{
		if(FuncUtil.isEmpty(receiveId)||FuncUtil.isEmpty(status))
			throw new AppException("请求参数有误");
		try {
			String contentPath=request.getContextPath();
			this.friendService.receiveFriend(receiveId, noticeId , status, contentPath);
		} catch (AppException e) {
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		return "redirect:/friend";
	}
	
	/**
	 *  删除好友
	  * @param friendId
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("removeFriend")
	@ResponseBody
	public Object removeFriend(String id,HttpServletRequest request) throws Exception{
		log.info("删除好友开始");
		if(FuncUtil.isEmpty(id)){
			throw new AppException("请求参数有误");
		}
		this.friendService.removeFriend(id);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除好友成功");
		result.put(MSG_TARGET, request.getContextPath()+"/friend");
		log.info("删除好友结束");
		return result;
	}
	
	
}
