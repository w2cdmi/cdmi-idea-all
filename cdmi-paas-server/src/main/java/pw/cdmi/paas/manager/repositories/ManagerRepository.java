package pw.cdmi.paas.manager.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.manager.model.entities.Manager;

@NoRepositoryBean
public interface ManagerRepository extends PagingAndSortingRepository<Manager, String>, QueryByExampleExecutor<Manager> {

}
