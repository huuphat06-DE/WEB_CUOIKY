package com.oneshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * THÀNH VIÊN 2: DTO dòng sản phẩm trong giỏ hàng
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private String sku;
    private String name;
    private String image;
    private BigDecimal price;
    private int quantity;

    public BigDecimal getSubtotal() {
        if (price == null) return BigDecimal.ZERO;
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
