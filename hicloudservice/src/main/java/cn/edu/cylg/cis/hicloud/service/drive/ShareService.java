package cn.edu.cylg.cis.hicloud.service.drive;

import java.util.List;
import java.util.Map;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

public interface ShareService extends BaseService{

	/**
	 *  根据条件查找
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<Share> findShareByCondition(ShareQuery query) throws Exception;
	
	
	/**
	 * 	首页查找
	  * @param query
	  * @return
	  * @throws Exception
	 */	
	public List<Map<String,String>> findShareForIndex(ShareQuery query) throws Exception;
	
	/**
	 *  批量取消分享
	  * @param shareId
	  * @throws Exception
	 */
	public void cancelShare(String shareId) throws Exception;
}
