package cn.edu.cylg.cis.hicloud.service.drive;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.File;
import cn.edu.cylg.cis.hicloud.entity.FileInfo;
import cn.edu.cylg.cis.hicloud.vo.query.FileQuery;

public interface FileService extends BaseService{

	/** 新建文件夹*/
	public File createDir(String userId, String fileName,String name, String refPath,String path) throws Exception;
	
	/**
     * 上传文件，或新建文件夹.
     */
    public File createFile(String userId,String fileName,String name, long size,String md5,
    		 String path,String refPath, String suffix, Integer isDir)throws Exception;
    
    /**
     * 显示文件
     */
    public List<File> listFiles(String createId,String path) throws Exception;
    
    /**
     * 搜索文件
     */
    public List<File> searchFiles(FileQuery query) throws Exception;
    
    /**
     * 搜索树文件
     */
    public List<File> getFileTree(String createId,String rootId) throws Exception;
    
    /**
     * 还原文件
     */
    public void updateFileForActive(String userId,String fileId) throws Exception;
    
    /**
     * 删除文件
     */
    public void deleteFiles(String userId,String fileId) throws Exception;
    
    /**
     * 重命名文件
     */
    public void changeFileVersion(File oldFile,File newFile) throws Exception;
    
    /**
     * 是否有文件下载权限
     */
    public boolean hasDownloadPerm(String userId,String fileId) throws Exception;
    
    /**
     * 群组文件下载权限判断
     */
    public boolean hasDownloadPerm(String groupId,String userId,String fileId) throws Exception;
    
    /**
     * 查询文件最大版本数
     */
    public Integer getMaxFileVersion(String serialNum) throws Exception;

    /**
     * 秒传验证
     * 根据文件的MD5签名判断该文件是否已经存在
     *
     * @param key   文件的md5签名
     * @return  若存在则返回该文件，不存在则返回null
     */
    public File md5Check(String key) throws Exception;

    /**
     * 分片验证
     * 验证对应分片文件是否存在，大小是否吻合
     * @param file  分片文件的路径
     * @param size  分片文件的大小
     * @return
     */
    public boolean chunkCheck(String file, Long size) throws Exception;

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
    public String chunksMerge(String folder, String ext, int chunks, String md5, String path) throws Exception;

    /**
     * 将MD5签名和目标文件path的映射关系存入持久层
     * @param key   md5签名
     * @param file  文件路径
     * @return
     */
    public boolean saveMd52FileMap(String key, String file) throws Exception;

    /**
     * 为上传的文件创建对应的保存位置
     * 若上传的是分片，则会创建对应的文件夹结构和tmp文件
     * @param info  上传文件的相关信息
     * @param path  文件保存根路径
     * @return
     */
    public java.io.File getReadySpace(FileInfo info, String path) throws Exception; 
    
    /**
      * 目录树
      * @param createId
      * @param parentId
      * @param isDir
      * @return
      * @throws Exception
     */
    public List<File> getDirTree(String createId,String parentId,Integer isDir) throws Exception;
    
}


