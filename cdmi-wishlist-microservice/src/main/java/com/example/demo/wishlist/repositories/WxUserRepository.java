package com.example.demo.wishlist.repositories;

import com.example.demo.wishlist.model.entities.WxUser;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface WxUserRepository extends PagingAndSortingRepository<WxUser, String>, QueryByExampleExecutor<WxUser>{

    WxUser findByWxOpenId(String openId);
}
