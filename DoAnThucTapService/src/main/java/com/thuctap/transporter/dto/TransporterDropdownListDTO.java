package com.thuctap.transporter.dto;

public class TransporterDropdownListDTO {
	private Integer id;
	private String code;
	public TransporterDropdownListDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TransporterDropdownListDTO(Integer id, String code) {
		super();
		this.id = id;
		this.code = code;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}
