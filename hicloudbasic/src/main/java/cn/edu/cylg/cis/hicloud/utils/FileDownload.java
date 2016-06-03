package cn.edu.cylg.cis.hicloud.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 文件下载
 * @author  Shawshank
 * @version 1.0
 * 2016年4月4日 创建文件
 */
public class FileDownload {
	
	private static final Log log  = LogFactory.getLog(FileDownload.class);

	/**
	 * @param response 
	 * @param filePath		//文件完整路径(包括文件名和扩展名)
	 * @param fileName		//下载后看到的文件名
	 * @return  文件名
	 */
	public static void fileDownload(final HttpServletResponse response, String filePath, String fileName) throws Exception{  
		     
		    byte[] data = FileUtil.toByteArray2(filePath);  
		    fileName = URLEncoder.encode(fileName, "UTF-8");  
		    response.reset();  
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
		    response.addHeader("Content-Length", "" + data.length);  
		    response.setContentType("application/octet-stream;charset=UTF-8");  
		    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
		    outputStream.write(data);  
		    outputStream.flush();  
		    outputStream.close();
		    response.flushBuffer();
		    
	}
	
	
	public static void fileDownload(final HttpServletRequest request,final HttpServletResponse response,String filePath,String fileName) throws Exception{
		InputStream inputStream = null;  
	    ServletOutputStream out = null;  
	    try {  
	        File file = new File(filePath);  
	        int fSize = Integer.parseInt(String.valueOf(file.length()));    
	        response.setCharacterEncoding("utf-8");  
	        response.setContentType("application/x-download");    
	        response.setHeader("Accept-Ranges", "bytes");    
	        response.setHeader("Content-Length", String.valueOf(fSize));    
	        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);  
	        inputStream=new FileInputStream(file);  
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
	        inputStream.skip(pos);    
	        byte[] buffer = new byte[1024*10];  
	        int length = 0;    
	        while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {    
	            out.write(buffer, 0, length); 
	        }  
	    } catch (Exception e) {  
	    	log.error(e.getMessage());  
	    }finally{  
	         try {  
	             if(null != out) out.flush();  
	             if(null != out) out.close();  
	             if(null != inputStream) inputStream.close();   
	        } catch (IOException e) {  
	        }  
	    }  
	}

}
