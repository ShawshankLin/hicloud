package cn.edu.cylg.cis.hicloud.service.sys.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import cn.edu.cylg.cis.hicloud.core.base.BaseDAO;
import cn.edu.cylg.cis.hicloud.core.base.BaseServiceImpl;
import cn.edu.cylg.cis.hicloud.core.exception.AppException;
import cn.edu.cylg.cis.hicloud.dao.sys.SpaceDao;
import cn.edu.cylg.cis.hicloud.dao.sys.UserDao;
import cn.edu.cylg.cis.hicloud.entity.Space;
import cn.edu.cylg.cis.hicloud.entity.User;
import cn.edu.cylg.cis.hicloud.service.sys.UserService;
import cn.edu.cylg.cis.hicloud.utils.FuncUtil;
import cn.edu.cylg.cis.hicloud.utils.excel.ExcelOperator;
import cn.edu.cylg.cis.hicloud.vo.query.UserQuery;


/**
 * UserServiceImpl 实现类
 * @author  Shawshank
 * @version 1.0
 * 2016年1月20日 创建文件
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
	
	@Resource
	private UserDao userDao;
	@Resource
	private SpaceDao spaceDao;

	@Override
	public BaseDAO getBaseDao() {
		// TODO Auto-generated method stub
		return userDao;
	}

	@Override
	public User findUserByUserName(String userName) throws Exception {
		// TODO Auto-generated method stub
		return userDao.findUserByUserName(userName);
	}

	@Override
	public List<User> findUserByCondition(UserQuery query) throws Exception {
		// TODO Auto-generated method stub
		return userDao.findUserByCondition(query);
	}

	@SuppressWarnings("unused")
	@Override
	public void importUser(String fileName, InputStream inputStream) throws Exception {
		// TODO Auto-generated method stub
		int rowIndex=1;
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		log.info("用户报表解析开始");
		//获得 读取Excel的数据
		List<Object[]> users = null ;
		try {
			users= fileName.endsWith(".xls") ? ExcelOperator.readXLS(bis, 0, 25,false) : ExcelOperator.readXLSX(bis, 0, 25,false);
			//判断用户表内容
			if(users == null || users.size() <=1)
			{
				throw new AppException("用户报表不能为空");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<User> userList=new ArrayList<User>();
		//循环行row
		for(int row=1;row<users.size();row++){
			log.info("开始对导入的excel表数据进行校验");
			Object[] object = users.get(row);
			User user=this.userDao.findUserByUserName(object[0].toString());
			if(user!=null)
				throw new AppException("用户"+user.getUserName()+"已经存在");
			user=new User();
			if(FuncUtil.isEmpty(object[0].toString()))
				throw new AppException("行："+row+"列："+1+"不能为空");
			user.setUserName(object[0].toString());
			if(FuncUtil.isEmpty(object[1].toString())){
				String encryptPWD = new SimpleHash("SHA-1", object[0].toString(), "123456").toString();
				user.setPassword(encryptPWD);
			}else {
				String encryptPWD = new SimpleHash("SHA-1", object[0].toString(), object[1].toString()).toString();
				user.setPassword(encryptPWD);
			}
			if(FuncUtil.isEmpty(object[2].toString()))
				throw new AppException("行："+row+"列："+3+"不能为空");
			user.setName(object[2].toString());
			user.setEmail(object[3].toString());
			user.setMobilePhone(object[4].toString());
			if(FuncUtil.notEmpty(object[5].toString())){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				sdf.parse(object[5].toString());
			}
			if(FuncUtil.notEmpty(object[6].toString())){
				if("男".equals(object[6].toString())){
					user.setSex(1);
				}else if("女".equals(object[6].toString())){
					user.setSex(0);
				}
			}
			user.setRemark(object[7].toString());
			user.setPhoto("/resources/images/user/"+new Random().nextInt(9)+".png");
			List<Space> spaces=this.spaceDao.findAllSpace();//分配低配空间
			if(spaces!=null&&spaces.size()>0){
				user.setSpace(spaces.get(0));
				log.info("分配用户存储空间"+spaces.get(0).getSpaceName()+":"+spaces.get(0).getSpaceSize());
			}
			user.setStatus(1);
			userList.add(user);
		}
		log.info("用户报表解析结束");
		this.save(userList);
		log.info("保存用户信息到数据库");
		//行自增
		rowIndex++;
		log.info("用户报表处理完成");
	}

	@Override
	public List<User> findAllUser() throws Exception {
		// TODO Auto-generated method stub
		return this.userDao.findAllUser();
	}

	@Override
	public void saveUserSpace(String userIds, String spaceId) throws Exception {
		Space space=this.get(Space.class, spaceId);
		if(space==null)
			throw new AppException("角色不存在");
		String[] userIdss=userIds.split(",");
		if(userIdss!=null&&userIdss.length>0){
			for(int i=0;i<userIdss.length;i++){
				User user=this.get(User.class, userIdss[i]);
				user.setSpace(space);
				this.update(user);
			}
		}
	}
}
