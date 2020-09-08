package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Office;

@NoRepositoryBean
public interface OfficeRepository extends PagingAndSortingRepository<Office, String>, QueryByExampleExecutor<Office>{
	
	public Office findOneByCompanyIdAndName(String companyId, String officeName);
	
	public Iterable<Office> findAllByCompanyId(String companyId);
	
	public Iterable<Office> findAllByCompanyIdAndNameLike(String companyId, String officeName);
	
	public Iterable<Office> findAllBySupervisorId(String employeeId);
	
	public Iterable<Office> findByAreaId(String areaId);
}
