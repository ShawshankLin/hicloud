package cn.edu.cylg.cis.hicloud.vo.query;

import cn.edu.cylg.cis.hicloud.core.base.BaseVO;


public class GroupUserQuery extends BaseVO{

	private String groupId;
	
	private String userId;
	
	private Integer isConfirm;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}

	
	
	
}
