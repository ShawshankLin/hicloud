package cn.edu.cylg.cis.hicloud.vo.query;

import cn.edu.cylg.cis.hicloud.core.base.BaseVO;

public class ShareQuery extends BaseVO{

	private String fileId;
	
	private String createId;
	
	private String toId;

	/**
	 * 分享类型
	 * 1-公开
	 * 2-私有
	 * 3-个人
	 */
	private Integer type;
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}
	
	
}
