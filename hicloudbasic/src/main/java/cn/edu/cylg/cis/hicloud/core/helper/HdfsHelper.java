package cn.edu.cylg.cis.hicloud.core.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

public class HdfsHelper {

	private static final Log log = LogFactory.getLog(HdfsHelper.class);
	private static FileSystem fs;
	private static Configuration conf;
	
	static{
		conf = new Configuration();
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	  * 判断文件是否存在
	  * @param dir
	  * @return
	  * @throws Exception
	 */
	public static boolean exists(String dir) throws Exception{
		if (fs.exists(new Path(dir))) {
			return true;
		}
		return false;
	}

	/**
	 * 创建文件夹
	 * @param dir 相对根目录路径
	 * @throws Exception
	 */
	public static void mkdir(String dir) throws Exception {
		if (!fs.exists(new Path(dir))) {
			fs.mkdirs(new Path(dir));
		}
	}
	
	/**
	 * 上传文件
	 * @param filePath 
	 * @param dir
	 * @throws Exception
	 */
	public static void upload(String localPath, String hdfsPath) throws Exception {
		log.info("上传开始");
		long startTime=new Date().getTime();
		InputStream in = new BufferedInputStream(new FileInputStream(localPath));
		OutputStream out = fs.create(new Path(hdfsPath), new Progressable() {

			@Override
			public void progress() {
				//System.out.println("ok");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
		long endTime=new Date().getTime();
		log.info("上传结束，耗时"+(endTime-startTime)+"s");
	}
	/**
	 * 已流形式上传
	 * @param in
	 * @param dir
	 * @throws Exception
	 */
	public static void upload(InputStream in, String dir) throws Exception {
		log.info("文件上传hdfs开始");
		long startTime=System.currentTimeMillis();
		OutputStream out = fs.create(new Path(dir), new Progressable() {
			@Override
			public void progress() {
				//System.out.println("ok");
			}
		});
		IOUtils.copyBytes(in, out, 4096, true);
		long endTime=System.currentTimeMillis();
		log.info("文件上传hdfs结束，耗时"+(endTime-startTime)+"s");
	}

	
	/**
	 *  下载文件
	  * @param srcPath
	  * @param dstPath
	  * @throws Exception
	 */
	public static void downloadFile(String srcPath, String dstPath) throws Exception{
		File dstDir = new File(dstPath.substring(0, dstPath.lastIndexOf("/")+1));
		if (!dstDir.exists()){
			dstDir.mkdirs();
		}
		FSDataInputStream in = null;
		FileOutputStream out = null;
		try{
			in = fs.open(new Path(srcPath));
			out = new FileOutputStream(dstPath);
			IOUtils.copyBytes(in, out, 4096, false);
		}finally{
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
	}
	
	/**
	 * 	文件流下载文件
	  * @param srcPath
	  * @param response
	  * @param fileName
	  * @throws Exception
	 */
	public static void downloadFile(final HttpServletResponse response,String srcPath,String fileName) throws Exception{
		FSDataInputStream in = null;
		OutputStream out =null;
		try{
			in = fs.open(new Path(srcPath));
		    response.reset();  
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + new String( fileName.getBytes("utf-8"), "ISO8859-1" ) + "\"");  
		    response.setContentType("application/octet-stream;charset=UTF-8"); 
		    out = new BufferedOutputStream(response.getOutputStream());  
		    IOUtils.copyBytes(in, out, 4096, false);
		    response.flushBuffer();
		}finally{
			IOUtils.closeStream(in);
			IOUtils.closeStream(out);
		}
	}
	
	/**
	 *  断点下载
	  * @param request
	  * @param response
	  * @param srcPath
	  * @param fileName
	  * @throws Exception
	 */
	public static void downloadFile(final HttpServletRequest request,final HttpServletResponse response,String srcPath,String fileName)throws Exception{
		FSDataInputStream in = null;
	    ServletOutputStream out = null;  
	    try {  
	    	in = fs.open(new Path(srcPath));  
	        long fSize = fileSize(srcPath);    
	        response.setCharacterEncoding("utf-8");  
	        response.setContentType("application/x-download");    
	        response.setHeader("Accept-Ranges", "bytes");    
	        response.setHeader("Content-Length", String.valueOf(fSize));    
	        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);  
	        
	        long pos = 0;    
	        if (null != request.getHeader("Range")) {  
	            // 断点续传  
	            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);    
	            try {    
	                pos = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));    
	            } catch (NumberFormatException e) {  
	                pos = 0;    
	            }    
	        }    
	        out = response.getOutputStream();    
	        String contentRange = new StringBuffer("bytes ").append(pos+"").append("-").append((fSize - 1)+"").append("/").append(fSize+"").toString();  
	        response.setHeader("Content-Range", contentRange);    
	        in.seek(pos);
	        IOUtils.copyBytes(in, out, 4096, false);
	    } catch (Exception e) {  
	    	log.error(e.getMessage());  
	    }finally{  
	    	IOUtils.closeStream(in);
			IOUtils.closeStream(out);
	    } 
	}
	
	/**
	 *  下载文件夹
	  * @param srcPath
	  * @param dstPath
	  * @throws Exception
	 */
	public static void downloadFolder(String srcPath, String dstPath) throws Exception{
		File dstDir = new File(dstPath);
		if (!dstDir.exists()){
			dstDir.mkdirs();
		}
		FileStatus[] srcFileStatus = fs.listStatus(new Path(srcPath));
		Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
		for (int i = 0; i < srcFilePath.length; i++){
			String srcFile = srcFilePath[i].toString();
			int fileNamePosi = srcFile.lastIndexOf('/');
			String fileName = srcFile.substring(fileNamePosi + 1);
			download(srcPath + '/' + fileName, dstPath + '/' + fileName);
		}
	}
	
	/**
	 *  下载
	  * @param srcPath
	  * @param dstPath
	  * @throws Exception
	 */
	public static void download(String srcPath, String dstPath) throws Exception{
		log.info("下载开始");
		long startTime=System.currentTimeMillis();
		if (fs.isFile(new Path(srcPath))){
			downloadFile(srcPath, dstPath);
		}else{
			downloadFolder(srcPath, dstPath);
		}
		long endTime=System.currentTimeMillis();
		log.info("下载结束，耗时"+(endTime-startTime)+"s");
	}
	
	
	/**
	 * 重命名文件
	 * @param src
	 * @param dst
	 * @throws Exception
	 */
	public static void rename(String src,String dst) throws Exception {
		fs.rename(new Path(src), new Path(dst));
	}
	
	/**
	 * 删除文件及文件夹
	 * @param name
	 * @throws Exception
	 */
	public static void delete(String name) throws Exception {
		fs.delete(new Path(name), true);
	}
	
	/**
	 * 移动或复制文件
	 * @param path
	 * @param dst
	 * @param src true 移动文件;false 复制文件
	 * @throws Exception
	 */
	public static void copy(String[] path, String dst,boolean src) throws Exception {
		Path[] paths = new Path[path.length];
		for (int i = 0; i < path.length; i++) {
			paths[i]=new Path(path[i]);
		}
		FileUtil.copy(fs, paths, fs, new Path(dst), src, true, conf);
	}
	
	/**
	 *  返回文件大小
	  * @param srcPath
	  * @return
	  * @throws Exception
	 */
	public static long fileSize(String srcPath) throws Exception{
		return fs.getFileStatus(new Path(srcPath)).getLen();
	}
	
	public static void main(String[] args) throws Exception {
		fileSize("/hicloud/drive/2c8a91fe541fe7d001541fef5f130000/8a872f7d6fe642bf86626a1e21507a98.doc");
		//mkdir(ROOT);

		 //String path = "/home/hadoop/下载/hive2.txt";
		 //hdfsDB.upload(path, "weir/"+"hive2.txt");
		/*List<FileSystemVo> files=hdfsDB.queryAll(ROOT+"user/hadoop/inputs");
		for(int i=0;i<files.size();i++){
			System.out.println(files.get(i).toString());
		}*/
//		hdfsDB.visitPath("hdfs://h1:9000/weir");
//		for (Menu menu : menus) {
//			System.out.println(menu.getName());
//			System.out.println(menu.getPname());
//		}
//		hdfsDB.delete("weirqq");
//		hdfsDB.mkdir("/weirqq");
		/*List<Menu>	menus=hdfsDB.tree("/");
		for(int i=0;i<menus.size();i++){
			System.out.println(menus.get(i).toString());
		}*/
		
		/*Path path=new Path(ROOT+"/402867815302c1fe015302c395a90000/test");
		System.out.println(fs.isFile(path));*/
		System.out.println("ok");
	}
}
