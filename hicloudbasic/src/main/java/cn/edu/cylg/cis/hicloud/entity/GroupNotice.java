package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;

/**
 * 群组公告
 * @author  Shawshank
 * @version 1.0
 * 2016年1月19日 创建文件
 */
@Entity
@Table(name="GROUP_GROUPNOTICE")
public class GroupNotice extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "GROUP_ID")
	private Group group;
	
	/**
	 * 公告
	 */
	@Column(name="NOTICE")
	private String notice;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	
	
}
