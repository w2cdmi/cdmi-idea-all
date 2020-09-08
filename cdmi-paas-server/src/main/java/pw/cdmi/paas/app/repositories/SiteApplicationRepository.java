package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteApplication;

@NoRepositoryBean
public interface SiteApplicationRepository
		extends PagingAndSortingRepository<SiteApplication, String>, QueryByExampleExecutor<SiteApplication> {

}
