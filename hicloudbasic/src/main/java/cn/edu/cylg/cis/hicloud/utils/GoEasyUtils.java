package cn.edu.cylg.cis.hicloud.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.goeasy.GoEasy;
import io.goeasy.publish.GoEasyError;
import io.goeasy.publish.PublishListener;

import com.google.gson.Gson;
/**
 * GoEasy 推送
 * @author  Shawshank
 * @version 1.0
 * 2016年5月5日 创建文件
 */
public class GoEasyUtils {
	
	private static final Log log =LogFactory.getLog(GoEasyUtils.class);
	
	private static final String GOEASY_APPKEY = PropUtil.readValue("goeasy_appKey");
	
	private static GoEasy goEasy = new GoEasy(GOEASY_APPKEY);
	
	public static void publishMsg(String channel,String message) throws Exception{
		
		goEasy.publish(channel, new Gson().toJson(message), new PublishListener(){
			@Override
			public void onSuccess() {
				log.info("消息发布成功");
			}
			
			@Override
			public void onFailed(GoEasyError error) {
				log.error("消息发布失败，错误编码"+error.getCode()+ " 错误信息： " +
						error.getContent());
			}
		});
	}
}
