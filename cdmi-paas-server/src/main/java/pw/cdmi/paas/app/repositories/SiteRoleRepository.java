package pw.cdmi.paas.app.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteRole;

@NoRepositoryBean
public interface SiteRoleRepository
		extends PagingAndSortingRepository<SiteRole, String>, QueryByExampleExecutor<SiteRole> {

	public Iterable<SiteRole> findByAppId(String appId);
	
	public Page<SiteRole> findByAppId(String appId, Pageable pageable);

	public Iterable<SiteRole> findByAppIdAndRoleEnum(String appId, String role);

	public SiteRole findOneByNameAndAppId(String name, String appId);

	public SiteRole findOneByRoleEnumAndAppId(String roleEnum, String appId);

	public SiteRole findOneByRoleEnumAndUserDomainAndAppId(String roleEnum, String userDomain, String appId);
	
	public Iterable<SiteRole> findByUserDomainAndAppId(String userDomain, String appId);
	
	public Page<SiteRole> findByUserDomainAndAppId(String userDomain, String appId, Pageable pageable);
	
	public SiteRole findOneByAppIdAndRoleEnum(String appId, String roleEnum);
	
}
