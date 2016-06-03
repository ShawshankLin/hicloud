package cn.edu.cylg.cis.hicloud.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
import cn.edu.cylg.cis.hicloud.core.validates.RequiredField;
import cn.edu.cylg.cis.hicloud.core.validates.Validation;
/**
 * 群组实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月19日 创建文件
 */
@Entity
@Table(name="GROUP_GROUP")
@JsonIgnoreProperties(value={"groupFiles","groupUsers","groupNotices"})
public class Group extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 群号
	 */
	@Column(name="GROUP_NO")
	private String groupNo;
	
	/**
	 * 群组名
	 */
	@Validation
	@RequiredField(message = "请输入群组名")
	@Column(name="GROUP_NAME", length = 50)
	private String groupName;
	/**
	 * 介绍
	 */
	@Column(name="DESCRIPTION", length = 255)
	private String description;
	
	/**
	 * 群封面
	 */
	@Column(name="COVER")
	private String cover;
	
	
	/**
	 * 进群方式
	 * 0-直接加入
	 * 1-输入邀请码加入
	 */
	@Column(name="JOIN_WAY",length=2)
	private Integer joinWay;
	
	/**
	 * 邀请码
	 */
	@Column(name="INVITATION_CODE")
	private String invitationCode;
	
	
	/**
	 * 已使用空间（KB计算）
	 */
	@Column(name="USED_SPACE")
	private long usedSpace;
	
	/**
	 * 存储空间
	 */
	@ManyToOne
	@JoinColumn(name = "SPACE_ID")
	private Space space;
	
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupNo() {
		return groupNo;
	}
	public void setGroupNo(String groupNo) {
		this.groupNo = groupNo;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	
	public Integer getJoinWay() {
		return joinWay;
	}
	public void setJoinWay(Integer joinWay) {
		this.joinWay = joinWay;
	}
	public String getInvitationCode() {
		return invitationCode;
	}
	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	public long getUsedSpace() {
		return usedSpace;
	}
	public void setUsedSpace(long usedSpace) {
		this.usedSpace = usedSpace;
	}

	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "group")
	private Set<GroupFile> groupFiles = new HashSet<GroupFile>();

	public Set<GroupFile> getGroupFiles() {
		return groupFiles;
	}
	public void setGroupFiles(Set<GroupFile> groupFiles) {
		this.groupFiles = groupFiles;
	}

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "group")
	private Set<GroupUser> groupUsers = new HashSet<GroupUser>();

	public Set<GroupUser> getGroupUsers() {
		return groupUsers;
	}
	public void setGroupUsers(Set<GroupUser> groupUsers) {
		this.groupUsers = groupUsers;
	}
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "group")
	private Set<GroupNotice> groupNotices = new HashSet<GroupNotice>();

	public Set<GroupNotice> getGroupNotices() {
		return groupNotices;
	}
	public void setGroupNotices(Set<GroupNotice> groupNotices) {
		this.groupNotices = groupNotices;
	}
	
	

}
