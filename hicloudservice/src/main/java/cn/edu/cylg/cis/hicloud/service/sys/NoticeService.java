package cn.edu.cylg.cis.hicloud.service.sys;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.vo.query.NoticeQuery;

/**
 * NoticeService 接口
 * @author  Shawshank
 * @version 1.0
 * 2016年5月4日 创建文件
 */
public interface NoticeService extends BaseService{

	/**
	 *  查询消息盒子
	  * @param query
	  * @return
	  * @throws Exception
	 */
	public List<Notice> findNoticeByCondition(NoticeQuery query) throws Exception;
}
