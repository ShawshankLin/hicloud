package cn.edu.cylg.cis.hicloud.service.sys.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.dao.sys.SpaceDao;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.service.sys.SpaceService;

@Service("spaceService")
public class SpaceServiceImpl extends BaseServiceImpl implements SpaceService{

	@Resource
	private SpaceDao spaceDao;
	
	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return spaceDao;
	}

	@Override
	public List<Space> findAllSpace() throws Exception {
		// TODO Auto-generated method stub
		return spaceDao.findAllSpace();
	}


}
