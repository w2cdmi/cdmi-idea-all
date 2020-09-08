package pw.cdmi.wishlist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.wishlist.model.entities.Winner;
import pw.cdmi.wishlist.repositories.jpa.JpaWinnerRepository;
import pw.cdmi.wishlist.service.WinnerService;

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
