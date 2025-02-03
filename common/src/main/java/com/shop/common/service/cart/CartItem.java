package com.shop.common.service.cart;

import lombok.Data;

@Data
public class CartItem {
    private Long productId;
    private Integer quantity;
} 