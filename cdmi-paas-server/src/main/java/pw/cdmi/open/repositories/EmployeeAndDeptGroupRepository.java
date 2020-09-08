package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.EmployeeAndDeptGroup;

@NoRepositoryBean
public interface EmployeeAndDeptGroupRepository
		extends PagingAndSortingRepository<EmployeeAndDeptGroup, String>, QueryByExampleExecutor<EmployeeAndDeptGroup> {

	Iterable<EmployeeAndDeptGroup> findByDeptGroupId(String deptGroupId);
	
	Iterable<EmployeeAndDeptGroup> findByEmployeeId(String employeeId);
	
	EmployeeAndDeptGroup findOneByDeptGroupIdAndEmployeeId(String deptGroupId, String employeeId);
	
	long countByDeptGroupId(String deptGroupId);
}
