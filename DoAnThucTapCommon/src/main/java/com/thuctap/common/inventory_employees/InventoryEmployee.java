package com.thuctap.common.inventory_employees;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_roles.InventoryRole;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_employees")
public class InventoryEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true, length = 125)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 10)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false, length = 65)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 65)
    private String lastName;

    @Column(name = "password", nullable = false, length = 125)
    private String password;

    @Column(name = "personal_image", length = 125)
    private String personalImage = "default.png";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_role_id")
    private InventoryRole inventoryRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
    
    

    // Getters & Setters

    public InventoryEmployee() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    

	public InventoryEmployee(Integer id) {
		super();
		this.id = id;
	}



	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalImage() {
        return personalImage;
    }

    public void setPersonalImage(String personalImage) {
        this.personalImage = personalImage;
    }

    public InventoryRole getInventoryRole() {
        return inventoryRole;
    }

    public void setInventoryRole(InventoryRole inventoryRole) {
        this.inventoryRole = inventoryRole;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
