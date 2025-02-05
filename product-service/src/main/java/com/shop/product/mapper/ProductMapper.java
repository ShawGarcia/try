package com.shop.product.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.entity.po.ProductPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<ProductPo> {

    List<ProductPo> listProducts(@Param("page") Integer page, @Param("pageSize") Long pageSize, @Param("categoryName") String categoryName);

    ProductPo getProductById(@Param("id") Long id);

    List<ProductPo> searchProducts(@Param("query") String query);
}