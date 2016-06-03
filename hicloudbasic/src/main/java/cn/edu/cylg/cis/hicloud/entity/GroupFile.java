package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;


/**
 * 群组文件
 * @author  Shawshank
 * @version 1.0
 * 2016年5月6日 创建文件
 */
@Entity
@Table(name="GROUP_GROUPFILE")
@JsonIgnoreProperties(value={"parentFile"})
public class GroupFile extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 群组
	 */
	@ManyToOne
	@JoinColumn(name="GROUP_ID")
	private Group group;
	
	/**
	 * uuid文件名
	 */
	@Column(name="FILE_NAME", length = 200)
	private String fileName;
	
	/**
	 * 真实文件名
	 */
	@Column(name="NAME",length=200)
	private String name;

	
	/**
	 * 文件类型(1文件夹，0文件)
	 */
	@Column(name="ISDIR",length=2)
	private Integer isDir;
	
	/**
	 * 父目录URL
	 */
	@Column(name="PARENT_PATH")
	@Lob()
	@Type(type = "org.hibernate.type.StringClobType")
	private String parentPath;
	
	
	/**
	 * 群组文件父目录
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private GroupFile parentFile;
	
	/**
	 * 源文件
	 */
	@ManyToOne
	@JoinColumn(name="FILE_ID")
	private File ref;

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsDir() {
		return isDir;
	}

	public void setIsDir(Integer isDir) {
		this.isDir = isDir;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public GroupFile getParentFile() {
		return parentFile;
	}

	public void setParentFile(GroupFile parentFile) {
		this.parentFile = parentFile;
	}

	public File getRef() {
		return ref;
	}

	public void setRef(File ref) {
		this.ref = ref;
	}
	
	
}
