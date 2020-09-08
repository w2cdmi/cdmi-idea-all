package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.entities.Product;

import java.util.List;

public interface ProductService {

    public Product createProduct(Product product);

    public Product updateProduct(Product product);

    public List<Product> getProductList();

    public List<Product> getInProgressProductList();

    public Product getProductById(String id);

    public Product getProductByIdAndStatus(String id, ProductStatus productStatus);

    public Boolean updateOnlookerNumber(String productId);

    public Boolean updateParticipantNumber(String productId);
}
