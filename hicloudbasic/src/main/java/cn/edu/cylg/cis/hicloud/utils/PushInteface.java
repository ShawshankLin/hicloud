package cn.edu.cylg.cis.hicloud.utils;

import java.util.ArrayList;
import java.util.List;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class PushInteface {

	private static final String host = PropUtil.readValue("getui_host");
	private static final String appId = PropUtil.readValue("getui_appId");
	private static final String appKey = PropUtil.readValue("getui_appKey");
	private static final String masterSecret = PropUtil.readValue("getui_masterSecret");
	
	private static IGtPush push = new IGtPush(host, appKey, masterSecret);

	/**
	 * 推送给单个用户
	 * 
	 * @param alias
	 *            用户名
	 * @param contents
	 *            透传消息内容
	 * @return
	 */
	public static boolean pushToSingle(String alias, String contents) {
		TransmissionTemplate template = transmissionTemplateDemo(contents);/* 透传消息模板 */
		// NotificationTemplate template =
		// notificationTemplateDemo(title,contents,contentsjson);
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		message.setPushNetWorkType(0); // 可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。

		Target target = new Target();
		target.setAppId(PropUtil.readValue("appId"));
		// target.setClientId(Constants.CID);
		// 用户别名推送，cid和用户别名只能2者选其一
		target.setAlias(alias);
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());

		}
		if (ret.getResponse().get("result").equals("ok")) {
			return true;
		} else
			return false;

	}

	/**
	 * 推送给单个用户
	 * 
	 * @param alias
	 *            用户名
	 * @param contents
	 *            透传消息内容
	 * @return
	 */
	public static boolean pushToSinglebytran(String alias, String contents) {
		TransmissionTemplate template = transmissionTemplateDemo(contents);
		// TransmissionTemplate template = transmissionTemplateDemo(contents);
		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		message.setPushNetWorkType(0); // 可选。判断是否客户端是否wifi环境下推送，1为在WIFI环境下，0为不限制网络环境。

		Target target = new Target();
		target.setAppId(appId);
		// target.setClientId(Constants.CID);
		// 用户别名推送，cid和用户别名只能2者选其一
		target.setAlias(alias);
		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());

		}
		if (ret.getResponse().get("result").equals("ok")) {
			System.out.println(ret.getResponse().get("result"));
			return true;
		} else
			return false;

	}

	/**
	 * 根据用户list推送
	 * 
	 * @param list
	 *            用户集
	 * @param contents
	 *            透传消息内容
	 * @return
	 */
	public static boolean pushMessageToList(List<String> list, String title,
			String contents) {
		// 通知透传模板
		TransmissionTemplate template = transmissionTemplateDemo(contents);
		ListMessage message = new ListMessage();
		message.setData(template);
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		// 配置推送目标
		List<Target> targets = new ArrayList<Target>();
		message.setOfflineExpireTime(24 * 1000 * 3600);
		for (String string : list) {
			Target target = new Target();
			target.setAppId(appId);
			target.setAlias(string);
			targets.add(target);
		}

		// 获取taskID
		String taskId = push.getContentId(message);
		// 使用taskID对目标进行推送
		IPushResult ret = push.pushMessageToList(taskId, targets);
		System.out.println(ret.getResponse().get("result"));
		if (ret.getResponse().get("result").equals("ok")) {
			return true;
		} else
			return false;

	}

	/**
	 * 透传消息
	 * 
	 * @param contents
	 *            推送内容{json字符串}
	 * @return
	 */
	public static TransmissionTemplate transmissionTemplateDemo(String contents) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(2);
		template.setTransmissionContent(contents);

		return template;
	}

	/**
	 * 推送的特定app。比如是Android ，ios ，特定标签用户
	 * 
	 * @param contents
	 *            透传消息内容
	 */
	@SuppressWarnings("deprecation")
	public static boolean pushToApp(String contents) {
		TransmissionTemplate template = transmissionTemplateDemo(contents);
		// NotificationTemplate template =
		// notificationTemplateDemo("测试标题","测试内容",contents);
		AppMessage message = new AppMessage();
		message.setData(template);
		message.setOffline(true);
		message.setOfflineExpireTime(24 * 1000 * 3600);

		List<String> appIdList = new ArrayList<String>();
		List<String> phoneTypeList = new ArrayList<String>();
		appIdList.add(appId);
		phoneTypeList.add("ANDROID");
		// provinceList.add("");
		// tagList.add("脾气");

		message.setAppIdList(appIdList);
		message.setPhoneTypeList(phoneTypeList);
		// message.setProvinceList(provinceList);
		// message.setTagList(tagList);
		// message.setPushNetWorkType(1);
		// message.setSpeed(1000);
		IPushResult ret = push.pushMessageToApp(message, "任务别名_toApp");
		if (ret.getResponse().get("result").equals("ok")) {
			return true;
		} else
			return false;
	}

	public static void main(String[] args) {
		System.out.println(PushInteface.pushToSingle("8ae82fb95470342b0154703489bf0000", "9998888888"));
	}
}
