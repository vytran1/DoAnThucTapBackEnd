package com.thuctap.stocking.dto;

public class StockingInventorySearchDTO {
	private Integer id;	
	private String image;
	private String name;
	private Integer quantity;
	
	
	
	public StockingInventorySearchDTO(Integer id, String image, String name, Integer quantity) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.quantity = quantity;
	}
	public StockingInventorySearchDTO(String image, String name, Integer quantity) {
		super();
		this.image = image;
		this.name = name;
		this.quantity = quantity;
	}
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	
	
	
}
