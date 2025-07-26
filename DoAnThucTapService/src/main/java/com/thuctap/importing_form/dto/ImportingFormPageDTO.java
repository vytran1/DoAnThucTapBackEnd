package com.thuctap.importing_form.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImportingFormPageDTO {
	private Integer id;
	private String code;
	private String employee;
	@JsonProperty("completed_at")
	private LocalDateTime completedAt;
	@JsonProperty("total_quantity")
	private Long totalQuantity;
	private BigDecimal values;
	
	
	


	public ImportingFormPageDTO(Integer id, String code, String employee, LocalDateTime completedAt, Long totalQuantity,
			BigDecimal values) {
		super();
		this.id = id;
		this.code = code;
		this.employee = employee;
		this.completedAt = completedAt;
		this.totalQuantity = totalQuantity;
		this.values = values;
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


	public String getEmployee() {
		return employee;
	}


	public void setEmployee(String employee) {
		this.employee = employee;
	}


	public LocalDateTime getCompletedAt() {
		return completedAt;
	}


	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}


	public Long getTotalQuantity() {
		return totalQuantity;
	}


	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}


	public BigDecimal getValues() {
		return values;
	}


	public void setValues(BigDecimal values) {
		this.values = values;
	}
	
	
	
	
	
	
}
