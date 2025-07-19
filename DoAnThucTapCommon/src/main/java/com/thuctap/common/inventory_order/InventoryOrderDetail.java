package com.thuctap.common.inventory_order;
import java.math.BigDecimal;

import com.thuctap.common.product_variant.ProductVariant;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory_order_details")
public class InventoryOrderDetail {
	
	
	@EmbeddedId
    private InventoryOrderDetailId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id", updatable = false)
    private InventoryOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku", referencedColumnName = "sku", updatable = false,insertable = false)
    private ProductVariant productVariant;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "quote_cost_price")
    private BigDecimal quoteCostPrice;
    
    @Column(name = "expected_price")
    private BigDecimal expectedPrice;

    @Column(name = "quantity")
    private Integer quantity;

	public InventoryOrderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public InventoryOrderDetailId getId() {
		return id;
	}

	public void setId(InventoryOrderDetailId id) {
		this.id = id;
	}

	public InventoryOrder getOrder() {
		return order;
	}

	public void setOrder(InventoryOrder order) {
		this.order = order;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getQuoteCostPrice() {
		return quoteCostPrice;
	}

	public void setQuoteCostPrice(BigDecimal quoteCostPrice) {
		this.quoteCostPrice = quoteCostPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}



	public BigDecimal getExpectedPrice() {
		return expectedPrice;
	}



	public void setExpectedPrice(BigDecimal expectedPrice) {
		this.expectedPrice = expectedPrice;
	}
	
	
	
    
}
