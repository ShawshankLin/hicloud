package cn.edu.cylg.cis.hicloud.controller.group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.HdfsHelper;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Friend;
import cn.edu.cylg.cis.hicloud.entity.Group;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.GroupNotice;
import cn.edu.cylg.cis.hicloud.entity.GroupUser;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.service.group.FriendService;
import cn.edu.cylg.cis.hicloud.service.group.GroupService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.SpaceService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.DocConverter;
import cn.edu.cylg.cis.hicloud.utils.FileUtil;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.GsonBuilderUtil;
import cn.edu.cylg.cis.hicloud.utils.PathUtil;
import cn.edu.cylg.cis.hicloud.utils.PropUtil;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.FriendQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupFileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupUserQuery;

import com.google.gson.Gson;

@Controller
@RequestMapping("/group")
public class GroupController implements WebConstants{

	protected static final Log log = LogFactory.getLog(GroupController.class);
	
	private static final String CACHE_PATH = PathUtil.getClasspath()+PropUtil.readValue("cachePath");
	
	@Resource(name="groupService")
	private GroupService groupService;
	@Resource(name="friendService")
	private FriendService friendService;
	@Resource(name="userService") 
	private UserService userService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
	@Resource(name="fileService")
	private FileService fileService;
	@Resource(name="spaceService")
	private SpaceService spaceService;
	
	/** 显示群首页*/
	@RequestMapping
	public String index(ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
		GroupUserQuery query=new GroupUserQuery();
		query.setUserId(userId);
		query.setIsConfirm(1);
		List<GroupUser> groupUsers=groupService.findGroupUserByCondition(query);
		m.addAttribute("groupUsers",groupUsers);
		return "/group/group-info-index";
	}
	
	
	/** 显示群内容信息*/
	@RequestMapping("content")
	public String content(@RequestParam String groupId,@RequestParam(defaultValue="")String path,@RequestParam(defaultValue="")String currentPath,
			@CookieValue(value="STYLE")String style,ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
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
		List<GroupFile> groupFiles=this.groupService.listFiles(groupId, path);
		List<GroupNotice> groupNotices=this.groupService.findGroupNoticeByGroupId(groupId);
		m.addAttribute("group",group);
		m.addAttribute("groupFiles", groupFiles);
		m.addAttribute("groupUsers", groupUsers);
		m.addAttribute("groupNotices", groupNotices);
		m.addAttribute("path",path);
		m.addAttribute("currentPath",currentPath);
		return "/group/group-info-"+(style!=null?style:DEFAULT_VIEW);
	}
	
	
	/**
     * 上一级目录.
     */
    @RequestMapping("/parentDir")
    public String parentDir(String groupId,String path,String currentPath) throws Exception {
        if (FuncUtil.isEmpty(path)) {
            return "redirect:/group/content?groupId="+groupId;
        }
        String parentPath = path.trim().substring(0, path.lastIndexOf("/"));
        String currentparentPath = currentPath.trim().substring(0, currentPath.lastIndexOf("/"));
        return "redirect:/group/content?groupId="+groupId+"&path=" + parentPath+"&currentPath="+currentparentPath;
    }
    
    
    /**
     *  搜索群组
      * @param groupNo
      * @return
      * @throws Exception
     */
    @RequestMapping("/searchGroup")
    public String searchGroup(String groupNo,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(groupNo)){
    		throw new AppException("请求参数有误");
    	}
    	GroupQuery query=new GroupQuery();
    	query.setGroupNo(groupNo);
    	List<Group> groups=this.groupService.findGroupByCondition(query);
    	m.addAttribute("groups",groups);
    	return "/group/group-info-index";
    }
    
    /**
     *  搜索群组文件
      * @param groupId
      * @param name
      * @param m
      * @return
      * @throws Exception
     */
    @RequestMapping("/saerchFile")
    public String saerchFile(String groupId,String name,@CookieValue(value="STYLE")String style,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(name)){
    		throw new AppException("请求参数有误");
    	}
    	GroupFileQuery query=new GroupFileQuery();
    	query.setGroupId(groupId);
    	query.setName(name);
    	List<GroupFile> groupFiles=this.groupService.findGroupFileByCondition(query);
    	m.addAttribute("groupFiles",groupFiles);
    	return "/group/group-search-"+(style!=null?style:DEFAULT_VIEW);
    }
    
    
    @RequestMapping("/removeGroupUser")
    public Object removeGroupUser(String groupId,String userId,HttpServletRequest request) throws Exception{
    	log.info("删除群组成员开始");
    	if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(userId)){
    		throw new AppException("请求参数有误");
    	}
    	Group group=this.groupService.get(Group.class, groupId);
    	if(group==null)
    		throw new AppException("群组不存在");
    	GroupUserQuery query=new GroupUserQuery();
    	query.setGroupId(groupId);
    	query.setUserId(userId);
    	query.setIsConfirm(1);
    	List<GroupUser> groupUsers=this.groupService.findGroupUserByCondition(query);
    	this.groupService.delete(groupUsers);
    	log.info("删除群组成员成功");
    	Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除群组成员成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+group.getId());
		log.info("删除群组用户结束");
		return result;
    }
	
	/**
	 *  创建群组
	  * @param ispage
	  * @param group
	  * @param friends
	  * @param request
	  * @param m
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("createGroup")
	public String createGroup(String ispage,String id,String cover,String groupNo,
			String groupName,String description,
			String friends,HttpServletRequest request,ModelMap m)throws Exception{
		if(FuncUtil.notEmpty(ispage)&&StringUtils.equals("true", ispage)){
			FriendQuery query =new FriendQuery();
			String userId=LoginHelper.getUserId();
			query.setCreateId(userId);
			query.setIsConfirm(1);
			List<Friend> fList=this.friendService.findFriendByCondition(query);
			m.addAttribute("friends", fList);
			return "/group/group-info-add";
		}
		if(FuncUtil.isEmpty(groupName))
			throw new AppException("请求参数有误");
		try {
			if(FuncUtil.notEmpty(id)){
				Group group=this.groupService.get(Group.class, id);
				if(group==null)
					throw new AppException("群组不存在");
				group.setCover(cover);
				group.setGroupNo(groupNo);
				group.setGroupName(groupName);
				group.setDescription(description);
				this.groupService.update(group);
			}else{
				Group group=new Group();
				group.setCover(cover);
				group.setGroupNo(groupNo);
				group.setGroupName(groupName);
				group.setDescription(description);
				String contentPath=request.getContextPath();
				List<Space> spaces=this.spaceService.findAllSpace();//分配低配空间
				if(spaces!=null&&spaces.size()>0){
					group.setSpace(spaces.get(0));
					log.info("分配群组存储空间"+spaces.get(0).getSpaceName()+":"+spaces.get(0).getSpaceSize());
				}
				this.groupService.createGroup(group, friends, contentPath);
			}
		} catch (AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Gson gson=GsonBuilderUtil.create();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加群组成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group");
		m.put(AJAX_MSG,gson.toJson(result));
		return AJAX_URL;
	}
	
	/**
	 *  修改群信息
	  * @param id
	  * @param m
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("editGroup")
	public String editGroup(String id,ModelMap m) throws Exception{
		if(FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		Group group=this.groupService.get(Group.class, id);
		if(group==null)
			throw new AppException("群组不存在");
		m.addAttribute("group", group);
		return "/group/group-info-add";
	}
	
	/**
	 *  解散群组
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("delGroup")
	public String delGroup(String id,ModelMap m) throws Exception{
		if(FuncUtil.isEmpty(id))
			throw new AppException("请求参数有误");
		Group group=this.groupService.get(Group.class, id);
		if(group==null)
			throw new AppException("群组不存在");
		String userId=LoginHelper.getUserId();
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
		return "redirect:/group";
	}
	
	/**
	  * 新建文件夹
	  * @param groupId
	  * @param path
	  * @param fileId
	  * @param dirName
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/mkdir")
	@ResponseBody
	public Object createDir(String groupId,@RequestParam(defaultValue="") String path,String fileId,
			String dirName,HttpServletRequest request) throws Exception{
		log.info("创建文件夹开始");
		if(FuncUtil.isEmpty(groupId)){
			throw new AppException("请求参数有误");
		}
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null){
			throw new AppException("群组不存在");
		}
		path=path!=null?path.trim():"";
		dirName=FuncUtil.isEmpty(dirName)?"新建文件夹":dirName.trim();
		try {
			String userId=LoginHelper.getUserId();
			this.groupService.createFile(userId, dirName, path, group);
		} catch (AppException a) {
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "创建文件夹成功");
		//result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+group.getId()+"&path="+path);
		log.info("创建文件夹结束");
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
	public Object uploadFile(String groupId,String fileId,@RequestParam(defaultValue="")String path,
			@RequestParam(defaultValue="")String currentPath,HttpServletRequest request) throws Exception{
		log.info("上传文件开始");
		if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId))
			throw new AppException("请求操作有误");
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null)
			throw new AppException("群组不存在");
		path=path!=null?path.trim():"";
		currentPath=currentPath!=null?currentPath.trim():"";
		String[] fileIds=fileId.split(",");
		if(fileIds.length>0){
			for(int i=0;i<fileIds.length;i++){
				File file=this.fileService.get(File.class, fileIds[i]);
				if(file==null)
					throw new AppException("上传文件不存在");
				try {
					String userId=LoginHelper.getUserId();
					this.groupService.createFile(userId, file.getName(), path, group, file, file.getIsDir());
				} catch(AppException a){
					throw new AppException(a.getMessage());
				} catch (Exception e) {
					throw new Exception(e);
				}
			}
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "上传文件成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+group.getId()+"&path="+path+"&currentPath="+currentPath);
		log.info("上传文件结束");
		return result;
	}
	
	/**
	 *  邀请好友入群
	  * @param groupId 群组id
	  * @param friendName 好友用户名
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/sendGroupRQ")
	@ResponseBody
	public Object sendGroupRQ(String groupId,String friendName,HttpServletRequest request) throws Exception{
		log.info("邀请好友开始");
		if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(friendName)){
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
	
	
	/**
	 *  接受入群邀请
	  * @param GroupUserId 群组用户id
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/receiveGroupRQ")
	public String receiveGroupRQ(String receiveId,String noticeId, Integer status ,HttpServletRequest request) throws Exception{
		if(FuncUtil.isEmpty(receiveId)||FuncUtil.isEmpty(status)){
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
		return "redirect:/group";
	}
	
	/**
	  * 发布群公告
	  * @param groupId
	  * @param notice
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/publishNotice")
	@ResponseBody
	public Object publishNotice(String groupId,String notice,HttpServletRequest request) throws Exception{
		log.info("添加群公告开始");
		if(FuncUtil.isEmpty(groupId)){
			throw new AppException("请求参数有误");
		}
		Group group=this.groupService.get(Group.class, groupId);
		if(group==null)
			throw new AppException("群组不存在");
		String userId=LoginHelper.getUserId();
		if(!userId.equals(group.getCreateId()))
			throw new AppException("只有群主可以发布群公告");
		GroupNotice groupNotice=new GroupNotice();
		groupNotice.setGroup(group);
		groupNotice.setNotice(notice);
		this.groupService.save(groupNotice);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "添加群公告成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+groupId);
		log.info("添加群公告结束");
		return result;
	}
	
	
	 /**
     * 删除
     */
    @RequestMapping("/deleteFile")
    @ResponseBody
    public Object deleteFile(String groupId, String fileId,String path,String currentPath,HttpServletRequest request) throws Exception{
    	log.info("删除文件开始");
    	if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId)){
    		throw new AppException("请求参数有误");
    	}
    	Group group=this.groupService.get(Group.class, groupId);
    	if(group==null){
    		throw new AppException("群组不存在");
    		
    	}
    	String userId = LoginHelper.getUserId();
    	if(!userId.equals(group.getCreateId())){
    		throw new AppException("无文件删除权限");
    	}
    	try {
    		this.groupService.deleteFiles(groupId, userId, fileId);
			Map<String,Object> msgMap=new HashMap<String, Object>();
			Gson gson=GsonBuilderUtil.create();
			msgMap.put(MSG_TYPE, MessageType.GROUP_FILE_DEL);
			msgMap.put(MSG_DATA, fileId);
			log.info("发送个推通知");
			log.info("发送通知报文"+gson.toJson(msgMap));
			PushInteface.pushToSinglebytran(userId, gson.toJson(msgMap)); 
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+groupId+"&path="+path+"&currentPath="+currentPath);
		log.info("删除文件结束");
    	return result;
    }
    
    @RequestMapping("/renameFile")
    @ResponseBody
    public Object renameFile(@RequestParam("fileId") String fileId,@RequestParam("newname") String newname,
    		String currentPath, HttpServletRequest request) throws Exception{
    	log.info("重命名文件开始");
    	if(FuncUtil.isEmpty(fileId)||FuncUtil.isEmpty(newname)){
    		throw new AppException("请求参数有误");
    	}
    	GroupFile oldFile=this.groupService.get(GroupFile.class, fileId);
    	String userId = LoginHelper.getUserId();
		if(!oldFile.getCreateId().equals(userId)){
			throw new AppException("无文件权限");
		}
		if(oldFile.getName().equals(newname)){
			throw new AppException("修改文件名与原文件名相同");
		}
    	try {
    		oldFile.setName(newname);
			this.groupService.update(oldFile);
			Map<String,Object> msgMap=new HashMap<String, Object>();
			Gson gson=GsonBuilderUtil.create();
			msgMap.put(MSG_TYPE, MessageType.GROUP_FILE_RENAME);
			Map<String,String> dataMap=new HashMap<String,String>();
			dataMap.put("fileId", fileId);
			dataMap.put("newname", newname);
			msgMap.put(MSG_DATA, dataMap);
			log.info("发送个推通知");
			log.info("发送通知报文"+gson.toJson(msgMap));
			PushInteface.pushToSinglebytran(userId, gson.toJson(msgMap)); 
			log.info("发送通知成功");
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "重命名文件成功");
		result.put(MSG_TARGET, request.getContextPath()+"/group/content?groupId="+oldFile.getGroup().getId()+"&path="+oldFile.getParentPath()+"&currentPath="+currentPath);
		log.info("重命名文件结束");
    	return result;
    }
    
    /**
     *  文件下载
      * @return
      * @throws Exception
     */
    @RequestMapping("/downloadFile")
    public void downloadFile(String groupId, String fileId,HttpServletRequest request,HttpServletResponse response,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(groupId)||FuncUtil.isEmpty(fileId))
    		throw new AppException("请求参数有误");
    	String userId=LoginHelper.getUserId();
    	String[] fileIds=fileId.split(",");
		if(fileIds!=null&&fileIds.length>0){
			if(fileIds.length==1){//单选文件下载
				GroupFile groupFile=this.groupService.get(GroupFile.class, fileId);
				if(this.fileService.hasDownloadPerm(groupId, userId, fileId)){
					if(groupFile.getIsDir()==1){//文件夹打包下载
						/*GroupFileQuery query=new GroupFileQuery();
						query.setParentId(groupFile.getId());
						List<GroupFile> groupFiles=this.groupService.findGroupFileByCondition(query);
						if(groupFiles!=null&&groupFiles.size()>0){
							for(int i=0;i<groupFiles.size();i++){
								GroupFile file=groupFiles.get(i);
							}
						}						
		    			String hdfsPath = groupFile.getRef().getRef();
		    			StringBuilder localPath = new StringBuilder().append(CACHE_PATH).append("/").append(UuidUtil.get32UUID());
		    			log.info("下载hdfs文件夹"+groupFile.getFileName()+"==>> 本地目录"+localPath);
		    			HdfsHelper.download(hdfsPath, localPath.append("/").append(groupFile.getFileName()).toString());
		    			List<GroupFile> groupFiles=this.groupService.getFileTree(groupId, userId, fileId);
		    			for(int j=0;j<groupFiles.size();j++){
		    				if(groupFiles.get(j).getIsDir()==0){
		    					StringBuilder targetPath=new StringBuilder().append(localPath).append(groupFiles.get(j).getParentPath()).append("/").append(groupFiles.get(j).getFileName());
		    					java.io.File f=new java.io.File(targetPath.toString());
		        				StringBuilder newName=new StringBuilder().append(localPath).append(groupFiles.get(j).getParentPath()).append("/").append(groupFiles.get(j).getName()).append(".").append(groupFiles.get(j).getRef().getSuffix());
		        				f.renameTo(new java.io.File(newName.toString()));//更正文件名
		    				}
		    			}
		        		String zipPath=localPath+".zip";
		        		log.info("压缩文件夹"+localPath+"==>> 目录"+zipPath);
		        		FileZip.zip(localPath.toString(), zipPath);
		        		FileDownload.fileDownload(response, zipPath, groupFile.getName()+".zip");
		        		FileUtil.delFile(zipPath);
		        		FileUtil.delFile(localPath.toString());*/
		        	}else{//单个文件下载
		        		String hdfsPath=groupFile.getRef().getRef();
		        		HdfsHelper.downloadFile(response, hdfsPath, groupFile.getName()+"."+groupFile.getRef().getSuffix());
		        	}
				}else{
					throw new AppException("无文件下载权限");
				}
			}else{//多选文件下载
				/*String localPath = CACHE_PATH+"/"+UuidUtil.get32UUID();
				for(int i=0;i<fileIds.length;i++){
					if(this.fileService.hasDownloadPerm(groupId, userId, fileId)){
						cn.edu.cylg.cis.hicloud.entity.File file = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileIds[i]);
	    				String hdfsPath = file.getRef();
	    				log.info("下载hdfs "+file.getFileName()+"==>> 本地目录"+localPath);
	    				if(file.getIsDir()==1){//文件夹打包下载
	    	    			HdfsHelper.download(hdfsPath, localPath+"/"+file.getFileName());
	    	    			List<cn.edu.cylg.cis.hicloud.entity.File> files = fileService.getFileTree(file.getCreateId(),file.getId());
	    	    			for(int j=0;j<files.size();j++){
	    	    				if(files.get(j).getIsDir()==0){
	    	    					StringBuilder targetPath=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getFileName());
	    	    					java.io.File f=new java.io.File(targetPath.toString());
	    	        				StringBuilder newName=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getName()).append(".").append(files.get(j).getSuffix());
	    	        				f.renameTo(new java.io.File(newName.toString()));//更正文件名
	    	    				}
	    	    			}
	    	        	}else{
	    	        		HdfsHelper.download(hdfsPath, localPath+"/"+file.getFileName());
	    	        		StringBuilder targetPath=new StringBuilder();
	    	        		targetPath.append(localPath).append(file.getParentPath()).append("/").append(file.getFileName());
	    	        		java.io.File f=new java.io.File(targetPath.toString());
	    	        		StringBuilder newName=new StringBuilder();
	        				newName.append(localPath).append(file.getParentPath()).append("/").append(file.getName()).append(".").append(file.getSuffix());
	        				f.renameTo(new java.io.File(newName.toString()));//更正文件名
	    	        	}
					}else{
						throw new AppException("无文件下载权限");
					}
    			}
				String zipPath=localPath+".zip";
        		log.info("压缩文件夹"+localPath+"==>> 目录"+zipPath);
        		FileZip.zip(localPath, zipPath);
        		FileDownload.fileDownload(response, zipPath, "hicloud.zip");
        		FileUtil.delFile(zipPath);
        		FileUtil.delFile(localPath);*/
			}	
		}
    }
    
    /**
     *  预览文件
      * @param fileId
      * @param m
      * @return
      * @throws Exception
     */
    @RequestMapping("/previewFile")
    public String previewFile(String fileId,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(fileId)){
    		throw new AppException("请求参数有误");
    	}
    	GroupFile groupFile=this.groupService.get(GroupFile.class, fileId);
    	if(groupFile==null)
    		throw new AppException("文件不存在");
    	File file=groupFile.getRef();
    	if(file!=null){
    		String hdfsPath=groupFile.getRef().getRef();
    		String userId=LoginHelper.getUserId();
    		StringBuilder converfilename=new StringBuilder().append(CACHE_PATH).append("/").append(userId).append("/priview");
    		log.info("初始化缓存目录........");
    		FileUtil.createDir(converfilename.toString());//检查文件目录，不存在则创建
    		converfilename.append("/").append(file.getFileName()).append(".").append(file.getSuffix());
    		log.info("下载hdfs文件到本地");
        	HdfsHelper.downloadFile(hdfsPath, converfilename.toString());
        	DocConverter d = new DocConverter(converfilename.toString());
        	d.conver();//调用conver方法开始转换，先执行doc2pdf()将office文件转换为pdf;再执行pdf2swf()将pdf转换为swf
        	StringBuilder swfpath=new StringBuilder();
        	swfpath.append(PropUtil.readValue("cachePath")).append("/").append(userId).append("/priview").append(d.getswfPath().substring(d.getswfPath().lastIndexOf("/")));
            log.info("swf文件输出路径"+swfpath.toString());
            m.addAttribute("swfpath",swfpath.toString());
    	}
    	return "/drive/drive-info-preview";
    }
    
    @RequestMapping("/saveMyDrive")
    public Object saveMyDrive(String id) throws Exception{
    	if(FuncUtil.isEmpty(id))
    		throw new AppException("请求参数有误");
    	GroupFile groupFile=this.groupService.get(GroupFile.class, id);
    	if(groupFile==null)
    		throw new AppException("共享群文件不存在");
    	File file=groupFile.getRef();
    	String userId=LoginHelper.getUserId();
    	if(userId.equals(file.getCreateId()))
    		throw new AppException("你的网盘已存在该文件");
    	log.info("查询网盘文件是否存在");
		FileQuery query=new FileQuery();
		query.setCreateId(userId);
		query.setMd5(file.getMD5());
		List<File> fileList=this.fileService.searchFiles(query);
		if(fileList!=null&&fileList.size()>0){
			throw new AppException("你的网盘已存在该文件");
		}
    	File myfile=new File();
    	myfile.setFileCode(file.getFileCode());
    	myfile.setDescription(file.getDescription());
    	myfile.setFileName(UuidUtil.get32UUID());
    	myfile.setFileSize(file.getFileSize());
    	myfile.setFileVersion(1);
    	myfile.setIsDir(file.getIsDir());
    	myfile.setMD5(file.getMD5());
    	myfile.setName(file.getName());
    	myfile.setParentFile(null);
    	myfile.setParentPath("");
    	myfile.setRef(file.getRef());
    	myfile.setStatus(file.getStatus());
    	myfile.setSuffix(file.getSuffix());
    	myfile.setSerialNum(UuidUtil.get32UUID());
    	fileService.save(myfile);
    	return "redirect:/myfile";
    }
}
