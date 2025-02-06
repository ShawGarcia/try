package com.shop.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shop.product.exception.ProductException;
import com.shop.common.service.product.*;
import com.shop.product.entity.po.ProductPo;
import com.shop.product.mapper.ProductMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@DubboService // Dubbo 注解，标记该类为Dubbo服务实现
@Slf4j // Lombok 注解，自动生成日志对象
@Service // Spring 注解，标记该类为服务层组件
public class ProductServiceImpl implements ProductCatalogService {

    @Resource // 依赖注入，注入ProductMapper
    private ProductMapper productMapper;

    @Override
    public ListProductsResp listProducts(ListProductsReq request) {
        try {
            // 分页查询商品
            long offset = (request.getPage() - 1) * request.getPageSize();
            List<ProductPo> productPos = productMapper.selectPage(offset, request.getPageSize());

            // 转换为AddProductReq对象
            List<AddProductReq> products = productPos.stream()
                    .map(this::convertToAddProductReq)
                    .collect(Collectors.toList());

            return ListProductsResp.builder()
                    .products(products)
                    .build();

        } catch (Exception e) {
            log.error("查询商品列表失败", e);
            throw new ProductException("查询商品列表失败");
        }
    }

    @Override
    public GetProductResp getProduct(GetProductReq request) {
        try {
            // 根据ID查询商品
            ProductPo productPo = productMapper.selectById(request.getId());
            if (productPo == null || productPo.getIsDeleted() == 1) {
                throw new ProductException("商品不存在或已被删除");
            }

            // 转换为AddProductReq对象
            AddProductReq product = convertToAddProductReq(productPo);

            return GetProductResp.builder()
                    .product(product)
                    .build();

        } catch (Exception e) {
            log.error("查询商品详情失败", e);
            throw new ProductException("查询商品详情失败");
        }
    }

    @Override
    public SearchProductsResp searchProducts(SearchProductsReq request) {
        try {
            // 根据关键词搜索商品
            List<ProductPo> productPos = productMapper.searchByName(request.getQuery());

            // 转换为AddProductReq对象
            List<AddProductReq> results = productPos.stream()
                    .map(this::convertToAddProductReq)
                    .collect(Collectors.toList());

            return SearchProductsResp.builder()
                    .results(results)
                    .build();

        } catch (Exception e) {
            log.error("搜索商品失败", e);
            throw new ProductException("搜索商品失败");
        }
    }

    /**
     * 将ProductPo转换为AddProductReq
     * @param productPo 商品持久化对象
     * @return AddProductReq
     */
    private AddProductReq convertToAddProductReq(ProductPo productPo) {
        return AddProductReq.builder()
                .id(productPo.getId().longValue()) // 将 int 转换为 Long
                .name(productPo.getName())
                .description(productPo.getDescription())
                .picture(productPo.getPicture())
                .price(productPo.getPrice().doubleValue()) // 将 BigDecimal 转换为 Double
                .categories(List.of(productPo.getCategoriesId().toString())) // 假设分类ID为字符串
                .build();
    }
}