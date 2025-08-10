package com.thuctap.exporting_form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExportingFormStatusDTO {
	private String updater;
	@JsonProperty("created_at")
	private String createdAt;
	private String status;
	private String description;
	
	
	public ExportingFormStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getUpdater() {
		return updater;
	}


	public void setUpdater(String updater) {
		this.updater = updater;
	}


	public String getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
