package com.shop.common.service.cart;

import lombok.Data;

@Data
public class AddItemReq {
    private Long userId;
    private CartItem item;
} 