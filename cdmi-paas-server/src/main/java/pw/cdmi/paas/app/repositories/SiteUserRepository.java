package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteUser;

public interface SiteUserRepository
		extends PagingAndSortingRepository<SiteUser, String>, QueryByExampleExecutor<SiteUser> {

}
