package pw.cdmi.wishlist.service.impl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import pw.cdmi.wishlist.model.entities.Onlooker;
import pw.cdmi.wishlist.model.entities.Participant;
import pw.cdmi.wishlist.model.entities.Winner;
import pw.cdmi.wishlist.repositories.jpa.JpaOnlookerRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaParticipantRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaPeopleAddressRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaWinnerRepository;
import pw.cdmi.wishlist.service.CrowdFundingService;

@Service
public class CrowdFundingServiceImpl implements CrowdFundingService {

    @Autowired
    private JpaOnlookerRepository jpaOnlookerRepository;

    @Autowired
    private JpaParticipantRepository jpaParticipantRepository;

    @Autowired
    private JpaWinnerRepository jpaWinnerRepository;

    @Autowired
    private JpaPeopleAddressRepository jpaPeopleAddressRepository;

    @Override
    public Onlooker getOnlooker(String wxUserId, String productId) {

        return jpaOnlookerRepository.findByWxuserIdAndProductId(wxUserId, productId);
    }

    @Override
    public Onlooker addOnlooker(String wxUserId, String productId, String inviterId) {
        Onlooker onlooker = new Onlooker();
        onlooker.setWxuserId(wxUserId);
        onlooker.setProductId(productId);
        onlooker.setInviterId(inviterId);
        onlooker.setCreatime(new Date());

        return jpaOnlookerRepository.save(onlooker);
    }

    @Override
    public Onlooker addOnlooker(String wxUserId, String productId) {
        Onlooker onlooker = new Onlooker();
        onlooker.setWxuserId(wxUserId);
        onlooker.setProductId(productId);
        onlooker.setCreatime(new Date());

        return jpaOnlookerRepository.save(onlooker);
    }

    @Override
    public List<Onlooker> getOnlookerList(String productId, String inviterId) {
        List<Onlooker> onlookerList = jpaOnlookerRepository.findByProductIdAndInviterId(productId, inviterId);
        return onlookerList;
    }

    @Override
    public Participant getParticipant(String wxUserId, String productId) {
        Participant participant = jpaParticipantRepository.findByWxuserIdAndProductId(wxUserId, productId);
        return participant;
    }

    @Override
    public long updateInviterNumberOfParticipant(Participant participant) {
        return 0;
    }

    @Override
    public long updateInviterNumberOfParticipant(String participantId) {
        return 0;
    }

    @Override
    public Participant addParticipant(String userId, String productId) {
        Participant participant = new Participant();
        participant.setWxuserId(userId);
        participant.setProductId(productId);
        participant.setCreatime(new Date());
        participant.setInviterNumber(Long.valueOf(0));
        participant = jpaParticipantRepository.save(participant);
        return participant;
    }

    @Override
    public List<Winner> findWinnerAll() {
        List<Winner> winnerList = (List<Winner>) jpaWinnerRepository.findAll(new Sort(Sort.Direction.DESC,"createTime"));
        return winnerList;
    }
}
