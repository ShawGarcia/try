package com.shop.product.mapper;

import com.shop.common.service.product.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<Product> listProducts(@Param("page") Integer page, @Param("pageSize") Long pageSize, @Param("categoryName") String categoryName);

    Product getProductById(@Param("id") Long id);

    List<Product> searchProducts(@Param("query") String query);
}