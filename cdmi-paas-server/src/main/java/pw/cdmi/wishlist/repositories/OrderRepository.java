package pw.cdmi.wishlist.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.wishlist.model.entities.Order;

@NoRepositoryBean
public interface OrderRepository extends PagingAndSortingRepository<Order, String>, QueryByExampleExecutor<Order> {
}
