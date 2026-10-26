package com.oneshop.dto;

import com.oneshop.entity.Order;
import lombok.Getter;
import lombok.Setter;

/**
 * THÀNH VIÊN 2: Form dữ liệu đặt hàng Checkout
 */
@Getter
@Setter
public class OrderRequestDto {
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Order.OrderType orderType = Order.OrderType.DELIVERY;
    private Long branchId; // Chi nhánh lấy hàng (nếu PICKUP) hoặc chi nhánh xuất kho
    private String shippingAddress;
    private Order.PaymentMethod paymentMethod = Order.PaymentMethod.COD;
    private String note;
}
