package cn.edu.cylg.cis.hicloud.service.sys;

import java.util.List;

import cn.edu.cylg.cis.hicloud.core.base.BaseService;
import cn.edu.cylg.cis.hicloud.entity.Space;

public interface SpaceService extends BaseService{

	public List<Space> findAllSpace() throws Exception;
}
