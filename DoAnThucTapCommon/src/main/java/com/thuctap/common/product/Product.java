package com.thuctap.common.product;

import java.time.LocalDateTime;

import com.thuctap.common.brandcategory.BrandCategory;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, length = 125)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "image", length = 125)
    private String image = "product_default.png";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_category_id")
    private BrandCategory brandCategory;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

   
    public Product() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BrandCategory getBrandCategory() {
        return brandCategory;
    }

    public void setBrandCategory(BrandCategory brandCategory) {
        this.brandCategory = brandCategory;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
