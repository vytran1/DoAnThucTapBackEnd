package com.thuctap.category.dto;

public class CategoryDropDownList {
	private Integer id;
	private String name;
	
	public CategoryDropDownList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoryDropDownList(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
