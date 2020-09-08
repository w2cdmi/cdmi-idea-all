package pw.cdmi.wishlist.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.wishlist.model.entities.PeopleAddress;


@NoRepositoryBean
public interface PeopleAddressRepository extends PagingAndSortingRepository<PeopleAddress, String>, QueryByExampleExecutor<PeopleAddress>{

}
