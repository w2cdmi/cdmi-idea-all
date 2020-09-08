package com.example.demo.wishlist.repositories.jpa;


import com.example.demo.wishlist.model.entities.PeopleAddress;
import com.example.demo.wishlist.repositories.PeopleAddressRepository;

import java.util.List;

public interface JpaPeopleAddressRepository extends PeopleAddressRepository {

    List<PeopleAddress> findByUserId(String userId);

}
