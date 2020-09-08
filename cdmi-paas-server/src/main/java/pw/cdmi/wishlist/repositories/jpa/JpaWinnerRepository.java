package pw.cdmi.wishlist.repositories.jpa;


import pw.cdmi.wishlist.model.entities.Winner;
import pw.cdmi.wishlist.repositories.WinnerRepository;

public interface JpaWinnerRepository extends WinnerRepository {

    Winner findByProductIdAndWxuserId(String productId, String userId);

}
