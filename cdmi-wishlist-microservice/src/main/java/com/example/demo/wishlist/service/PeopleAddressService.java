package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.entities.PeopleAddress;

import java.util.List;

public interface PeopleAddressService {

    public PeopleAddress createPeopleAddress(PeopleAddress peopleAddress);

    public PeopleAddress updatePeopleAddress(PeopleAddress peopleAddress);

    public PeopleAddress setPeopleAddressToDefault(PeopleAddress peopleAddress);

    public PeopleAddress getById(String userId);

    public List<PeopleAddress> getPeopleAddressList(String userId);

    public void deletePeopleAddress(String id);

    public PeopleAddress getDefaultPeopleAddress(String userId);
}
