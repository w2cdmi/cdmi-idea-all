package pw.cdmi.paas.app.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.paas.app.model.NewApplication;
import pw.cdmi.paas.app.model.SiteAccessStatus;
import pw.cdmi.paas.app.model.SiteStatus;
import pw.cdmi.paas.app.model.entities.AuthApplication;
import pw.cdmi.paas.app.repositories.AuthApplicationRepository;
import pw.cdmi.paas.app.service.AuthApplicationService;

@Service
@Transactional
public class AuthApplicationServiceImpl implements AuthApplicationService {

	@Autowired
	private AuthApplicationRepository repository;

	@Override
	public String createAuthApplication(NewApplication authapp) {
		// 判断应用的名称是否已存在，如果已存在，则不允许创建
		AuthApplication app = repository.findAuthApplicaitonByAppName(authapp.getName());
		if (app != null) {
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		app = new AuthApplication();
		app.setAppName(authapp.getName());
		app.setDescription(authapp.getDescription());
		app.setAttribution(authapp.getAttribution());
		app.setType(authapp.getType());
		app.setUrlIcon(authapp.getLogo_url());
		app.setStatus(SiteStatus.Normal);
		app.setAccessStatus(SiteAccessStatus.Normal);
		app.setAttribution(authapp.getAttribution());

		app.setCreateTime(new Date());
		app.setUpdateTime(app.getCreateTime());
		app = repository.save(app);

		return app.getAppId();
	}

	@Override
	public String createAuthApplication(NewApplication authapp, String owner_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<AuthApplication> listAuthApplication(String developerId) {
		AuthApplication authApplication = new AuthApplication();

		authApplication.setDeveloperId(developerId);
		return repository.findAll(Example.of(authApplication));
	}

	@Override
	public AuthApplication findAuthApplicationById(String id) {
		return repository.findById(id).get();
	}

	@Override
	public void updateAuthApplication(AuthApplication application) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(String id) {
		if (repository.findById(id).get() == null) {
			throw new SecurityException("删除失败");
		}
		repository.deleteById(id);

	}
}
