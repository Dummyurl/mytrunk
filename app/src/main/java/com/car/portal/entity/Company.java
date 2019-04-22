package com.car.portal.entity;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.car.portal.util.StringUtil;

import org.litepal.crud.LitePalSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Company extends LitePalSupport implements Parcelable{
	private Integer id;
	private String name;
	private String alias;
	private String tel;
	private String legaler;
	private String idcar;
	private String idcar_photo;
	private String address;
	private String decs;
	private String license_id;
	private String org_id;
	private String org_photo;
	private String office_tel;
	private String fax;
	private Integer server_flag;
	private Integer valid;
	private String logo;
	private String license_photo;
	private Integer operatorId;
	private String court;
	private Integer duration;
	private Float remainder;
	private Integer memberLocation;
	private Integer verify_val;
	private Integer message;
	private Integer company_type;
	private Integer location;
	private Date registerTime;
	private String provice;
	private String city;
	private Double safe_val;
	private Integer parentId;
	private String courtCity;
	private Integer cityId;
	private Integer cityCode;
	private Integer proviceId;
	private String prefecture;
	private Integer prefectureId;


	public Company() {
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLicense_photo() {
		return license_photo;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public void setLicense_photo(String license_photo) {
		this.license_photo = license_photo;
	}

	public String getLogo() {
		return logo;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCourt() {
		return court;
	}

	public void setCourt(String court) {
		this.court = court;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Float getRemainder() {
		return remainder;
	}

	public void setRemainder(Float remainder) {
		this.remainder = remainder;
	}

	public Integer getMemberLocation() {
		return memberLocation;
	}

	public void setMemberLocation(Integer memberLocation) {
		this.memberLocation = memberLocation;
	}

	public Integer getVerify_val() {
		return verify_val;
	}

	public void setVerify_val(Integer verify_val) {
		this.verify_val = verify_val;
	}

	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel == null ? "" : tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLegaler() {
		return legaler == null ? "" : legaler;
	}

	public void setLegaler(String legaler) {
		this.legaler = legaler;
	}

	public String getIdcar() {
		return idcar;
	}

	public void setIdcar(String idcar) {
		this.idcar = idcar;
	}

	public String getIdcar_photo() {
		return idcar_photo;
	}

	public void setIdcar_photo(String idcar_photo) {
		this.idcar_photo = idcar_photo;
	}

	public String getAddress() {
		return address == null ? "" : address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDecs() {
		return decs == null ? "" : decs;
	}

	public void setDecs(String decs) {
		this.decs = decs;
	}

	public String getLicense_id() {
		return license_id;
	}

	public void setLicense_id(String license_id) {
		this.license_id = license_id;
	}

	public String getOrg_id() {
		return org_id;
	}

	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}

	public String getOrg_photo() {
		return org_photo;
	}

	public void setOrg_photo(String org_photo) {
		this.org_photo = org_photo;
	}

	public String getOffice_tel() {
		return office_tel == null ? "" : office_tel;
	}

	public void setOffice_tel(String office_tel) {
		this.office_tel = office_tel;
	}

	public String getFax() {
		return fax == null ? "" : fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public Integer getServer_flag() {
		return server_flag;
	}

	public void setServer_flag(Integer server_flag) {
		this.server_flag = server_flag;
	}

	public Integer getCompany_type() {
		return company_type;
	}

	public void setCompany_type(Integer company_type) {
		this.company_type = company_type;
	}

	public String getCourtCity() {
		return courtCity;
	}

	public void setCourtCity(String courtCity) {
		this.courtCity = courtCity;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Double getSafe_val() {
		return safe_val;
	}

	public void setSafe_val(Double safe_val) {
		this.safe_val = safe_val;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getProviceId() {
		return proviceId;
	}

	public void setProviceId(Integer proviceId) {
		this.proviceId = proviceId;
	}

	public String getPrefecture() {
		return prefecture;
	}

	public void setPrefecture(String prefecture) {
		this.prefecture = prefecture;
	}

	public Integer getPrefectureId() {
		return prefectureId;
	}

	public void setPrefectureId(Integer prefectureId) {
		this.prefectureId = prefectureId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(alias);
		dest.writeString(tel);
		dest.writeString(legaler);
		dest.writeString(idcar);
		dest.writeString(idcar_photo);
		dest.writeString(office_tel);
		dest.writeString(fax);
		dest.writeInt(valid);
		dest.writeString(logo);
		dest.writeString(license_photo);
		dest.writeInt(operatorId);
		dest.writeInt(memberLocation);
		dest.writeInt(verify_val);
		dest.writeInt(message);
		dest.writeInt(company_type);
		dest.writeInt(location);
		dest.writeString(registerTime == null ? "" : format.format(registerTime));
		dest.writeString(provice);
		dest.writeString(city);
		dest.writeDouble(safe_val);
		dest.writeInt(parentId);
		dest.writeInt(cityId);
		dest.writeInt(proviceId);
		dest.writeString(prefecture);
		dest.writeInt(prefectureId);
		dest.writeString(address);
	}
	
	@SuppressLint("SimpleDateFormat")
	public Company(Parcel in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		id = in.readInt();
		name = in.readString();
		alias = in.readString();
		tel = in.readString();
		legaler = in.readString();
		idcar = in.readString();
		idcar_photo = in.readString();
		office_tel = in.readString();
		fax = in.readString();
		valid = in.readInt();
		logo = in.readString();
		license_photo = in.readString();
		operatorId = in.readInt();
		memberLocation = in.readInt();
		verify_val = in.readInt();
		message = in.readInt();
		company_type = in.readInt();
		location = in.readInt();
		String time = in.readString();
		if(StringUtil.isNullOrEmpty(time)) {
			registerTime = null;
		} else {
			try {
				registerTime = format.parse(time);
			} catch (ParseException e) {
				System.out.println(e.getMessage());
			}
		}
		provice = in.readString();
		city = in.readString();
		safe_val = in.readDouble();
		parentId = in.readInt();
		cityId = in.readInt();
		proviceId = in.readInt();
		prefecture = in.readString();
		prefectureId = in.readInt();
		address = in.readString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {
		
		@Override
		public Company[] newArray(int size) {
			Company[] coms = new Company[size];
			return coms;
		}
		
		@Override
		public Company createFromParcel(Parcel source) {
			return new Company(source);
		}
	};
}
