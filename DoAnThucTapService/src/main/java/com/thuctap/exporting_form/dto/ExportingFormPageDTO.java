package com.thuctap.exporting_form.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExportingFormPageDTO {
	
	private Integer id;
	
	@JsonProperty("exporting_form_code")
	private String exportingFormCode;
	
	@JsonProperty("move_from_inventory_code")
	private String moveFromInventoryCode;
	
	@JsonProperty("move_to_inventory_code")
	private String moveToInventoryCode;
	
	@JsonProperty("total_quantity")
	private Long totalQuantity;
	
	private String status;

	public ExportingFormPageDTO(Integer id, String exportingFormCode, String moveFromInventoryCode, String moveToInventoryCode,
			Long totalQuantity, String status) {
		super();
		this.id = id;
		this.exportingFormCode = exportingFormCode;
		this.moveFromInventoryCode = moveFromInventoryCode;
		this.moveToInventoryCode = moveToInventoryCode;
		this.totalQuantity = totalQuantity;
		this.status = status;
	}

	public ExportingFormPageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExportingFormCode() {
		return exportingFormCode;
	}

	public void setExportingFormCode(String exportingFormCode) {
		this.exportingFormCode = exportingFormCode;
	}

	public String getMoveFromInventoryCode() {
		return moveFromInventoryCode;
	}

	public void setMoveFromInventoryCode(String moveFromInventoryCode) {
		this.moveFromInventoryCode = moveFromInventoryCode;
	}

	public String getMoveToInventoryCode() {
		return moveToInventoryCode;
	}

	public void setMoveToInventoryCode(String moveToInventoryCode) {
		this.moveToInventoryCode = moveToInventoryCode;
	}

	public Long getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
