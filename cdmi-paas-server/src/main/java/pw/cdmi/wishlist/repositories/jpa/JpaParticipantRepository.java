package pw.cdmi.wishlist.repositories.jpa;

import java.util.List;

import pw.cdmi.wishlist.model.entities.Participant;
import pw.cdmi.wishlist.repositories.ParticipantRepository;

public interface JpaParticipantRepository extends ParticipantRepository {

    Participant findByWxuserIdAndProductId(String wxuserId, String productId);

    List<Participant> findByProductId(String productId);

    Long countByProductId(String productId);

}
