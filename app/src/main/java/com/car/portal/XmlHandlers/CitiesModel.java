package com.car.portal.XmlHandlers;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.LitePalSupport;

public class CitiesModel extends LitePalSupport implements Parcelable {
	private int id;
	private int cid;
	private String name;
	private int code;
	private int parentCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public int getParentCode() {
		return parentCode;
	}

	public void setParentCode(int parentCode) {
		this.parentCode = parentCode;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeInt(this.cid);
		dest.writeString(this.name);
		dest.writeInt(this.code);
		dest.writeInt(this.parentCode);
	}

	public CitiesModel() {
	}

	protected CitiesModel(Parcel in) {
		this.id = in.readInt();
		this.cid = in.readInt();
		this.name = in.readString();
		this.code = in.readInt();
		this.parentCode = in.readInt();
	}

	public static final Parcelable.Creator<CitiesModel> CREATOR = new Parcelable.Creator<CitiesModel>() {
		@Override
		public CitiesModel createFromParcel(Parcel source) {
			return new CitiesModel(source);
		}

		@Override
		public CitiesModel[] newArray(int size) {
			return new CitiesModel[size];
		}
	};
}
