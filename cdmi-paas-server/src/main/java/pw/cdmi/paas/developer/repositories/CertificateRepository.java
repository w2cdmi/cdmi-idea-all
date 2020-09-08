package pw.cdmi.paas.developer.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.paas.developer.model.entities.AuthCertificate;

@NoRepositoryBean
public interface CertificateRepository
		extends PagingAndSortingRepository<AuthCertificate, String>, QueryByExampleExecutor<AuthCertificate> {

}
