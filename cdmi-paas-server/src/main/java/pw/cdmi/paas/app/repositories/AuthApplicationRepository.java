package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.AuthApplication;

@NoRepositoryBean
public interface AuthApplicationRepository
		extends PagingAndSortingRepository<AuthApplication, String>, QueryByExampleExecutor<AuthApplication> {

	public AuthApplication findAuthApplicaitonByAppName(String name);
}
