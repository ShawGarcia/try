package com.shop.product.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data // Lombok 注解，自动生成getter、setter等方法
@TableName("product") // MyBatis-Plus 注解，指定表名
public class ProductPo {
     @TableId(type = IdType.AUTO) // 主键自增
     private Integer id; // 商品ID，对应数据库的int unsigned
     private String name; // 商品名称，对应数据库的varchar
     private String description; // 商品描述，对应数据库的text
     private String picture; // 商品图片，对应数据库的varchar
     private BigDecimal price; // 商品价格，对应数据库的decimal
     private Integer categoriesId; // 商品分类ID，对应数据库的int
     private LocalDateTime createAt; // 创建时间，对应数据库的datetime
     private LocalDateTime updateAt; // 更新时间，对应数据库的datetime
     private Integer isDeleted; // 是否删除，对应数据库的tinyint
     private LocalDateTime deleteAt; // 删除时间，对应数据库的datetime
}