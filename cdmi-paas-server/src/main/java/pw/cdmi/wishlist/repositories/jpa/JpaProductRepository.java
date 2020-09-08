package pw.cdmi.wishlist.repositories.jpa;


import java.util.List;

import pw.cdmi.wishlist.model.ProductStatus;
import pw.cdmi.wishlist.model.entities.Product;
import pw.cdmi.wishlist.repositories.ProductRepository;

public interface JpaProductRepository extends ProductRepository {

    List<Product> findByStatus(ProductStatus productStatus);

    Product findByIdAndStatus(String id, ProductStatus productStatus);

}
