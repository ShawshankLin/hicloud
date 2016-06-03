package cn.edu.cylg.cis.hicloud.service.drive.impl;

import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.dao.drive.FileDao;
import cn.edu.cylg.cis.hicloud.dao.drive.ShareDao;
import cn.edu.cylg.cis.hicloud.dao.group.GroupDao;
import cn.edu.cylg.cis.hicloud.dao.sys.UserDao;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.FileInfo;
import cn.edu.cylg.cis.hicloud.entity.GroupFile;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.drive.FileService;
import cn.edu.cylg.cis.hicloud.utils.FileLock;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.UuidUtil;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.GroupFileQuery;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

@Service("fileService")
public class FileServiceImpl extends BaseServiceImpl implements FileService{
	
	@Autowired
	private FileDao fileDao;
	@Autowired
	private ShareDao shareDao;
	@Autowired
	private GroupDao groupDao;
	@Autowired
	private UserDao userDao;
	
	private static final Log log = LogFactory.getLog(FileServiceImpl.class);
	
	@Override
	public BaseDAO getBaseDao() {
		return fileDao;
	}

	@Override
	public File createDir(String userId, String fileName,String name, String refPath,String path)
			throws Exception {
		return this.createFile(userId, fileName, name, 0, null, path, refPath, null, 1);
	}

	@Override
	public File createFile(String userId, String fileName,String name,long size,String md5,
			String path, String refPath, String suffix, Integer isDir)
			throws Exception {
		User user=this.userDao.get(User.class,userId);
		if(user.getSpace()==null)
			throw new AppException("用户未分配存储空间");
		if((user.getUsedSpace()+size)>user.getSpace().getSpaceSize())
			throw new AppException("用户存储空间不足");
		user.setUsedSpace(user.getUsedSpace()+size);
		this.userDao.update(user);
		log.info("更新用户存储空间");
		File file=new File();
		if(FuncUtil.notEmpty(path)){
			int index=path.lastIndexOf("/");
			String parentPath=path.substring(index+1);
			String grandfatherPath=path.substring(0,index);
            FileQuery query=new FileQuery();
            query.setCreateId(userId);
            query.setFileName(parentPath);
            query.setParentPath(grandfatherPath);
            query.setStatus("active");
            query.setIsDir(1);
            List<File> parentFiles=fileDao.findFilesByCondition(query);
            if(parentFiles!=null&&parentFiles.size()>0){
            	file.setParentFile(parentFiles.get(0));
            }else{
            	file.setParentFile(null);
            }
		}else{
			file.setParentFile(null);
		}
		String serialNum="";
      	Integer version=1;
      	if(isDir==0){//查找文件版本
          	FileQuery query = new FileQuery();
          	query.setCreateId(userId);
          	query.setName(name);
          	query.setParentPath(path);
          	List<cn.edu.cylg.cis.hicloud.entity.File> files=this.searchFiles(query);
          	if(files!=null&&files.size()>0){
          		log.info("当前目录存在同名文件");
          		boolean flat=true;
          		for(int i=0;i<files.size();i++){
          			cn.edu.cylg.cis.hicloud.entity.File ff=files.get(i);
          			log.info("判断文件MD5值是否相同");
          			if(!ff.getMD5().equals(md5)){
          				log.info("更新文件历史版本");
    	        		ff.setStatus("deprecated");
    	        		serialNum=ff.getSerialNum();
    	        		flat=false;
          			}
          		}
          		if(flat){
          			log.info("同名文件无作修改，无需上传");
          			return null;
          		}
          		log.info("同名文件覆盖");
          		this.update(files);
          	}
          	if(FuncUtil.isEmpty(serialNum)){
          		serialNum=UuidUtil.get32UUID();
          	}else{
          		version=this.getMaxFileVersion(serialNum)+1;
          	}
      	}
		file.setCreateId(userId);
		file.setCreateDate(new Date());
		file.setFileName(fileName);
		file.setName(name);
		file.setFileSize(size);
		file.setRef(refPath);
		file.setStatus("active");
		file.setFileVersion(version);
		file.setSuffix(suffix);//小写后缀
		file.setParentPath(path);
		file.setIsDir(isDir);
		file.setSerialNum(serialNum);
		file.setMD5(md5);
		this.save(file);
		return file;
	}

	@Override
	public List<File> listFiles(String createId, String path) throws Exception {
		return fileDao.listFiles(createId, path);
	}

	@Override
	public List<File> searchFiles(FileQuery query) throws Exception {
		return fileDao.findFilesByCondition(query);
	}

	@Override
	public List<File> getFileTree(String createId,String rootId) throws Exception {
		// TODO Auto-generated method stub
		return fileDao.getFileTree(createId, rootId);
	}

	@Override
	public void updateFileForActive(String userId, String fileId) throws Exception {
		User user=this.userDao.get(User.class, userId);
		String[] fileIds=fileId.split(",");
		for(int i=0;i<fileIds.length;i++){
			cn.edu.cylg.cis.hicloud.entity.File file=this.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileIds[i]);
			if(file.getIsDir()==1){//目录
    			List<cn.edu.cylg.cis.hicloud.entity.File> files=this.getFileTree(userId,file.getId());
    			for(File f:files){
    				if(!f.getCreateId().equals(userId)){
    					throw new AppException("无文件权限");
    				}
    				user.setUsedSpace(user.getUsedSpace()+file.getFileSize());
    				f.setStatus("active");
    			}
    			this.userDao.update(user);
    			log.info("更新用户存储空间");
    			this.update(files);
    		}else{//文件
    			if(!file.getCreateId().equals(userId)){
					throw new AppException("无文件权限");
				}
    			user.setUsedSpace(user.getUsedSpace()+file.getFileSize());
    			this.userDao.update(user);
    			log.info("更新用户存储空间");
    			file.setStatus("active");
    			this.update(file);
    		}
		}
	}

	@Override
	public void deleteFiles(String userId,String fileId) throws Exception {
		User user=this.userDao.get(User.class, userId);
		String[] fileIds=fileId.split(",");
		for(int i=0;i<fileIds.length;i++){
			cn.edu.cylg.cis.hicloud.entity.File file = this.get(cn.edu.cylg.cis.hicloud.entity.File.class, fileIds[i]);
			if(file.getIsDir()==1){//目录
    			List<cn.edu.cylg.cis.hicloud.entity.File> files=this.getFileTree(userId,file.getId());
    			if(files!=null&&files.size()>0){
    				for(File f:files){
    					if(!f.getCreateId().equals(userId)){
    						throw new AppException("无文件权限");
    					}
    					user.setUsedSpace(user.getUsedSpace()-file.getFileSize());
    					f.setStatus("invalid");
    				}
        			this.userDao.update(user);
        			log.info("更新用户存储空间");
    				this.update(files);
    			}
    		}else{//文件
    			user.setUsedSpace(user.getUsedSpace()-file.getFileSize());
    			this.userDao.update(user);
    			log.info("更新用户存储空间");
    			file.setStatus("invalid");
    			this.update(file);
    		}
		}
		log.info("删除文件成功");
		ShareQuery query=new ShareQuery();
		query.setFileId(fileId);
		List<Share> shares=this.shareDao.findShareByCondition(query);
		this.shareDao.delete(shares);
		log.info("删除文件相关分享记录成功");
	}

	/**
	  * 修改文件版本
	  * @param oldFile
	  * @param newFile
	  * @throws Exception
	  * @see cn.edu.cylg.cis.hicloud.service.drive.FileService#changeFileVersion(cn.edu.cylg.cis.hicloud.entity.File, cn.edu.cylg.cis.hicloud.entity.File)
	 */
	@Override
	public void changeFileVersion(File oldFile,File newFile) throws Exception {
		newFile.setFileSize(oldFile.getFileSize());
		newFile.setFileVersion(oldFile.getFileVersion()+1);
		newFile.setIsDir(oldFile.getIsDir());
		newFile.setMD5(oldFile.getMD5());
		newFile.setParentFile(oldFile.getParentFile());
		newFile.setParentPath(oldFile.getParentPath());
		newFile.setStatus(oldFile.getStatus());
		newFile.setSuffix(oldFile.getSuffix());
		newFile.setDescription(oldFile.getDescription());
		newFile.setSerialNum(oldFile.getSerialNum());
		this.save(newFile);
		oldFile.setStatus("deprecated");
		this.update(oldFile);
	}

	/**
	 * 检查文件下载权限
	  * @param userId
	  * @param fileId
	  * @return
	  * @throws Exception
	  * @see cn.edu.cylg.cis.hicloud.service.drive.FileService#hasDownloadPerm(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasDownloadPerm(String userId,String fileId) throws Exception {
		log.info("验证文件下载权限");
		log.info("判断文件是否有公开分享");
		ShareQuery query = new ShareQuery();
		query.setFileId(fileId);
		query.setType(1);
		List<Share> shares=this.shareDao.findShareByCondition(query);
		if(shares!=null&&shares.size()>0){
			log.info("判断结果：文件有公开分享");
			return true;
		}
		log.info("文件无公开分享");
		log.info("判断是否是文件创建者");
		File file=this.get(File.class, fileId);
		if(file!=null){
			if(userId.equals(file.getCreateId())){
				log.info("判断结果：是文件的创建者");
				return true;
			}
		}
		log.info("判断结果：不是文件的所有者");
		log.info("判断是否好友分享");
		query.setFileId(fileId);
		query.setToId(userId);
		query.setType(3);
		shares=this.shareDao.findShareByCondition(query);
		if(shares!=null&&shares.size()>0){
			log.info("判断结果：无好友分享权限");
			return true;
		}
		return false;
	}

	/**
	 *  检查文件下载权限
	  * @param groupId
	  * @param userId
	  * @param fileId
	  * @return
	  * @throws Exception
	  * @see cn.edu.cylg.cis.hicloud.service.drive.FileService#hasDownloadPerm(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean hasDownloadPerm(String groupId,String userId, String fileId)
			throws Exception {
		log.info("判断文件是否是群文件");
		GroupFileQuery query=new GroupFileQuery();
		query.setCreateId(userId);
		query.setGroupId(groupId);
		query.setId(fileId);
		List<GroupFile> groupFiles=this.groupDao.findGroupFileByCondition(query);
		if(groupFiles!=null&&groupFiles.size()>0){
			log.info("判断结果：文件是群文件，有下载权限");
			return true;
		}
		log.info("判断结果：文件不是群文件，没有下载权限");
		return hasDownloadPerm(userId,fileId);
	}

	@Override
	public Integer getMaxFileVersion(String serialNum) throws Exception {
		return fileDao.getMaxFileVersion(serialNum);
	}
	
    /**
     * 秒传验证
     * 根据文件的MD5签名判断该文件是否已经存在
     *
     * @param key   文件的md5签名
     * @return  若存在则返回该文件的路径，不存在则返回null
     */
	@Override
	public File md5Check(String key) throws Exception {
        FileQuery query = new FileQuery();
        query.setMd5(key);
        List<File> files=fileDao.findFilesByCondition(query);
        if(files!=null&&files.size()>0){
        	File file=files.get(0);
        	return file;
        }
        return null;
	}

    /**
     * 分片验证
     * 验证对应分片文件是否存在，大小是否吻合
     * @param file  分片文件的路径
     * @param size  分片文件的大小
     * @return
     */
	@Override
	public boolean chunkCheck(String file, Long size) throws Exception {
		 //检查目标分片是否存在且完整
        java.io.File target = new java.io.File(file);
        if(target.isFile() && size == target.length()){
            return true;
        }else{
            return false;
        }
	}

	/**
     * 分片合并操作
     * 要点:
     *  > 合并: NIO
     *  > 并发锁: 避免多线程同时触发合并操作
     *  > 清理: 合并清理不再需要的分片文件、文件夹、tmp文件
     * @param folder    分片文件所在的文件夹名称
     * @param ext       合并后的文件后缀名
     * @param chunks    分片总数
     * @param md5       文件签名
     * @param path      合并后的文件所存储的位置
     * @return
     */
	@SuppressWarnings("resource")
	@Override
	public String chunksMerge(String folder, String ext, int chunks,
			String md5, String path) throws Exception {
        //合并后的目标文件
        //String target;

        //检查是否满足合并条件：分片数量是否足够
        if(chunks == this.getChunksNum(path + "/" + folder)){

            //同步指定合并的对象
            Lock lock = FileLock.getLock(folder);
            lock.lock();
            try{
                //检查是否满足合并条件：分片数量是否足够
                //File[] files = this.getChunks(path + "/" +folder);
                List<java.io.File> files = new ArrayList<java.io.File>(Arrays.asList(this.getChunks(path + "/" +folder)));
                if(chunks == files.size()){

                    //按照名称排序文件，这里分片都是按照数字命名的
                    Collections.sort(files, new Comparator<java.io.File>() {
                        @Override
                        public int compare(java.io.File o1, java.io.File o2) {
                            if(Integer.valueOf(o1.getName()) < Integer.valueOf(o2.getName())){
                                return -1;
                            }
                            return 1;
                        }
                    });

                    //创建合并后的文件
                    java.io.File outputFile = new java.io.File(path + "/" + this.randomFileName(ext));
                    if(outputFile.exists()){
                        log.error("文件[" + folder + "]随机命名冲突");
                        //this.setErrorMsg("文件随机命名冲突");
                        return null;
                    }
                    outputFile.createNewFile();
                    FileChannel outChannel = new FileOutputStream(outputFile).getChannel();

                    //合并
                    FileChannel inChannel;
                    for(java.io.File file : files){
                        inChannel = new FileInputStream(file).getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                        inChannel.close();

                        //删除分片
                        if(!file.delete()){
                            log.error("分片[" + folder + "=>" + file.getName() + "]删除失败");
                        }
                    }
                    outChannel.close();
                    files = null;

                    //将MD5签名和合并后的文件path存入持久层
                    /*if(this.saveMd52FileMap(md5, outputFile.getName())){
                        log.error("文件[" + md5 + "=>" + outputFile.getName() + "]保存关系到持久成失败，但并不影响文件上传，只会导致日后该文件可能被重复上传而已");
                    }*/

                    //清理：文件夹，tmp文件
                    this.cleanSpace(folder, path);

                    return  outputFile.getName();
                }
            }catch(Exception ex){
                log.error("数据分片合并失败", ex);
                //this.setErrorMsg("数据分片合并失败");
                return null;

            }finally {
                //解锁
                lock.unlock();
                //清理锁对象
                FileLock.removeLock(folder);
            }
        }

        //去持久层查找对应md5签名，直接返回对应path
        /*target = this.md5Check(md5);
        if(target == null){
            log.error("文件[签名:" + md5 + "]数据不完整，可能该文件正在合并中");
            //this.setErrorMsg("数据不完整，可能该文件正在合并中");
            return null;
        }*/

        //return target;
        return null;
	}

    /**
     * 将MD5签名和目标文件path的映射关系存入持久层
     * @param key   md5签名
     * @param file  文件路径
     * @return
     */
	@Override
	public boolean saveMd52FileMap(String key, String file) throws Exception {
        //todo
        return true;
	}

    /**
     * 为上传的文件创建对应的保存位置
     * 若上传的是分片，则会创建对应的文件夹结构和tmp文件
     * @param info  上传文件的相关信息
     * @param path  文件保存根路径
     * @return
     */
	@Override
	public java.io.File getReadySpace(FileInfo info, String path) throws Exception {
        //创建上传文件所需的文件夹
        if(!this.createFileFolder(path, false)){
            return null;
        }

        String newFileName;	//上传文件的新名称

        //如果是分片上传，则需要为分片创建文件夹
        if (info.getChunks() > 0) {
            newFileName = String.valueOf(info.getChunk());

            String fileFolder = this.md5(info.getUserId() + info.getName() + info.getType() + info.getLastModifiedDate() + info.getSize());
            if(fileFolder == null){
                return null;
            }

            path += "/" + fileFolder;    //文件上传路径更新为指定文件信息签名后的临时文件夹，用于后期合并

            if(!this.createFileFolder(path, true)){
                return null;
            }

        } else {
            //生成随机文件名
            newFileName = this.randomFileName(info.getName());
        }

        return new java.io.File(path, newFileName);
	}
	
	
    /**
     * 清理分片上传的相关数据
     * 文件夹，tmp文件
     * @param folder    文件夹名称
     * @param path      上传文件根路径
     * @return
     */
    private boolean cleanSpace(String folder, String path){
        //删除分片文件夹
    	java.io.File garbage = new java.io.File(path + "/" + folder);
        if(!garbage.delete()){
            return false;
        }

        //删除tmp文件
        garbage = new java.io.File(path + "/" + folder + ".tmp");
        if(!garbage.delete()){
            return false;
        }

        return true;
    }

    /**
     * 获取指定文件的所有分片
     * @param folder    文件夹路径
     * @return
     */
    private java.io.File[] getChunks(String folder){
        java.io.File targetFolder = new java.io.File(folder);
        return targetFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(java.io.File file) {
                if (file.isDirectory()) {
                    return false;
                }
                return true;
            }
        });
    }

    /**
     * 获取指定文件的分片数量
     * @param folder    文件夹路径
     * @return
     */
    private int getChunksNum(String folder){

    	java.io.File[] filesList = this.getChunks(folder);
        return filesList.length;
    }

    /**
     * 创建存放上传的文件的文件夹
     * @param file  文件夹路径
     * @return
     */
    private boolean createFileFolder(String file, boolean hasTmp) throws Exception{

        //创建存放分片文件的临时文件夹
    	java.io.File tmpFile = new java.io.File(file);
        if(!tmpFile.exists()){
            try {
                tmpFile.mkdirs();
            }catch(SecurityException ex){
                log.error("无法创建文件夹", ex);
                throw new AppException("无法创建文件夹");
            }
        }

        if(hasTmp){
            //创建一个对应的文件，用来记录上传分片文件的修改时间，用于清理长期未完成的垃圾分片
            tmpFile = new java.io.File(file + ".tmp");
            if(tmpFile.exists()){
                tmpFile.setLastModified(System.currentTimeMillis());
            }else{
                try{
                    tmpFile.createNewFile();
                }catch(IOException ex){
                    log.error("无法创建tmp文件", ex);
                    //this.setErrorMsg("无法创建tmp文件");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 为上传的文件生成随机名称
     * @param originalName  文件的原始名称，主要用来获取文件的后缀名
     * @return
     */
    private String randomFileName(String originalName){
        String ext[] = originalName.split("\\.");
        return UUID.randomUUID().toString() + "." + ext[ext.length-1];
    }

    /**
     * MD5签名
     * @param content   要签名的内容
     * @return
     */
    private String md5(String content){
        StringBuffer sb = new StringBuffer();
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes("UTF-8"));
            byte[] tmpFolder = md5.digest();

            for(int i = 0; i < tmpFolder.length; i++){
                sb.append(Integer.toString((tmpFolder[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException ex){
            log.error("无法生成文件的MD5签名", ex);
            //this.setErrorMsg("无法生成文件的MD5签名");
            return null;
        }catch(UnsupportedEncodingException ex){
            log.error("无法生成文件的MD5签名", ex);
            //this.setErrorMsg("无法生成文件的MD5签名");
            return null;
        }
    }

	@Override
	public List<File> getDirTree(String createId, String parentId, Integer isDir)
			throws Exception {
		return this.fileDao.getDirTree(createId, parentId, isDir);
	}
}
