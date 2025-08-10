package com.thuctap.exporting_form.dto;

public class CreateExportingFormDetailDTO {
	private String sku;
	private String name;
	private Integer quantity;
	private Integer maxQuantity;
	public CreateExportingFormDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getMaxQuantity() {
		return maxQuantity;
	}
	public void setMaxQuantity(Integer maxQuantity) {
		this.maxQuantity = maxQuantity;
	}
	@Override
	public String toString() {
		return "CreateExportingFormDetailDTO [sku=" + sku + ", name=" + name + ", quantity=" + quantity
				+ ", maxQuantity=" + maxQuantity + "]";
	}
	
	
	
}	
