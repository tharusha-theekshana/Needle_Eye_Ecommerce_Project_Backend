package com.needleeye.product_service.Entity;

import com.needleeye.product_service.Utils.Enums.SizeType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String productId;
    private String imageUrl;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double price;
    private Double discountPercentage;
    private Double lastPrice;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "product_colors",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "color_id")
    )
    private List<Color> colors;

    @ElementCollection(targetClass = SizeType.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private List<SizeType> sizes;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public Product() {
    }

    public Product(Long id, String productId, String imageUrl, String name, String description, Double price, Double discountPercentage, Double lastPrice, Boolean isAvailable, Category category, List<Color> colors, List<SizeType> sizes, List<Review> reviews, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.lastPrice = lastPrice;
        this.isAvailable = isAvailable;
        this.category = category;
        this.colors = colors;
        this.sizes = sizes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Color> getColors() {
        return colors;
    }

    public void setColors(List<Color> colors) {
        this.colors = colors;
    }

    public List<SizeType> getSizes() {
        return sizes;
    }

    public void setSizes(List<SizeType> sizes) {
        this.sizes = sizes;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
