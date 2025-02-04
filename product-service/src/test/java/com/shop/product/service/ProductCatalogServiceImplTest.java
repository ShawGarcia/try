package com.shop.product.service;

import com.shop.common.service.product.*;
import com.shop.product.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductCatalogServiceImplTest {

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductCatalogServiceImpl productCatalogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts() {
        // 准备测试数据
        ListProductsReq request = new ListProductsReq();
        request.setPage(1);
        request.setPageSize(10L);
        request.setCategoryName("Electronics");

        Product product1 = Product.builder().id(1L).name("Laptop").price(999.99f).build();
        Product product2 = Product.builder().id(2L).name("Smartphone").price(499.99f).build();
        List<Product> products = Arrays.asList(product1, product2);

        // 模拟 Mapper 行为
        when(productMapper.listProducts(1, 10L, "Electronics")).thenReturn(products);

        // 调用方法
        ListProductsResp response = productCatalogService.listProducts(request);

        // 验证结果
        assertEquals(2, response.getProducts().size());
        assertEquals("Laptop", response.getProducts().get(0).getName());
        verify(productMapper, times(1)).listProducts(1, 10L, "Electronics");
    }

    @Test
    void testGetProduct() {
        // 准备测试数据
        GetProductReq request = new GetProductReq();
        request.setId(1L);

        Product product = Product.builder().id(1L).name("Laptop").price(999.99f).build();

        // 模拟 Mapper 行为
        when(productMapper.getProductById(1L)).thenReturn(product);

        // 调用方法
        GetProductResp response = productCatalogService.getProduct(request);

        // 验证结果
        assertEquals("Laptop", response.getProduct().getName());
        verify(productMapper, times(1)).getProductById(1L);
    }

    @Test
    void testSearchProducts() {
        // 准备测试数据
        SearchProductsReq request = new SearchProductsReq();
        request.setQuery("Laptop");

        Product product1 = Product.builder().id(1L).name("Laptop").price(999.99f).build();
        Product product2 = Product.builder().id(2L).name("Gaming Laptop").price(1299.99f).build();
        List<Product> products = Arrays.asList(product1, product2);

        // 模拟 Mapper 行为
        when(productMapper.searchProducts("Laptop")).thenReturn(products);

        // 调用方法
        SearchProductsResp response = productCatalogService.searchProducts(request);

        // 验证结果
        assertEquals(2, response.getResults().size());
        assertEquals("Laptop", response.getResults().get(0).getName());
        verify(productMapper, times(1)).searchProducts("Laptop");
    }
}