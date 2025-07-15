package com.thuctap.common.audit;

import jakarta.persistence.*;


import java.time.LocalDateTime;

import com.thuctap.common.inventory_employees.InventoryEmployee;

@Entity
@Table(name = "audits")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;

    private String tableName;

    private String recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_employee_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_audit_employee"))
    private InventoryEmployee inventoryEmployee;

    private String fieldName;

    private String action; // CREATE, UPDATE, DELETE

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    private Boolean isDelete;
    
    

    public Audit() {
		super();
		// TODO Auto-generated constructor stub
	}

    

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public String getTableName() {
		return tableName;
	}



	public void setTableName(String tableName) {
		this.tableName = tableName;
	}



	public String getRecordId() {
		return recordId;
	}



	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}



	



	public InventoryEmployee getInventoryEmployee() {
		return inventoryEmployee;
	}



	public void setInventoryEmployee(InventoryEmployee inventoryEmployee) {
		this.inventoryEmployee = inventoryEmployee;
	}



	public String getFieldName() {
		return fieldName;
	}



	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}



	public String getOldValue() {
		return oldValue;
	}



	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}



	public String getNewValue() {
		return newValue;
	}



	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}



	public Boolean getIsDelete() {
		return isDelete;
	}



	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}



	@PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
