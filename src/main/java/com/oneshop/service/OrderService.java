package com.oneshop.service;

import com.oneshop.dto.CartDto;
import com.oneshop.dto.CartItemDto;
import com.oneshop.dto.OrderRequestDto;
import com.oneshop.entity.*;
import com.oneshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * THÀNH VIÊN 1 & 2: Dịch vụ Xử lý Đơn hàng & Quản lý xuất nhập tồn chuỗi
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BranchStockRepository branchStockRepository;

    /**
     * Tạo đơn hàng mới từ giỏ hàng và trừ tồn kho chi nhánh tương ứng
     */
    public Order placeOrder(CartDto cart, OrderRequestDto form, User user) {
        if (cart == null || cart.getItemList().isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng của bạn đang trống!");
        }

        // 1. Xác định chi nhánh xuất kho
        Branch branch = null;
        if (form.getBranchId() != null) {
            branch = branchRepository.findById(form.getBranchId()).orElse(null);
        }
        if (branch == null) {
            // Mặc định chọn chi nhánh đầu tiên nếu chưa chọn
            List<Branch> branches = branchRepository.findByActiveTrue();
            if (!branches.isEmpty()) {
                branch = branches.get(0);
            }
        }

        // 2. Tạo đối tượng Order
        Order order = new Order();
        String code = "ONE-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + (1000 + new Random().nextInt(9000));
        order.setOrderCode(code);
        order.setUser(user);
        order.setBranch(branch);
        order.setCustomerName(form.getCustomerName());
        order.setCustomerPhone(form.getCustomerPhone());
        order.setCustomerEmail(form.getCustomerEmail());
        order.setOrderType(form.getOrderType() != null ? form.getOrderType() : Order.OrderType.DELIVERY);
        order.setPaymentMethod(form.getPaymentMethod() != null ? form.getPaymentMethod() : Order.PaymentMethod.COD);
        order.setPaymentStatus(form.getPaymentMethod() == Order.PaymentMethod.VNPAY ? Order.PaymentStatus.PAID : Order.PaymentStatus.UNPAID);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setNote(form.getNote());
        order.setCreatedAt(LocalDateTime.now());

        if (order.getOrderType() == Order.OrderType.PICKUP) {
            order.setShippingAddress("Nhận tại chi nhánh: " + (branch != null ? branch.getName() + " - " + branch.getAddress() : "Tại cửa hàng"));
            order.setShippingFee(BigDecimal.ZERO);
        } else {
            order.setShippingAddress(form.getShippingAddress());
            order.setShippingFee(new BigDecimal("30000")); // Phí ship chuẩn 30.000đ
        }

        BigDecimal subtotal = cart.getTotalAmount();
        order.setTotalAmount(subtotal.add(order.getShippingFee()));

        Order savedOrder = orderRepository.save(order);

        // 3. Lưu chi tiết từng sản phẩm và trừ tồn kho của chi nhánh
        for (CartItemDto itemDto : cart.getItemList()) {
            Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
            if (product != null) {
                OrderItem orderItem = new OrderItem(savedOrder, product, itemDto.getPrice(), itemDto.getQuantity());
                orderItemRepository.save(orderItem);

                // Trừ tồn kho tại chi nhánh phụ trách đơn hàng
                if (branch != null) {
                    Optional<BranchStock> stockOpt = branchStockRepository.findByBranchAndProduct(branch, product);
                    if (stockOpt.isPresent()) {
                        BranchStock stock = stockOpt.get();
                        stock.setQuantity(Math.max(0, stock.getQuantity() - itemDto.getQuantity()));
                        branchStockRepository.save(stock);
                    }
                }
            }
        }

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Order> getOrdersByBranch(Long branchId) {
        return orderRepository.findByBranchIdOrderByCreatedAtDesc(branchId);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng: " + orderId));
        order.setStatus(status);
        if (status == Order.OrderStatus.COMPLETED) {
            order.setPaymentStatus(Order.PaymentStatus.PAID);
        }
        return orderRepository.save(order);
    }
}
