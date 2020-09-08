package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.AuditPrivilege;

@NoRepositoryBean
public interface AuditPrivilegeRepository extends PagingAndSortingRepository<AuditPrivilege, String>, QueryByExampleExecutor<AuditPrivilege>{
	
}
