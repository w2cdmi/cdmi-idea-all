package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteMenuAcl;

@NoRepositoryBean
public interface SiteMenuAclRepository
		extends PagingAndSortingRepository<SiteMenuAcl, String>, QueryByExampleExecutor<SiteMenuAcl> {

	public int countByRoleId(String privilegeId);
	
	public Iterable<SiteMenuAcl> findByAppId(String appId);
	
	public Iterable<SiteMenuAcl> findByRoleId(String roleId);
	
	public int deleteByRoleId(String roleId);
}
