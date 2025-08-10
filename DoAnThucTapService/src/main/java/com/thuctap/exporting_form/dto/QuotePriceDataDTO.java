package com.thuctap.exporting_form.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thuctap.common.exporting_form.QuotePriceData;
import com.thuctap.utility.UtilityGlobal;

public class QuotePriceDataDTO {
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
	private String quotationDate;
	@JsonProperty("total_kilometer")
	private Integer totalKilometer;
	@JsonProperty("price_per_kilometer")
	private BigDecimal pricePerKilometer;
	@JsonProperty("price_per_item")
	private BigDecimal pricePerItem;
	private BigDecimal discount;

	public QuotePriceDataDTO(QuotePriceData source) {
		this.companyName = source.getCompanyName();
		this.companyCode = source.getCompanyCode();
		this.address = source.getAddress();
		this.phone = source.getPhone();
		this.email = source.getEmail();
		this.quotedBy = source.getQuotedBy();
		this.position = source.getPosition();

		if (source.getQuotationDate() != null) {
			this.quotationDate = source.getQuotationDate().format(UtilityGlobal.getVietnamDateTimeFormatter());
		}

		this.totalKilometer = source.getTotalKilometer();
		this.pricePerKilometer = source.getPricePerKilometer();
		this.pricePerItem = source.getPricePerItem();
		this.discount = source.getDiscount();
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

	public String getQuotationDate() {
		return quotationDate;
	}

	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}

	public Integer getTotalKilometer() {
		return totalKilometer;
	}

	public void setTotalKilometer(Integer totalKilometer) {
		this.totalKilometer = totalKilometer;
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

}
