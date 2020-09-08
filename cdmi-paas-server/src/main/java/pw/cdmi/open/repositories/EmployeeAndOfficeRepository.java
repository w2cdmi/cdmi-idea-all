package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.EmployeeAndOffice;

@NoRepositoryBean
public interface EmployeeAndOfficeRepository
		extends PagingAndSortingRepository<EmployeeAndOffice, String>, QueryByExampleExecutor<EmployeeAndOffice> {
	
	Iterable<EmployeeAndOffice> findByEmployeeId(String employeeId);
	
	Iterable<EmployeeAndOffice> findByOfficeId(String officeId);
	
	EmployeeAndOffice findOneByEmployeeId(String employeeId);
	
	EmployeeAndOffice findOneByOfficeIdAndEmployeeId(String officeId, String employeeId);
	
	long countByOfficeId(String officeId);
}
