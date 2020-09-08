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
public interface PermissionService {

    /**
     * 为应用创建一个新的用户角色
     * @param appId 应用的编号
     * @param loginDomain 登录域的编号，可以为空
     * @param siteRole 新的用户角色
     */
    public void createSiteRole(String appId, String loginDomain, SiteRole siteRole);

    /**
     * 为应用更新一个用户角色描述信息，更新不能变更角色美剧值
     * @param appId 应用的编号
     * @param siteRole 待编辑的用户角色
     */
    public void updateSiteRole(String appId, SiteRole siteRole);

    /**
     * 为应用删除一个用户自定义的角色，系统角色不允许被删除
     * @param appId 应用的编号
     */
    public boolean deleteSiteRoleById(String id);

    /**
     * 通过角色的Id，获得角色的详情
     * @param id 角色的Id
     * @return 一个用户角色
     */
    public SiteRole getSiteRoleById(String id);

    /**
     * 通过角色的枚举值，获得角色的详情
     * @param appId 应用的编号
     * @param roleEnum 角色的枚举值，在同一个站点中，枚举值唯一
     * @return 一个用户角色
     */
    public SiteRole getSiteRoleByRoleEnum(String appId, String roleEnum);

    /**
     * 获得一个应用登录域下的所有的用户角色，
     * @param appId 应用的编号
     * @param loginDomain 登录域的编号，可以为空
     * @return 符合条件的用户角色列表
     */
    public Iterable<SiteRole> findSiteRole(String appId, String loginDomain);

    /**
     * 获得指定用户在指定应用的登录域下的所有的角色
     * @param appId 应用的编号
     * @param loginDomain 登录域的编号，可以为空
     * @param userId
     * @return
     */
    public Iterable<SiteRole> findSiteRolesByUserId(String appId, String loginDomain, String userId);

    public Page<SiteRole> findSiteRoleByPage(String appId, String loginDomain, int pageNo, int pageSize);

    public void createSiteMenu(SiteMenu siteMenu);

    public void updateSiteMenu(SiteMenu siteMenu);

    public void deleteSiteMenuById(String id);

    public SiteMenu getSiteMenuById(String id);

    public Iterable<SiteMenu> findAllSiteMenu(String appId);

    public Iterable<SiteMenu> findAllSiteMenuWithSub();

    public Iterable<SiteMenu> findAllSiteMenuBeClass();

    public Iterable<SiteMenu> findAllSiteMenuByParentId(String parentId);

    // public List<SiteMenu> findSiteMenuListByRoleId(int roleId);

    public void createDataAccess(DataAccess dataAccess);

    public void updateDataAccess(DataAccess dataAccess);

    public void deleteDataAccessById(String id);

    public DataAccess getDataAccessById(String id);

    public Iterable<DataAccess> findAllDataAccess();

    public Iterable<DataAccess> findDataAccessListByRoleId(String roleId);

    public void createSiteUserPermissionByUser(String appId, String userDomain, String userId, List<String> roleIds);

    public void createSiteUserPermissionByRole(String appId, String userDomain, String roleEnum, List<String> users);

    public void deleteSiteUserPermissionById(String id);

    public void deleteSiteUserPermissionByUser(String userId);

    public void deleteSiteUserPermissionByRoleId(String roleId);

    public SiteUserRole getSiteUserPermissionById(String id);

    public Iterable<SiteUserRole> findAllSiteUserPermission();

    public Iterable<SiteUserRole> findSiteUserRoleByUserId(String userId);

    public Iterable<SiteUserRole> findSiteUserRoleByRoleId(String roleId);

    public void createSiteMenuAcl(List<String> roleIds, List<String> menuIds);

    public void deleteSiteMenuAclById(String id);

    public void deleteSiteMenuAclByMenuId(String menuId);

    public void deleteSiteMenuAclByRoleId(String roleId);

    public void deleteSiteMenuByMenuId(String menuId);

    public SiteMenuAcl getSiteMenuAclById(String id);

    public Iterable<SiteMenuAcl> findAllSiteMenuAcl();

    public Iterable<SiteMenuAcl> findSiteMenuAclListByRoleId(String roleId);

    public void createRoleAndDataAccess(List<String> roleIds, List<String> dataAccessIds);

    public void deleteRoleAndDataAccessById(String id);

    public void deleteRoleAndDataAccessByDataAccessId(String dataAccessId);

    public void deleteRoleAndDataAccessByRoleId(String roleId);

    public RoleAndDataAccess getRoleAndDataAccessById(String id);

    public Iterable<RoleAndDataAccess> findAllRoleAndDataAccess();

    public Iterable<RoleAndDataAccess> findRoleAndDataAccessListByRoleId(String roleId);

    public void createSiteUserRole(String appId, String userId, String RoleEnum, String roleId);

    public void updateSiteUserRole(SiteUserRole siteUserRole);

}
