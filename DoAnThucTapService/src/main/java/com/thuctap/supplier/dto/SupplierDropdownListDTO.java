package com.thuctap.supplier.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SupplierDropdownListDTO {
	private Integer id;
	
	@JsonProperty("supplier_code")
	private String supplierCode;
	
	private String name;
	
	

	public SupplierDropdownListDTO(Integer id, String supplierCode,String name) {
		super();
		this.id = id;
		this.supplierCode = supplierCode;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
