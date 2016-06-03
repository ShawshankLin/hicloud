package cn.edu.cylg.cis.hicloud.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Md5CaculateUtil {
	
	private static final Log log = LogFactory.getLog(Md5CaculateUtil.class);

    private Md5CaculateUtil(){
        
    }
    
    private static char[] hexChar = {
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f'
    };
    
    public static String getHash(InputStream ins,String hashType) throws IOException, NoSuchAlgorithmException{
    	log.info("开始计算文件MD5值");
    	long start = System.currentTimeMillis();
    	byte[] buffer = new byte[8192];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        int len;
        while((len = ins.read(buffer)) != -1){
            md5.update(buffer, 0, len);
        }
        ins.close();
//      也可以用apache自带的计算MD5方法
        String md5Value=DigestUtils.md5Hex(md5.digest());
//      自己写的转计算MD5方法
//      String md5Value= toHexString(md5.digest());
        long end = System.currentTimeMillis();
        log.info("一共耗时:"+(end-start)+"毫秒");
        return md5Value;
    }
    
    public static String getHash(String fileName,String hashType) throws IOException, NoSuchAlgorithmException{
        File f = new File(fileName);
        InputStream ins = new FileInputStream(f);
        return getHash(ins,hashType);
    }
    
    public static String getHash2(String fileName){
        File f = new File(fileName);
        return String.valueOf(f.lastModified());
    }
    
    
    protected static String toHexString(byte[] b){
        StringBuilder sb = new StringBuilder(b.length*2);
        for(int i=0;i<b.length;i++){
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    
    /*
     * 获取MessageDigest支持几种加密算法
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static String[] getCryptolmpls(String serviceType){
        
        Set result = new HashSet();
//        all providers
        Provider[] providers = Security.getProviders();
        for(int i=0;i<providers.length;i++){
//            get services provided by each provider
            Set keys = providers[i].keySet();
            for(Iterator it = keys.iterator();it.hasNext();){
                String key = it.next().toString();
                key = key.split(" ")[0];
                
                if(key.startsWith(serviceType+".")){
                    result.add(key.substring(serviceType.length()+1));
                }else if(key.startsWith("Alg.Alias."+serviceType+".")){
                    result.add(key.substring(serviceType.length()+11));
                }
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }
    
    
    public static void main(String[] args) throws Exception, Exception {
//        调用方法
//        String[] names = getCryptolmpls("MessageDigest");
//        for(String name:names){
//            System.out.println(name);
//        }
        String fileName = "G:\\Movie\\行尸走肉.The.Walking.Dead.S06E16.中英字幕.HDTVrip.1024x576.mp4";
////        String fileName = "E:\\SoTowerStudio-3.1.0.exe";
        String hashType = "MD5";
        String hash = getHash(fileName,hashType);
        System.out.println("MD5:"+hash);
        
    }
}
