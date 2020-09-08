package com.example.demo.wishlist.repositories.jpa;


import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.entities.Product;
import com.example.demo.wishlist.repositories.ProductRepository;

import java.util.List;

public interface JpaProductRepository extends ProductRepository {

    List<Product> findByStatus(ProductStatus productStatus);

    Product findByIdAndStatus(String id, ProductStatus productStatus);

}
