package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;

/**
 * 用户-用户组关联实体
 * @author  Shawshank
 * @version 1.0
 * 2016年2月4日 创建文件
 */
@Entity
@Table(name="GROUP_GROUPUSER")
public class GroupUser extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	private Group group;

	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}
	
	/**
	 * 是否确定
	 * 1-确定
	 * 0-未确定
	 */
	private Integer isConfirm;

	public Integer getIsConfirm() {
		return isConfirm;
	}

	public void setIsConfirm(Integer isConfirm) {
		this.isConfirm = isConfirm;
	}

	
	
	
}
