package com.shop.common.service.cart;

import lombok.Data;

import java.util.List;

@Data
public class Cart {
    private Long userId;
    private List<CartItem> items;
} 