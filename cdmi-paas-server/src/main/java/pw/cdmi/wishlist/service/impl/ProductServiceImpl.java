package pw.cdmi.wishlist.service.impl;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.websocket.WebSocket;

import pw.cdmi.wechat.miniprogram.model.entities.TemplateData;
import pw.cdmi.wechat.miniprogram.model.entities.TemplateMessage;
import pw.cdmi.wishlist.model.OrderStatus;
import pw.cdmi.wishlist.model.ProductStatus;
import pw.cdmi.wishlist.model.entities.Onlooker;
import pw.cdmi.wishlist.model.entities.Order;
import pw.cdmi.wishlist.model.entities.Participant;
import pw.cdmi.wishlist.model.entities.Product;
import pw.cdmi.wishlist.model.entities.Winner;
import pw.cdmi.wishlist.model.entities.WxUser;
import pw.cdmi.wishlist.model.wechat.MessageEnum;
import pw.cdmi.wishlist.repositories.jpa.JpaOnlookerRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaOrderRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaParticipantRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaProductRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaWinnerRepository;
import pw.cdmi.wishlist.repositories.jpa.JpaWxUserRepository;
import pw.cdmi.wishlist.service.PeopleAddressService;
import pw.cdmi.wishlist.service.ProductService;
import pw.cdmi.wishlist.wechat.WxOauth2Proxy;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private JpaProductRepository jpaProductRepository;

    @Autowired
    private JpaOnlookerRepository jpaOnlookerRepository;

    @Autowired
    private JpaParticipantRepository jpaParticipantRepository;

    @Autowired
    private JpaWinnerRepository jpaWinnerRepository;

    @Autowired
    private JpaWxUserRepository jpaWxUserRepository;

    @Autowired
    private JpaOrderRepository jpaOrderRepository;

    @Autowired
    private WxOauth2Proxy wxOauth2Proxy;

    @Autowired
    private PeopleAddressService peopleAddressService;

    @Override
    public Product createProduct(Product product) {
        Product responseProduct = jpaProductRepository.save(product);

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
                String beanName = "scheduledTask";
                if (!applicationContext.containsBean(beanName)) {
                    applicationContext.register(ScheduledTask.class);
                    applicationContext.registerBean(ScheduledTask.class, responseProduct.getSalesValidity());
                }
                applicationContext.refresh();
                while (applicationContext.containsBean(beanName)) {
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        continue;
                    }
                    //判断愿望商品是否达到抽奖人员数量
                    Product product = jpaProductRepository.findById(responseProduct.getId()).get();
                    if (product == null) {
                        //移除异步任务
                        applicationContext.removeBeanDefinition(beanName);
                        return;
                    }
                    if (product.getParticipantNumber() >= product.getRatedNumber()) {
                        product.setStatus(ProductStatus.ChoiceWinner);
                        jpaProductRepository.save(product);
                        startLottery(product);
                        //移除异步任务
                        applicationContext.removeBeanDefinition(beanName);
                    }else {
                        if (product.getSalesValidity().getTime() < new Date().getTime()) {
                            product.setStatus(ProductStatus.Canceled);
                            product.setCancelReason("参与人数不够，抽奖取消");
                            jpaProductRepository.save(product);
                            //退还钱

                            //移除异步任务
                            applicationContext.removeBeanDefinition(beanName);
                        }
                    }

                }
            }
        });

        return responseProduct;
    }

    @Override
    public Product updateProduct(Product product) {
        return jpaProductRepository.save(product);
    }

    private void startLottery(Product product) {
        List<Participant> participantList = jpaParticipantRepository.findByProductId(product.getId());
        List<Onlooker> onlookerList = new ArrayList<>();
        List<Onlooker> tempOnlookerList = null;
        //暂时方案，后面改成多线程查询数据库
        for (Participant participant : participantList){
            tempOnlookerList = jpaOnlookerRepository.findByProductIdAndInviterId(product.getId(), participant.getWxuserId());
            if(tempOnlookerList != null && tempOnlookerList.size() > 0){
                onlookerList.addAll(tempOnlookerList);
            }
        }
        List<String> userIdList = new ArrayList<>();
        //每人默认概率
        for (Participant participant : participantList) {
            userIdList.add(participant.getWxuserId());
        }
        //邀请人数增加概率
        for (Onlooker onlooker : onlookerList) {
            userIdList.add(onlooker.getInviterId());
        }
        int num = userIdList.size();
        //中奖索引
        int index = (int)(Math.random() * num);
        String luckUserId = userIdList.get(index);

        Winner winner = new Winner();
        winner.setProductId(product.getId());
        winner.setWxuserId(luckUserId);
        winner.setPayed(false);
        winner.setCreateTime(new Date());
        jpaWinnerRepository.save(winner);
        product.setStatus(ProductStatus.WaitPay);
        product.setWinnerId(luckUserId);
        product.setWinTime(new Date());
        jpaProductRepository.save(product);

        WxUser wxUser = jpaWxUserRepository.findById(luckUserId).get();

        //发送消息
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTouser(wxUser.getWxOpenId());
        templateMessage.setPage("pages/winner/winner?productId=" + product.getId());
        templateMessage.setTemplate_id(MessageEnum.LUCK_USER_MESSAGE.getMsgNumber());
        Order order = jpaOrderRepository.findByUserIdAndProductIdAndStatus(wxUser.getId(), product.getId(), OrderStatus.COMPLETE);
        templateMessage.setForm_id(order.getPrepayId());

        Map<String, TemplateData> map = new HashMap<>();
        //中奖姓名
        TemplateData templateData = new TemplateData(wxUser.getNick(),"red");
        map.put("keyword1", templateData);
        //商品名称
        templateData = new TemplateData(product.getTitle(),"red");
        map.put("keyword2", templateData);
        //开奖结果
        templateData = new TemplateData("恭喜您中奖了","red");
        map.put("keyword3", templateData);
        //中奖时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        templateData = new TemplateData(simpleDateFormat.format(new Date()),"red");
        map.put("keyword4", templateData);
        //领奖方式
        templateData = new TemplateData("请点击消息通知进入小程序，并且在24小时内支付众筹价格获得商品","red");
        map.put("keyword5", templateData);

        templateMessage.setData(map);
        wxOauth2Proxy.sendMessageToUser(templateMessage);
    }

    @Override
    public List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        productList = (List<Product>) jpaProductRepository.findAll();
        return productList;
    }

    @Override
    public List<Product> getInProgressProductList() {
        List<Product> productList = new ArrayList<>();
        productList = (List<Product>) jpaProductRepository.findByStatus(ProductStatus.InProgress);
        return productList;
    }

    @Override
    public Product getProductById(String id) {
        Product product = jpaProductRepository.findById(id).get();

        return product;
    }

    @Override
    public Product getProductByIdAndStatus(String id, ProductStatus productStatus) {
        Product product = jpaProductRepository.findByIdAndStatus(id, productStatus);
        return product;
    }

    @Override
    public Boolean updateOnlookerNumber(String productId) {
        Boolean flag = false;
        Long num = jpaOnlookerRepository.countByProductId(productId);
        if (num == null || num == 0) {
            return flag;
        }
        Product product = jpaProductRepository.findById(productId).get();
        if (product == null) {
            return flag;
        }

        product.setOnlookerNumber(product.getOnlookerNumber() + 1);
        product = jpaProductRepository.save(product);

        if (product != null) {
            flag = true;
            JSONObject json = new JSONObject();
            json.put("onlookerNumber", product.getOnlookerNumber());
            try {
                for (WebSocket item : WebSocket.webSocketSet) {
                    item.sendMessage(json.toJSONString());
                }
            } catch (IOException e) {
                System.out.println("productId = [" + productId + "]");
            }
        }
        return flag;
    }

    @Override
    public Boolean updateParticipantNumber(String productId) {
        Boolean flag = false;
        Long num = jpaParticipantRepository.countByProductId(productId);
        if (num == null || num == 0) {
            return flag;
        }

        Product product = jpaProductRepository.findById(productId).get();
        if (product == null) {
            return flag;
        }

        product.setParticipantNumber(product.getParticipantNumber() + 1);
        product = jpaProductRepository.save(product);

        if (product != null) {
            flag = true;
            JSONObject json = new JSONObject();
            json.put("participantNumber", product.getParticipantNumber());
            try {
                for (WebSocket item : WebSocket.webSocketSet) {
                    item.sendMessage(json.toJSONString());
                }
            } catch (IOException e) {
                System.out.println("productId = [" + productId + "]");
            }
        }

        return flag;
    }


}
