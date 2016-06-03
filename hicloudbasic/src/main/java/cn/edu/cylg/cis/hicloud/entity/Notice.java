package cn.edu.cylg.cis.hicloud.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import cn.edu.cylg.cis.hicloud.core.base.BaseEntity;
/**
 * 系统消息通知
 * @version 1.0
 * 2016年5月17日 创建文件
 */
@Entity
@Table(name="SYS_NOTICE")
public class Notice extends BaseEntity{


	private static final long serialVersionUID = 1L;
	
	/**
	 * 接受者
	 */
	@ManyToOne
	@JoinColumn(name = "RECEIVER") 
	private User receiver;
	
	/**
	 * 内容
	 */
	@Column(name="CONTENT")
	private String content;
	
	/**
	 * 回调操作
	 */
	@Lob()
	@Type(type = "org.hibernate.type.StringClobType")
	@Column(name="TARGET")
	private String target;
	
	/**
	 * 消息类型
	 */
	@Column(name="TYPE")
	private String type;
	
	/**
	 * 消息状态
	 * -1-失效 
	 *  0-未读
	 *  1-已读 
	 */
	@Column(name="STATUS",length=2)
	private Integer status;
	
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	
	
	
}
