package com.thuctap.exporting_form.dto;

import java.util.List;

public class CreateExportingFormRequest {
	private Integer transporter;
	private Integer inventory;
	private List<CreateExportingFormDetailDTO> details;
	
	
	public CreateExportingFormRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getTransporter() {
		return transporter;
	}
	public void setTransporter(Integer transporter) {
		this.transporter = transporter;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public List<CreateExportingFormDetailDTO> getDetails() {
		return details;
	}
	public void setDetails(List<CreateExportingFormDetailDTO> details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "CreateExportingFormRequest [transporter=" + transporter + ", inventory=" + inventory + ", details="
				+ details + "]";
	}
	
	
	
	
	
}
