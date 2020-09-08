package pw.cdmi.paas.app.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.app.model.entities.RoleAndDataAccess;

@NoRepositoryBean
public interface RoleAndDataAccessRepository
		extends PagingAndSortingRepository<RoleAndDataAccess, String>, QueryByExampleExecutor<RoleAndDataAccess> {

	public Iterable<RoleAndDataAccess> findByRoleId(String roleId);
	
	public Iterable<RoleAndDataAccess> findByDataAccessIdAndRoleId(String accessId, String roleId);
	
}
