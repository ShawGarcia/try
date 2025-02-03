package com.shop.common.service.checkout;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutResp {
    private String orderId;
    private String transactionId;
} 