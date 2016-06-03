package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 好友实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月20日 创建文件
 */
@Entity
@Table(name="GROUP_FRIEND")
public class Friend extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "FRIEND_ID") 
	private User friend;

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}
	
	/**
	 *-1-已拒绝
	 * 0-未确认
	 * 1-已确认
	 */
	@Column(name="ISCONFIRM",length=2)
	private Integer isConfirm;

	public Integer getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}
	
	/**
	 * 备注姓名
	 */
	@Column(name="REMARK",length=20)
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}

