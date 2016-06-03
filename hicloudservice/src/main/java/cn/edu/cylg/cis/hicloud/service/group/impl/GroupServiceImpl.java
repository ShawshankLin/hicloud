package cn.edu.cylg.cis.hicloud.service.group.impl;

import java.util.ArrayList;
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
import cn.edu.cylg.cis.hicloud.dao.group.GroupDao;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.GroupNotice;
import cn.edu.cylg.cis.hicloud.entity.GroupUser;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.group.GroupService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.GoEasyUtils;
import cn.edu.cylg.cis.hicloud.utils.GsonBuilderUtil;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.GroupFileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupUserQuery;

import com.google.gson.Gson;

@Service("groupService")
public class GroupServiceImpl extends BaseServiceImpl implements GroupService,WebConstants{

	@Autowired
	private GroupDao groupDao;
	@Resource
	private UserService userService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return groupDao;
	}

	@Override
	public List<Group> findGroupByCondition(GroupQuery query) throws Exception {
		// TODO Auto-generated method stub
		return this.groupDao.findGroupByCondition(query);
	}

	@Override
	public String getGroupNo() throws Exception {
		String groupNo=this.groupDao.getGroupNo();
		if(FuncUtil.isEmpty(groupNo)){
			return "1";
		}else{
			return String.valueOf((Integer.parseInt(groupNo)+1));
		}
	}

	@Override
	public GroupFile createFile(String userId,String dirName, String path, Group group) throws Exception {
		return this.createFile(userId, dirName, path, group , null, 1);
	}

	@Override
	public GroupFile createFile(String userId,String name, String path, Group group, File ref, Integer isDir)
			throws Exception {
		if(group.getSpace()==null)
			throw new AppException("未分配群组存储空间");
		if(ref!=null){
			if((group.getUsedSpace()+ref.getFileSize())>group.getSpace().getSpaceSize())
				throw new AppException("群组存储空间不足");
			group.setUsedSpace(group.getUsedSpace()+ref.getFileSize());
			this.groupDao.update(group);
			log.info("更新群组存储空间");
		}
		GroupFile groupFile=new GroupFile();
		if(FuncUtil.notEmpty(path)){
			int index=path.lastIndexOf("/");
			String parentPath=path.substring(index+1);
			String grandfatherPath=path.substring(0,index);
			GroupFileQuery query=new GroupFileQuery();
            query.setCreateId(userId);
            query.setFileName(parentPath);
            query.setParentPath(grandfatherPath);
            query.setIsDir(1);
            List<GroupFile> parentFiles=this.groupDao.findGroupFileByCondition(query);
            if(parentFiles!=null&&parentFiles.size()>0){
            	groupFile.setParentFile(parentFiles.get(0));
            }else{
            	groupFile.setParentFile(null);
            }
		}else{
			groupFile.setParentFile(null);
		}
		String fileName=UuidUtil.get32UUID();
		groupFile.setFileName(fileName);
		groupFile.setName(name);
		groupFile.setIsDir(isDir);
		groupFile.setRef(ref);
		groupFile.setGroup(group);
		groupFile.setParentPath(path);
		this.save(groupFile);
		return groupFile;
	}

	@Override
	public List<GroupFile> listFiles(String groupId ,String parentPath)
			throws Exception {
		// TODO Auto-generated method stub
		return this.groupDao.listFiles(groupId, parentPath);
	}

	@Override
	public List<GroupUser> findGroupUserByCondition(GroupUserQuery query)
			throws Exception {
		// TODO Auto-generated method stub
		return this.groupDao.findGroupUserByCondition(query);
	}



	@Override
	public void sendGroup(String groupId,String friendName,String contentPath) throws Exception {
		Group group=this.get(Group.class, groupId);
		if(group==null)
			throw new AppException("群组不存在");
		User to = this.userService.findUserByUserName(friendName);
		if(to==null)
			throw new AppException("好友不存在");
		String userId=LoginHelper.getUserId();
		String name=LoginHelper.getName();
		if(userId.equals(to.getId())){
			throw new AppException("邀请好友不能为自己");
		}
		GroupUserQuery query=new GroupUserQuery();
		query.setGroupId(groupId);
		query.setUserId(to.getId());
		query.setIsConfirm(1);
		List<GroupUser> groupUsers=this.findGroupUserByCondition(query);
		if(groupUsers!=null&&groupUsers.size()>0){
			throw new AppException(friendName+"已在群组");			
		}
		Gson gson=GsonBuilderUtil.create();
		String content=name+"邀请您加入群组"+group.getGroupName();
		StringBuilder target=new StringBuilder();
		target.append("<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+contentPath+"/group/receiveGroupRQ?receiveId=");	
		String getuiTarget="";
		query.setGroupId(groupId);
		query.setUserId(to.getId());
		query.setIsConfirm(0);
		groupUsers=this.groupDao.findGroupUserByCondition(query);
		if(groupUsers!=null&&groupUsers.size()>0){
			log.info("重新发送邀请");
			GroupUser groupUser=groupUsers.get(0);
			target.append(groupUser.getId()+"&status=1'\">接受</button><button class=\"am-btn am-btn-danger am-btn-xs am-margin-left-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveGroupRQ?receiveId="+groupUser.getId()+"&status=-1'\">拒绝</button>").toString();
			getuiTarget="/api/group/receiveGroupRQ?receiveId="+groupUser.getId();
		}else{//入库并发送通知
			GroupUser groupUser=new GroupUser();
			groupUser.setGroup(group);
			groupUser.setUser(to);
			groupUser.setIsConfirm(0);
			this.save(groupUser);
			Notice notice=new Notice();
			notice.setReceiver(to);
			notice.setContent(content);
			target.append(groupUser.getId()+"&status=1'\">接受</button><button class=\"am-btn am-btn-danger am-btn-xs am-margin-left-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveGroupRQ?receiveId="+groupUser.getId()+"&noticeId="+notice.getId()+"&status=-1'\">拒绝</button>");
			getuiTarget="/api/group/receiveGroupRQ?receiveId="+groupUser.getId();
			notice.setTarget(target.toString());
			notice.setType(MessageType.GROUP_SEND);
			notice.setStatus(0);
			this.noticeService.save(notice);
		}
		Map<String,String> msgMap=new HashMap<String, String>();
		msgMap.put(MSG_TYPE, MessageType.GROUP_SEND);
		msgMap.put(MSG_TARGET, target.toString());
		msgMap.put(MSG_INFO, content);
		log.info("发送通知给用户"+to.getId());
		log.info("通知报文"+ gson.toJson(msgMap));
		GoEasyUtils.publishMsg(to.getId(), gson.toJson(msgMap));
		log.info("发送GoEasy成功");
		msgMap.put(MSG_TARGET, getuiTarget);
		log.info("通知报文"+ gson.toJson(msgMap));
		PushInteface.pushToSinglebytran(to.getId(), gson.toJson(msgMap));
		log.info("发送个推成功");
	}
	
	@Override
	public void receiveGroup(String receiveId,String noticeId,Integer status,String contentPath) throws Exception {
		GroupUser groupUser=this.get(GroupUser.class, receiveId);
		if(groupUser==null)
			throw new AppException("入群邀请不存在");
		Group group=groupUser.getGroup();	
		String name=LoginHelper.getName();
		String content=null;
		String target=null;
		if(status==1){
			content=name+"已通过您的加群请求。";
			target="<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+contentPath+"/group/content?groupId="+group.getId()+"'\">刷新</button>";
		}else if(status==-1){
			content=name+"拒绝了您的好友请求";
		}else{
			throw new AppException("无效的请求状态");
		}
		groupUser.setIsConfirm(status);
		this.update(groupUser);
		log.info("更新群组发送请求状态成功");
		if(FuncUtil.notEmpty(noticeId)){
			Notice sendNotice=this.get(Notice.class, noticeId);
			sendNotice.setStatus(1);
			log.info("更新对方发送通知状态");
			this.update(sendNotice);
		}
		User user=this.get(User.class, groupUser.getCreateId());
		Notice receiveNotice=new Notice();//添加消息记录
		receiveNotice.setReceiver(user);
		receiveNotice.setContent(content);
		receiveNotice.setTarget(target);
		receiveNotice.setType(MessageType.GROUP_RECEIVE);
		receiveNotice.setStatus(status);
		this.noticeService.save(receiveNotice);
		log.info("发送回执消息");
		Map<String,String> msgMap=new HashMap<String, String>();
		Gson gson=GsonBuilderUtil.create();
		msgMap.put(MSG_INFO,content );
		log.info("通知报文"+ gson.toJson(msgMap));
		PushInteface.pushToSinglebytran(user.getId(), gson.toJson(msgMap));
		log.info("发送个推成功");
		msgMap.put(MSG_TARGET, target);
		log.info("发送通知给用户"+user.getId());
		log.info("通知报文"+ gson.toJson(msgMap));
		GoEasyUtils.publishMsg(user.getId(),gson.toJson(msgMap));
		log.info("发送GoEasy成功");
	}

	@Override
	public Group createGroup(Group group, String friends, String contentPath) throws Exception {
		String groupNo=this.getGroupNo();
		log.info("获取群组编号"+groupNo);
		group.setGroupNo(groupNo);
		if(FuncUtil.isEmpty(group.getCover())){
			group.setCover("/resources/images/group/group_head.gif");
		}
		this.save(group);
		log.info("保存群组信息成功");
		String userName=LoginHelper.getUserName();
		String content=userName+"邀请您加入群组"+group.getGroupName();
		StringBuilder target=new StringBuilder();
		target.append("<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+contentPath+"/group/receiveGroupRQ?receiveId=");	
		String getuiTarget="";
		List<Notice> notices=new ArrayList<Notice>();
		User user=LoginHelper.getCurrentUser();//给自己入群
		GroupUser groupUser=new GroupUser();
		groupUser.setUser(user);
		groupUser.setGroup(group);
		groupUser.setIsConfirm(1);//设置自己状态为已确认
		this.save(groupUser);
		log.info("保存群组自己成功");
		if(FuncUtil.notEmpty(friends)){
			String[] fs=friends.split(",");
			for(int i=0;i<fs.length;i++){
				user=this.userService.get(User.class, fs[i]);
				groupUser=new GroupUser();
				groupUser.setUser(user);
				groupUser.setGroup(group);
				groupUser.setIsConfirm(0);
				this.save(groupUser);
				log.info("保存群组用户成功");
				Notice notice=new Notice();
				notice.setReceiver(user);
				notice.setContent(content);
				target.append(groupUser.getId()+"&status=1'\">接受</button><button class=\"am-btn am-btn-danger am-btn-xs am-margin-left-xs\" onclick=\"window.location.href='"+contentPath+"/friend/receiveGroupRQ?receiveId="+groupUser.getId()+"&noticeId="+notice.getId()+"&status=-1'\">拒绝</button>");
				getuiTarget="/api/group/receiveGroupRQ?receiveId="+groupUser.getId();
				notice.setTarget(target.toString());
				notices.add(notice);
				Map<String,String> msgMap=new HashMap<String, String>();
				Gson gson=GsonBuilderUtil.create();
				msgMap.put(MSG_INFO, content);
				msgMap.put(MSG_TARGET, target.toString());
				log.info("发送通知给用户"+user.getId());
				log.info("通知报文"+ gson.toJson(msgMap));
				GoEasyUtils.publishMsg(groupUser.getUser().getId(), gson.toJson(msgMap));
				log.info("发送Goeasy成功");
				msgMap.put(MSG_TARGET, getuiTarget.toString());
				log.info("发送通知给用户"+user.getId());
				log.info("通知报文"+ gson.toJson(msgMap));
				PushInteface.pushToSinglebytran(user.getId(), gson.toJson(msgMap));
				log.info("发送个推成功");
			}
		}
		this.noticeService.save(notices);
		log.info("保存消息记录成功");
		return group;
	}

	@Override
	public List<GroupFile> findGroupFileByCondition(GroupFileQuery query)
			throws Exception {
		// TODO Auto-generated method stub
		return this.groupDao.findGroupFileByCondition(query);
	}

	@Override
	public List<GroupNotice> findGroupNoticeByGroupId(String groupId) throws Exception {
		return this.groupDao.findGroupNoticeByGroupId(groupId);
	}

	@Override
	public void deleteFiles(String groupId, String userId, String fileId)
			throws Exception {
		Group group=this.groupDao.get(Group.class, groupId);
		String[] fileIds=fileId.split(",");
		for(int i=0;i<fileIds.length;i++){
			GroupFile groupFile = this.get(GroupFile.class, fileIds[i]);
			if(groupFile==null)
				throw new AppException("群组文件不存在");
			if(groupFile.getIsDir()==1){//目录
    			List<GroupFile> groupFiles=this.groupDao.getFileTree(groupId, groupFile.getId());
    			if(groupFiles!=null&&groupFiles.size()>0){
    				for(int j=0;j<groupFiles.size();j++){
    					group.setUsedSpace(group.getUsedSpace()-groupFile.getRef().getFileSize());
    				}
    				this.groupDao.update(group);
        			log.info("更新群组存储空间");
    				this.delete(groupFiles);
    			}
    		}else{//文件
    			group.setUsedSpace(group.getUsedSpace()-groupFile.getRef().getFileSize());
    			this.groupDao.update(group);
    			log.info("更新群组存储空间");
    			this.delete(groupFile);
    		}
		}
		
	}

	@Override
	public List<GroupFile> getFileTree(String groupId,String fileId) throws Exception {
		return groupDao.getFileTree(groupId, fileId);
	}

	@Override
	public void saveGroupSpace(String groupIds, String spaceId) throws Exception {
		Space space=this.get(Space.class, spaceId);
		if(space==null)
			throw new AppException("角色不存在");
		String[] groupIdss=groupIds.split(",");
		if(groupIdss!=null&&groupIdss.length>0){
			for(int i=0;i<groupIdss.length;i++){
				Group group=this.get(Group.class, groupIdss[i]);
				group.setSpace(space);
				this.update(group);
			}
		}
	}

	@Override
	public List<Group> findAllGroup() throws Exception {
		// TODO Auto-generated method stub
		return this.groupDao.findAllGroup();
	}
	
	

}
