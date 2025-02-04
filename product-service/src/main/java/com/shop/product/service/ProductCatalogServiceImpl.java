package com.shop.product.service;

import com.shop.common.service.product.*;
import com.shop.product.entity.po.ProductPo;
import com.shop.product.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class ProductCatalogServiceImpl implements ProductCatalogService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ListProductsResp listProducts(ListProductsReq request) {
        // 假设这里根据请求参数进行产品列表查询
        List<ProductPo> productPos = productMapper.selectList(null); // 实际应根据请求参数构建查询条件
        List<Product> products = new ArrayList<>();
        for (ProductPo productPo : productPos) {
            Product product = new Product();
            BeanUtils.copyProperties(productPo, product);
            products.add(product);
        }
        ListProductsResp resp = new ListProductsResp();
        resp.setProducts(products);
        return resp;
    }

    @Override
    public GetProductResp getProduct(GetProductReq request) {
        ProductPo productPo = productMapper.selectById(request.getId());
        if (productPo == null) {
            return new GetProductResp();
        }
        Product product = new Product();
        BeanUtils.copyProperties(productPo, product);
        GetProductResp resp = new GetProductResp();
        resp.setProduct(product);
        return resp;
    }

    @Override
    public SearchProductsResp searchProducts(SearchProductsReq request) {
        // 假设这里根据请求参数进行产品搜索
        // 实际应根据请求参数构建查询条件
        List<ProductPo> productPos = productMapper.selectList(null);
        List<Product> products = new ArrayList<>();
        for (ProductPo productPo : productPos) {
            Product product = new Product();
            BeanUtils.copyProperties(productPo, product);
            products.add(product);
        }
        SearchProductsResp resp = new SearchProductsResp();
        resp.setProducts(products);
        return resp;
    }
}