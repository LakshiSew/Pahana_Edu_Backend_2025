package com.Pahana_Edu_Backend.Order.service;

import com.Pahana_Edu_Backend.Order.entity.Orders;
import java.util.List;
import java.util.Optional;

public interface OrdersService {
    Orders createOrder(Orders order);
    Optional<Orders> getOrderById(String orderId);
    List<Orders> getAllOrders();
    List<Orders> getOrdersByCustomerId(String customerId);
    Orders updateOrderStatus(String orderId, String status);
    void deleteOrder(String orderId);
    String generateBill(String orderId);
}