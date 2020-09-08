package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.EmployeeAndCommissioner;

@NoRepositoryBean
public interface EmployeeAndCommissionerRepository extends PagingAndSortingRepository<EmployeeAndCommissioner, String>,
		QueryByExampleExecutor<EmployeeAndCommissioner> {

	Iterable<EmployeeAndCommissioner> findByCommissionerId(String commissionerId);

	EmployeeAndCommissioner findOneByCommissionerIdAndEmployeeId(String commissionerId, String employeeId);
	
	Iterable<EmployeeAndCommissioner> findByEmployeeId(String employeeId);
	
	long countByCommissionerId(String commissionerId);
}
