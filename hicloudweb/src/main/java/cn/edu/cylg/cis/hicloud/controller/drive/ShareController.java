package cn.edu.cylg.cis.hicloud.controller.drive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.service.drive.ShareService;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.GoEasyUtils;
import cn.edu.cylg.cis.hicloud.utils.GsonBuilderUtil;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

import com.google.gson.Gson;

/**
 * 分享 ctrl
 * @author  Shawshank
 * @version 1.0
 * 2016年4月12日 创建文件
 */
@Controller
@RequestMapping("/share")
public class ShareController implements WebConstants{

	protected static final Log log = LogFactory.getLog(ShareController.class);
	
	@Resource(name="fileService")
	private FileService fileService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="shareService")
	private ShareService shareService;
	@Resource(name="noticeService")
	private NoticeService noticeService;
 	
	
	/**
	 * 分享明细
	 */
	@RequestMapping("/{shareId}")
	public String share(@PathVariable("shareId")String shareId,Integer extractionCode,ModelMap m) throws Exception{		
		Share share = this.shareService.get(Share.class, shareId);
		if(FuncUtil.isEmpty(share)){
			throw new AppException("分享链接已失效，<br/>可能原因：分享者已取消此分享，或删除了分享的文件。");
		}else{
			m.addAttribute("share",share);
			if(share.getType()==1){//公开类型
				return "/drive/drive-share-detail";
			}else if(share.getType()==2){
				if(extractionCode==null||!extractionCode.equals(share.getExtractionCode())){//无提取码不可以查看分享文件
					return "/drive/drive-share-encrypt";
				}
				return "/drive/drive-share-detail";
			}else if(share.getType()==3){//好友类型
				String userId=LoginHelper.getUserId();
				if(!userId.equals(share.getTo().getId())&&!userId.equals(share.getCreateId())){
					throw new AppException("无权限操作");
				}
				return "/drive/drive-share-detail";
			}else{
				throw new AppException("请求不合法");
			}
		}
	}
	
	/**
	 * 我的分享
	 */
	@RequestMapping("/myshare")
	public String myShare(ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
		ShareQuery query=new ShareQuery();
		query.setCreateId(userId);
		List<Map<String,String>> shares=this.shareService.findShareForIndex(query);
		m.addAttribute("result",shares);
		return "/drive/drive-share-list";
	}
	
	
	

	/**
	  * 分享文件
	  * @param fileIds 文件id
	  * @param type 分享类型
	  * @param to    被分享者
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/toshare")
	@ResponseBody
	public Object toShare(String fileId,Integer type,String toId,HttpServletRequest request) throws Exception{
		log.info("分享文件开始");
		String userId=LoginHelper.getUserId();
		if(FuncUtil.isEmpty(fileId)||FuncUtil.isEmpty(type)){
			throw new AppException("请求参数有误");
		}
		Share share=new Share();
		share.setType(type);
		if(type==2){
			share.setExtractionCode(FuncUtil.getRandomNum());
		}else if(type==3){//个人分享文件直接返回
			if(FuncUtil.isEmpty(toId)){
				throw new AppException("请求参数有误");
			}
			User toUser=this.userService.get(User.class, toId);
			if(toUser==null){
				throw new AppException("分享者不存在");
			}
			share.setTo(toUser);
			List<Share> shares=new ArrayList<Share>();
			String[] fileIds=fileId.split(",");//多文件
			for(int j=0;j<fileIds.length;j++){
				File file=this.fileService.get(File.class, fileIds[j]);
				if(file==null){
					throw new AppException("文件不存在");
				}else{
					if(!file.getCreateId().equals(userId)){
						throw new AppException(file.getName()+"无此文件权限");
					}else{
						ShareQuery query = new ShareQuery();
						query.setFileId(fileIds[j]);
						query.setToId(toId);
						List<Share> sList=this.shareService.findShareByCondition(query);
						if(!FuncUtil.isEmpty(sList)){
							throw new AppException(file.getName()+"文件已分享");
						}
					}
				}
				Share s=new Share();
				BeanUtils.copyProperties(s, share);
				s.setFile(file);
				shares.add(s);
			}
			this.shareService.save(shares);
			log.info("分享文件入库成功");
			Notice notice=new Notice();//添加消息记录
			String name=LoginHelper.getName();
			String content=name+"给您分享了一个文件";
			String target="<button class=\"am-btn am-btn-success am-btn-xs\" onclick=\"window.location.href='"+request.getContextPath()+"/notice/readNotice?noticeId="+notice.getId()+"'\">标记为已读</butto>";
			notice.setReceiver(toUser);
			notice.setContent(content);
			notice.setType(MessageType.SHARE_FILE);
			notice.setStatus(0);
			this.noticeService.save(notice);
			notice.setTarget(target);
			this.noticeService.update(notice);
			Map<String,String> msgMap=new HashMap<String, String>();
			Gson gson=GsonBuilderUtil.create();
			msgMap.put(MSG_INFO,content );
			log.info("发送通知给用户"+toId);
			log.info("通知报文"+ gson.toJson(msgMap));
			GoEasyUtils.publishMsg(toId,gson.toJson(msgMap));
			log.info("发送GoEasy成功");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put(MSG_STATUS, MessageType.SUCCESS );
			result.put(MSG_INFO, "分享文件成功" );
			return result;
		}
		File file = this.fileService.get(File.class, fileId);
		if(file==null){
			throw new AppException("文件不存在");
		}else{
			if(!file.getCreateId().equals(userId)){
				throw new AppException("无此文件权限");
			}else{
				ShareQuery query = new ShareQuery();
				query.setFileId(fileId);
				List<Share> shares=this.shareService.findShareByCondition(query);
				if(!FuncUtil.isEmpty(shares)){
					throw new AppException("文件已分享");
				}
			}
		}
		share.setFile(file);
		share.setType(type);
		share=this.shareService.save(share);
		StringBuilder msgData=new StringBuilder();
		msgData.append("<div class=\"am-modal-hd\">"+(share.getType()==1?"成功创建公开链接":"成功创建私有链接")+"</div><div class=\"am-modal-bd am-text-break\">"+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/share/"+share.getId());
		if(share.getExtractionCode()!=null){
			msgData.append("(提取码 "+share.getExtractionCode()+")");
		}
		msgData.append("<input type=\"hidden\" id=\"share-link-input\" value=\""+request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/share/"+share.getId()+"\"/></div>");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS );
		result.put(MSG_INFO, "分享文件成功" );
		result.put(MSG_DATA, msgData);
		return result;
	}
	
	/**
	 * 取消分享
	 */
	@RequestMapping("cancel")
	@ResponseBody
	public Object cancelShare(@RequestParam("shareId") String shareId) throws Exception{
		if(FuncUtil.isEmpty(shareId)){
			throw new AppException("分享文件不允许为空");
		}
		this.shareService.cancelShare(shareId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "取消分享文件成功" );
		return result;
	}
	
	/**
	 * 保存分享文件至我的网盘
	 */
	@RequestMapping("saveMyDrive")
	@ResponseBody
	public Object saveMyDrive(String fileId) throws Exception{
		if(FuncUtil.isEmpty(fileId))
    		throw new AppException("请求参数有误");
		File file = this.fileService.get(File.class, fileId);
		if(file==null){
			throw new AppException("文件不存在");
		}
		String userId=LoginHelper.getUserId();
		if(userId.equals(file.getCreateId()))throw new AppException("文件已存在");
		log.info("查询网盘文件是否存在");
		FileQuery query=new FileQuery();
		query.setCreateId(userId);
		query.setMd5(file.getMD5());
		List<File> fileList=this.fileService.searchFiles(query);
		if(fileList!=null&&fileList.size()>0){
			throw new AppException("你的网盘已存在该文件");
		}
		File myFile = new File();
		myFile.setDescription(file.getDescription());
		myFile.setFileCode(file.getFileCode());
		myFile.setFileName(UuidUtil.get32UUID());
		myFile.setFileSize(file.getFileSize());
		myFile.setFileVersion(file.getFileVersion());
		myFile.setIsDir(file.getIsDir());
		myFile.setMD5(file.getMD5());
		myFile.setName(file.getName());
		myFile.setParentPath("");
		myFile.setParentFile(null);
		myFile.setRef(file.getRef());
		myFile.setSerialNum(UuidUtil.get32UUID());
		myFile.setStatus(file.getStatus());
		myFile.setSuffix(file.getSuffix());
		this.fileService.save(myFile);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "保存分享文件成功" );
		return result;
	}
	
	
	/**
	 *  获取分享历史
	  *	@param type 分享类型
	  * @param to 分享者
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("getShareHistory")
	@ResponseBody
	public Object getShareHistory(String toId) throws Exception{
		log.info("获取分享历史开始");
		if(FuncUtil.isEmpty(toId)){
			throw new AppException("请求参数有误");
		}
		User toUser=this.userService.get(User.class, toId);
		if(toUser==null){
			throw new AppException("分享者不存在");
		}
		String userId=LoginHelper.getUserId();
		ShareQuery query=new ShareQuery();
		query.setToId(toId);
		query.setCreateId(userId);
		query.setType(3);
		List<Share> toList=this.shareService.findShareByCondition(query);//获取好友分享给自己的文件
		query.setCreateId(toId);
		query.setToId(userId);
		query.setType(3);
		List<Share> myList=this.shareService.findShareByCondition(query);//获取自己分享给好友的文件
		toList.addAll(myList);
		Collections.sort(toList, new Comparator<Share>() {
			@Override
			public int compare(Share o1, Share o2) {
				// TODO Auto-generated method stub
				if(o1.getCreateDate().getTime()>o2.getCreateDate().getTime()){
					return 1;
				}
				return -1;
			}
		});
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "获取分享历史成功" );
		result.put(MSG_DATA, toList);
		return result;
	}
	
	/**
	 *  获取公开分享
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("openshare")
	public String openShare(ModelMap m) throws Exception{
		log.info("获取公开分享开始");
		ShareQuery qeury=new ShareQuery();
		qeury.setType(1);
		List<Share> shares=this.shareService.findShareByCondition(qeury);
		m.addAttribute("shares", shares);
		return "/portal/index";
	}
	
}
