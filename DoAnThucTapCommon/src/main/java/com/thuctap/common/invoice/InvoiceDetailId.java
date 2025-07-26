package com.thuctap.common.invoice;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class InvoiceDetailId {

	 	@Column(name = "invoice_id")
	    private Integer invoiceId;

	    @Column(name = "sku", length = 125)
	    private String sku;
	    
	    

		public InvoiceDetailId() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		

		public InvoiceDetailId(Integer invoiceId, String sku) {
			super();
			this.invoiceId = invoiceId;
			this.sku = sku;
		}



		public Integer getInvoiceId() {
			return invoiceId;
		}

		public void setInvoiceId(Integer invoiceId) {
			this.invoiceId = invoiceId;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}



		@Override
		public int hashCode() {
			return Objects.hash(invoiceId, sku);
		}



		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			InvoiceDetailId other = (InvoiceDetailId) obj;
			return Objects.equals(invoiceId, other.invoiceId) && Objects.equals(sku, other.sku);
		}
	    
	    
	
}
