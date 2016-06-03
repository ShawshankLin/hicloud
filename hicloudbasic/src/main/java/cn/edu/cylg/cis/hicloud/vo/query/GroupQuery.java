package cn.edu.cylg.cis.hicloud.vo.query;

import cn.edu.cylg.cis.hicloud.core.base.BaseVO;

public class GroupQuery extends BaseVO{

	private String groupNo;
	
	private String groupName;
	
	private String createId;
	
	private String spaceId;

	public String getGroupNo() {
		return groupNo;
	}

	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(String spaceId) {
		this.spaceId = spaceId;
	}
	
	
}
