package com.needleeye.inventory_service.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryUpdateDto {
    @NotNull(message = "Total inventory is required")
    @Min(value = 0, message = "Total inventory cannot be negative")
    private Integer totalInventory;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer available;

    @NotNull(message = "Sell quantity is required")
    @Min(value = 0, message = "Sell quantity cannot be negative")
    private Integer sell;

    public InventoryUpdateDto() {
    }

    public InventoryUpdateDto(@NotNull(message = "Total inventory is required") Integer totalInventory, @NotNull(message = "Available quantity is required") Integer available, @NotNull(message = "Sell quantity is required") Integer sell) {
        this.totalInventory = totalInventory;
        this.available = available;
        this.sell = sell;
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
