package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 分享实体
 * @author  Shawshank
 * @version 1.0
 * 2016年1月20日 创建文件
 */
@Entity
@Table(name="DRIVE_SHARE")
public class Share extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 点赞数
	 */
	@Column(name="SUPPORT",columnDefinition="int(11) default 0")
	private Integer support = 0;
	
	
	/**
	 * 下载次数
	 */
	@Column(name="HOT",columnDefinition="int(11) default 0")
	private Integer hot = 0;
	
	
	/**
	 * 提取码
	 */
	@Column(name="EXTRACTION_CODE")
	private Integer extractionCode;
	
	
	/**
	 * 分享类型
	 * 1-公开
	 * 2-私有
	 * 3-个人
	 */
	@Column(name="TYPE",length=2)
	private Integer type;
	
	/**
	 * 分享好友
	 */
	@ManyToOne
	@JoinColumn(name = "TO_ID")
	private User to;
	
	public Integer getSupport() {
		return support;
	}
	public void setSupport(Integer support) {
		this.support = support;
	}
	public Integer getHot() {
		return hot;
	}
	public void setHot(Integer hot) {
		this.hot = hot;
	}
	
	public Integer getExtractionCode() {
		return extractionCode;
	}
	public void setExtractionCode(Integer extractionCode) {
		this.extractionCode = extractionCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * 文件id
	 */
	@ManyToOne
	@JoinColumn(name = "FILE_ID")
	private File file;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public User getTo() {
		return to;
	}
	public void setTo(User to) {
		this.to = to;
	}
	
	
	
}
