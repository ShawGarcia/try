package com.shop.common.service.product;

public interface ProductCatalogService {
    ListProductsResp listProducts(ListProductsReq request);

    GetProductResp getProduct(GetProductReq request);

    SearchProductsResp searchProducts(SearchProductsReq request);
}