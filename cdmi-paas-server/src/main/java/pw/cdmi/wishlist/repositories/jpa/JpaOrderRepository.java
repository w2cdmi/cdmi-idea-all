package pw.cdmi.wishlist.repositories.jpa;

import pw.cdmi.wishlist.model.OrderStatus;
import pw.cdmi.wishlist.model.entities.Order;
import pw.cdmi.wishlist.repositories.OrderRepository;

public interface JpaOrderRepository extends OrderRepository {
    Order findByUserIdAndProductIdAndStatus(String userId, String productId, OrderStatus orderStatus);
}
