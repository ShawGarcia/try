package com.shop.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.entity.po.ProductPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper // MyBatis 注解，标记该接口为Mapper
public interface ProductMapper extends BaseMapper<ProductPo> {

    /**
     * 根据分类ID查询商品列表
     * @param categoriesId 分类ID
     * @return 商品列表
     */
    @Select("SELECT * FROM product WHERE categories_id = #{categoriesId} AND is_deleted = 0")
    List<ProductPo> selectByCategoriesId(@Param("categoriesId") Integer categoriesId);

    /**
     * 根据关键词搜索商品
     * @param query 关键词
     * @return 商品列表
     */
    @Select("SELECT * FROM product WHERE name LIKE CONCAT('%', #{query}, '%') AND is_deleted = 0")
    List<ProductPo> searchByName(@Param("query") String query);

    /**
     * 分页查询商品列表
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 商品列表
     */
    @Select("SELECT * FROM product WHERE is_deleted = 0 LIMIT #{offset}, #{pageSize}")
    List<ProductPo> selectPage(@Param("offset") long offset, @Param("pageSize") long pageSize);

    /**
     * 查询商品总数
     * @return 商品总数
     */
    @Select("SELECT COUNT(*) FROM product WHERE is_deleted = 0")
    long selectCount();
}