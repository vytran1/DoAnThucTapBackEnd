package com.thuctap.common.transporter;
import java.time.LocalDateTime;

import jakarta.persistence.*;


@Entity
@Table(name = "transporters")
public class Transporter {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(nullable = false, length = 125)
	    private String name;

	    @Column(nullable = false, length = 125)
	    private String website;

	    @Column(name = "transporter_code", nullable = false, unique = true, length = 125)
	    private String transporterCode;

	    @Column(name = "contact_mail", nullable = false, length = 125)
	    private String contactMail;

	    @Column(name = "contact_number", nullable = false, length = 125)
	    private String contactNumber;

	    @Column(nullable = false, length = 125)
	    private String address;

	    @Column(name = "created_at", insertable = false, updatable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at", insertable = false)
	    private LocalDateTime updatedAt;

	    @Column(name = "is_delete")
	    private Boolean isDelete = false;
	    
	    
	    

		public Transporter() {
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

		public String getTransporterCode() {
			return transporterCode;
		}

		public void setTransporterCode(String transporterCode) {
			this.transporterCode = transporterCode;
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
	    
	    
	
}
