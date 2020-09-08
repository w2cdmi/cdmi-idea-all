package pw.cdmi.wishlist.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.wishlist.model.entities.Winner;

@NoRepositoryBean
public interface WinnerRepository extends PagingAndSortingRepository<Winner, String>, QueryByExampleExecutor<Winner>{

}
