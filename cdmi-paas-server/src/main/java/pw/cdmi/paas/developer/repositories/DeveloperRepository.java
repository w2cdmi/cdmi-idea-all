package pw.cdmi.paas.developer.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.developer.model.entities.Developer;

@NoRepositoryBean
public interface DeveloperRepository
		extends PagingAndSortingRepository<Developer, String>, QueryByExampleExecutor<Developer> {

	public Developer findOneByUserId(String userId);
}
