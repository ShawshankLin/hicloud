package cn.edu.cylg.cis.hicloud.controller.api.group;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.HdfsHelper;
import cn.edu.cylg.cis.hicloud.core.validates.ValidateUtils;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.GroupUser;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.service.group.GroupService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.GroupUserQuery;

/**
 * 
 * @author  Shawshank
 * @version 1.0
 * 2016年5月7日 创建文件
 */
@Controller("ApiGroupCtrl")
@RequestMapping("/api/group")
public class GroupController implements WebConstants{
	
	protected static final Log log = LogFactory.getLog(GroupController.class);
	
	@Resource(name="groupService")
	private GroupService groupService;
	@Resource(name="fileService")
	private FileService fileService;

	/** 显示群首页*/
	@RequestMapping
	@ResponseBody
	public Object index(String userId) throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		log.info("加载群组列表开始");
		Long startTime = System.currentTimeMillis();
		GroupUserQuery query=new GroupUserQuery();
		query.setUserId(userId);
		query.setIsConfirm(1);
		List<GroupUser> groupUsers=groupService.findGroupUserByCondition(query);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "群组列表加载成功");
		result.put(MSG_DATA, groupUsers);
		Long endTime = System.currentTimeMillis();
		log.info("加载群组列表结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	/** 获取群信息*/
	@RequestMapping("getGroupInfo")
	@ResponseBody
	public Object getGroupInfo(String userId, String groupId) throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		log.info("查询群组信息开始");
		Long startTime = System.currentTimeMillis();
		Group group=this.groupService.get(Group.class, groupId);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "查询群组信息成功");
		result.put(MSG_DATA, group);
		Long endTime = System.currentTimeMillis();
		log.info("查询群组信息结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	/** 获取群组成员*/
	@RequestMapping("getGroupUser")
	@ResponseBody
	public Object getGroupUser(String userId, String groupId) throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		log.info("加载群组用户开始");
		Long startTime = System.currentTimeMillis();
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null)
			throw new AppException("共享群不存在");
		GroupUserQuery query=new GroupUserQuery();
		query.setGroupId(groupId);
		query.setIsConfirm(1);
		List<GroupUser> groupUsers=this.groupService.findGroupUserByCondition(query);
		boolean isMember=false;
		for(int i=0;i<groupUsers.size();i++){
			GroupUser groupUser=groupUsers.get(i);
			if(userId.equals(groupUser.getUser().getId())){
				isMember=true;
			}
		}
		if(!isMember){
			throw new AppException("无共享群访问权限");
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "加载群组内容成功");
		result.put(MSG_DATA, groupUsers);
		Long endTime = System.currentTimeMillis();
		log.info("加载群组用户结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	/** 显示群内容信息*/
	@RequestMapping("getGroupFile")
	@ResponseBody
	public Object getGroupFile(String userId, String groupId,@RequestParam(defaultValue="")String path) throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		log.info("查找群组文件开始");
		Long startTime = System.currentTimeMillis();
		List<GroupFile> groupFiles=this.groupService.listFiles(groupId, path);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "查找群组文件成功");
		result.put(MSG_DATA, groupFiles);
		Long endTime = System.currentTimeMillis();
		log.info("查找群组文件结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	
	@RequestMapping("/sendGroupRQ")
	@ResponseBody
	public Object sendGroupRQ(String userId,String groupId,String friendName,HttpServletRequest request) throws Exception{
		log.info("邀请好友开始");
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(friendName)){
			throw new AppException("请求参数有误");
		}
		try {
			String contentPath=request.getContextPath();
			this.groupService.sendGroup(groupId,friendName,contentPath);
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "已发送入群请求，等待对方确认");
		log.info("邀请好友入群结束");
		return result;
	}
	
	@RequestMapping("/receiveGroupRQ")
	@ResponseBody
	public Object receiveGroupRQ(String userId, String receiveId,String noticeId, Integer status ,HttpServletRequest request) throws Exception{
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(receiveId)||FuncUtil.isEmpty(status)){
			throw new AppException("请求参数有误");
		}
		try {
			String contentPath=request.getContextPath();
			this.groupService.receiveGroup(receiveId, noticeId , status, contentPath);
		} catch (AppException e) {
			throw new AppException(e.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "发送入群回执通知");
		log.info("邀请好友入群结束");
		return result;
	}
	
	
	@RequestMapping("createGroup")
	@ResponseBody 
	public Object createGroup(String userId, Group group,String friends,HttpServletRequest request)throws Exception{
		if(FuncUtil.isEmpty(userId)){
			throw new AppException("请求参数有误");
		}
		String errInfo="";
		try {
			List<String> errorList=ValidateUtils.validate(group);
			if(!FuncUtil.isEmpty(errorList)) {
				StringBuffer sb=new StringBuffer();
				for(int i=0;i<errorList.size();i++){
					sb.append(errorList.get(i));
				}
				errInfo=sb.toString();
				throw new AppException(errInfo);
			}
			if(FuncUtil.notEmpty(group.getId())){
				this.groupService.update(group);
			}else{
				String contentPath=request.getContextPath();
				group=this.groupService.createGroup(group, friends, contentPath);
			}
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加群组成功");
		result.put(MSG_DATA, group);
		return result;
	}
	
	@RequestMapping("/mkdir")
	@ResponseBody
	public Object createDir(String userId,String groupId,@RequestParam(defaultValue="") String path,String fileId,
			String dirName,HttpServletRequest request) throws Exception{
		log.info("创建群组文件夹开始");
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(groupId)){
			throw new AppException("请求参数有误");
		}
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null){
			throw new AppException("群组不存在");
		}
		dirName=FuncUtil.isEmpty(dirName)?"新建文件夹":dirName.trim();
		GroupFile groupFile=null;
		try {
			groupFile=this.groupService.createFile(userId, dirName, path, group);
		} catch (AppException a) {
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "创建群组文件夹成功");
		result.put(MSG_DATA, groupFile);
		log.info("创建群组文件夹结束");
		return result;
	}

	
	/**
	 *  解散群组
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("delGroup")
	@ResponseBody
	public Object delGroup(String userId,String id,ModelMap m) throws Exception{
		log.info("解散群组开始");
		if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		Group group=this.groupService.get(Group.class, id);
		if(group==null)
			throw new AppException("群组不存在");
		if(userId.equals(group.getCreateId())){
			this.groupService.delete(group);//级联删除相关记录
		}else{
			GroupUserQuery query=new GroupUserQuery();
			query.setUserId(userId);
			query.setGroupId(group.getId());
			query.setIsConfirm(1);
			List<GroupUser> groupUsers=this.groupService.findGroupUserByCondition(query);
			this.groupService.delete(groupUsers);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "解散群组成功");
		log.info("解散群组结束");
		return result;
		
	}
	
	
	 /**
     *  文件下载
      * @return
      * @throws Exception
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(String userId,String groupId, String fileId,HttpServletRequest request,HttpServletResponse response,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId))
    		throw new AppException("请求参数有误");
    	String[] fileIds=fileId.split(",");
		if(fileIds!=null&&fileIds.length>0){
			if(fileIds.length==1){//单选文件下载
				GroupFile groupFile=this.groupService.get(GroupFile.class, fileId);
				if(groupFile==null){
					throw new AppException("群组文件不存在");
				}
				if(this.fileService.hasDownloadPerm(groupId, userId, fileId)){
					if(groupFile.getIsDir()==1){//文件夹打包下载
						
						
		        	}else{//单个文件下载
		        		String hdfsPath=groupFile.getRef().getRef();
		        		HdfsHelper.downloadFile(request, response, hdfsPath, groupFile.getName()+"."+groupFile.getRef().getSuffix());
		        	}
				}else{
					throw new AppException("无文件下载权限");
				}
			}else{//多选文件下载
				
			}	
		}
    }
	
    
    /**
     * 删除
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public Object deleteFile(String userId, String groupId, String fileId,HttpServletRequest request) throws Exception{
    	log.info("删除群组文件开始");
    	if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId)){
    		throw new AppException("请求参数有误");
    	}
    	Group group=this.groupService.get(Group.class, groupId);
    	if(group==null){
    		throw new AppException("群组不存在");
    		
    	}
    	if(!userId.equals(group.getCreateId())){
    		throw new AppException("无文件删除权限");
    	}
    	try {
    		this.groupService.deleteFiles(groupId, userId, fileId);
    		log.info("删除群组文件成功");
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除群组文件成功");
		log.info("删除文件结束");
    	return result;
    }
    
    @RequestMapping("/renameFile")
    @ResponseBody
    public Object renameFile(String userId,String fileId,String newname) throws Exception{
    	log.info("重命名文件开始");
    	if(FuncUtil.isEmpty(userId)||FuncUtil.isEmpty(fileId)||FuncUtil.isEmpty(newname)){
    		throw new AppException("请求参数有误");
    	}
    	GroupFile oldFile=this.groupService.get(GroupFile.class, fileId);
		if(!oldFile.getCreateId().equals(userId)){
			throw new AppException("无文件权限");
		}
		if(oldFile.getName().equals(newname)){
			throw new AppException("修改文件名与原文件名相同");
		}
    	try {
    		oldFile.setName(newname);
			this.groupService.update(oldFile);
			log.info("修改群组文件名成功");
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "重命名文件成功");
		log.info("重命名文件结束");
    	return result;
    }
    
    
    /**
	  * 上传文件
	  * @param groupId 群组id
	  * @param fileId 文件id
	  * @param path 上传实际路径
	  * @param currentPath 上传虚拟路径
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/uploadFile")
	@ResponseBody
	public Object uploadFile(String userId,String groupId,String fileId,@RequestParam(defaultValue="")String path,HttpServletRequest request) throws Exception{
		log.info("上传文件开始");
		if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId))
			throw new AppException("请求操作有误");
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null)
			throw new AppException("群组不存在");
		path=path!=null?path.trim():"";
		String[] fileIds=fileId.split(",");
		if(fileIds.length>0){
			for(int i=0;i<fileIds.length;i++){
				File file=this.fileService.get(File.class, fileIds[i]);
				if(file==null)
					throw new AppException("上传文件不存在");
				try {
					this.groupService.createFile(userId, file.getName(), path, group, file, file.getIsDir());
				} catch (Exception e) {
					throw new AppException("文件"+file.getName()+"上传失败");
				}
			}
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "上传文件成功");
		log.info("上传文件结束");
		return result;
	}
}
