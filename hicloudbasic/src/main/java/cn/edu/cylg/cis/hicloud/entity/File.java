package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 文件实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月19日 创建文件
 */
@Entity
@Table(name="DRIVE_FILE")
@JsonIgnoreProperties(value={"parentFile"})
public class File extends BaseEntity{

	private static final long serialVersionUID = 1L;

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
	 * 后缀名
	 */
	@Column(name="SUFFIX",length = 10)
	private String suffix;
	
	/**
	 * 文件大小
	 */
	@Column(name="FILE_SIZE")
	private Long fileSize;
	
	
	/**
	 * 文件MD5标识
	 */
	@Column(name="MD5",length = 200)
	private String MD5;
	
	/**
	 * 父目录URL
	 */
	@Column(name="PARENT_PATH",length=1024)
	private String parentPath;
	
	/**
	 * 文件二维码
	 */
	@Column(name="FILE_CODE",length = 200)
	private String fileCode;
	
	/**
	 * 备注
	 */
	@Column(name="DESCRIPTION",length = 255)
	private String description;
	
	/**
	 * 父目录
	 */
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private File parentFile;
	
	/**
	 * 文件类型(1文件夹，0文件)
	 */
	@Column(name="ISDIR",length=2)
	private Integer isDir;

	
	/**
	 * 文件版本
	 */
	@Column(name="FILE_VERSION")
	private Integer fileVersion;
	
	
	/**
	 * 状态
	 */
	@Column(name="STATUS",length=20)
	private String status;
	
	
	/**
	 * 外链
	 */
	@Column(name="REF",length=1024)
	private String ref;
	
	
	/**
	 * 文件序列号
	 */
	@Column(name="SERIAL_NUM",length=50)
	private String serialNum;
	

	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getSuffix() {
		return suffix;
	}


	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}


	public Long getFileSize() {
		return fileSize;
	}


	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}


	public String getMD5() {
		return MD5;
	}


	public void setMD5(String mD5) {
		MD5 = mD5;
	}


	public String getParentPath() {
		return parentPath;
	}


	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}


	public String getFileCode() {
		return fileCode;
	}


	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public File getParentFile() {
		return parentFile;
	}


	public void setParentFile(File parentFile) {
		this.parentFile = parentFile;
	}


	public Integer getIsDir() {
		return isDir;
	}


	public void setIsDir(Integer isDir) {
		this.isDir = isDir;
	}


	public Integer getFileVersion() {
		return fileVersion;
	}


	public void setFileVersion(Integer fileVersion) {
		this.fileVersion = fileVersion;
	}
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String getSerialNum() {
		return serialNum;
	}


	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentFile")
	private Set<File> childrenFiles = new HashSet<File>(0);
	
	public Set<File> getChildrenFiles() {
		return childrenFiles;
	}


	public void setChildrenFiles(Set<File> childrenFiles) {
		this.childrenFiles = childrenFiles;
	}*/
	
	
	
	
}
