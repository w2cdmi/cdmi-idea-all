package com.example.demo.wishlist.service.impl;

import com.example.demo.wishlist.model.entities.Winner;
import com.example.demo.wishlist.repositories.jpa.JpaWinnerRepository;
import com.example.demo.wishlist.service.WinnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WinnerServiceImpl implements WinnerService{

    @Autowired
    private JpaWinnerRepository jpaWinnerRepository;


    @Override
    public Winner createWinner(Winner winner) {
        return jpaWinnerRepository.save(winner);
    }

    @Override
    public Winner updateWinner(Winner winner) {
        return jpaWinnerRepository.save(winner);
    }

    @Override
    public Winner getWinnerById(String id) {
        return jpaWinnerRepository.findById(id).get();
    }

    @Override
    public Winner getWinnerByProductIdAndUserId(String productId, String userId) {
        return jpaWinnerRepository.findByProductIdAndWxuserId(productId, userId);
    }


}
