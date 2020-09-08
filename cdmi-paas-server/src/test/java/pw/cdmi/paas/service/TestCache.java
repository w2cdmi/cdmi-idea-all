package pw.cdmi.paas.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.cdmi.paas.Application;
import pw.cdmi.paas.account.commons.AuthMobileRepository;
import pw.cdmi.paas.account.commons.AuthMobileService;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
//
//public class TestCache {
//
//    @Autowired
//    private AuthMobileService authMobileService;
//    @Autowired
//    private AuthMobileRepository authMobileRepository;
//    @Test
//    public void test() throws Exception {
//    	String mobile="18081257466";
//    	
//    	authMobileService.sendMessage(mobile);
//    	Thread.sleep(1000*5);
//    	
//    	Object value = authMobileRepository.getValue(mobile);
//    	
//    	System.out.println(value+"  "+authMobileService.AuthMobile(mobile, (String)value)); 
//    }
//     
//  
//}