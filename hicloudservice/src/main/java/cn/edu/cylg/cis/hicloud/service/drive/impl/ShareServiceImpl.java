package cn.edu.cylg.cis.hicloud.service.drive.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.dao.drive.ShareDao;
import cn.edu.cylg.cis.hicloud.entity.Share;
import cn.edu.cylg.cis.hicloud.service.drive.ShareService;
import cn.edu.cylg.cis.hicloud.vo.query.ShareQuery;

@Service("shareService")
public class ShareServiceImpl extends BaseServiceImpl implements ShareService{

	@Autowired
	private ShareDao shareDao;
	
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return shareDao;
	}


	@Override
	public List<Share> findShareByCondition(ShareQuery query) throws Exception {
		// TODO Auto-generated method stub
		return this.shareDao.findShareByCondition(query);
	}


	@Override
	public List<Map<String, String>> findShareForIndex(ShareQuery query)
			throws Exception {
		// TODO Auto-generated method stub
		return this.shareDao.findShareForIndex(query);
	}


	@Override
	@Transactional(readOnly=true,rollbackFor=Exception.class)
	public void cancelShare(String shareId) throws Exception {
		String shares[]=shareId.split(",");
		for(int i=0;i<shares.length;i++){
			Share share = this.get(Share.class, shares[i]);
			if(share==null){
				throw new AppException("不存在分享文件");
			}
			this.delete(share);
		}
	}

}
