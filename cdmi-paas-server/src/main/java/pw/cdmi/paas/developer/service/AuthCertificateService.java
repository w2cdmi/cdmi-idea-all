package pw.cdmi.paas.developer.service;

import pw.cdmi.paas.developer.model.entities.AuthCertificate;

public interface AuthCertificateService {

	/**
	 * 创建一个开放者访问凭证
	 * @return
	 */
	public AuthCertificate createCertificate(String developerId);
	/**
	 * 该开发者所有的ak/sk
	 * @param developerId
	 * @return
	 */
	public Iterable<AuthCertificate> listAuthCertificate(String developerId);
	/**
	 * 更新sk
	 * @param id
	 * @return
	 */
	public String updateAuthCertificate(String id);
	/**
	 * 删除该ak/sk
	 * @param id
	 */
	public void deleteAuthCertificate(String id);
}
