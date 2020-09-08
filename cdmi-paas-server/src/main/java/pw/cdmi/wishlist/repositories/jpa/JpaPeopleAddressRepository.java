package pw.cdmi.wishlist.repositories.jpa;


import java.util.List;

import pw.cdmi.wishlist.model.entities.PeopleAddress;
import pw.cdmi.wishlist.repositories.PeopleAddressRepository;

public interface JpaPeopleAddressRepository extends PeopleAddressRepository {

    List<PeopleAddress> findByUserId(String userId);

}
