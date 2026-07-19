package com.needleeye.order_service.Dto.Response;

public class OrderItemResponseDto {
    private Long itemId;
    private String productId;
    private String productName;
    private String imageUrl;
    private Double price;
    private String size;
    private String color;
    private Integer quantity;
    private Double subtotal;

    public OrderItemResponseDto() {
    }

    public OrderItemResponseDto(Long itemId, String productId, String productName, String imageUrl, Double price, String size, String color, Integer quantity, Double subtotal) {
        this.itemId = itemId;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
        this.price = price;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
