package pw.cdmi.wishlist.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.wishlist.model.entities.Onlooker;

@NoRepositoryBean
public interface OnlookerRepository
		extends PagingAndSortingRepository<Onlooker, String>, QueryByExampleExecutor<Onlooker> {

}
