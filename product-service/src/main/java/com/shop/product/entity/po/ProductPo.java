package com.shop.product.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductPo {
     @TableId(type = com.baomidou.mybatisplus.annotation.IdType.AUTO)
     private Long id;
     private String name;
     private String description;
     private String categoriesId;
     private BigDecimal price;
     private LocalDateTime createAt;
     private LocalDateTime updateAt;
     private Boolean isDeleted;
     private LocalDateTime deleteAt;
}