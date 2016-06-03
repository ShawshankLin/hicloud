package cn.edu.cylg.cis.hicloud.core.init;


/**
 * 服务器启动执行接口
 * @author Jeanzhou
 * @version
 *		1.0 2014年12月14日下午10:41:12
 */
public interface StartInit {

	/**
	 * 初始化方法
	 */
	public void init();
	
	/**
	 * 执行顺序号
	 * @return
	 */
	public int getSortNo();
	
}
