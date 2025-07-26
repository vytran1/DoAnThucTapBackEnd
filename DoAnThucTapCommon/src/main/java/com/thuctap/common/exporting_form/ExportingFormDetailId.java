package com.thuctap.common.exporting_form;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExportingFormDetailId {
	  	@Column(name = "exporting_form_id")
	    private Integer exportingFormId;

	    @Column(name = "sku", length = 125)
	    private String sku;

	    public ExportingFormDetailId() {}

	    public ExportingFormDetailId(Integer exportingFormId, String sku) {
	        this.exportingFormId = exportingFormId;
	        this.sku = sku;
	    }

		public Integer getExportingFormId() {
			return exportingFormId;
		}

		public void setExportingFormId(Integer exportingFormId) {
			this.exportingFormId = exportingFormId;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		@Override
		public int hashCode() {
			return Objects.hash(exportingFormId, sku);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExportingFormDetailId other = (ExportingFormDetailId) obj;
			return Objects.equals(exportingFormId, other.exportingFormId) && Objects.equals(sku, other.sku);
		}
	    
	    
}
