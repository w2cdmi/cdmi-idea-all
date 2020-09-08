package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.SiteMenu;

@NoRepositoryBean
public interface SiteMenuRepository
		extends PagingAndSortingRepository<SiteMenu, String>, QueryByExampleExecutor<SiteMenu> {

	public Iterable<SiteMenu> findByParentIdIsNotNull();
	
	public Iterable<SiteMenu> findByParentId(String parentId);
	
	public Iterable<SiteMenu> findByAppId(String appId);	
	
	public Iterable<SiteMenu> findByParentIdNotNull();
	
	public Iterable<SiteMenu> findByBeClass(boolean isClass);
}
