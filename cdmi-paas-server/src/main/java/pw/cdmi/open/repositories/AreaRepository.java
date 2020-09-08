package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Area;

@NoRepositoryBean
public interface AreaRepository
		extends PagingAndSortingRepository<Area, String>, QueryByExampleExecutor<Area> {

	Iterable<Area> findByCompanyId(String companyId);
}
