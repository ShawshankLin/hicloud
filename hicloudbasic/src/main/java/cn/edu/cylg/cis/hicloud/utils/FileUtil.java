package cn.edu.cylg.cis.hicloud.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.cylg.cis.hicloud.core.cache.DataConverter;
import cn.edu.cylg.cis.hicloud.core.constants.FileType;

public class FileUtil {
	
	private static final Log log = LogFactory.getLog(FileUtil.class);
	
	private static final int BUFFER_SIZE = 100* 100 * 1024;
	
	/**
	 * 创建目录
	 * 
	 * @param destDirName
	 *            目标目录名
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建单个目录
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			if(myDelFile.isDirectory()){
				FileUtils.deleteDirectory(myDelFile);
			}else{
				myDelFile.delete();
			}
		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 读取到字节数组0
	 * 
	 * @param filePath //路径
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 读取到字节数组1
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 读取到字节数组2
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			//System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
     * 获取文件后缀.
     */
    public static String getSuffix(String name) {
        if (name.indexOf(".") == -1) {
            return "";
        }

        String suffix = name.substring(name.lastIndexOf(".") + 1);

        return suffix.trim().toLowerCase();
    }
    
    /**
     * 获取纯文件名
     */
    public static String getFileName(String name){
    	if (name.indexOf(".") == -1) {
            return "";
        }
    	String fileName = name.substring(0,name.lastIndexOf("."));
    	return fileName;
    }

    /**
     * 重名文件自动改名.
     */
    public static String calculateName(String name, List<String> checkedNames) {
        // System.out.println("name : " + name);
        // System.out.println("checkedNames : " + checkedNames);
        List<String> targetCheckedNames = new ArrayList<String>();

        name = name.trim();

        String targetPrefix = name;
        String targetSuffix = "";
        int index = name.lastIndexOf(".");

        if (index != -1) {
            targetPrefix = name.substring(0, index);
            targetSuffix = name.substring(index + 1);
        }

        String prefix = targetPrefix.toLowerCase();
        String suffix = null;

        if (targetSuffix.length() > 0) {
            suffix = "." + targetSuffix.toLowerCase();
        }

        for (String checkedName : checkedNames) {
            String targetCheckedName = checkedName.trim().toLowerCase();

            if (suffix != null) {
                if (targetCheckedName.endsWith(suffix)) {
                    targetCheckedNames.add(targetCheckedName.substring(0,
                            targetCheckedName.length() - suffix.length()));
                }
            } else {
                targetCheckedNames.add(targetCheckedName);
            }
        }

        // System.out.println("targetCheckedNames : " + targetCheckedNames);
        int count = 0;

        while (true) {
            boolean existsDumplicated = false;
            String currentName = prefix;

            if (count != 0) {
                currentName += ("(" + count + ")");
            }

            for (String checkedName : targetCheckedNames) {
                // System.out.println("checkedName : " + checkedName);
                // System.out.println("currentName : " + currentName);
                if (checkedName.equals(currentName)) {
                    existsDumplicated = true;

                    break;
                }
            }

            if (!existsDumplicated) {
                break;
            }

            count++;
        }

        if (count > 0) {
            targetPrefix = (targetPrefix + "(" + count + ")");
        }

        String targetName = targetPrefix;

        if (targetSuffix.length() > 0) {
            targetName += ("." + targetSuffix);
        }

        return targetName;
    }
    
    /**
     * 根据后缀名查找文件类型
      * @param suffix
      * @return
     */
	public static String getFileTypeBySuffix(String suffix){
		Map<String,String> fileTypeMap = DataConverter.getFileType();
		Set<Map.Entry<String,String>> fileTypeSet=fileTypeMap.entrySet();
		Iterator<Entry<String, String>> iterator=fileTypeSet.iterator();
		while(iterator.hasNext()){
			Entry<String, String> entry=iterator.next();
			String types[]=entry.getValue().split(",");
			for(int i=0;i<types.length;i++){
				if(types[i].equals(suffix)){
					return entry.getKey();
				}
			}
		}
		return FileType.OTHER;
	}
	
	
	public static void appendFile(InputStream in, File destFile) {
        OutputStream out = null;
        try {
            // plupload 配置了chunk的时候新上传的文件append到文件末尾
            if (destFile.exists()) {
                out = new BufferedOutputStream(new FileOutputStream(destFile, true), BUFFER_SIZE); 
            } else {
                out = new BufferedOutputStream(new FileOutputStream(destFile),BUFFER_SIZE);
            }
            in = new BufferedInputStream(in, BUFFER_SIZE);
             
            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];          
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {     
            try {
                if (null != in) {
                    in.close();
                }
                if(null != out){
                    out.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
	
	
	public static void main(String[] args) {
		System.out.println(getFileTypeBySuffix("jpg"));
	}

}