package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Department;

@NoRepositoryBean
public interface DepartmentRepository
		extends PagingAndSortingRepository<Department, String>, QueryByExampleExecutor<Department> {

	public Department findOneByCompanyIdAndNameAndParentId(String companyId, String name, String parentId);
	
	public Iterable<Department> findAllByCompanyIdAndParentIdIsNull(String companyId);
	
	public Iterable<Department> findAllByCompanyId(String companyId);
}
