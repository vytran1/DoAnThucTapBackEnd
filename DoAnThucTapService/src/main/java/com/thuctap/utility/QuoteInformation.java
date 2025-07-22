package com.thuctap.utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteInformation {
		@JsonProperty("company_name")
		private String companyName;
		@JsonProperty("company_code")
	    private String companyCode;
	    private String address;
	    private String phone;
	    private String email;
	    @JsonProperty("quoted_by")
	    private String quotedBy;
	    private String position;
	    @JsonProperty("quotation_date")
	    private LocalDateTime quotationDate;
	    @JsonProperty("order_code")
	    private String orderCode;
	    @JsonProperty("shipping_fee")
	    private BigDecimal shippingFee;
	    private String reason;
		public String getCompanyName() {
			return companyName;
		}
		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}
		public String getCompanyCode() {
			return companyCode;
		}
		public void setCompanyCode(String companyCode) {
			this.companyCode = companyCode;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getQuotedBy() {
			return quotedBy;
		}
		public void setQuotedBy(String quotedBy) {
			this.quotedBy = quotedBy;
		}
		public String getPosition() {
			return position;
		}
		public void setPosition(String position) {
			this.position = position;
		}
		public LocalDateTime getQuotationDate() {
			return quotationDate;
		}
		public void setQuotationDate(LocalDateTime quotationDate) {
			this.quotationDate = quotationDate;
		}
		public String getOrderCode() {
			return orderCode;
		}
		public void setOrderCode(String orderCode) {
			this.orderCode = orderCode;
		}
		
		
		
		public BigDecimal getShippingFee() {
			return shippingFee;
		}
		public void setShippingFee(BigDecimal shippingFee) {
			this.shippingFee = shippingFee;
		}
		
		
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		@Override
		public String toString() {
			return "QuoteInformation [companyName=" + companyName + ", companyCode=" + companyCode + ", address="
					+ address + ", phone=" + phone + ", email=" + email + ", quotedBy=" + quotedBy + ", position="
					+ position + ", quotationDate=" + quotationDate + ", orderCode=" + orderCode + ", shippingFee="
					+ shippingFee + ", reason=" + reason + "]";
		}
		
	    
	    
}
