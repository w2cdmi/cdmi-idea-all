package pw.cdmi.wishlist.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.wishlist.model.entities.WxUser;

@NoRepositoryBean
public interface WxUserRepository extends PagingAndSortingRepository<WxUser, String>, QueryByExampleExecutor<WxUser>{

    WxUser findByWxOpenId(String openId);
}
