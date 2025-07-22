package com.thuctap.common.importing_form;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ImportingFormDetailId implements Serializable {

	@Column(name = "inventory_form_id")
	private Integer inventoryFormId;

	@Column(name = "sku", length = 125)
	private String sku;

	public ImportingFormDetailId() {
	}

	public ImportingFormDetailId(Integer inventoryFormId, String sku) {
		this.inventoryFormId = inventoryFormId;
		this.sku = sku;
	}

	public Integer getInventoryFormId() {
		return inventoryFormId;
	}

	public void setInventoryFormId(Integer inventoryFormId) {
		this.inventoryFormId = inventoryFormId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@Override
	public int hashCode() {
		return Objects.hash(inventoryFormId, sku);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImportingFormDetailId other = (ImportingFormDetailId) obj;
		return Objects.equals(inventoryFormId, other.inventoryFormId) && Objects.equals(sku, other.sku);
	}

}
