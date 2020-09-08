package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Commissioner;

@NoRepositoryBean
public interface CommissionerRepository
		extends PagingAndSortingRepository<Commissioner, String>, QueryByExampleExecutor<Commissioner> {

	public Commissioner findOneByCompanyIdAndName(String companyId, String name);
	
	public Iterable<Commissioner> findByCompanyIdAndName(String companyId, String name);
	
	public Iterable<Commissioner> findByCompanyId(String companyId);
}
