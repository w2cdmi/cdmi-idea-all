package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.MobileVerification;

@NoRepositoryBean
public interface MobileVerificationRepository
		extends PagingAndSortingRepository<MobileVerification, String>, QueryByExampleExecutor<MobileVerification> {

	MobileVerification findOneByMobileAndCode(String mobile, String code);
}
