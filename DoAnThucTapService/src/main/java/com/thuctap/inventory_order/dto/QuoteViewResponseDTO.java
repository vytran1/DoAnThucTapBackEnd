package com.thuctap.inventory_order.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thuctap.utility.QuoteData;

public class QuoteViewResponseDTO {
	
	private boolean status;
	@JsonProperty("status_type")
	private String statusType;
	private QuoteData data;
	private List<InventoryOrderDetailForOverviewDTO> details;
	public QuoteViewResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	



	public boolean isStatus() {
		return status;
	}







	public void setStatus(boolean status) {
		this.status = status;
	}


	




	public List<InventoryOrderDetailForOverviewDTO> getDetails() {
		return details;
	}







	public void setDetails(List<InventoryOrderDetailForOverviewDTO> details) {
		this.details = details;
	}







	public QuoteData getData() {
		return data;
	}
	public void setData(QuoteData data) {
		this.data = data;
	}







	public String getStatusType() {
		return statusType;
	}







	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	
	
	
}
