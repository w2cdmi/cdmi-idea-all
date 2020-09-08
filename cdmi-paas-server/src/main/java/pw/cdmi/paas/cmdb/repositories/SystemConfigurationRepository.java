package pw.cdmi.paas.cmdb.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.cmdb.model.entities.SystemConfiguration;

@NoRepositoryBean
public interface SystemConfigurationRepository
		extends PagingAndSortingRepository<SystemConfiguration, String>, QueryByExampleExecutor<SystemConfiguration> {

}
