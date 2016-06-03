package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 存储空间表
 * @author  Shawshank
 * @version 1.0
 * 2016年5月27日 创建文件
 */
@Entity
@Table(name="SYS_SPACE")
public class Space extends BaseEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 存储空间名称
	 */
	@Column(name="SPACE_NAME")
	private String spaceName;
	
	/**
	 * 存储空间大小（KB计算）
	 */
	@Column(name="SPACE_SIZE")
	private long spaceSize;

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public long getSpaceSize() {
		return spaceSize;
	}

	public void setSpaceSize(long spaceSize) {
		this.spaceSize = spaceSize;
	}

	

}
