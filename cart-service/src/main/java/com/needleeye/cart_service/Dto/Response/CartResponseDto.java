package com.needleeye.cart_service.Dto.Response;

import java.util.List;

public class CartResponseDto {

    private Long cartId;
    private String userId;
    private List<CartProductItemResponseDto> items;
    private Integer totalItems;
    private Double totalAmount;

    public CartResponseDto() {
    }

    public CartResponseDto(Long cartId, String userId, List<CartProductItemResponseDto> items, Integer totalItems, Double totalAmount) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.totalItems = totalItems;
        this.totalAmount = totalAmount;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartProductItemResponseDto> getItems() {
        return items;
    }

    public void setItems(List<CartProductItemResponseDto> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
