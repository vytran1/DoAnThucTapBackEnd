package com.thuctap.common.exporting_form;
import com.thuctap.common.product_variant.ProductVariant;

import jakarta.persistence.*;

@Entity
@Table(name = "exporting_form_details")
public class ExportingFormDetail {
	  	@EmbeddedId
	    private ExportingFormDetailId id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @MapsId("exportingFormId")
	    @JoinColumn(name = "exporting_form_id", nullable = false)
	    private ExportingForm exportingForm;

	    @ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "sku", referencedColumnName = "sku", updatable = false, insertable = false)
		 private ProductVariant productVariant;

	    @Column(name = "quantity", nullable = false)
	    private Integer quantity;

		public ExportingFormDetail() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ExportingFormDetailId getId() {
			return id;
		}

		public void setId(ExportingFormDetailId id) {
			this.id = id;
		}

		public ExportingForm getExportingForm() {
			return exportingForm;
		}

		public void setExportingForm(ExportingForm exportingForm) {
			this.exportingForm = exportingForm;
		}

		public ProductVariant getProductVariant() {
			return productVariant;
		}

		public void setProductVariant(ProductVariant productVariant) {
			this.productVariant = productVariant;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}
	    
	    
}
