package cn.edu.cylg.cis.hicloud.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
import cn.edu.cylg.cis.hicloud.core.validates.RequiredField;
import cn.edu.cylg.cis.hicloud.core.validates.Validation;

/**
 * 用户实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月24日 创建文件
 */
@Entity
@Table(name="SYS_USER")
public class User extends BaseEntity{
   
	private static final long serialVersionUID = 1L;
	/**
	 * 账号
	 */
	@Validation
	@RequiredField(message = "请输入账号")
	@Column(name="USERNAME",length = 50,unique=true,nullable=true)
	private String userName;
	/**
	 * 用户密码
	 */
	@Validation
	@RequiredField(message = "请输入密码")
	@Column(name="PASSWORD",length = 50)
	private String password;
	/**
	 * 真实姓名
	 */
	@Validation
	@RequiredField(message = "请输入姓名")
	@Column(name="NAME",length = 50)
	private String name;
	/**
	 * 性别（1男，2女）
	 */
	@Column(name="SEX",columnDefinition="int(2) default 1")
	private Integer sex;
	/**
	 * 出生日期
	 */
	@Column(name="BIRTHDAY")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthDay;
	/**
	 * 电子邮件
	 */
	@Column(name="EMAIL",length = 50)
	private String email;
	/**
	 * 手机号码
	 */
	@Column(name="MOBILE_PHONE",length = 50)
	private String mobilePhone;
	/**
	 * 登陆时间
	 */
	@Column(name="LAST_LOGIN",length = 10)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") 
	private Date lastLogin;
	/**
	 * 状态（0禁用1有效）
	 */
	@Column(name="STATUS",length = 10)
	private Integer status;
	
	/**
	 * 头像
	 */
	@Column(name="PHOTO")
	private String photo;
	
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

	
	/**
	 * 注释
	 */
	@Column(name="REMARK",length=255)
	private String remark;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public long getUsedSpace() {
		return usedSpace;
	}
	public void setUsedSpace(long usedSpace) {
		this.usedSpace = usedSpace;
	}
	
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	
	public Space getSpace() {
		return space;
	}
	public void setSpace(Space space) {
		this.space = space;
	}
}