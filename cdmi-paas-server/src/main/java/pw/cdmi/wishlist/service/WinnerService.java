package pw.cdmi.wishlist.service;

import pw.cdmi.wishlist.model.entities.Winner;

public interface WinnerService {

    public Winner createWinner(Winner winner);

    public Winner updateWinner(Winner winner);

    public Winner getWinnerById(String id);

    public Winner getWinnerByProductIdAndUserId(String productId, String userId);
}
