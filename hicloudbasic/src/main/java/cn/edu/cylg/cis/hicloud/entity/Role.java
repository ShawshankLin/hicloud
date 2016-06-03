
package cn.edu.cylg.cis.hicloud.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 角色实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月23日 创建文件
 */
@Entity
@Table(name="SYS_ROLE")
@JsonIgnoreProperties(value={"userRoles"})
public class Role extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 角色名
	 */
	@Column(name="ROLE_NAME", length = 20)
	private String roleName;

	
	/**
	 * 描述
	 */
	@Column(name="DESCRIPTION",length = 255)
	private String description;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<UserRole> userRoles=new HashSet<UserRole>();

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
}
