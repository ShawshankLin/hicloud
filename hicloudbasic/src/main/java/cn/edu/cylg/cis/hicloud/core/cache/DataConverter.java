package cn.edu.cylg.cis.hicloud.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.edu.cylg.cis.hicloud.core.constants.FileType;

public class DataConverter {

	private static Map<String,String> fileTypeMap;
	
	public  static Map<String,String> getFileType(){
		if(fileTypeMap==null){
			fileTypeMap=new LinkedHashMap<String, String>();
			fileTypeMap.put(FileType.IMG, "jpg,png,gif");
			fileTypeMap.put(FileType.VEDIO, "rmvb,mpeg1－4,mov,mtv,dat,wmv,avi,3gp,amv,dmv,mp4");
			fileTypeMap.put(FileType.MUSCI, "mp3");
			fileTypeMap.put(FileType.DOCUMENT, "doc,docx,pdf,txt,ppt,cpp,xls,xlsx,java,html");
			fileTypeMap.put(FileType.COMPRESS, "zip,rar");
			fileTypeMap.put(FileType.OTHER, "zip,rar");
		}
		return fileTypeMap;
	}
	
	public static String getFileTypeByKey(String key){
		return getFileType().get(key);
	}
	

	
}
