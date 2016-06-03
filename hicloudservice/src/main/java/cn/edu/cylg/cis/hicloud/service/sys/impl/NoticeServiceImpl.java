package cn.edu.cylg.cis.hicloud.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.dao.sys.NoticeDao;
import cn.edu.cylg.cis.hicloud.entity.Notice;
import cn.edu.cylg.cis.hicloud.service.sys.NoticeService;
import cn.edu.cylg.cis.hicloud.vo.query.NoticeQuery;


/**
 * NoticeServiceImpl 实现类
 * @author  Shawshank
 * @version 1.0
 * 2016年1月20日 创建文件
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseServiceImpl implements NoticeService{
	
	@Resource
	private NoticeDao noticeDao;
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return noticeDao;
	}
	
	
	public List<Notice> findNoticeByCondition(NoticeQuery query) throws Exception{
		return this.noticeDao.findNoticeByCondition(query);
	}
	

	
}
