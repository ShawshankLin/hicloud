package cn.edu.cylg.cis.hicloud.controller.api.drive;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.edu.cylg.cis.hicloud.core.cache.DataConverter;
import cn.edu.cylg.cis.hicloud.core.constants.MessageType;
import cn.edu.cylg.cis.hicloud.core.constants.WebConstants;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.core.helper.HdfsHelper;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.utils.FileDownload;
import cn.edu.cylg.cis.hicloud.utils.FileUtil;
import cn.edu.cylg.cis.hicloud.utils.FileZip;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.Md5CaculateUtil;
import cn.edu.cylg.cis.hicloud.utils.PathUtil;
import cn.edu.cylg.cis.hicloud.utils.PropUtil;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;
/**
 * 
 * @author  Shawshank
 * @version 1.0
 * 2016年3月30日 创建文件
 */
@Controller("ApiFileCtrl")
@RequestMapping("/api/myfile")
public class FileController implements WebConstants{
	
	private static final Log log = LogFactory.getLog(FileController.class);
	
	private static final String CACHE_PATH = PathUtil.getClasspath()+PropUtil.readValue("cachePath");
	private static final String HDFS_PATH = PropUtil.readValue("hdfsPath");
	
	
	@Resource(name="fileService")
	private FileService fileService;

	@RequestMapping
	@ResponseBody
	public Object index(@RequestParam("userId") String userId,@RequestParam(defaultValue="")String path) throws Exception{
		log.info("加载文件列表开始");
		Long startTime = System.currentTimeMillis();
		List<cn.edu.cylg.cis.hicloud.entity.File> files=fileService.listFiles(userId, path);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "文件列表加载成功");
		result.put(MSG_DATA, files);
		Long endTime = System.currentTimeMillis();
		log.info("加载文件列表结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	 /** 搜索文件*/
    @RequestMapping("/search")
    @ResponseBody
    public Object search(@RequestParam("userId") String userId,String fileName,String suffix,ModelMap m) throws Exception{
    	log.info("文件搜索开始");
    	Long startTime = System.currentTimeMillis();
    	if(FuncUtil.notEmpty(suffix)){
    		suffix=DataConverter.getFileTypeByKey(suffix);
    	}
    	FileQuery query = new FileQuery();
    	query.setCreateId(userId);
    	query.setName(fileName);
    	query.setSuffix(suffix);
    	query.setStatus("active");
		List<cn.edu.cylg.cis.hicloud.entity.File> files=fileService.searchFiles(query);
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "文件列表加载成功");
		result.put(MSG_DATA, files);
		Long endTime = System.currentTimeMillis();
		log.info("文件搜索结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
    }
	
	/**
     * 上一级目录.
     */
    @RequestMapping("/parentDir")
    @ResponseBody
    public Object parentDir(@RequestParam("userId") String userId,String path) throws Exception {
    	String parentPath = (FuncUtil.notEmpty(path)?path.trim().substring(0, path.lastIndexOf("/")):"");
        List<cn.edu.cylg.cis.hicloud.entity.File> files=null;
		try {
			files=fileService.listFiles(userId, parentPath);
		} catch (Exception e) {
			throw new AppException("文件列表加载失败");
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "文件列表加载成功");
		result.put(MSG_DATA, files);
		return result;
    }
	
	@RequestMapping("/mkdir")
	@ResponseBody
	public Object createDir(@RequestParam("userId") String userId,@RequestParam(defaultValue="") String path,String dirName) throws Exception{
		log.info("创建文件夹开始");
		Long startTime = System.currentTimeMillis();
		dirName=FuncUtil.isEmpty(dirName)?"新建文件夹":dirName.trim();
		path=FuncUtil.isEmpty(path)?"":path.trim();
		cn.edu.cylg.cis.hicloud.entity.File file=null;
		try {
			StringBuilder dirPath = new StringBuilder();
			String fileName=UuidUtil.get32UUID();
			dirPath.append(HDFS_PATH).append("/").append(userId).append(path).append("/").append(fileName);
			HdfsHelper.mkdir(dirPath.toString());
			file=fileService.createDir(userId, fileName, dirName, dirPath.toString(),path);
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
		Map<String,Object> result=new HashMap<String,Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "创建文件夹成功");
		result.put(MSG_DATA, file);
		Long endTime = System.currentTimeMillis();
		log.info("创建文件夹结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
	
	
	/**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object deleteFile(@RequestParam("userId")String userId,
    		@RequestParam("fileId")String fileId,HttpServletRequest request) throws Exception{
    	log.info("删除文件开始");
    	Long startTime = System.currentTimeMillis();
    	try {
    		fileService.deleteFiles(userId,fileId);
		} catch(AppException a){
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Map<String,Object> result=new HashMap<String,Object>();
    	result.put(MSG_STATUS, MessageType.SUCCESS);
		result.put(MSG_INFO, "删除成功");
		Long endTime = System.currentTimeMillis();
		log.info("删除文件结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
    	return result;
    }
    
    /**
     * 重命名.
     */
    @RequestMapping("/rename")
    @ResponseBody
    public Object renameFile(@RequestParam("userId") String userId,
    		@RequestParam("fileId") String fileId,
            @RequestParam("newname") String newname,HttpServletRequest request) throws Exception{
    	log.info("重命名文件开始");
    	Long startTime = System.currentTimeMillis();
    	if(FuncUtil.isEmpty(fileId)){
    		throw new AppException("文件不允许为空");
    	}
    	cn.edu.cylg.cis.hicloud.entity.File oldFile = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
		cn.edu.cylg.cis.hicloud.entity.File newFile = new cn.edu.cylg.cis.hicloud.entity.File();
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

        		newFile.setFileName(newFileName);
        		newFile.setName(newname);
        		newFile.setRef(newRef.toString());
        		this.fileService.changeFileVersion(oldFile,newFile);
        		log.info("重命名文件成功");
    		}
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
		result.put(MSG_DATA, newFile);
		Long endTime = System.currentTimeMillis();
		log.info("重命名文件结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
    	return result;
    }
    
    /** 批量上传文件*/
    @RequestMapping("/upload")
    @ResponseBody
	public Object uploadFile(@RequestParam("userId") String userId,
			@RequestParam("path") String path,@RequestParam MultipartFile file, HttpServletRequest request, HttpSession session) throws Exception{
		log.info("批量上传文件开始");
		Long startTime = System.currentTimeMillis();
    	String hdfsFile=null;//上传hdfs文件
    	cn.edu.cylg.cis.hicloud.entity.File f = null;
    	try {
			String fileName=file.getOriginalFilename();
			log.info("上传文件名："+fileName);
			long fileSize=file.getSize();
			log.info("上传文件大小"+(file.getSize()/1024/1024)+"MB");
			log.info("开始计算文件MD5值");
        	String md5=Md5CaculateUtil.getHash(file.getInputStream(), "MD5");
        	log.info("计算文件MD5值>>>>>"+md5);
			//创建hdfs目录
            StringBuilder hdfsPath=new StringBuilder().append(HDFS_PATH).append("/").append(userId).append(path);
            HdfsHelper.mkdir(hdfsPath.toString());
            String hdfsFileName = UUID.randomUUID().toString().replace( "-", "" )
				      .concat( "." )
				      .concat( FilenameUtils.getExtension(fileName) );
	        hdfsFile=hdfsPath.append("/").append(hdfsFileName).toString();
	        log.info("上传文件==>> hdfs");
			HdfsHelper.upload(file.getInputStream(), hdfsPath.toString());
			log.info("上传hdfs成功");//入库
			f=fileService.createFile(userId, FileUtil.getFileName(hdfsFileName), FileUtil.getFileName(fileName), fileSize, md5, path, hdfsFile, FileUtil.getSuffix(hdfsFileName), 0);
		} catch(AppException a){
			if(HdfsHelper.exists(hdfsFile)){
				log.info("删除已上传hdfs文件");
				HdfsHelper.delete(hdfsFile);
			}
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			if(HdfsHelper.exists(hdfsFile)){
				log.info("删除已上传hdfs文件");
				HdfsHelper.delete(hdfsFile);
			}
			throw new Exception("文件上传失败");
		 } 
    	Map<String, Object> result = new HashMap<String, Object>();
		result.put(MSG_STATUS, MessageType.SUCCESS );
		result.put(MSG_INFO, "文件上传成功" );
		result.put(MSG_DATA, f);
		Long endTime = System.currentTimeMillis();
		log.info("批量上传文件结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
		return result;
	}
    
    /**
     * 下载文件
     */
    @RequestMapping("/download")
    public void download(String userId,@RequestParam("fileId")String fileId,
    		HttpServletRequest request,HttpServletResponse response,ModelMap m) throws Exception{
    	log.info("下载文件开始");
		Long startTime = System.currentTimeMillis();
    	try {
    		String[] fileIds=fileId.split(",");
    		if(fileIds!=null&&fileIds.length>0){
    			if(fileIds.length==1){//单选文件下载
    				cn.edu.cylg.cis.hicloud.entity.File file = fileService.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileId);
    				if(file!=null){
    					if(file.getIsDir()==1){//文件夹打包下载
        	    			String hdfsPath = file.getRef();
        	    			StringBuilder localPath = new StringBuilder().append(CACHE_PATH).append("/").append(UuidUtil.get32UUID());
        	    			log.info("下载hdfs文件夹"+file.getFileName()+"==>> 本地目录"+localPath);
        	    			HdfsHelper.download(hdfsPath, localPath.append("/").append(file.getFileName()).toString());
        	    			List<cn.edu.cylg.cis.hicloud.entity.File> files = fileService.getFileTree(file.getCreateId(),file.getId());
        	    			for(int j=0;j<files.size();j++){
        	    				if(files.get(j).getIsDir()==0){
        	    					StringBuilder targetPath=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getFileName());
        	    					File f=new File(targetPath.toString());
        	        				StringBuilder newName=new StringBuilder().append(localPath).append(files.get(j).getParentPath()).append("/").append(files.get(j).getName()).append(".").append(files.get(j).getSuffix());
        	        				f.renameTo(new File(newName.toString()));//更正文件名
        	    				}
        	    			}
        	        		String zipPath=localPath+".zip";
        	        		log.info("压缩文件夹"+localPath+"==>> 目录"+zipPath);
        	        		FileZip.zip(localPath.toString(), zipPath);
        	        		FileDownload.fileDownload(response, zipPath, file.getFileName()+".zip");
        	        		FileUtil.delFile(zipPath);
        	        		FileUtil.delFile(localPath.toString());
        	        	}else{//单个文件下载
        	        		String hdfsPath=file.getRef();
        	        		HdfsHelper.downloadFile(request, response, hdfsPath, file.getName()+"."+file.getSuffix());
        	        	}
    				}else{
    					throw new AppException("文件不存在");
    				}
    			}else{//多选文件下载
    				String localPath = CACHE_PATH+"/"+UuidUtil.get32UUID();
    				for(int i=0;i<fileIds.length;i++){
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
        			}
    				String zipPath=localPath+".zip";
	        		log.info("压缩文件夹"+localPath+"==>> 目录"+zipPath);
	        		FileZip.zip(localPath, zipPath);
	        		FileDownload.fileDownload(response, zipPath, "hicloud.zip");
	        		FileUtil.delFile(zipPath);
	        		FileUtil.delFile(localPath);
    			}	
    		}
		} catch (AppException a) {
			throw new AppException(a.getMessage());
		} catch (Exception e) {
			throw new Exception(e);
		}
    	Long endTime = System.currentTimeMillis();
		log.info("下载文件结束,耗时["+new BigDecimal(endTime-startTime).divide(new BigDecimal(1000),3,RoundingMode.HALF_UP)+"s]");
    }
    
    
    
}
