package pw.cdmi.paas.app.repositories.jpa;

import org.springframework.data.jpa.repository.Query;

import pw.cdmi.paas.app.repositories.SiteMenuAclRepository;

public interface JpaSiteMenuAclRepository extends SiteMenuAclRepository {

	@Query("delete SiteMenuAcl where roleId= ?1")
	public int deleteByRoleId(String roleId);
}
