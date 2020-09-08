package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.entities.Product;
import com.example.demo.wishlist.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class ProductServiceTest {

//    @Autowired
//    private ProductService productService;

    @Test
    public void createTest() {
//        Product product = new Product();
//        product.setTitle("123");
//        product.setPhotoList("[{url:11},{url:22}]");
//        product.setOriginalPrice(20f);
//        product.setActualPrice(10f);
//        product.setSalesValidity(new Date());
//        product.setOnlookerNumber(1000);
//        product.setParticipantNumber(30);
//        product.setStatus(ProductStatus.NotStarted);
//        product.setPayLimitTime(new Date());
//        product.setWinTime(new Date());
//        product.setTotalMoney(30);
//
//        productService.createProduct(product);
    }
}
