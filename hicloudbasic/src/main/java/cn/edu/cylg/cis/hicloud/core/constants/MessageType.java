package cn.edu.cylg.cis.hicloud.core.constants;

public interface MessageType {

	/** 成功消息 */
	public static final String SUCCESS = "success";
	/** 失败消息 */
	public static final String ERROR = "error";
	/** 警告消息 */
	public static final String WARNING = "warning";
	/** 文件夹创建*/
	public static final String FOLDER_CREATE = "01";
	/** 文件添加 */
	public static final String FILE_UPLOAD = "02";
	/** 文件删除 */
	public static final String FILE_DEL = "03";
	/** 文件移动 */
	public static final String FILE_MOVE = "04";
	/** 文件修改 */
	public static final String FILE_RENAME = "05";
	/** 添加好友 */
	public static final String FRIEND_SEND = "06";
	/** 好友接受 */
	public static final String FRIEND_RECEIVE = "07";
	/** 群组发送邀请*/
	public static final String GROUP_SEND = "08";
	/** 接受群组邀请 */
	public static final String GROUP_RECEIVE = "09";
	/** 群组文件夹创建*/
	public static final String GROUP_FOLDER_CREATE = "10";
	/** 群组文件修改文件名*/
	public static final String GROUP_FILE_RENAME = "11";
	/** 群组文件删除*/
	public static final String GROUP_FILE_DEL = "12";
	/** 群组文件移动*/
	public static final String GROUP_FILE_MOVE = "13";
	/** 分享文件*/
	public static final String SHARE_FILE = "14";

	
}
