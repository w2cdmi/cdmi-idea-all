package pw.cdmi.wishlist.repositories.jpa;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pw.cdmi.wishlist.model.entities.Onlooker;

public interface JpaOnlookerRepository extends JpaRepository<Onlooker, Long> {

    Onlooker findByWxuserIdAndProductId(String wxuserId, String productId);

    List<Onlooker> findByProductIdAndInviterId(String productId, String inviterId);

    Long countByProductId(String productId);

    List<Onlooker> findByProductIdAndInviterIdNotNull(String productId);
}
