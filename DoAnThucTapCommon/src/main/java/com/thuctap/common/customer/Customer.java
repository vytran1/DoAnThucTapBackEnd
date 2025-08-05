package com.thuctap.common.customer;
import jakarta.persistence.*;
import com.thuctap.common.authentication_type.AuthenticationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
public class Customer {
	
	   	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(nullable = false, length = 65, unique = true)
	    private String email;

	    @Column(nullable = false, length = 255)
	    private String password;

	    @Column(name = "first_name", nullable = false, length = 65)
	    private String firstName;

	    @Column(name = "last_name", nullable = false, length = 65)
	    private String lastName;

	    @Column(name = "phone_number", nullable = false, length = 10)
	    private String phoneNumber;

	    @Column(nullable = false, length = 125)
	    private String address;

	    @Column(name = "created_at", columnDefinition = "DATETIME")
	    private LocalDateTime createdAt;

	    @Column(name = "is_deleted")
	    private Boolean isDeleted = false;

	    @Column(name = "reset_password_token", length = 255)
	    private String resetPasswordToken;

	    @ManyToOne
	    @JoinColumn(name = "authentication_type_id", referencedColumnName = "id")
	    private AuthenticationType authenticationType;
	    
	    public Customer() {
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

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
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

		public Boolean getIsDeleted() {
			return isDeleted;
		}

		public void setIsDeleted(Boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

		public String getResetPasswordToken() {
			return resetPasswordToken;
		}

		public void setResetPasswordToken(String resetPasswordToken) {
			this.resetPasswordToken = resetPasswordToken;
		}

		public AuthenticationType getAuthenticationType() {
			return authenticationType;
		}

		public void setAuthenticationType(AuthenticationType authenticationType) {
			this.authenticationType = authenticationType;
		}
	    
	    
		 @Transient
		 public String getFullName() {
		    return this.firstName + " " + this.lastName;
		 }
		 
		 @Transient
		 public void setFullName(String fullName) {
			 String[] names = fullName.split(" ");
			 this.firstName = names[0];
			 this.lastName = names[1];
		 }
	
	
}
