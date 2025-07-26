package com.thuctap.common.exporting_form;
import java.time.LocalDateTime;

import com.thuctap.common.exporting_status.ExportingStatus;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.transporter.Transporter;

import jakarta.persistence.*;


@Entity
@Table(name = "exporting_form_status")
public class ExportingFormStatus {
	  	@EmbeddedId
	    private ExportingFormStatusId id;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @MapsId("exportingFormId")
	    @JoinColumn(name = "exporting_form_id", nullable = false)
	    private ExportingForm exportingForm;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @MapsId("statusId")
	    @JoinColumn(name = "status_id", nullable = false)
	    private ExportingStatus status;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "transporter_id")
	    private Transporter transporter;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "employee_id")
	    private InventoryEmployee employee;

	    @Column(name = "created_at", insertable = false, updatable = false)
	    private LocalDateTime createdAt;

		public ExportingFormStatus() {
			super();
			// TODO Auto-generated constructor stub
		}

		public ExportingFormStatusId getId() {
			return id;
		}

		public void setId(ExportingFormStatusId id) {
			this.id = id;
		}

		public ExportingForm getExportingForm() {
			return exportingForm;
		}

		public void setExportingForm(ExportingForm exportingForm) {
			this.exportingForm = exportingForm;
		}

		public ExportingStatus getStatus() {
			return status;
		}

		public void setStatus(ExportingStatus status) {
			this.status = status;
		}

		public Transporter getTransporter() {
			return transporter;
		}

		public void setTransporter(Transporter transporter) {
			this.transporter = transporter;
		}

		public InventoryEmployee getEmployee() {
			return employee;
		}

		public void setEmployee(InventoryEmployee employee) {
			this.employee = employee;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    
}
