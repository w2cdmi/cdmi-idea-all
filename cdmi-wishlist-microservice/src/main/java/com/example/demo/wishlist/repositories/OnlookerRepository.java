package com.example.demo.wishlist.repositories;

import com.example.demo.wishlist.model.entities.Onlooker;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface OnlookerRepository
		extends PagingAndSortingRepository<Onlooker, String>, QueryByExampleExecutor<Onlooker> {

}
