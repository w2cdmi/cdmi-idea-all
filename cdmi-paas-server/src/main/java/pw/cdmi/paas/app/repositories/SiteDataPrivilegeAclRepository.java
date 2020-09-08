package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteDataPrivilegeAcl;

@NoRepositoryBean
public interface SiteDataPrivilegeAclRepository extends PagingAndSortingRepository<SiteDataPrivilegeAcl, String>, QueryByExampleExecutor<SiteDataPrivilegeAcl> {

	public Iterable<SiteDataPrivilegeAcl> findByAuthEnumAndAppId(Object auth, String appId);
}
