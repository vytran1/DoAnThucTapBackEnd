package com.thuctap.common.exporting_form;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuotePriceData {
	private String companyName;
	private String companyCode;
	private String address;
	private String phone;
	private String email;
	private String quotedBy;
	private String position;
	private LocalDateTime quotationDate;
	private Integer totalKilometer;
	private BigDecimal pricePerKilometer;
	private BigDecimal pricePerItem;
	private BigDecimal discount;
	private String reason;
	
	
	
	
	
	public QuotePriceData() {
		super();
		// TODO Auto-generated constructor stub
	}
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
	public BigDecimal getPricePerKilometer() {
		return pricePerKilometer;
	}
	public void setPricePerKilometer(BigDecimal pricePerKilometer) {
		this.pricePerKilometer = pricePerKilometer;
	}
	public BigDecimal getPricePerItem() {
		return pricePerItem;
	}
	public void setPricePerItem(BigDecimal pricePerItem) {
		this.pricePerItem = pricePerItem;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Integer getTotalKilometer() {
		return totalKilometer;
	}
	public void setTotalKilometer(Integer totalKilometer) {
		this.totalKilometer = totalKilometer;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
	
}
