package pw.cdmi.open.service;

import java.util.List;

import org.springframework.data.domain.Page;

import pw.cdmi.open.model.entities.DataAccess;
import pw.cdmi.paas.app.model.entities.RoleAndDataAccess;
import pw.cdmi.paas.app.model.entities.SiteMenu;
import pw.cdmi.paas.app.model.entities.SiteMenuAcl;
import pw.cdmi.paas.app.model.entities.SiteRole;
import pw.cdmi.paas.app.model.entities.SiteUserRole;

/************************************************************
 * 接口类，提供权限管理的相关操作方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-7
 ************************************************************/
public interface SiteMenuService {

	public void createSiteRole(SiteRole siteRole);

	public void updateSiteRole(SiteRole siteRole);

	public void deleteSiteRoleById(String id);

	public SiteRole getSiteRoleById(String id);

	public Iterable<SiteRole> findAllSiteRole();

	public Page<SiteRole> findSiteRoleByPage(int pageNo, int pageSize);

	public void createSiteMenu(SiteMenu siteMenu);

	public void updateSiteMenu(SiteMenu siteMenu);

	public void deleteSiteMenuById(String id);

	public SiteMenu getSiteMenuById(String id);

	public Iterable<SiteMenu> findAllSiteMenu();

	public Iterable<SiteMenu> findAllSiteMenuWithSub();

//	public List<SiteMenu> findSiteMenuListByRoleId(int roleId);

	public void createDataAccess(DataAccess dataAccess);

	public void updateDataAccess(DataAccess dataAccess);

	public void deleteDataAccessById(String id);

	public DataAccess getDataAccessById(String id);

	public Iterable<DataAccess> findAllDataAccess();

	public Iterable<DataAccess> findDataAccessListByRoleId(String roleId);

	public Iterable<SiteUserRole> findAllSiteUserPermission();

	public Iterable<SiteUserRole> findSiteUserPermissionListByUserId(String employeeId);

	public Iterable<SiteUserRole> findSiteUserPermissionListByRoleId(String roleId);

	public void createSiteMenuAcl(Iterable<String> roleIds, Iterable<String> menuIds);

	public void deleteSiteMenuAclById(String id);

	public void deleteSiteMenuAclByMenuId(String menuId);

	public void deleteSiteMenuAclByRoleId(String roleId);

	public SiteMenuAcl getSiteMenuAclById(String id);

	public Iterable<SiteMenuAcl> findAllSiteMenuAcl();

	public Iterable<SiteMenuAcl> findSiteMenuAclListByRoleId(String roleId);

	public void createRoleAndDataAccess(Iterable<String> roleIds, Iterable<String> dataAccessIds);

	public void deleteRoleAndDataAccessById(String id);

	public void deleteRoleAndDataAccessByDataAccessId(String dataAccessId);

	public void deleteRoleAndDataAccessByRoleId(String roleId);

	public RoleAndDataAccess getRoleAndDataAccessById(String id);

	public Iterable<RoleAndDataAccess> findAllRoleAndDataAccess();

	public Iterable<RoleAndDataAccess> findRoleAndDataAccessListByRoleId(String roleId);

}
