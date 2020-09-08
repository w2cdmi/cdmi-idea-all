package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.entities.Order;
import org.aspectj.weaver.ast.Or;

public interface OrderService {
    public Order createOrder(Order order);

    public Order getById(String orderId);

    public Order updateOrder(Order order);
}
