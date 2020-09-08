package com.example.demo.wishlist.service.impl;

import com.example.demo.wishlist.model.entities.Order;
import com.example.demo.wishlist.repositories.jpa.JpaOrderRepository;
import com.example.demo.wishlist.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Override
    public Order createOrder(Order order) {
        return jpaOrderRepository.save(order);
    }

    @Override
    public Order getById(String orderId) {
        Order order = (Order) jpaOrderRepository.findById(orderId).get();
        return order;
    }

    @Override
    public Order updateOrder(Order order) {
        return jpaOrderRepository.save(order);
    }
}
