package com.thuctap.common.invoice;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

import com.thuctap.common.product_variant.ProductVariant;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice_details")
public class InvoiceDetail {

	 	@EmbeddedId
	    private InvoiceDetailId id;

	    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
	    private BigDecimal unitPrice;

	    @Column(name = "quantity", nullable = false)
	    private Integer quantity;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @MapsId("invoiceId")
	    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
	    private Invoice invoice;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
	    private ProductVariant productVariant;

		public InvoiceDetail() {
			super();
			// TODO Auto-generated constructor stub
		}

		public InvoiceDetailId getId() {
			return id;
		}

		public void setId(InvoiceDetailId id) {
			this.id = id;
		}

		public BigDecimal getUnitPrice() {
			return unitPrice;
		}

		public void setUnitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Invoice getInvoice() {
			return invoice;
		}

		public void setInvoice(Invoice invoice) {
			this.invoice = invoice;
		}

		public ProductVariant getProductVariant() {
			return productVariant;
		}

		public void setProductVariant(ProductVariant productVariant) {
			this.productVariant = productVariant;
		}
	    
	    
	
}
