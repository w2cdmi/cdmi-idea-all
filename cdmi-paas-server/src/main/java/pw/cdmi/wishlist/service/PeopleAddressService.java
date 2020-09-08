package pw.cdmi.wishlist.service;

import java.util.List;

import pw.cdmi.wishlist.model.entities.PeopleAddress;

public interface PeopleAddressService {

    public PeopleAddress createPeopleAddress(PeopleAddress peopleAddress);

    public PeopleAddress updatePeopleAddress(PeopleAddress peopleAddress);

    public PeopleAddress setPeopleAddressToDefault(PeopleAddress peopleAddress);

    public PeopleAddress getById(String userId);

    public List<PeopleAddress> getPeopleAddressList(String userId);

    public void deletePeopleAddress(String id);

    public PeopleAddress getDefaultPeopleAddress(String userId);
}
