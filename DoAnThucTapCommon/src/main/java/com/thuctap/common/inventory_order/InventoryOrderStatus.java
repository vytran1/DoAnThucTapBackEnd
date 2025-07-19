package com.thuctap.common.inventory_order;

import java.time.LocalDateTime;

import com.thuctap.common.importing_status.ImportingStatus;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.supplier.Supplier;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "inventory_order_status")
@IdClass(InventoryOrderStatusId.class)
public class InventoryOrderStatus {
	  	@Id
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "order_id", nullable = false)
	    private InventoryOrder order;

	    @Id
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "status_id", nullable = false)
	    private ImportingStatus status;

	    @Column(name = "created_at")
	    private LocalDateTime createdAt = LocalDateTime.now();

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "supplier_id")
	    private Supplier supplier;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "employee_id")
	    private InventoryEmployee employee;

		public InventoryOrderStatus() {
			super();
			// TODO Auto-generated constructor stub
		}

		public InventoryOrder getOrder() {
			return order;
		}

		public void setOrder(InventoryOrder order) {
			this.order = order;
		}

		public ImportingStatus getStatus() {
			return status;
		}

		public void setStatus(ImportingStatus status) {
			this.status = status;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public Supplier getSupplier() {
			return supplier;
		}

		public void setSupplier(Supplier supplier) {
			this.supplier = supplier;
		}

		public InventoryEmployee getEmployee() {
			return employee;
		}

		public void setEmployee(InventoryEmployee employee) {
			this.employee = employee;
		}
	    
	    
}
