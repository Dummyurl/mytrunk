package com.car.portal.entity;

import java.util.Date;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name="appmessage")
public class AppMessage implements java.io.Serializable {
	private static final long serialVersionUID = 8508048247814849749L;
	@Column(name = "id" ,autoGen=false, isId=true)
	private Integer id;
	@Column(name = "content")
	private String content;
	@Column(name = "type")
	private Short type;
	@Column(name = "uid")
	private Integer uid;
	@Column(name = "createTime")
	private Date createTime;
	@Column(name = "senderId")
	private Integer senderId;
	@Column(name = "senderName")
	private String senderName;
	@Column(name = "isRead")
	private Short isRead;
	@Column(name = "isDelete")
	private Short isDelete;
	@Column(name = "companyId")
	private Integer companyId;

	public AppMessage() {
	}

	public AppMessage(String content, Short type, Integer uid, Date createTime, Integer senderId, String senderName,
			Short isRead, Short isDelete, Integer companyId) {
		this.content = content;
		this.type = type;
		this.uid = uid;
		this.createTime = createTime;
		this.senderId = senderId;
		this.senderName = senderName;
		this.isRead = isRead;
		this.isDelete = isDelete;
		this.companyId = companyId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Short getIsRead() {
		return this.isRead;
	}

	public void setIsRead(Short isRead) {
		this.isRead = isRead;
	}

	public Short getIsDelete() {
		return this.isDelete;
	}

	public void setIsDelete(Short isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
