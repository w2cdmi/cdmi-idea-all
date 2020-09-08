package com.example.demo.wishlist.repositories;

import com.example.demo.wishlist.model.entities.Participant;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;


@NoRepositoryBean
public interface ParticipantRepository extends PagingAndSortingRepository<Participant, String>, QueryByExampleExecutor<Participant>{

}
