package com.thuctap.common.importing_form;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.common.supplier.Supplier;

import jakarta.persistence.*;

@Entity
@Table(name = "importing_form")
public class ImportingForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "importing_form_code", nullable = false, unique = true, length = 225)
	private String importingFormCode;

	@Column(name = "shipping_fee", precision = 12, scale = 2)
	private BigDecimal shippingFee = BigDecimal.ZERO;

	@Column(name = "created_at", columnDefinition = "DATETIME")
	private LocalDateTime createdAt;

	@Column(name = "completed_at", columnDefinition = "DATETIME")
	private LocalDateTime completedAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false, unique = true)
	private InventoryOrder order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private InventoryEmployee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inventory_id", nullable = false)
	private Inventory inventory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", nullable = false)
	private Supplier supplier;

	public ImportingForm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImportingFormCode() {
		return importingFormCode;
	}

	public void setImportingFormCode(String importingFormCode) {
		this.importingFormCode = importingFormCode;
	}

	public BigDecimal getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(BigDecimal shippingFee) {
		this.shippingFee = shippingFee;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public InventoryOrder getOrder() {
		return order;
	}

	public void setOrder(InventoryOrder order) {
		this.order = order;
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

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

}
