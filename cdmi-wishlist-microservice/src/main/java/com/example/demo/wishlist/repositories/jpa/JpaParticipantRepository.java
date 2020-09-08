package com.example.demo.wishlist.repositories.jpa;

import com.example.demo.wishlist.model.entities.Participant;
import com.example.demo.wishlist.repositories.ParticipantRepository;

import java.util.List;

public interface JpaParticipantRepository extends ParticipantRepository {

    Participant findByWxuserIdAndProductId(String wxuserId, String productId);

    List<Participant> findByProductId(String productId);

    Long countByProductId(String productId);

}
