package com.needleeye.product_service.Dto.Response;

public class InventoryResponseDto {
    private Long id;
    private String productId;
    private Integer totalInventory;
    private Integer available;
    private Integer sell;

    public InventoryResponseDto() {
    }

    public InventoryResponseDto(Integer totalInventory, Integer available, Integer sell) {
        this.totalInventory = totalInventory;
        this.available = available;
        this.sell = sell;
    }

    public InventoryResponseDto(Long id, String productId, Integer totalInventory, Integer available, Integer sell) {
        this.id = id;
        this.productId = productId;
        this.totalInventory = totalInventory;
        this.available = available;
        this.sell = sell;
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

    public Integer getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(Integer totalInventory) {
        this.totalInventory = totalInventory;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Integer getSell() {
        return sell;
    }

    public void setSell(Integer sell) {
        this.sell = sell;
    }
}
