package pw.cdmi.paas.developer.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.paas.developer.model.entities.AuthCertificate;
import pw.cdmi.paas.developer.repositories.CertificateRepository;
import pw.cdmi.paas.developer.service.AuthCertificateService;

@Service
public class AuthCertificateServiceImpl implements AuthCertificateService {

	@Autowired
	private CertificateRepository certificateRepository;
	@Override
	public AuthCertificate createCertificate(String developerId) {
		String ak = UUID.randomUUID().toString().replaceAll("-", "");
		String sk = UUID.randomUUID().toString().replaceAll("-", "");
		
		AuthCertificate authCertificate = new AuthCertificate();
		authCertificate.setDeveloperId(developerId);
		authCertificate.setAccessKey(ak);
		authCertificate.setSecretKey(sk);
		authCertificate.setCreateTime(new Date());
		authCertificate.setUpDateTime(authCertificate.getCreateTime());
		
		
		return certificateRepository.save(authCertificate);
	}

	@Override
	public Iterable<AuthCertificate> listAuthCertificate(String developerId) {
		AuthCertificate authCertificate = new AuthCertificate();
		authCertificate.setDeveloperId(developerId);
		return certificateRepository.findAll(Example.of(authCertificate));
	}

	@Override
	public String updateAuthCertificate(String id) {
		AuthCertificate authCertificate = certificateRepository.findById(id).get();
		if(authCertificate==null){
			throw new SecurityException("authCertificate is null");
		}
		String sk = UUID.randomUUID().toString().replaceAll("-", "");
		authCertificate.setSecretKey(sk);
		
		return certificateRepository.save(authCertificate).getSecretKey();
	}

	@Override
	public void deleteAuthCertificate(String id) {
		certificateRepository.deleteById(id);
		
	}

}
