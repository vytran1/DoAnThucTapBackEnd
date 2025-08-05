package com.thuctap.common.authentication_type;

import jakarta.persistence.*;

@Entity
@Table(name = "authentication_types")
public class AuthenticationType {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 65)
    private String name;

    // Constructors
    public AuthenticationType() {
    }

    public AuthenticationType(String name) {
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
