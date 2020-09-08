package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.DepartmentGroup;

@NoRepositoryBean
public interface DepartmentGroupRepository
		extends PagingAndSortingRepository<DepartmentGroup, String>, QueryByExampleExecutor<DepartmentGroup> {
	
	public DepartmentGroup findOneByDeptIdAndName(String deptId, String deptname);
	
	public Iterable<DepartmentGroup> findByNameLike(String name);
	
	public Iterable<DepartmentGroup> findAllByDeptId(String deptId);
 	
}
