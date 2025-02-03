package com.shop.common.service.order;

import lombok.Data;

@Data
public class MarkOrderPaidReq {
    private Long userId;
    private String orderId;
} 