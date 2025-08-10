package com.thuctap.exporting_form.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExportingFormOverviewDTO {
	private String code;
	@JsonProperty("inventory_move_from_code")
	private String inventoryMoveFromCode;
	@JsonProperty("inventory_move_to_code")
	private String inventoryMoveToCode;
	@JsonProperty("create_employee")
	private String createEmployee;
	@JsonProperty("commit_employee")
	private String commitEmployee;
	@JsonProperty("transporter_code")
	private String transporterCode;
	
	private List<ExportingFormDetailOverviewDTO> details;
	
	
	
	
	
	
	



	public ExportingFormOverviewDTO(String code, String inventoryMoveFromCode, String inventoryMoveToCode,
			String createEmployee, String commitEmployee, String transporterCode) {
		super();
		this.code = code;
		this.inventoryMoveFromCode = inventoryMoveFromCode;
		this.inventoryMoveToCode = inventoryMoveToCode;
		this.createEmployee = createEmployee;
		this.commitEmployee = commitEmployee;
		this.transporterCode = transporterCode;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}


	



	public String getInventoryMoveFromCode() {
		return inventoryMoveFromCode;
	}



	public void setInventoryMoveFromCode(String inventoryMoveFromCode) {
		this.inventoryMoveFromCode = inventoryMoveFromCode;
	}



	public String getInventoryMoveToCode() {
		return inventoryMoveToCode;
	}



	public void setInventoryMoveToCode(String inventoryMoveToCode) {
		this.inventoryMoveToCode = inventoryMoveToCode;
	}



	public String getCreateEmployee() {
		return createEmployee;
	}



	public void setCreateEmployee(String createEmployee) {
		this.createEmployee = createEmployee;
	}



	public String getCommitEmployee() {
		return commitEmployee;
	}



	public void setCommitEmployee(String commitEmployee) {
		this.commitEmployee = commitEmployee;
	}



	public String getTransporterCode() {
		return transporterCode;
	}



	public void setTransporterCode(String transporterCode) {
		this.transporterCode = transporterCode;
	}



	public List<ExportingFormDetailOverviewDTO> getDetails() {
		return details;
	}



	public void setDetails(List<ExportingFormDetailOverviewDTO> details) {
		this.details = details;
	}
	
	
	
	
	
	
	
}
