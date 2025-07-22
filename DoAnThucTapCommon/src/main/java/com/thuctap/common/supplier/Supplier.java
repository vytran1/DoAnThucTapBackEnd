package com.thuctap.common.supplier;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "suppliers")
public class Supplier {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(nullable = false, length = 125)
	    private String name;

	    @Column(nullable = false, length = 125)
	    private String website;

	    @Column(name = "supplier_code", nullable = false, length = 125, unique = true)
	    private String supplierCode;

	    @Column(name = "contact_mail", nullable = false, length = 125)
	    private String contactMail;

	    @Column(name = "contact_number", nullable = false, length = 125)
	    private String contactNumber;

	    @Column(nullable = false, length = 125)
	    private String address;

	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	    @Column(name = "is_delete")
	    private Boolean isDelete;
	    
	    @Column(name = "secret_key")
	    private String secretKey;
	    
	    
	    

		public Supplier(Integer id) {
			super();
			this.id = id;
		}

		public Supplier() {
			super();
			// TODO Auto-generated constructor stub
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

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}

		public String getSupplierCode() {
			return supplierCode;
		}

		public void setSupplierCode(String supplierCode) {
			this.supplierCode = supplierCode;
		}

		public String getContactMail() {
			return contactMail;
		}

		public void setContactMail(String contactMail) {
			this.contactMail = contactMail;
		}

		public String getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(String contactNumber) {
			this.contactNumber = contactNumber;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}

		public Boolean getIsDelete() {
			return isDelete;
		}

		public void setIsDelete(Boolean isDelete) {
			this.isDelete = isDelete;
		}
		
		
	    
	    public String getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(String secretKey) {
			this.secretKey = secretKey;
		}

		@Transient
	    public String getFullContact() {
	    	return this.name + "-" + this.supplierCode; 
	    }
	    
}
