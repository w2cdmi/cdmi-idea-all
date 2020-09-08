package pw.cdmi.paas.app.repositories;

import java.util.Collection;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.DataAccess;

@NoRepositoryBean
public interface DataAccessRepository
		extends PagingAndSortingRepository<DataAccess, String>, QueryByExampleExecutor<DataAccess> {

	public Iterable<DataAccess> findByParentIdIsNotNull();
	
	public Iterable<DataAccess> findByParentIdIsNull();
	
	public Iterable<DataAccess> findByParentId(String parentId);
	
	public Iterable<DataAccess> findByParentIdAndIdIn(String parentId, Collection<String> list);
	
	public Iterable<DataAccess> findByParentIdNotNullAndIdIn(Collection<String> list);
}
