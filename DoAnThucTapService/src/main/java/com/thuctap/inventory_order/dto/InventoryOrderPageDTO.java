package com.thuctap.inventory_order.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryOrderPageDTO {
	
	
	private Integer id;
	
	@JsonProperty("code")
	private String orderCode;
	
	@JsonProperty("stock_in_date")
	private LocalDateTime stockInDate;
	
	@JsonProperty("total_quantity")
	private Long totalQuantity;
	
	
	private String employee;
	
	
	
	
	
	public InventoryOrderPageDTO(Integer id, String orderCode, LocalDateTime stockInDate, Long totalQuantity, String employee) {
		super();
		this.id = id;
		this.orderCode = orderCode;
		this.stockInDate = stockInDate;
		this.totalQuantity = totalQuantity;
		this.employee = employee;
	}






	public InventoryOrderPageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}



	
	

	public Integer getId() {
		return id;
	}






	public void setId(Integer id) {
		this.id = id;
	}






	public String getOrderCode() {
		return orderCode;
	}


	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}


	public LocalDateTime getStockInDate() {
		return stockInDate;
	}


	public void setStockInDate(LocalDateTime stockInDate) {
		this.stockInDate = stockInDate;
	}


	public Long getTotalQuantity() {
		return totalQuantity;
	}


	public void setTotalQuantity(Long totalQuantity) {
		this.totalQuantity = totalQuantity;
	}


	public String getEmployee() {
		return employee;
	}


	public void setEmployee(String employee) {
		this.employee = employee;
	}
	
	
	
}
