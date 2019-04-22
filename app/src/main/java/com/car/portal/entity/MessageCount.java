package com.car.portal.entity;

public class MessageCount {
	private Integer typeId;
	private String typeName;
	private Integer count;

	public MessageCount() {
		super();
	}

	public MessageCount(Integer typeId, String typeName, Integer count) {
		super();
		this.typeId = typeId;
		this.typeName = typeName;
		this.count = count;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
