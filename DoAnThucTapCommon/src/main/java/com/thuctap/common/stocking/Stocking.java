package com.thuctap.common.stocking;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.product_variant.ProductVariant;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stockings")
public class Stocking {

    @EmbeddedId
    private StockingId id;

    @Column(name = "quantity")
    private Integer quantity = 0;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inventoryId")
    @JoinColumn(name = "inventory_id", insertable = false, updatable = false)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sku")
    @JoinColumn(name = "sku", referencedColumnName = "sku", insertable = false, updatable = false)
    private ProductVariant productVariant;

    public Stocking() {}

    public StockingId getId() {
        return id;
    }

    public void setId(StockingId id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }
}
