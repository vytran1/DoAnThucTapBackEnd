package com.thuctap.common.importing_form;
import jakarta.persistence.*;
import java.math.BigDecimal;

import com.thuctap.common.product_variant.ProductVariant;

@Entity
@Table(name = "importing_form_details")
public class ImportingFormDetail {
	
	
	 @EmbeddedId
	 private ImportingFormDetailId id;
	 
	 @ManyToOne(fetch = FetchType.LAZY)
	 @MapsId("inventoryFormId")
	 @JoinColumn(name = "inventory_form_id")
	 private ImportingForm importingForm;

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "sku", referencedColumnName = "sku", updatable = false, insertable = false)
	 private ProductVariant productVariant;

	 @Column(name = "cost_price", precision = 12, scale = 2, nullable = false)
	 private BigDecimal costPrice;

	 @Column(name = "quantity", nullable = false)
	 private Integer quantity;

	public ImportingFormDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ImportingFormDetailId getId() {
		return id;
	}

	public void setId(ImportingFormDetailId id) {
		this.id = id;
	}

	public ImportingForm getImportingForm() {
		return importingForm;
	}

	public void setImportingForm(ImportingForm importingForm) {
		this.importingForm = importingForm;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	 
	 
}
