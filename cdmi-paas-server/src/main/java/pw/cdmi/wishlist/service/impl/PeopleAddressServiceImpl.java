package pw.cdmi.wishlist.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.wishlist.model.entities.PeopleAddress;
import pw.cdmi.wishlist.repositories.jpa.JpaPeopleAddressRepository;
import pw.cdmi.wishlist.service.PeopleAddressService;

@Service
public class PeopleAddressServiceImpl implements PeopleAddressService{

    @Autowired
    private JpaPeopleAddressRepository jpaPeopleAddressRepository;

    @Override
    public PeopleAddress createPeopleAddress(PeopleAddress peopleAddress) {
        return jpaPeopleAddressRepository.save(peopleAddress);
    }

    @Override
    public PeopleAddress updatePeopleAddress(PeopleAddress peopleAddress) {
        return jpaPeopleAddressRepository.save(peopleAddress);
    }

    @Override
    public PeopleAddress setPeopleAddressToDefault(PeopleAddress peopleAddress) {
        List<PeopleAddress> peopleAddresses = jpaPeopleAddressRepository.findByUserId(peopleAddress.getUserId());
        if(!peopleAddresses.isEmpty()){
            for (PeopleAddress address: peopleAddresses) {
                if(address.getIsDefault() == 1 && !address.getId().equals(peopleAddress.getId())){
                    address.setIsDefault(0);
                    jpaPeopleAddressRepository.save(address);
                }
            }
        }
        jpaPeopleAddressRepository.save(peopleAddress);
        return peopleAddress;
    }

    @Override
    public PeopleAddress getById(String id) {
        PeopleAddress peopleAddress = jpaPeopleAddressRepository.findById(id).get();
        return peopleAddress;
    }

    @Override
    public List<PeopleAddress> getPeopleAddressList(String userId) {
        return jpaPeopleAddressRepository.findByUserId(userId);
    }

    @Override
    public void deletePeopleAddress(String id) {
        jpaPeopleAddressRepository.deleteById(id);
    }

    @Override
    public PeopleAddress getDefaultPeopleAddress(String userId) {
        PeopleAddress defaultPeopleAddress = null;
        List<PeopleAddress> peopleAddresses = jpaPeopleAddressRepository.findByUserId(userId);
        if(peopleAddresses.isEmpty()){
            return defaultPeopleAddress;
        }
        for (PeopleAddress peopleAddress: peopleAddresses){
            if(peopleAddress.getIsDefault() == 1){
                defaultPeopleAddress = peopleAddress;
            }
        }
        if (defaultPeopleAddress == null){
            defaultPeopleAddress = peopleAddresses.get(0);
        }
        return defaultPeopleAddress;
    }
}
