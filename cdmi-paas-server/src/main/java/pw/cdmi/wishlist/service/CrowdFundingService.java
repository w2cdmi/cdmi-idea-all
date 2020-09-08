package pw.cdmi.wishlist.service;

import java.util.List;

import pw.cdmi.wishlist.model.entities.Onlooker;
import pw.cdmi.wishlist.model.entities.Participant;
import pw.cdmi.wishlist.model.entities.Winner;

public interface CrowdFundingService {

    //获取参与到众筹商品活动的人员信息
    public Onlooker getOnlooker(String wxUserId, String productId);

    //邀请好友围观指定用户参与的众筹商品活动
    public Onlooker addOnlooker(String wxUserId, String productId, String inviterId);

    //直接指定用户参与的众筹商品活动
    public Onlooker addOnlooker(String wxUserId, String productId);

    //查询我邀请的围观人员信息
    public List<Onlooker> getOnlookerList(String productId, String inviterId);

    //获得众筹参与人的信息
    public Participant getParticipant(String wxUserId, String productId);

    //增加众筹参与人的邀请人数，数据库要加锁
    public long updateInviterNumberOfParticipant(Participant participant);

    //增加众筹参与人的邀请人数，数据库要加锁
    public long updateInviterNumberOfParticipant(String  participantId);

    //将指定的商品添加到指定用户的心愿单中
    public Participant addParticipant(String userId, String productId);

    //获取以前中奖人员信息
    public List<Winner> findWinnerAll();

}
