package com.example.demo.wishlist.rs.v1;

import com.example.demo.common.scheduling.ScheduledTask;
import com.example.demo.common.websocket.domain.SocketMessage;
import com.example.demo.exception.InvalidParamterException;
import com.example.demo.oauth.server.UserTokenHelper;
import com.example.demo.weixin.message.model.MessageEnum;
import com.example.demo.weixin.message.model.TemplateData;
import com.example.demo.weixin.message.model.TemplateMessage;
import com.example.demo.weixin.proxy.WxOauth2Proxy;
import com.example.demo.weixin.rs.v1.domain.UserToken;
import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.entities.*;
import com.example.demo.wishlist.rs.v1.domain.RestOnlookerReponse;
import com.example.demo.wishlist.rs.v1.domain.RestOnlookerRequest;
import com.example.demo.wishlist.rs.v1.domain.RestPeopleAddressRequest;
import com.example.demo.wishlist.rs.v1.domain.RestWinnerResponse;
import com.example.demo.wishlist.service.CrowdFundingService;
import com.example.demo.wishlist.service.PeopleAddressService;
import com.example.demo.wishlist.service.ProductService;
import com.example.demo.wishlist.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/wishlist/crowdfundings/v1")
public class CrowdFundingResource {

    @Autowired
    private UserTokenHelper userTokenHelper;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private CrowdFundingService crowdFundingService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PeopleAddressService peopleAddressService;

    /**
     * 创建围观人员
     * 1、创建我邀请的好友进行商品围观
     * 2、直接进入小程序，点击商品，创建商品围观人员
     *
     * @return
     */
    @RequestMapping(value = "onlooker", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Onlooker> createOnlooker(@RequestBody RestOnlookerRequest restOnlookerRequest, @RequestHeader("Authorization") String token) {
        restOnlookerRequest.checkParameter();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        Onlooker onlooker = null;

        WxUser user = wxUserService.getWxUserByWxOpenId(userToken.getOpenId());
        if (user == null) {
//            throw new AWSClientException(GlobalClientError.InvalidRequest, ClientReason.NoFoundData);
        }

        //判断人员是否为该商品的围观文员
        onlooker = crowdFundingService.getOnlooker(user.getId(), restOnlookerRequest.getProductId());
        if (onlooker == null) {
            if (StringUtils.isEmpty(restOnlookerRequest.getInviterId())) {
                onlooker = crowdFundingService.addOnlooker(user.getId(), restOnlookerRequest.getProductId());
            } else {
                onlooker = crowdFundingService.addOnlooker(user.getId(), restOnlookerRequest.getProductId(), restOnlookerRequest.getInviterId());
            }

            if (onlooker != null) {
                Boolean flag = productService.updateOnlookerNumber(restOnlookerRequest.getProductId());
            }
        }
        return new ResponseEntity<Onlooker>(onlooker, HttpStatus.OK);
    }

    /**
     * 是否参与商品抽奖
     *
     * @param productId
     * @param token
     * @return
     */
    @RequestMapping(value = "isParticipant", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Boolean> isParticipant(@RequestParam String productId, @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(productId)) {
            String msg = "productId is null.";
            throw new InvalidParamterException(msg);
        }
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        Boolean flag = false;

        Participant participant = crowdFundingService.getParticipant(userToken.getUserId(), productId);
        if (participant != null) {
            flag = true;
        }

        return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
    }

    /**
     * 查看我的心愿商品支持围观用户
     *
     * @return
     */
    @RequestMapping(value = "/onlooker/support", method = RequestMethod.GET)
    @ResponseBody
    public List<RestOnlookerReponse> listMyOnlooker(@RequestParam String productId, @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(productId)) {
            String msg = "productId is null.";
            throw new InvalidParamterException(msg);
        }
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        List<Onlooker> onlookerList = crowdFundingService.getOnlookerList(productId, userToken.getUserId());
        List<RestOnlookerReponse> restOnlookerReponsesList = new ArrayList<>();
        if (onlookerList != null && onlookerList.size() > 0) {
            RestOnlookerReponse restOnlookerReponse = null;
            for (Onlooker onlooker : onlookerList) {
                restOnlookerReponse = new RestOnlookerReponse();
                restOnlookerReponse.setId(onlooker.getId());
                restOnlookerReponse.setUserId(onlooker.getWxuserId());
                restOnlookerReponse.setProductId(onlooker.getProductId());
                restOnlookerReponse.setInviterId(onlooker.getInviterId());
                restOnlookerReponse.setCreatime(onlooker.getCreatime());
                restOnlookerReponsesList.add(restOnlookerReponse);
            }
        }

        return restOnlookerReponsesList;
    }

    /**
     * 将众筹商品加入到我的心愿单，前置条件是需要支付1元后进行
     *
     * @return
     */
    @RequestMapping(value = "/wish", method = RequestMethod.POST)
    public ResponseEntity<Boolean> createMyWishList(@RequestParam String productId, @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(productId)) {
            String msg = "productId is null.";
            throw new InvalidParamterException(msg);
        }
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);

        Boolean flag = false;
        // 检查商品是否存在
        Product product = productService.getProductById(productId);
        if (product == null || product.getStatus() != ProductStatus.InProgress) {
            //商品不存在或众筹状态不满足加入心愿单的条件
//            throw new AWSClientException(GlobalClientError.InvalidRequest, ClientReason.DataConsistent);
            return null;
        }
        if (product.getSalesValidity() != null && product.getSalesValidity().getTime() > new Date().getTime()) {
            // 众筹时间已过，不能加入到心愿单
//            throw new AWSClientException(GlobalClientError.InvalidRequest, ClientReason.DataConsistent);
            return null;
        }
        // TODO 检查ownerId是否有资格参加，账号是否存在，是否已支付1元钱。

        //将商品加入到ownerId的心愿商品单中
        Participant participant = crowdFundingService.addParticipant(userToken.getUserId(), productId);
        if (participant != null) {
            flag = true;

            productService.updateParticipantNumber(productId);
        }
        return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
    }

    /**
     * 获取以前抽中奖品的用户
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "winners", method = RequestMethod.GET)
    public ResponseEntity<List<RestWinnerResponse>> findWinnerAll(@RequestHeader("Authorization") String token) {
        List<RestWinnerResponse> restWinnerResponseList = new ArrayList<>();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);

        List<Winner> winnerList = crowdFundingService.findWinnerAll();
        if (winnerList == null) {
            return null;
        }

        RestWinnerResponse restWinnerResponse = null;
        for (Winner winner : winnerList) {
            restWinnerResponse = new RestWinnerResponse();
            restWinnerResponse.setCreateTime(winner.getCreateTime());

            WxUser wxUser = wxUserService.getById(winner.getWxuserId());
            if(wxUser == null){
                continue;
            }
            restWinnerResponse.setUserName(wxUser.getNick());
            restWinnerResponse.setHeadImageUrl(wxUser.getHeadImageUrl());

            Product product = productService.getProductById(winner.getProductId());
            if(product == null){
                continue;
            }
            restWinnerResponse.setProductName(product.getTitle());
            restWinnerResponseList.add(restWinnerResponse);
        }
        return new ResponseEntity<List<RestWinnerResponse>>(restWinnerResponseList, HttpStatus.OK);
    }

    /**
     * 创建收货地址
     * @param restPeopleAddressRequest
     * @param token
     * @return
     */
    @RequestMapping(value = "address", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PeopleAddress> createAddress(@RequestBody RestPeopleAddressRequest restPeopleAddressRequest, @RequestHeader("Authorization") String token) {
        restPeopleAddressRequest.checkParameter();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);

        PeopleAddress peopleAddress = new PeopleAddress();
        peopleAddress.setUserId(userToken.getUserId());
        peopleAddress.setAddressName(restPeopleAddressRequest.getAddressName());
        peopleAddress.setPhoneNumber(restPeopleAddressRequest.getPhoneNumber());
        peopleAddress.setAddress(restPeopleAddressRequest.getAddress());
        peopleAddress.setCreateDate(new Date());

        PeopleAddress peopleAddressResponse = peopleAddressService.createPeopleAddress(peopleAddress);
        return new ResponseEntity<PeopleAddress>(peopleAddressResponse, HttpStatus.OK);
    }

    /**
     * 修改收货地址
     * @param restPeopleAddressRequest
     * @param token
     * @return
     */
    @RequestMapping(value = "address", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<PeopleAddress> editAddress(@RequestBody RestPeopleAddressRequest restPeopleAddressRequest, @RequestHeader("Authorization") String token) {
        restPeopleAddressRequest.checkParameter();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);

        PeopleAddress peopleAddress = peopleAddressService.getById(restPeopleAddressRequest.getId());
        if(peopleAddress == null){
            return new ResponseEntity<PeopleAddress>(peopleAddress, HttpStatus.BAD_REQUEST);
        }
        peopleAddress.setAddressName(restPeopleAddressRequest.getAddressName());
        peopleAddress.setPhoneNumber(restPeopleAddressRequest.getPhoneNumber());
        peopleAddress.setAddress(restPeopleAddressRequest.getAddress());

        PeopleAddress peopleAddressResponse = peopleAddressService.updatePeopleAddress(peopleAddress);
        return new ResponseEntity<PeopleAddress>(peopleAddressResponse, HttpStatus.OK);
    }

    /**
     * 获得指定心愿商品的参与人的获胜中奖概率列表
     *
     * @param productId 指定的心愿商品的ID
     * @param cursor    从符合条件的结果集中按顺序从cursor位置开始
     * @param maxcount  最大获取的结果集数量
     * @return
     */
//    public List<ParticipantResponse> listParticipantByProduct(String productId, int cursor, int maxcount) {
//        return null;
//    }

    /**
     * 查看我的中奖记录
     *
     * @return
     */
//    public List<WinProduct> listWinProduct() {
//        return null;
//    }

    /**
     *  获取登录用户是否参与当前商品的抽奖活动
     * @param token
     * @param productId
     * @return
     */
    @RequestMapping(value = "isJoinLottery/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Boolean> isJoinLottery(@RequestHeader("Authorization") String token, @PathVariable String productId){
        Boolean flag = false;
        if(StringUtils.isEmpty(productId)){
            return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
        }
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        Participant participant = crowdFundingService.getParticipant(userToken.getUserId(), productId);
        if (participant != null){
            flag = true;
        }

        return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
    }

    /**
     * 获取某一个用户的所有收货地址
     * @param token
     * @return
     */
    @RequestMapping(value = "/address/userId",method = RequestMethod.GET)
    public ResponseEntity<List<PeopleAddress>> getPeopleAddressList(@RequestHeader("Authorization") String token){
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        List<PeopleAddress> peopleAddressList = peopleAddressService.getPeopleAddressList(userToken.getUserId());
        return  new ResponseEntity<List<PeopleAddress>>(peopleAddressList, HttpStatus.OK);
    }

    /**
     * 删除一个收获地址
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/address/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteAddress(@RequestHeader("Authorization") String token, @PathVariable String id){
        Boolean flag = false;
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        peopleAddressService.deletePeopleAddress(id);
        flag = true;
        return  new ResponseEntity<Boolean>(flag,HttpStatus.OK);
    }

    /**
     * 获取一个收货地址详细信息
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/address/{id}",method = RequestMethod.GET)
    public ResponseEntity<PeopleAddress> getPeopleAddressById(@RequestHeader("Authorization") String token, @PathVariable String id){
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        PeopleAddress peopleAddress = peopleAddressService.getById(id);
        return new ResponseEntity<PeopleAddress>(peopleAddress, HttpStatus.OK);
    }

    /**
     * 设置一个地址为默认收货地址
     * @param token
     * @param id
     * @return
     */
    @RequestMapping(value = "/address/setDefault/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Boolean> setAddressToDefault(@RequestHeader("Authorization") String token, @PathVariable String id){
        Boolean flag = false;
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        PeopleAddress peopleAddress = peopleAddressService.getById(id);
        if (peopleAddress == null){
            return new ResponseEntity<Boolean>(flag, HttpStatus.BAD_REQUEST);
        }
        peopleAddress.setIsDefault(1);  // 1：true
        peopleAddressService.setPeopleAddressToDefault(peopleAddress);
        flag = true;
        return new ResponseEntity<Boolean>(flag, HttpStatus.OK);
    }

    @RequestMapping(value = "/address/default", method = RequestMethod.GET)
    public ResponseEntity<PeopleAddress> getDefaultPeopleAddress(@RequestHeader("Authorization") String token){
        PeopleAddress defalutAddress = null;
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);
        defalutAddress = peopleAddressService.getDefaultPeopleAddress(userToken.getUserId());
        return new ResponseEntity<PeopleAddress>(defalutAddress, HttpStatus.OK);
    }

    @Autowired
    private WxOauth2Proxy wxOauth2Proxy;

    @RequestMapping(value = "sendMessage", method = RequestMethod.GET)
    @ResponseBody
    public void sendMessage(){
        //发送消息
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser("oJ4vW5czqeV9Pev_JXwvzZK-LRB8");
        templateMessage.setPage("pages/lottery/lottery");
        templateMessage.setTemplate_id(MessageEnum.LUCK_USER_MESSAGE.getMsgNumber());
        templateMessage.setForm_id("1529665102937");

        //中奖姓名
        TemplateData templateData = new TemplateData("wang","red");
        Map<String, TemplateData> map = new HashMap<>();
        map.put("keyword1", templateData);
        templateMessage.setData(map);
        //商品名称
        templateData = new TemplateData("iphone8","red");
        map.put("keyword2", templateData);
        templateMessage.setData(map);
        //开奖结果
        templateData = new TemplateData("恭喜您中奖了","red");
        map.put("keyword3", templateData);
        templateMessage.setData(map);
        //中奖时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        templateData = new TemplateData(simpleDateFormat.format(new Date()),"red");
        map.put("keyword4", templateData);
        templateMessage.setData(map);
        //寄送地址
        templateData = new TemplateData("四川成都","red");
        map.put("keyword5", templateData);
        templateMessage.setData(map);

        wxOauth2Proxy.sendMessageToUser(templateMessage);
    }
}
