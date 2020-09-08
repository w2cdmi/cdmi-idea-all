package com.example.demo.wishlist.repositories;

import com.example.demo.wishlist.model.entities.PeopleAddress;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;


@NoRepositoryBean
public interface PeopleAddressRepository extends PagingAndSortingRepository<PeopleAddress, String>, QueryByExampleExecutor<PeopleAddress>{

}
