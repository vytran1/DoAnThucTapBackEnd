package com.thuctap.importing_form.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImportingFormOverviewDTO {
	
	private String code;
	private String employee;
	@JsonProperty("created_at")
	private LocalDateTime createdAt;
	@JsonProperty("line_items")
	private Integer lineItems;
	private List<ImportingFormDetailOverviewDTO> details;
	
	
	public ImportingFormOverviewDTO(String code, String employee, LocalDateTime createdAt) {
		super();
		this.code = code;
		this.employee = employee;
		this.createdAt = createdAt;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getEmployee() {
		return employee;
	}


	public void setEmployee(String employee) {
		this.employee = employee;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public Integer getLineItems() {
		return lineItems;
	}


	public void setLineItems(Integer lineItems) {
		this.lineItems = lineItems;
	}


	public List<ImportingFormDetailOverviewDTO> getDetails() {
		return details;
	}


	public void setDetails(List<ImportingFormDetailOverviewDTO> details) {
		this.details = details;
	}
	
	
	
	
	
	
}
