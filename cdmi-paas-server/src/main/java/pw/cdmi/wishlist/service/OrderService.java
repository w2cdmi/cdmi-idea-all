package pw.cdmi.wishlist.service;

import pw.cdmi.wishlist.model.entities.Order;

public interface OrderService {
    public Order createOrder(Order order);

    public Order getById(String orderId);

    public Order updateOrder(Order order);
}
