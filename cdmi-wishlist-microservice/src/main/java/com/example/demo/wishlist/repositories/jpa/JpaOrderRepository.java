package com.example.demo.wishlist.repositories.jpa;

import com.example.demo.wishlist.model.OrderStatus;
import com.example.demo.wishlist.model.entities.Order;
import com.example.demo.wishlist.repositories.OrderRepository;

public interface JpaOrderRepository extends OrderRepository {
    Order findByUserIdAndProductIdAndStatus(String userId, String productId, OrderStatus orderStatus);
}
