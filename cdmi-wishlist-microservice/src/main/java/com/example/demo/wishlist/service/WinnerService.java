package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.entities.Winner;

public interface WinnerService {

    public Winner createWinner(Winner winner);

    public Winner updateWinner(Winner winner);

    public Winner getWinnerById(String id);

    public Winner getWinnerByProductIdAndUserId(String productId, String userId);
}
