package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Company;

@NoRepositoryBean
public interface CompanyRepository extends PagingAndSortingRepository<Company, String>, QueryByExampleExecutor<Company>{
	
	public Iterable<Company> findByParentId(String parentId);
	
	public Iterable<Company> findByParentIdNotNull();
	
	public Iterable<Company> findByParentIdAndAreaId(String parentId, String areaId);
	
	public Iterable<Company> findBySupervisorId(String employeeId);
	
	public Company findOneByOpenId(String openId);
	
	public Company findByNameLike(String name);
	
	public Company findByBusinessRegistrationNameLike(String businessRegistrationName);
	
	public Company findByTaxCode(String taxCode);
	
	public Company findOneByAppId(String appId);
}
