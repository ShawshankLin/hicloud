package cn.edu.cylg.cis.hicloud.controller.api.drive;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.service.drive.ShareService;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

@Controller("ApiShareCtrl")
@RequestMapping("/api/share")
public class ShareController implements WebConstants{

	
	private static final Log log = LogFactory.getLog(FileController.class);
	
	@Resource(name="fileService")
	private FileService fileService;
	@Resource(name="userService")
	private UserService userService;
	@Resource(name="shareService")
	private ShareService shareService;
	
	/**
	 *  分享好友文件（只限好友分享）
	  * @param userId
	  * @param fileId
	  * @param type
	  * @param toId
	  * @param request
	  * @return
	  * @throws Exception
	 */
	@RequestMapping("/toshare")
	@ResponseBody
	public Object toShare(String userId, String fileId, String toId,HttpServletRequest request) throws Exception{
		log.info("分享文件开始");
		if(FuncUtil.isEmpty(fileId)||FuncUtil.isEmpty(toId)){
			throw new AppException("请求参数有误");
		}
		User toUser=this.userService.get(User.class, toId);
		if(toUser==null){
			throw new AppException("分享者不存在");
		}
		Share share=new Share();
		share.setType(3);
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS );
		result.put(MSG_INFO, "分享文件成功" );
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
	public Object getShareHistory(String userId,String toId) throws Exception{
		log.info("获取分享历史开始");
		if(FuncUtil.isEmpty(toId)){
			throw new AppException("请求参数有误");
		}
		User toUser=this.userService.get(User.class, toId);
		if(toUser==null){
			throw new AppException("分享者不存在");
		}
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
}
