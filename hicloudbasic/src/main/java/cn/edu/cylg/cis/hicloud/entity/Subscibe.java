package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 订阅实体         
 * @author  Shawshank
 * @version 1.0
 * 2016年1月19日 创建文件
 */
@Entity
@Table(name="DRIVE_SUBSCIBE")
public class Subscibe extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 订阅者
	 */

	@ManyToOne
	@JoinColumn(name = "SUBSCIBER_ID")
	private User subscriber;
	
	public User getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(User subscriber) {
		this.subscriber = subscriber;
	}
	/**
	 * 作者
	 */
	@ManyToOne(cascade = {}, fetch = FetchType.EAGER)
	@JoinColumn(name = "AUTHOR_ID")
	private User author;

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
	
	
}
