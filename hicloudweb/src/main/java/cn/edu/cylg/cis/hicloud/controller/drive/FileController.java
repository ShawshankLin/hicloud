package cn.edu.cylg.cis.hicloud.controller.drive;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.edu.cylg.cis.hicloud.core.cache.DataConverter;
import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.HdfsHelper;
import cn.edu.cylg.cis.hicloud.core.helper.LoginHelper;
import cn.edu.cylg.cis.hicloud.core.plugins.tree.TreeNode;
import cn.edu.cylg.cis.hicloud.entity.FileInfo;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.utils.DocConverter;
import cn.edu.cylg.cis.hicloud.utils.FileDownload;
import cn.edu.cylg.cis.hicloud.utils.FileUtil;
import cn.edu.cylg.cis.hicloud.utils.FileZip;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.GsonBuilderUtil;
import cn.edu.cylg.cis.hicloud.utils.Md5CaculateUtil;
import cn.edu.cylg.cis.hicloud.utils.PathUtil;
import cn.edu.cylg.cis.hicloud.utils.PropUtil;
import cn.edu.cylg.cis.hicloud.utils.PushInteface;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;

import com.google.gson.Gson;
/**
 * 文件上传ctrl
 * @author  Shawshank
 * @version 1.0
 * 2016年2月12日 创建文件
 */
@Controller
@RequestMapping("/myfile")
public class FileController implements WebConstants{
	
	protected static final Log log = LogFactory.getLog(FileController.class);

	@Resource(name="fileService")
	private FileService fileService;
	
	private static final String CACHE_PATH = PathUtil.getClasspath()+PropUtil.readValue("cachePath");
	private static final String HDFS_PATH = PropUtil.readValue("hdfsPath");
	
	
	/** 显示文件信息*/
	@RequestMapping
	public String index(@RequestParam(defaultValue="")String path,@RequestParam(defaultValue="")String currentPath,
			@CookieValue(value="STYLE")String style,ModelMap m) throws Exception{
		String userId=LoginHelper.getUserId();
		List<cn.edu.cylg.cis.hicloud.entity.File> files=fileService.listFiles(userId, path);
		m.addAttribute("files",files);
		m.addAttribute("path",path);
		m.addAttribute("currentPath",currentPath);
		return "/drive/drive-info-"+(style!=null?style:DEFAULT_VIEW);
	}
	
	 /** 搜索文件*/
    @RequestMapping("/search")
    public String search(String fileName,String suffix,@CookieValue(value="STYLE")String style,ModelMap m) throws Exception{
    	if(FuncUtil.notEmpty(suffix)){
    		m.addAttribute("type", suffix);
    		suffix=DataConverter.getFileTypeByKey(suffix);
    	}
    	String userId=LoginHelper.getUserId();
    	FileQuery query = new FileQuery();
    	query.setCreateId(userId);
    	query.setName(fileName);
    	query.setSuffix(suffix);
    	query.setStatus("active");
		List<cn.edu.cylg.cis.hicloud.entity.File> files=fileService.searchFiles(query);
		m.addAttribute("files",files);
		m.addAttribute("fileName",fileName);
    	return "/drive/drive-search-"+(style!=null?style:DEFAULT_VIEW);
    }
    
    
    /** 大文件上传 */
  	@RequestMapping(value = "upload", method = RequestMethod.POST)
  	@ResponseBody
  	public String uploadFile(String opt, @RequestParam(defaultValue="")String path, FileInfo fileInfo, HttpServletRequest request) throws Exception{
  		if (request instanceof MultipartHttpServletRequest) {
  			MultipartFile file=((MultipartHttpServletRequest) request).getFile("file");
  			if(file != null && !file.isEmpty()){	//验证请求不会包含数据上传，所以避免NullPoint这里要检查一下file变量是否为null
  				try {
  					String userId=LoginHelper.getUserId();
  					StringBuilder uploadDir=new StringBuilder().append(CACHE_PATH).append("/").append(userId);
  					File target = fileService.getReadySpace(fileInfo, uploadDir.toString());	//为上传的文件准备好对应的位置
  					if(target == null){
  						return "{\"status\": 0, \"message\": \"初始化缓存空间失败\"}";
  					}
  					file.transferTo(target);	//保存上传文件
  					if(fileInfo.getChunks() <= 0){//单文件块上传
  						StringBuilder hdfsPath=new StringBuilder().append(HDFS_PATH).append("/").append(userId);
  			            HdfsHelper.mkdir(hdfsPath.toString());//为上传的文件准备好对应的位置
  			            String hdfsFileName = UuidUtil.get32UUID().replace( "-", "" )
  							      .concat( "." )
  							      .concat( FileUtil.getSuffix(fileInfo.getName()).toLowerCase());
  				        String hdfsFile=hdfsPath.append("/").append(path).append("/").append(hdfsFileName).toString();
  						try {	
  					        log.info("上传本地文件 "+fileInfo.getName()+"==>> hdfs文件" +hdfsFileName);
  							HdfsHelper.upload(target.getAbsolutePath(), hdfsFile);
  							log.info("上传文件至hdfs成功");
  							log.info("文件入库信息");
  							log.info("文件所有者"+userId);
  							String fileName=FileUtil.getFileName(hdfsFileName);
  							log.info("文件名："+fileName);
  							String name=FileUtil.getFileName(fileInfo.getName());
  							log.info("文件原名："+name);
  							long fileSize=Long.valueOf(fileInfo.getSize());
  							log.info("文件大小："+fileSize);
  							String md5=Md5CaculateUtil.getHash(target.getAbsolutePath(), "MD5");
  							log.info("文件md5值："+md5);
  							String suffix=FileUtil.getSuffix(fileInfo.getName()).toLowerCase();
  							log.info("文件后缀名："+suffix);
  							fileService.createFile(userId, fileName, name, fileSize, md5, path, hdfsFile, suffix, 0);		
  							log.info("文件入库成功");
  						} catch(AppException a){
  							log.info("文件上传失败");
  							if(HdfsHelper.exists(hdfsPath.toString())){
  								HdfsHelper.delete(hdfsPath.toString());
  								log.info("清空hdfs上传失败文件");
  							}
  							throw new AppException(a.getMessage());
  						} catch (Exception e) {
  							log.info("文件上传失败");
  							if(HdfsHelper.exists(hdfsPath.toString())){
  								HdfsHelper.delete(hdfsPath.toString());
  								log.info("清空hdfs上传失败文件");
  							}
  							throw new Exception(e);
  						}finally{
  							if(target.exists()){
  								target.delete();
  								log.info("清空服务器缓存目录文件");
  							}
  						}
  					}
  					return "{\"status\": 1, \"path\": \"" + target.getName() + "\"}";

  				}catch(IOException ex){
  					log.error("数据上传失败", ex);
  					return "{\"status\": 0, \"message\": \"数据上传失败\"}";
  				}
  			}
  	    } else {
  	    	if("md5Check".equals(opt)){	
				//验证当前目录是否存在相同文件
  	    		FileQuery query = new FileQuery();
				String userId=LoginHelper.getUserId();
  	    		query.setCreateId(userId);
  	          	query.setName(fileInfo.getName());
  	          	query.setParentPath(path);
  	          	List<cn.edu.cylg.cis.hicloud.entity.File> files=this.fileService.searchFiles(query);
  	    		if(files!=null&&files.size()>0){
  	          		log.info("当前目录存在同名文件");
  	          		boolean flat=true;
  	          		for(int i=0;i<files.size();i++){
  	          			cn.edu.cylg.cis.hicloud.entity.File ff=files.get(i);
  	          			log.info("判断文件MD5值是否相同");
  	          			if(ff!=null){
  	  	          			if(!ff.getMD5().equals(fileInfo.getMd5())){
  	  	    	        		flat=false;
  	  	          			}
  	          			}
  	          		}
  	          		if(flat){
  	          			log.info("同名文件无作修改，无需上传");
  	          			return "{\"ifExist\": 1, \"path\": \"8888\"}";
  	          		}
  	    		}
  	    		//秒传验证
  	    		cn.edu.cylg.cis.hicloud.entity.File file = fileService.md5Check(fileInfo.getMd5());
				if(file == null){
					return "{\"ifExist\": 0}";
				}else{
					String fileName=UuidUtil.get32UUID();
					this.fileService.createFile(userId, fileName, file.getName(), file.getFileSize(), file.getMD5(), path, file.getRef(), file.getSuffix().toLowerCase(), 0);
					return "{\"ifExist\": 1, \"path\": \"" + file.getRef() + "\"}";
				}
			}else if("chunkCheck".equals(opt)){	//分块验证
				String userId=LoginHelper.getUserId();
				StringBuilder uploadDir=new StringBuilder().append(CACHE_PATH).append("/").append(userId).append("/").append(fileInfo.getName()).append("/").append( fileInfo.getChunkIndex());
				//检查目标分片是否存在且完整
				if(fileService.chunkCheck(uploadDir.toString(), Long.valueOf(fileInfo.getSize()))){
					return "{\"ifExist\": 1}";
				}else{
					return "{\"ifExist\": 0}";
				}
			}else if("chunksMerge".equals(opt)){	//分块合并
				String userId=LoginHelper.getUserId();
				StringBuilder uploadDir=new StringBuilder().append(CACHE_PATH).append("/").append(userId);
				String ppath = fileService.chunksMerge(fileInfo.getName(), fileInfo.getExt(), fileInfo.getChunks(), fileInfo.getMd5(), uploadDir.toString());
				if(ppath == null){
					return "{\"status\": 0, \"message\": \"文件合并失败\"}";
				}
				StringBuilder localPath=new StringBuilder().append(CACHE_PATH).append("/").append(userId).append("/").append(ppath);
				StringBuilder hdfsPath=new StringBuilder().append(HDFS_PATH).append("/").append(userId);
	            HdfsHelper.mkdir(hdfsPath.toString());//为上传的文件准备好对应的位置
	            String hdfsFileName = UuidUtil.get32UUID().replace( "-", "" )
					      .concat( "." )
					      .concat( fileInfo.getExt() );
		        String hdfsFile=hdfsPath.append("/").append(hdfsFileName).toString();
				try {	
			        log.info("上传本地文件 "+fileInfo.getName()+"==>> hdfs文件" +hdfsFileName);
					HdfsHelper.upload(localPath.toString(), hdfsFile);
					log.info("上传文件至hdfs成功");
					fileService.createFile(userId, FileUtil.getFileName(hdfsFileName), FileUtil.getFileName(fileInfo.getRealName()), Long.valueOf(fileInfo.getSize()), fileInfo.getMd5(), path, hdfsFile, fileInfo.getExt().toLowerCase(), 0);		
					log.info("文件入库成功");
				} catch(AppException a){
					if(HdfsHelper.exists(hdfsPath.toString())){
						HdfsHelper.delete(hdfsPath.toString());
						log.info("清空hdfs上传失败文件");
					}
					throw new AppException(a.getMessage());
				} catch (Exception e) {
					if(HdfsHelper.exists(hdfsPath.toString())){
						HdfsHelper.delete(hdfsPath.toString());
						log.info("清空hdfs上传失败文件");
					}
					throw new Exception(e);
				}finally{
					File cacheFile=new File(localPath.toString());
					if(cacheFile.exists()){
						cacheFile.delete();
						log.info("清空服务器缓存目录文件");
					}
				}
				return "{\"status\": 1, \"path\": \"" + ppath + "\", \"message\": \"中文测试\"}";
			}
  	    }
  		log.error("请求参数不完整");
  		return "{\"status\": 0, \"message\": \"请求参数不完整\"}";
  	}
	
	
	
	/** 新建文件夹*/
	@RequestMapping("/mkdir")
	@ResponseBody
	public Object createDir(@RequestParam(defaultValue="")String path,String dirName,HttpServletRequest request) throws Exception{
		log.info("创建文件夹开始");
		dirName=FuncUtil.isEmpty(dirName)?"新建文件夹":dirName.trim();
		path=FuncUtil.isEmpty(path)?"":path.trim();
		String userId=LoginHelper.getUserId();
		try {
			StringBuilder dirPath=new StringBuilder();
			String fileName=UuidUtil.get32UUID();
			dirPath.append(HDFS_PATH).append("/").append(userId).append(path).append("/").append(fileName);
			log.info("创建hdfs文件夹");
			HdfsHelper.mkdir(dirPath.toString());
			cn.edu.cylg.cis.hicloud.entity.File file=fileService.createDir(userId, fileName, dirName, dirPath.toString(),path);
			Map<String,Object> msgMap=new HashMap<String, Object>();
			Gson gson = GsonBuilderUtil.create(); 
			msgMap.put(MSG_TYPE, MessageType.FOLDER_CREATE);
			msgMap.put(MSG_DATA, file);
			log.info("发送个推通知");
			log.info("发送通知报文"+gson.toJson(msgMap));
			PushInteface.pushToSinglebytran(userId, gson.toJson(msgMap)); 
		} catch(AppException a){
			throw new AppException("创建文件夹失败");
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "创建文件夹成功");
		log.info("创建文件夹结束");
		return result;
	}
	
	/**
     * 上一级目录.
     */
    @RequestMapping("/parentDir")
    public String parentDir(String path,String currentPath) throws Exception {
        if (FuncUtil.isEmpty(path)) {
            return "redirect:/myfile";
        }
        String parentPath = path.trim().substring(0, path.lastIndexOf("/"));
        String currentparentPath = currentPath.trim().substring(0, currentPath.lastIndexOf("/"));
        return "redirect:/myfile?path=" + parentPath+"&currentPath="+currentparentPath;
    }
    
    /**
     * 下载文件
     */
    @RequestMapping("/download")
    public void download(@RequestParam("fileId")String fileId,HttpServletRequest request,HttpServletResponse response,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(fileId))
    		throw new AppException("请求参数有误");
    	String userId=LoginHelper.getUserId();
    	String[] fileIds=fileId.split(",");
		if(fileIds!=null&&fileIds.length>0){
			if(fileIds.length==1){//单选文件下载
				cn.edu.cylg.cis.hicloud.entity.File file = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
				if(this.fileService.hasDownloadPerm(userId,fileId)){
					if(file.getIsDir()==1){//文件夹打包下载
		    			String hdfsPath = file.getRef();
		    			StringBuilder localPath = new StringBuilder().append(CACHE_PATH).append("/").append(UuidUtil.get32UUID());
		    			log.info("下载hdfs文件夹"+file.getFileName()+"==>> 本地目录"+localPath);
		    			HdfsHelper.download(hdfsPath, localPath.append("/").append(file.getFileName()).toString());
		    			List<cn.edu.cylg.cis.hicloud.entity.File> files = fileService.getFileTree(file.getCreateId(),file.getId());
		    			for(int j=0;j<files.size();j++){
		    				if(files.get(j).getIsDir()==0){
		    					StringBuilder targetPath=new StringBuilder().append(localPath).append("/").append(files.get(j).getFileName()).append(".").append(files.get(j).getSuffix());
		    					File f=new File(targetPath.toString());
		        				StringBuilder newName=new StringBuilder().append(localPath).append("/").append(files.get(j).getName()).append(".").append(files.get(j).getSuffix());
		        				f.renameTo(new File(newName.toString()));//更正文件名
		    				}else{
		    					StringBuilder targetPath=new StringBuilder().append(localPath).append("/").append(files.get(j).getFileName());
		    					File f=new File(targetPath.toString());
		        				StringBuilder newName=new StringBuilder().append(localPath).append("/").append(files.get(j).getName());
		        				f.renameTo(new File(newName.toString()));//更正文件夹名
		    				}
		    			}
		        		String zipPath=localPath+".zip";
		        		log.info("压缩文件夹"+localPath+"==>> 目录"+zipPath);
		        		FileZip.zip(localPath.toString(), zipPath);
		        		FileDownload.fileDownload(response, zipPath, file.getFileName()+".zip");
		        		log.info("下载压缩文件成功");
		        		FileUtil.delFile(zipPath);
		        		FileUtil.delFile(localPath.toString());
		        		log.info("清除服务器缓存文件成功");
		        	}else{//单个文件下载
		        		String hdfsPath=file.getRef();
		        		HdfsHelper.downloadFile(response, hdfsPath, file.getName()+"."+file.getSuffix());
		        	}
				}else{
					throw new AppException("无文件下载权限");
				}
			}else{//多选文件下载
				String localPath = CACHE_PATH+"/"+UuidUtil.get32UUID();
				for(int i=0;i<fileIds.length;i++){
					if(this.fileService.hasDownloadPerm(userId, fileIds[i])){
						cn.edu.cylg.cis.hicloud.entity.File file = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileIds[i]);
	    				String hdfsPath = file.getRef();
	    				log.info("下载hdfs "+file.getFileName()+"==>> 本地目录"+localPath);
	    				if(file.getIsDir()==1){//文件夹打包下载
	    	    			HdfsHelper.download(hdfsPath, localPath+"/"+file.getFileName());
	    	    			List<cn.edu.cylg.cis.hicloud.entity.File> files = fileService.getFileTree(file.getCreateId(),file.getId());
	    	    			for(int j=0;j<files.size();j++){
	    	    				if(files.get(j).getIsDir()==0){
	    	    					StringBuilder targetPath=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getFileName());
	    	    					File f=new File(targetPath.toString());
	    	        				StringBuilder newName=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getName()).append(".").append(files.get(j).getSuffix());
	    	        				f.renameTo(new File(newName.toString()));//更正文件名
	    	    				}
	    	    			}
	    	        	}else{
	    	        		HdfsHelper.download(hdfsPath, localPath+"/"+file.getFileName());
	    	        		StringBuilder targetPath=new StringBuilder();
	    	        		targetPath.append(localPath).append(file.getParentPath()).append("/").append(file.getFileName());
	    	        		File f=new File(targetPath.toString());
	    	        		StringBuilder newName=new StringBuilder();
	        				newName.append(localPath).append(file.getParentPath()).append("/").append(file.getName()).append(".").append(file.getSuffix());
	        				f.renameTo(new File(newName.toString()));//更正文件名
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
        		FileUtil.delFile(localPath);
        		log.info("清除服务器缓存文件");
			}	
		}
    }
    
    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteFile(@RequestParam("fileId")String fileId,String path,HttpServletRequest request) throws Exception{
    	log.info("删除文件开始");
    	Map<String,Object> result=new HashMap<String,Object>();
    	if(FuncUtil.isEmpty(fileId)){
			throw new AppException("文件不允许为空");
		}
    	try {	
    		String userId = LoginHelper.getUserId();
    		fileService.deleteFiles(userId,fileId);
			Map<String,Object> msgMap=new HashMap<String, Object>();
			Gson gson = GsonBuilderUtil.create(); 
			msgMap.put(MSG_TYPE, MessageType.FILE_DEL);
			msgMap.put(MSG_DATA, fileId);
			log.info("发送个推通知");
			log.info("发送通知报文"+gson.toJson(msgMap));
			PushInteface.pushToSinglebytran(userId, gson.toJson(msgMap)); 
		} catch(AppException a){
			throw new AppException("删除文件失败");
		} catch (Exception e) {
			throw new Exception(e);
		}
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");
		result.put(MSG_TARGET, request.getContextPath()+"/myfile?path="+path);
		log.info("删除文件结束");
    	return result;
    }
    
    /**
     * 重命名.
     */
    @RequestMapping("/rename")
    @ResponseBody
    public Object renameFile(@RequestParam("fileId") String fileId,@RequestParam("newname") String newname,HttpServletRequest request) throws Exception{
    	log.info("重命名文件开始");
    	if(FuncUtil.isEmpty(fileId)){
    		throw new AppException("文件不允许为空");
    	}
    	cn.edu.cylg.cis.hicloud.entity.File oldFile = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
    	String userId = LoginHelper.getUserId();
		if(!oldFile.getCreateId().equals(userId)){
			throw new AppException("无文件权限");
		}
		if(oldFile.getName().equals(newname)){
			throw new AppException("修改文件名与原文件名相同");
		}
		StringBuilder newRef=new StringBuilder();
    	try {
    		if(oldFile.getIsDir()==1){
    			oldFile.setName(newname);
    			this.fileService.update(oldFile);
    			log.info("重命名文件夹成功");
    		}else{
    	    	String newFileName=UuidUtil.get32UUID();
    			newRef.append(oldFile.getRef().substring(0, oldFile.getRef().lastIndexOf("/")+1)).append(newFileName).append(".").append(oldFile.getSuffix());
    			HdfsHelper.copy(new String[]{oldFile.getRef()}, newRef.toString(), false);//复制文件
    			cn.edu.cylg.cis.hicloud.entity.File newFile = new cn.edu.cylg.cis.hicloud.entity.File();
        		newFile.setFileName(newFileName);
        		newFile.setName(newname);
        		newFile.setRef(newRef.toString());
        		this.fileService.changeFileVersion(oldFile,newFile);
        		log.info("重命名文件成功");
    		}
			Map<String,Object> msgMap=new HashMap<String, Object>();
			Gson gson = GsonBuilderUtil.create(); 
			msgMap.put(MSG_TYPE, MessageType.FILE_RENAME);
			Map<String,String> dataMap=new HashMap<String,String>();
			dataMap.put("fileId", fileId);
			dataMap.put("newname", newname);
			msgMap.put(MSG_DATA, dataMap);
			log.info("发送个推通知");
			log.info("发送通知报文"+gson.toJson(msgMap));
			PushInteface.pushToSinglebytran(userId, gson.toJson(msgMap)); 
			log.info("发送通知成功");
		} catch(AppException a){
			if(HdfsHelper.exists(newRef.toString())){
				HdfsHelper.delete(newRef.toString());
				log.info("删除操作失败文件");
			}
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			if(HdfsHelper.exists(newRef.toString())){
				HdfsHelper.delete(newRef.toString());
				log.info("删除操作失败文件");
			}
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "重命名文件成功");
		result.put(MSG_TARGET, request.getContextPath()+"/myfile?path="+oldFile.getParentPath());
		log.info("重命名文件结束");
    	return result;
    }
    
    /**
     * 回收站
     */
    @RequestMapping("/trash")
    public String trash(@CookieValue(value="STYLE")String style,ModelMap m) throws Exception{
    	FileQuery query = new FileQuery();
    	query.setStatus("invalid");
    	List<cn.edu.cylg.cis.hicloud.entity.File> files=this.fileService.searchFiles(query);
    	m.addAttribute("files",files);
    	m.addAttribute("style",style);
    	return "/drive/drive-trash-"+(style!=null?style:DEFAULT_VIEW);
    }
    
    /**
     * 文件还原
     */
    @RequestMapping("/restore")
    @ResponseBody
    public Object restoreFile(@RequestParam("fileId")String fileId,HttpServletRequest request,ModelMap m) throws Exception{
    	if(FuncUtil.isEmpty(fileId)){
    		throw new AppException("文件不允许为空");
    	}
		String userId=LoginHelper.getUserId();
    	this.fileService.updateFileForActive(userId,fileId);
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
    	result.put(MSG_INFO, "还原文件成功");
    	result.put(MSG_TARGET, request.getContextPath()+"/myfile/trash");
    	return result;
    }
    
    /**
     * 清空回收站
     */
    @RequestMapping("/emptyTrash")
    @ResponseBody
    public Object emptyTrash(@RequestParam("fileId")String fileId,HttpServletRequest request) throws Exception{
    	if(FuncUtil.isEmpty(fileId)){
    		throw new AppException("文件不允许为空");
    	}
    	String userId=LoginHelper.getUserId();
		if(fileId.equals("all")){
			FileQuery query = new FileQuery();
			query.setCreateId(userId);
			query.setStatus("invalid");//查找全部失效文件
			List<cn.edu.cylg.cis.hicloud.entity.File> invalidFiles=this.fileService.searchFiles(query);
			if(invalidFiles!=null&&invalidFiles.size()>0){
				for(int i=0;i<invalidFiles.size();i++){
					cn.edu.cylg.cis.hicloud.entity.File file=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class,invalidFiles.get(i).getId());
					/*if(!file.getCreateId().equals(userId)){
	    				throw new AppException("无文件权限");
	    			}*/
	    			delComplete(file);		
				}
			}
		}else{
			String[] fileIds=fileId.split(",");
	    	if(fileIds!=null&&fileIds.length>0){
	    		for(int i=0;i<fileIds.length;i++){
	    			cn.edu.cylg.cis.hicloud.entity.File file=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class,fileIds[i]);
	    			if(!file.getCreateId().equals(userId)){
	    				throw new AppException("无文件权限");
	    			}
	    			delComplete(file);		
	    		}
	    	}
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
    	result.put(MSG_INFO, "清空回收站成功");
    	result.put(MSG_TARGET, request.getContextPath()+"/myfile/trash");
    	return result;
    }
    
    
    private void delComplete(cn.edu.cylg.cis.hicloud.entity.File file) throws Exception{
    	if(file.getIsDir()==1){
			List<cn.edu.cylg.cis.hicloud.entity.File> invalidFiles=this.fileService.getFileTree(file.getCreateId(),file.getId());
			if(invalidFiles!=null&&invalidFiles.size()>0){
				for(cn.edu.cylg.cis.hicloud.entity.File f:invalidFiles){
    				f.setStatus("lost");
    			}  
				this.fileService.update(invalidFiles);
			}
		}else{
			FileQuery query =new FileQuery();
			query.setSerialNum(file.getSerialNum());//删除历史版本
			List<cn.edu.cylg.cis.hicloud.entity.File> historyFiles=this.fileService.searchFiles(query);
			for(cn.edu.cylg.cis.hicloud.entity.File f:historyFiles){
				if(HdfsHelper.exists(f.getRef())){
					f.setStatus("lost");
    			} 
			}  
			this.fileService.update(historyFiles);
		}
    }
    
    /**
     * 查找文件目录树
     */
    @RequestMapping("/getDirTree")
    @ResponseBody
    public Object getDirTree( @RequestParam(defaultValue="")String rootId) throws Exception {
    	List<TreeNode> nodes=new ArrayList<TreeNode>();
    	if(FuncUtil.isEmpty(rootId)){
	    	nodes.add(new TreeNode("0","0","全部文件",true,true,true));
		}
		String userId=LoginHelper.getUserId();
		List<cn.edu.cylg.cis.hicloud.entity.File> fileList=this.fileService.getDirTree(userId, rootId, 1);
    	if(fileList!=null&&fileList.size()>0){
    		for(int i=0;i<fileList.size();i++){
    			cn.edu.cylg.cis.hicloud.entity.File file=fileList.get(i);
    			if(file.getIsDir()==1){
    				nodes.add(new TreeNode(file.getId(),"0",file.getName(),true,false,true));
    			}else{
    				nodes.add(new TreeNode(file.getId(),"0",file.getName(),true,false,false));
    			}
    		}
    	}
    	return nodes;
    }
    
    /**
     * 文件时光机
     */
    @RequestMapping("/history")
    public String historyFile(String isPage,@RequestParam("fileId")String fileId,
    		HttpServletRequest request,ModelMap m) throws Exception{
    	if("true".equals(isPage)){
    		cn.edu.cylg.cis.hicloud.entity.File file=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
        	FileQuery query = new FileQuery();
        	query.setSerialNum(file.getSerialNum());
        	List<cn.edu.cylg.cis.hicloud.entity.File> fileList=this.fileService.searchFiles(query);
        	m.addAttribute("result", fileList);
        	m.addAttribute("file", file);
        	return "/drive/drive-history-list";
    	}
    	try {
    		cn.edu.cylg.cis.hicloud.entity.File file=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
        	FileQuery query = new FileQuery();
        	query.setSerialNum(file.getSerialNum());
        	List<cn.edu.cylg.cis.hicloud.entity.File> fileList=this.fileService.searchFiles(query);
        	for(cn.edu.cylg.cis.hicloud.entity.File f:fileList){
        		if(f.getId().equals(file.getId())){
        			f.setStatus("active");
        			continue;
        		}
        		f.setStatus("deprecated");
        	}
        	this.fileService.update(fileList);
		} catch (AppException e) {
			throw new AppException("创越到历史版本失败");
		} catch (Exception e) {
			throw new Exception("e");
		}
    	Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "成功创越到历史版本");
		result.put(MSG_TARGET, request.getContextPath()+"/myfile/history?isPage=true&fileId="+fileId);
		m.put(AJAX_MSG,new Gson().toJson(result));
		return AJAX_URL;
    }
    
    @RequestMapping("/getFileJsonData")
    @ResponseBody
	public Object getFileJsonData(@RequestParam(defaultValue="")String path) throws Exception{
    	String userId=LoginHelper.getUserId();
		List<cn.edu.cylg.cis.hicloud.entity.File> files=fileService.listFiles(userId, path);
    	return files;	
    }
    
    /**
     *  在线预览
      * @param fileId
      * @return
      * @throws Exception
     */
    @RequestMapping("/preview")
    public String previewFile(String fileId,ModelMap m,HttpServletRequest request) throws Exception{
    	if(FuncUtil.isEmpty(fileId))
    		throw new AppException("请求参数有误");
    	cn.edu.cylg.cis.hicloud.entity.File file = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
    	if(file==null)
    		throw new AppException("文件不存在");
    	String hdfsPath=file.getRef();
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
    	return "/drive/drive-info-preview";
    }
    
    /**
     * 移动文件
      * @param fileId 文件id
      * @param dirId  目录id
      * @return
      * @throws Exception
     */
    @RequestMapping("/move")
    @ResponseBody
    public Object moveFile(String fileId,String dirId,HttpServletRequest request) throws Exception{
    	if(FuncUtil.isEmpty(fileId)||FuncUtil.isEmpty(dirId))
    		throw new AppException("请求参数有误");
    	String[] fileIds=fileId.split(",");
    	if(fileIds.length>0){
    		for(int i=0;i<fileIds.length;i++){
    			cn.edu.cylg.cis.hicloud.entity.File file=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileIds[i]);
    	    	if(file==null)throw new AppException("移动文件不存在");
    	    	if("0".equals(dirId)){
    	    		file.setParentFile(null);
    	        	file.setParentPath("");
    	        	this.fileService.update(file);
    	        	log.info("更新文件位置");
    	    	}else{
    	        	cn.edu.cylg.cis.hicloud.entity.File dir=this.fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, dirId);
    	        	if(dir==null)throw new AppException("移动目录不存在");
    	        	file.setParentFile(dir);
    	        	file.setParentPath(dir.getParentPath()+"/"+dir.getFileName());
    	        	this.fileService.update(file);
    	        	log.info("更新文件位置");
    	    	}
    		}
    	}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
    	result.put(MSG_INFO, "移动文件成功");
    	result.put(MSG_TARGET, request.getContextPath()+"/myfile");
    	return result;
    }
}
