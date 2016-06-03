package cn.edu.cylg.cis.hicloud.vo.query;

import cn.edu.cylg.cis.hicloud.core.base.BaseVO;

public class NoticeQuery extends BaseVO{

	private String createId;
	
	private String receiverId;
	/**
	 * -1-失效 
	 *  0-未确认 
	 *  1-已确认 
	 */
	private Integer status;

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
