package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteUserRole;

@NoRepositoryBean
public interface SiteUserRoleRepository
		extends PagingAndSortingRepository<SiteUserRole, String>, QueryByExampleExecutor<SiteUserRole> {
	
	public Iterable<SiteUserRole> findByUserIdAndAppId(String userId, String appId);
	
	public Iterable<SiteUserRole> findByRoleId(String roleId);
	
	public Iterable<SiteUserRole> findByUserId(String userId);
	
	public void deleteByRoleId(String roleId);
	
	public void deleteByUserId(String userId);
	
}
