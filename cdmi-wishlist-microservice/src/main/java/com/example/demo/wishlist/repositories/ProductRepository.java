package com.example.demo.wishlist.repositories;

import com.example.demo.wishlist.model.entities.Product;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;


@NoRepositoryBean
public interface ProductRepository extends PagingAndSortingRepository<Product, String>, QueryByExampleExecutor<Product>{

}
