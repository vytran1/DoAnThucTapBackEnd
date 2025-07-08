package com.thuctap.common.inventory_roles;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory_roles")
public class InventoryRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 65)
    private String name;

    @Column(name = "description", length = 255)
    private String description;
    
    
    
    // Getters & Setters

    public InventoryRole() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

	public InventoryRole(Integer id) {
		super();
		this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
