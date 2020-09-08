package com.example.demo.weixin.rs.v1;

import com.example.demo.oauth.server.UserTokenHelper;
import com.example.demo.weixin.message.model.MessageEnum;
import com.example.demo.weixin.message.model.TemplateData;
import com.example.demo.weixin.message.model.TemplateMessage;
import com.example.demo.weixin.proxy.WxOauth2Proxy;
import com.example.demo.weixin.rs.v1.domain.RestWxPayRequest;
import com.example.demo.weixin.rs.v1.domain.UserToken;
import com.example.demo.weixin.utils.WXPayConstants.SignType;
import com.example.demo.weixin.utils.WXPayUtil;
import com.example.demo.wishlist.model.OrderStatus;
import com.example.demo.wishlist.model.PayType;
import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.entities.*;
import com.example.demo.wishlist.service.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wxpay/v1")
public class WxPayResource {

    private static Logger logger = LoggerFactory.getLogger(WxPayResource.class);

    @Autowired
    private UserTokenHelper userTokenHelper;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxOauth2Proxy wxOauth2Proxy;

    @Autowired
    private CrowdFundingService crowdFundingService;

    @Autowired
    private WinnerService winnerService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private PeopleAddressService peopleAddressService;

    /**
     * 统一下单
     */
    @RequestMapping(value = "/unifiedorder", method = RequestMethod.POST)
    public ResponseEntity<?> unifiedorder(@RequestHeader("Authorization") String token, @RequestBody RestWxPayRequest restWxPayRequest, HttpServletRequest request) {

        restWxPayRequest.checkRequestPram();
        UserToken userToken = userTokenHelper.checkTokenAndGetUserToken(token);

        //获取产品信息
        Product product = productService.getProductById(restWxPayRequest.getProductId());
        if (product == null) {
            logger.warn("Product information does not exist, productId: " + restWxPayRequest.getProductId());
            return new ResponseEntity<>("Failed to get ProductInfo: return value is null", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(!ProductStatus.InProgress.equals(product.getStatus())){
            //检查商品是否处于等待抽奖状态
            if(!ProductStatus.WaitPay.equals(product.getStatus())){
                return new ResponseEntity<>("商品不是处于待支付状态，productId = " + product.getId() + ", openId = " + userToken.getOpenId(), HttpStatus.BAD_REQUEST);
            }
            //检查商品是否已经处于等待支付状态，并且该商品中奖人是他
            Winner winner = winnerService.getWinnerByProductIdAndUserId(product.getId(), userToken.getUserId());
            if(winner == null){
                return new ResponseEntity<>("sorry，您不是该商品的中奖者，没有低价购买资格！，productId = " + product.getId() + ", openId = " + userToken.getOpenId(), HttpStatus.BAD_REQUEST);
            }
            if(winner.isPayed()){
                return new ResponseEntity<>("sorry，您已经购买过该商品了！，productId = " + product.getId() + ", openId = " + userToken.getOpenId(), HttpStatus.BAD_REQUEST);
            }
        }

        //创建订单
        Order order = new Order();
        // 商户订单号
        String outTradeNo = String.valueOf(new Date().getTime()) + (int) (Math.random() * 100);

        order.setId(outTradeNo);
        order.setProductId(restWxPayRequest.getProductId());
        order.setOriginalPrice(product.getOriginalPrice());
        order.setActualPrice(product.getActualPrice());
        order.setLotteryPrice(product.getLotteryPrice());
        /**
         *  两种情况
         *  1、抽奖支付，获取抽奖资格
         *  2、抽中支付
         */
        if (restWxPayRequest.getPayType() != null && PayType.buy.equals(restWxPayRequest.getPayType())) {
            order.setPayPrice(product.getActualPrice());
            order.setPayType(PayType.buy);
        } else {
            order.setPayPrice(product.getLotteryPrice());
            order.setPayType(PayType.lottery);
        }
        order.setSubmitDate(new Date());
        order.setUserId(userToken.getUserId());
        order.setStatus(OrderStatus.UNPAID);

        orderService.createOrder(order);

        //获取真实ip地址
        String spbillCreateIP = WXPayUtil.getIpAddr(request);
        //微信统一下单接口
        return douUnifiedorder(product, order, spbillCreateIP, userToken.getOpenId(), restWxPayRequest.getPayType());

    }

    /**
     * 调用微信统一下单接口
     *
     * @param product        产品名字
     * @param spbillCreateIP 真是ip
     * @param openId         微信openId
     * @return
     */
    public ResponseEntity<?> douUnifiedorder(Product product, Order order, String spbillCreateIP, String openId, PayType payType) {
        Map<String, String> packageParams = new HashMap<String, String>();

        packageParams.put("appid", wxOauth2Proxy.getAppId());
        packageParams.put("mch_id", wxOauth2Proxy.getMatchId());
        packageParams.put("nonce_str", WXPayUtil.generateNonceStr());
        packageParams.put("body", product.getTitle());
        packageParams.put("detail", product.getTitle());
        packageParams.put("out_trade_no", order.getId());
        packageParams.put("total_fee", order.getPayPrice().intValue() + "");// 支付金额，这边需要转成字符串类型，否则后面的签名会失败
        packageParams.put("spbill_create_ip", spbillCreateIP);
        if (payType != null && payType.equals(PayType.buy)) {
            packageParams.put("notify_url", wxOauth2Proxy.getNotifyBuyUrl());// 支付成功后的回调地址
        } else {
            packageParams.put("notify_url", wxOauth2Proxy.getNotifyLotteryUrl());// 支付成功后的回调地址
        }
        packageParams.put("trade_type", "JSAPI");// 支付方式
        packageParams.put("openid", openId);

        // MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
        String requestXml = "";
        try {
            requestXml = WXPayUtil.generateSignedXml(packageParams, wxOauth2Proxy.getKey(), SignType.MD5);
        } catch (Exception e) {
            logger.error("wx pay unified order sign exception,outTradeNo:" + order.getId());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 调用统一下单接口，并接受返回的结果
        String result = WXPayUtil.httpRequest("https://api.mch.weixin.qq.com/pay/unifiedorder", "POST", requestXml);

        Map<String, String> map = new HashMap<String, String>();
        try {
            map = WXPayUtil.xmlToMap(result);
        } catch (Exception e) {
            logger.error("xml convert to map exception,outTradeNo:" + order.getId());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String return_code = (String) map.get("return_code");
        String result_code = (String) map.get("result_code");

        Map<String, String> response = new HashMap<String, String>();// 返回给小程序端需要的参数
        if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
            String nonceStr = WXPayUtil.generateNonceStr();
            String timeStamp = new Date().getTime() + "";

            String prepay_id = (String) map.get("prepay_id");// 返回的预付单信息
            order.setPrepayId(prepay_id);
            orderService.updateOrder(order);

            response.put("appId", wxOauth2Proxy.getAppId());
            response.put("nonceStr", nonceStr);
            response.put("package", "prepay_id=" + prepay_id);
            response.put("signType", "MD5");
            response.put("timeStamp", timeStamp);// 时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误

            String paySign = null;
            try {
                paySign = WXPayUtil.generateSignature(response,
                        wxOauth2Proxy.getKey(), SignType.MD5);
            } catch (Exception e) {
                logger.error("wx pay sign exception");
                e.printStackTrace();
            }
            response.remove("appId");
            response.put("paySign", paySign);
            response.put("payMoney", order.getPayPrice() + "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @return
     * @throws Exception
     * @Description:微信支付
     */
    @RequestMapping(value = "/notify/lottery")
    @ResponseBody
    public String wxLotteryNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = "";
        InputStream inputStream = request.getInputStream();
        String notityXml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Map<String, String> map = WXPayUtil.xmlToMap(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            if (WXPayUtil.isSignatureValid(map, wxOauth2Proxy.getKey())) {
                String out_trade_no = (String) map.get("out_trade_no");
                if (!StringUtils.isEmpty(out_trade_no)) {
                    Order order = orderService.getById(out_trade_no);
                    if (order != null) {
                        order.setFinishedDate(new Date());
                        order.setStatus(OrderStatus.COMPLETE);
                        //更新订单
                        orderService.updateOrder(order);
                        //添加到抽奖列表中
                        crowdFundingService.addParticipant(order.getUserId(), order.getProductId());
                        productService.updateParticipantNumber(order.getProductId());
                    }

                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }

        return resXml;

    }

    /**
     * @return
     * @throws Exception
     * @Description:微信支付
     */
    @RequestMapping(value = "/notify/buy")
    @ResponseBody
    public String wxBuyNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resXml = "";
        InputStream inputStream = request.getInputStream();
        String notityXml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        Map<String, String> map = WXPayUtil.xmlToMap(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            if (WXPayUtil.isSignatureValid(map, wxOauth2Proxy.getKey())) {
                String out_trade_no = (String) map.get("out_trade_no");
                if (!StringUtils.isEmpty(out_trade_no)) {
                    Order order = orderService.getById(out_trade_no);
                    if (order != null) {
                        order.setFinishedDate(new Date());
                        order.setStatus(OrderStatus.COMPLETE);
                        //更新订单
                        orderService.updateOrder(order);
                        //更新商品信息
                        Product product = productService.getProductById(order.getProductId());
                        if(product != null){
                            product.setStatus(ProductStatus.Finished);
                            productService.updateProduct(product);
                        }
                        //添加到抽奖列表中
                        Winner winner = winnerService.getWinnerByProductIdAndUserId(order.getProductId(), order.getUserId());
                        if (winner != null) {
                            winner.setPayed(true);
                            winner.setPayedTime(order.getFinishedDate());
                            winnerService.updateWinner(winner);

                            WxUser wxUser = wxUserService.getById(order.getUserId());
                            if (wxUser != null){
                                //发送消息
                                TemplateMessage templateMessage = new TemplateMessage();
                                templateMessage.setTouser(wxUser.getWxOpenId());
                                templateMessage.setPage("");
                                templateMessage.setTemplate_id(MessageEnum.USER_PAYED_SUCCESS.getMsgNumber());
                                templateMessage.setForm_id(order.getPrepayId());

                                Map<String, TemplateData> messageData = new HashMap<>();
                                //交易单号
                                TemplateData templateData = new TemplateData(order.getId(),"red");
                                messageData.put("keyword1", templateData);
                                //商品名称
                                templateData = new TemplateData(product.getTitle(),"red");
                                messageData.put("keyword2", templateData);
                                //购买价格
                                templateData = new TemplateData(String.valueOf(order.getPayPrice()),"red");
                                messageData.put("keyword3", templateData);

                                PeopleAddress peopleAddress = peopleAddressService.getDefaultPeopleAddress(order.getUserId());
                                //联系人名字
                                templateData = new TemplateData(peopleAddress.getAddressName(),"red");
                                messageData.put("keyword4", templateData);
                                //联系人地址
                                templateData = new TemplateData(peopleAddress.getAddress(),"red");
                                messageData.put("keyword5", templateData);
                                //联系人电话
                                templateData = new TemplateData(peopleAddress.getPhoneNumber(),"red");
                                messageData.put("keyword6", templateData);
                                //售后客服
                                templateData = new TemplateData("18615703273","red");
                                messageData.put("keyword7", templateData);

                                templateMessage.setData(messageData);
                                wxOauth2Proxy.sendMessageToUser(templateMessage);
                            }
                        }
                    }

                    //通知微信服务器已经支付成功
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }

        return resXml;

    }
}
