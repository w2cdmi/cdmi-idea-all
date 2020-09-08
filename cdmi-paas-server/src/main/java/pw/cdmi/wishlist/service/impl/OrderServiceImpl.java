package pw.cdmi.wishlist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.wishlist.model.entities.Order;
import pw.cdmi.wishlist.repositories.jpa.JpaOrderRepository;
import pw.cdmi.wishlist.service.OrderService;

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
