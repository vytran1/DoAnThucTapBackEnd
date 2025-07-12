package com.thuctap.product.dto;

public class ProductFindAllDTO {
	private Integer id;
	private String image;
	private String name;
	private String sku;
	private String brand;
	private String category;
	
	
	

	public ProductFindAllDTO(Integer id, String image, String name, String sku, String brand, String category) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.sku = sku;
		this.brand = brand;
		this.category = category;
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


	public String getSku() {
		return sku;
	}


	public void setSku(String sku) {
		this.sku = sku;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	
	
}
