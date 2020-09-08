package com.example.demo.wishlist.repositories.jpa;


import com.example.demo.wishlist.model.entities.Onlooker;
import com.example.demo.wishlist.repositories.OnlookerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOnlookerRepository extends JpaRepository<Onlooker, Long> {

    Onlooker findByWxuserIdAndProductId(String wxuserId, String productId);

    List<Onlooker> findByProductIdAndInviterId(String productId, String inviterId);

    Long countByProductId(String productId);

    List<Onlooker> findByProductIdAndInviterIdNotNull(String productId);
}
