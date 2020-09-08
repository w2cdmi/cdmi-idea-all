package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Employee;

@NoRepositoryBean
public interface EmployeeRepository extends PagingAndSortingRepository<Employee, String>, QueryByExampleExecutor<Employee> {

	Employee findOneByCompanyIdAndCode(String companyId, String code);
	
	Employee findOneByPeopleIdAndCompanyId(String peopleId, String companyId);
	
	Employee findOneByAccountId(String accountId);
	
	Iterable<Employee> findByStatus(String status);
}
