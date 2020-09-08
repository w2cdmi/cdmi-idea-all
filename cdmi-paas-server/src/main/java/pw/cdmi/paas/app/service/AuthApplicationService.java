package pw.cdmi.paas.app.service;

import pw.cdmi.paas.app.model.NewApplication;
import pw.cdmi.paas.app.model.entities.AuthApplication;

public interface AuthApplicationService {
	
	public String createAuthApplication(NewApplication authapp);
	
	public String createAuthApplication(NewApplication authapp, String owner_id);
	
	public Iterable<AuthApplication> listAuthApplication(String developerId);
	
	public AuthApplication findAuthApplicationById(String id);
	
	public void updateAuthApplication(AuthApplication application);
	
	public void deleteById(String id);
	
}
