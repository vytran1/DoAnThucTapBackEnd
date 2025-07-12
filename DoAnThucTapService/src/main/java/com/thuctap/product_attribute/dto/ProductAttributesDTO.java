package com.thuctap.product_attribute.dto;

import java.util.Arrays;

public class ProductAttributesDTO {
	
	private String name;
	private String[] values;
	
	public ProductAttributesDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "ProductAttributesDTO [name=" + name + ", values=" + Arrays.toString(values) + "]";
	}
	
	
	
	
	
}
