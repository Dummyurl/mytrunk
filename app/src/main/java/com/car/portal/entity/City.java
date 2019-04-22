package com.car.portal.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

@Table(name = "city")
public class City implements Parcelable {
	@Column(name = "id" ,autoGen=false, isId=true)
	private int id;
	@Column(name = "cid")
	private int cid;
	@Column(name = "city")
	private String city;
	@Column(name = "provice")
	private String provice;
	@Column(name = "provice_id")
	private int provice_id;
	@Column(name = "level")
	private int level;
	@Column(name = "code")
	private int code;
	@Column(name = "areaName")
	private String areaName;
	@Column(name = "parentId")
	private int parentId;
	@Column(name = "shortName")
	private String shortName;
	@Column(name = "newLevel")
	private int newLevel;
	@Column(name = "sort")
	private int sort;
	
	public City() {
	}

	public City(Parcel in) {
		id = in.readInt();
		cid = in.readInt();
		city = in.readString();
		provice = in.readString();
		provice_id = in.readInt();
		level = in.readInt();
		code = in.readInt();
		areaName = in.readString();
		parentId = in.readInt();
		shortName = in.readString();
		newLevel = in.readInt();
		sort = in.readInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public int getProvice_id() {
		return provice_id;
	}

	public void setProvice_id(int provice_id) {
		this.provice_id = provice_id;
	}

	public int getLevel() {
		return level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cid;
		result = prime * result + id;
		result = prime * result + provice_id;
		return result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public void setNewLevel(int newLevel) {
		this.newLevel = newLevel;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (cid != other.cid)
			return false;
		if (id != other.id)
			return false;
		if (provice_id != other.provice_id)
			return false;
		return true;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * @param dest
	 * @param flags
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(cid);
		dest.writeString(city);
		dest.writeString(provice);
		dest.writeInt(provice_id);
		dest.writeInt(level);
		dest.writeInt(code);
		dest.writeString(areaName);
		dest.writeInt(parentId);
		dest.writeString(shortName);
		dest.writeInt(newLevel);
		dest.writeInt(sort);
	}

	public static final Parcelable.Creator<City> CREATOR = new Creator<City>() {
		@Override
		public City[] newArray(int size) {
			return new City[size];
		}
		
		@Override
		public City createFromParcel(Parcel source) {
			return new City(source);
		}
	};
}
