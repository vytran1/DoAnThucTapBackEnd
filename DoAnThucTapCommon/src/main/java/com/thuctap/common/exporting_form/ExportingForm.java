package com.thuctap.common.exporting_form;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.transporter.Transporter;

import jakarta.persistence.*;

@Entity
@Table(name = "exporting_forms")
public class ExportingForm {
	  	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;

	    @Column(name = "exporting_form_code", nullable = false, unique = true, length = 125)
	    private String exportingFormCode;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "move_from_inventory_id", nullable = false)
	    private Inventory moveFromInventory;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "move_to_inventory_id", nullable = false)
	    private Inventory moveToInventory;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "create_employee_id", nullable = false)
	    private InventoryEmployee createEmployee;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "receive_employee_id", nullable = false)
	    private InventoryEmployee receiveEmployee;

	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "transporter_id", nullable = false)
	    private Transporter transporter;

	    @Column(name = "shipping_fee", precision = 12, scale = 2)
	    private BigDecimal shippingFee = BigDecimal.ZERO;

	    @Column(name = "quote_shipping_fee", precision = 12, scale = 2)
	    private BigDecimal quoteShippingFee = BigDecimal.ZERO;

	    @Column(name = "created_at", insertable = false, updatable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "completed_at", insertable = false)
	    private LocalDateTime completedAt;
	    
	    
	    

		public ExportingForm() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getExportingFormCode() {
			return exportingFormCode;
		}

		public void setExportingFormCode(String exportingFormCode) {
			this.exportingFormCode = exportingFormCode;
		}

		public Inventory getMoveFromInventory() {
			return moveFromInventory;
		}

		public void setMoveFromInventory(Inventory moveFromInventory) {
			this.moveFromInventory = moveFromInventory;
		}

		public Inventory getMoveToInventory() {
			return moveToInventory;
		}

		public void setMoveToInventory(Inventory moveToInventory) {
			this.moveToInventory = moveToInventory;
		}

		public InventoryEmployee getCreateEmployee() {
			return createEmployee;
		}

		public void setCreateEmployee(InventoryEmployee createEmployee) {
			this.createEmployee = createEmployee;
		}

		public InventoryEmployee getReceiveEmployee() {
			return receiveEmployee;
		}

		public void setReceiveEmployee(InventoryEmployee receiveEmployee) {
			this.receiveEmployee = receiveEmployee;
		}

		public Transporter getTransporter() {
			return transporter;
		}

		public void setTransporter(Transporter transporter) {
			this.transporter = transporter;
		}

		public BigDecimal getShippingFee() {
			return shippingFee;
		}

		public void setShippingFee(BigDecimal shippingFee) {
			this.shippingFee = shippingFee;
		}

		public BigDecimal getQuoteShippingFee() {
			return quoteShippingFee;
		}

		public void setQuoteShippingFee(BigDecimal quoteShippingFee) {
			this.quoteShippingFee = quoteShippingFee;
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
	    
	    
	
	
}
