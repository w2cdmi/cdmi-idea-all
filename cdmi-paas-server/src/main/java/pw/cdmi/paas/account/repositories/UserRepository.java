package pw.cdmi.paas.account.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.account.model.entities.UserAccount;

public interface UserRepository extends PagingAndSortingRepository<UserAccount, String>, QueryByExampleExecutor<UserAccount> {
	
	public UserAccount findOneByWxOpenId(String wxOpenId);
}
