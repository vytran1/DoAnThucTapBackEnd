package com.thuctap.common.exporting_form;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ExportingFormStatusId {
		@Column(name = "exporting_form_id")
	    private Integer exportingFormId;

	    @Column(name = "status_id")
	    private Integer statusId;
	    
	    public ExportingFormStatusId() {}

	    public ExportingFormStatusId(Integer exportingFormId, Integer statusId) {
	        this.exportingFormId = exportingFormId;
	        this.statusId = statusId;
	    }

		public Integer getExportingFormId() {
			return exportingFormId;
		}

		public void setExportingFormId(Integer exportingFormId) {
			this.exportingFormId = exportingFormId;
		}

		public Integer getStatusId() {
			return statusId;
		}

		public void setStatusId(Integer statusId) {
			this.statusId = statusId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(exportingFormId, statusId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExportingFormStatusId other = (ExportingFormStatusId) obj;
			return Objects.equals(exportingFormId, other.exportingFormId) && Objects.equals(statusId, other.statusId);
		}
	    
	    
}
