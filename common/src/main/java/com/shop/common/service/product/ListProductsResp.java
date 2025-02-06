package com.shop.common.service.product;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder // 添加 @Builder 注解
public class ListProductsResp {
    private List<AddProductReq> products; // 商品列表
}