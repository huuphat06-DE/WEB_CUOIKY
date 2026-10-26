package com.oneshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * THÀNH VIÊN 2: DTO Giỏ hàng lưu trữ danh sách sản phẩm và tổng tiền
 */
@Getter
@Setter
public class CartDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Long, CartItemDto> items = new LinkedHashMap<>();

    public void addItem(CartItemDto item) {
        if (items.containsKey(item.getProductId())) {
            CartItemDto existing = items.get(item.getProductId());
            existing.setQuantity(existing.getQuantity() + item.getQuantity());
        } else {
            items.put(item.getProductId(), item);
        }
    }

    public void updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
        } else if (items.containsKey(productId)) {
            items.get(productId).setQuantity(quantity);
        }
    }

    public void removeItem(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItemDto> getItemList() {
        return new ArrayList<>(items.values());
    }

    public int getTotalQuantity() {
        return items.values().stream().mapToInt(CartItemDto::getQuantity).sum();
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDto item : items.values()) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }
}
