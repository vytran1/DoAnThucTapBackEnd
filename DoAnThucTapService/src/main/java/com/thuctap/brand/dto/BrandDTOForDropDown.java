package com.thuctap.brand.dto;

public class BrandDTOForDropDown {
	private Integer id;
	private String name;
	
	
	
	public BrandDTOForDropDown(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public BrandDTOForDropDown() {
		super();
		// TODO Auto-generated constructor stub
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
	
	
	
}
