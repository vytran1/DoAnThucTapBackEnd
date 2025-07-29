package com.thuctap.reports.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

public class ReportItemDTO {
	
	private Date date;
	private BigDecimal value;
	
	
	

	public ReportItemDTO(Date date, BigDecimal value) {
		super();
		this.date = date;
		this.value = value;
	}


	public ReportItemDTO() {
	}
	
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
	
	
}
