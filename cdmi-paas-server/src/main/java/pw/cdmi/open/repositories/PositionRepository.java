package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.Position;

@NoRepositoryBean
public interface PositionRepository
		extends PagingAndSortingRepository<Position, String>, QueryByExampleExecutor<Position> {

	Position findOneByCompanyIdAndName(String companyId, String name);
	
	Iterable<Position> findByCompanyId(String companyId);
	
	Iterable<Position> findByCompanyIdAndNameLike(String companyId, String name);
}
