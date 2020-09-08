package com.example.demo.wishlist.repositories.jpa;


import com.example.demo.wishlist.model.entities.Winner;
import com.example.demo.wishlist.repositories.WinnerRepository;

public interface JpaWinnerRepository extends WinnerRepository {

    Winner findByProductIdAndWxuserId(String productId, String userId);

}
