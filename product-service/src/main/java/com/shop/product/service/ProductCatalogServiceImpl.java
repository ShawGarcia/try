package com.shop.product.service;

import com.shop.common.service.product.*;
import com.shop.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ListProductsResp listProducts(ListProductsReq request) {
        List<Product> products = productMapper.listProducts(request.getPage(), request.getPageSize(), request.getCategoryName());
        ListProductsResp response = new ListProductsResp();
        response.setProducts(products);
        return response;
    }

    @Override
    public GetProductResp getProduct(GetProductReq request) {
        Product product = productMapper.getProductById(request.getId());
        GetProductResp response = new GetProductResp();
        response.setProduct(product);
        return response;
    }

    @Override
    public SearchProductsResp searchProducts(SearchProductsReq request) {
        List<Product> products = productMapper.searchProducts(request.getQuery());
        SearchProductsResp response = new SearchProductsResp();
        response.setProducts(products);
        return response;
    }
}