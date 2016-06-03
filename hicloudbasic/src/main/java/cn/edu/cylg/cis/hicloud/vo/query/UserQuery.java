package cn.edu.cylg.cis.hicloud.vo.query;

import cn.edu.cylg.cis.hicloud.core.base.BaseVO;

public class UserQuery extends BaseVO{

	private String userName;
	
	private String email;
	
	private String mobilePhone;
	
	private Integer status;
	
	private String spaceId;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	
	
}
