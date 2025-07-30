package com.thuctap.common.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "invoice_code", nullable = false, unique = true, length = 225)
	    private String invoiceCode;

	    @Column(name = "tax", precision = 12, scale = 2)
	    private BigDecimal tax;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "employee_id", nullable = false)
	    private InventoryEmployee employee;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "inventory_id", nullable = false)
	    private Inventory inventory;

	    @Column(name = "created_at")
	    private LocalDateTime createdAt;

		public Invoice() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getInvoiceCode() {
			return invoiceCode;
		}

		public void setInvoiceCode(String invoiceCode) {
			this.invoiceCode = invoiceCode;
		}

		public BigDecimal getTax() {
			return tax;
		}

		public void setTax(BigDecimal tax) {
			this.tax = tax;
		}

		public InventoryEmployee getEmployee() {
			return employee;
		}

		public void setEmployee(InventoryEmployee employee) {
			this.employee = employee;
		}

		public Inventory getInventory() {
			return inventory;
		}

		public void setInventory(Inventory inventory) {
			this.inventory = inventory;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    
	
}
