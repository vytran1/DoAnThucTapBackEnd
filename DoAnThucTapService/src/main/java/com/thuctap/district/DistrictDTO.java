package com.thuctap.district;

public class DistrictDTO {
	private Integer id;
	private String name;
	private Integer provinceId;	
	private String provinceName;
	
	public DistrictDTO() {
		super();
	}
	
	public DistrictDTO(Integer id, String name, Integer provinceId, String provinceName) {
		super();
		this.id = id;
		this.name = name;
		this.provinceId = provinceId;
		this.provinceName = provinceName;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	
	
	
}
