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
     Long id;
     String productName;
     BigDecimal price;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}