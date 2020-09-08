package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.PositionalTitle;

@NoRepositoryBean
public interface PositionalTitleRepository
		extends PagingAndSortingRepository<PositionalTitle, String>, QueryByExampleExecutor<PositionalTitle> {
	
	PositionalTitle findOneByCompanyIdAndName(String companyId, String name);
	
	Iterable<PositionalTitle> findAllByCompanyId(String companyId);
}
