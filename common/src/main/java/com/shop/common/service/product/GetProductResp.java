package com.shop.common.service.product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProductResp {
    private AddProductReq product;
} 