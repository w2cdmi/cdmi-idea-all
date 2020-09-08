package pw.cdmi.open.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.AWSServiceException;
import pw.cdmi.core.http.exception.ClientError;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.core.http.exception.SystemReason;
import pw.cdmi.open.model.entities.DataAccess;
import pw.cdmi.open.service.PermissionService;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.paas.app.model.entities.RoleAndDataAccess;
import pw.cdmi.paas.app.model.entities.SiteMenu;
import pw.cdmi.paas.app.model.entities.SiteMenuAcl;
import pw.cdmi.paas.app.model.entities.SiteRole;
import pw.cdmi.paas.app.model.entities.SiteUser;
import pw.cdmi.paas.app.model.entities.SiteUserRole;
import pw.cdmi.paas.app.repositories.DataAccessRepository;
import pw.cdmi.paas.app.repositories.RoleAndDataAccessRepository;
import pw.cdmi.paas.app.repositories.SiteMenuAclRepository;
import pw.cdmi.paas.app.repositories.SiteMenuRepository;
import pw.cdmi.paas.app.repositories.SiteRoleRepository;
import pw.cdmi.paas.app.repositories.SiteUserRoleRepository;

/************************************************************
 * 实现类，提供权限管理的操作方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-7
 ************************************************************/
@Service
public class PermissionServiceImpl implements PermissionService {

	private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

	@Autowired
	private SiteRoleRepository daoImpl;

	@Autowired
	private SiteUserRoleRepository userRoleRepository;

	@Autowired
	private SiteMenuRepository menuRepository;

	@Autowired
	private DataAccessRepository dataAccessRepository;

	@Autowired
	private RoleAndDataAccessRepository roleAndDataAccessRepository;

	@Autowired
	private SiteMenuAclRepository menuAclRepository;

	@Autowired
	private UserService userService;

	@Override
	@Transactional
	public void createSiteRole(String appId, String loginDomain, SiteRole siteRole) {
		if (!StringUtils.isEmpty(siteRole.getAppId()) && !siteRole.getAppId().equals(appId)) {
			throw new AWSClientException(ClientError.ErrorPermissionType, ClientReason.ErrorPermissionType);
		}
		if (StringUtils.isEmpty(siteRole.getName()) || StringUtils.isEmpty(siteRole.getRoleEnum())) {
			throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
		}
		SiteRole role2 = daoImpl.findOneByNameAndAppId(siteRole.getName(), appId);
		if (role2 != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}

		SiteRole role = null;
		if (StringUtils.isEmpty(loginDomain)) {
			role = daoImpl.findOneByRoleEnumAndAppId(siteRole.getRoleEnum(), appId);
		} else {
			role = daoImpl.findOneByRoleEnumAndUserDomainAndAppId(siteRole.getRoleEnum(), loginDomain, appId);
		}
		if (role == null) {
			siteRole.setAppId(appId);
			daoImpl.save(siteRole);
		} else {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
	}

	@Override
	@Transactional
	public void updateSiteRole(String appId, SiteRole siteRole) {
		if (!StringUtils.isEmpty(siteRole.getAppId()) && !siteRole.getAppId().equals(appId)) {
			throw new AWSClientException(ClientError.ErrorPermissionType, ClientReason.ErrorPermissionType);
		}
		if (StringUtils.isEmpty(siteRole.getName())) {
			throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
		}
		SiteRole role2 = daoImpl.findOneByNameAndAppId(siteRole.getName(), appId);
		if (role2 != null) {
			throw new AWSClientException(ClientError.DataAlreadyExists, ClientReason.DataAlreadyExists);
		}
		// 只有自定义的角色才能进行修改，但也不允许修改RoleEnmu值
		SiteRole role = daoImpl.findById(siteRole.getId()).get();
		if (role.getCustomRole() != null && !role.getCustomRole().booleanValue()) {
			throw new AWSClientException(ClientError.ErrorPermissionType, ClientReason.ErrorPermissionType);
		}
		if (StringUtils.isEmpty(siteRole.getRoleEnum()) || !siteRole.getRoleEnum().equals(role.getRoleEnum())) {
			throw new AWSClientException(ClientError.ErrorPermissionType, ClientReason.ErrorPermissionType);
		}
		siteRole.setAppId(appId);
		daoImpl.save(siteRole);

	}

	@Override
	@Transactional
	public boolean deleteSiteRoleById(String id) {
		try {
			SiteRole siteRole = getSiteRoleById(id);
			if (siteRole != null && siteRole.getCustomRole() != null && siteRole.getCustomRole()) {
				daoImpl.delete(siteRole);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public SiteRole getSiteRoleById(String id) {
		return daoImpl.findById(id).get();
	}

	@Override
	public SiteRole getSiteRoleByRoleEnum(String appId, String roleEnum) {
		return daoImpl.findOneByAppIdAndRoleEnum(appId, roleEnum);
	}

	@Override
	public Iterable<SiteRole> findSiteRole(String appId, String loginDomain) {
		if (StringUtils.isEmpty(loginDomain)) {
			return daoImpl.findByAppId(appId);
		} else {
			return daoImpl.findByUserDomainAndAppId(loginDomain, appId);
		}

	}

	@Override
	public Iterable<SiteRole> findSiteRolesByUserId(String appId, String loginDomain, String userId) {
		// String hql =
		// "select r from SiteRole r, SiteUserRole u where r.appId = '"
		// + appId
		// + "' and r.loginDomain='"
		// + loginDomain
		// + "' and u.accountId = "
		// + accountId
		// + " and r.roleEnum = u.roleEnum";
		// return jpaImpl.find(hql);

		Iterable<SiteUserRole> userroles = userRoleRepository.findByUserIdAndAppId(userId, appId);
		List<SiteRole> list = new ArrayList<SiteRole>();
		for (SiteUserRole userrole : userroles) {
			if (StringUtils.isEmpty(loginDomain)) {
				list.add(daoImpl.findOneByRoleEnumAndAppId(userrole.getRoleEnum(), appId));
			} else {
				list.add(daoImpl.findOneByRoleEnumAndUserDomainAndAppId(userrole.getRoleEnum(), loginDomain, appId));
			}

		}
		return list;
	}

	@Override
	public Page<SiteRole> findSiteRoleByPage(String appId, String loginDomain, int pageNo, int pageSize) {
		Pageable pageable = new PageRequest(pageNo, pageSize);
		if (StringUtils.isEmpty(loginDomain)) {
			return daoImpl.findByAppId(appId, pageable);
		} else {
			return daoImpl.findByUserDomainAndAppId(loginDomain, appId, pageable);
		}
	}

	@Override
	@Transactional
	public void createSiteMenu(SiteMenu siteMenu) {
		menuRepository.save(siteMenu);
	}

	@Override
	@Transactional
	public void updateSiteMenu(SiteMenu siteMenu) {
		try {
			menuRepository.save(siteMenu);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteSiteMenuById(String id) {
		try {
			SiteMenu siteMenu = getSiteMenuById(id);
			if (siteMenu != null) {
				menuRepository.delete(siteMenu);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public SiteMenu getSiteMenuById(String id) {
		SiteMenu siteMenu = null;
		try {
			siteMenu = menuRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return siteMenu;
	}

	@Override
	public Iterable<SiteMenu> findAllSiteMenu(String appId) {
		try {
			return menuRepository.findByAppId(appId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Iterable<SiteMenu> findAllSiteMenuWithSub() {
		Iterable<SiteMenu> list = new ArrayList<SiteMenu>();
		try {
			list = menuRepository.findByParentIdNotNull();
			list = getAllSubMenu(list);
		} catch (NoResultException e) {
			log.warn("没有找到相应的结果！");
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	/**
	 * 获取所有菜单的所有下级子菜单列表列表
	 * 
	 * @param list
	 *            上级菜单列表
	 * @return 返回所有菜单列表
	 */
	private Iterable<SiteMenu> getAllSubMenu(Iterable<SiteMenu> list) {
		for (SiteMenu siteMenu : list) {
			Iterable<SiteMenu> subList = menuRepository.findByParentId(siteMenu.getId());
			if (subList.iterator().hasNext()) {
				subList = getAllSubMenu(subList);
				siteMenu.setSubMenus(subList);
			}
		}
		return list;
	}

	// @Override
	// public List<SiteMenu> findSiteMenuListByRoleId(int roleId) {
	// List<SiteMenu> list = new ArrayList<SiteMenu>();
	// try {
	// list = jpaImpl.findByNamedQuery("SiteMenu.findListByRoleId", new Object[]
	// { roleId, null });
	// list = getSubMenus(list, roleId);
	// } catch (NoResultException e) {
	// log.warn("没有找到相应的结果！");
	// return null;
	// } catch (Exception e) {
	// log.error(e.getMessage());
	// e.printStackTrace();
	// throw new AWSServiceException(SystemError.SQLError);
	// }
	// return list;
	// }

	// /**
	// * 根据角色编号获取菜单下级子菜单的方法
	// *
	// * @param list 上级菜单列表
	// * @param roleId 角色的编号
	// * @return 返回菜单列表
	// */
	// private List<SiteMenu> getSubMenus(List<SiteMenu> list, int roleId) {
	// for (SiteMenu siteMenu : list) {
	// List<SiteMenu> subList =
	// jpaImpl.findByNamedQuery("SiteMenu.findListByRoleId", new Object[] {
	// roleId,
	// siteMenu.getId() });
	// if (!subList.isEmpty()) {
	// subList = getSubMenus(subList, roleId);
	// siteMenu.setSubMenus(subList);
	// }
	// }
	// return list;
	// }

	@Override
	@Transactional
	public void createDataAccess(DataAccess dataAccess) {
		try {
			dataAccessRepository.save(dataAccess);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void updateDataAccess(DataAccess dataAccess) {
		try {
			dataAccessRepository.save(dataAccess);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void deleteDataAccessById(String id) {
		try {
			DataAccess dataAccess = getDataAccessById(id);
			if (dataAccess != null) {
				dataAccessRepository.delete(dataAccess);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public DataAccess getDataAccessById(String id) {
		DataAccess dataAccess = null;
		try {
			dataAccess = dataAccessRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return dataAccess;
	}

	@Override
	public Iterable<DataAccess> findAllDataAccess() {
		Iterable<DataAccess> list = new ArrayList<DataAccess>();
		try {
			list = dataAccessRepository.findByParentIdIsNull();
			list = getAllSubDataAccess(list);
		} catch (NoResultException e) {
			log.warn("没有找到相应的结果！");
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	/**
	 * 查找所有下级数据权限列表
	 * 
	 * @param list
	 *            上级数据权限列表
	 * @return 返回所有数据权限列表
	 */
	private Iterable<DataAccess> getAllSubDataAccess(Iterable<DataAccess> list) {
		for (DataAccess da : list) {
			Iterable<DataAccess> subList = dataAccessRepository.findByParentId(da.getId());
			if (subList.iterator().hasNext()) {
				subList = getAllSubDataAccess(subList);
				da.setSubDataAccess(subList);
			}
		}
		return list;
	}

	@Override
	public Iterable<DataAccess> findDataAccessListByRoleId(String roleId) {
		Iterable<RoleAndDataAccess> rada_list = roleAndDataAccessRepository.findByRoleId(roleId);
		if (rada_list.iterator().hasNext()) {
			Collection<String> ids = new ArrayList<String>();
			for(RoleAndDataAccess rada : rada_list) {
				ids.add(rada.getId());
			}
			Iterable<DataAccess> subList = dataAccessRepository.findByParentIdNotNullAndIdIn(ids);
			Iterator<DataAccess> iter = subList.iterator();
			if (iter.hasNext()) {
				DataAccess da = iter.next();
				subList = getSubDataAccesses(subList, roleId);
				da.setSubDataAccess(subList);
			}
			return subList;
		}else {
			return null;
		}
	}

	/**
	 * 根据角色编号获取菜单下级数据权限的方法
	 * 
	 * @param list
	 *            上级数据权限列表
	 * @param roleId
	 *            角色的编号
	 * @return 返回数据权限列表
	 */
	private Iterable<DataAccess> getSubDataAccesses(Iterable<DataAccess> list, String roleId) {
		for (DataAccess da : list) {
			Iterable<RoleAndDataAccess> rada_list = roleAndDataAccessRepository.findByRoleId(roleId);
			if (rada_list.iterator().hasNext()) {
				Collection<String> ids = new ArrayList<String>();
				for(RoleAndDataAccess rada : rada_list) {
					ids.add(rada.getId());
				}
				Iterable<DataAccess> subList = dataAccessRepository.findByParentIdAndIdIn(da.getId(), ids);
				if (subList.iterator().hasNext()) {
					subList = getSubDataAccesses(subList, roleId);
					da.setSubDataAccess(subList);
				}
			}
		}
		return list;
	}

	@Override
	@Transactional
	public void createSiteUserPermissionByUser(String appId, String userDomain, String userId, List<String> roleIds) {
		// 检查用户是否存在
		SiteUser user = userService.getSiteUserByAccountId(userId, appId);
		if (user == null) {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}
		if (!appId.equals(user.getAppId())
				|| (!StringUtils.isEmpty(userDomain)) && userDomain.equals(user.getUserDomain())) {
			throw new AWSClientException(ClientError.DataConsistent, ClientReason.DataConsistent);
		}
		deleteSiteUserPermissionByUser(userId);
		if (roleIds.size() > 0) {
			for (String roleId : roleIds) {
				SiteRole siteRole = getSiteRoleById(roleId);
				if (siteRole != null) {
					SiteUserRole userrole = new SiteUserRole();
					userrole.setRoleEnum(siteRole.getRoleEnum());
					userrole.setAppId(appId);
					userrole.setUserId(userId);
					userrole.setRoleId(roleId);
					userRoleRepository.save(userrole);
				}
			}
		} else {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}
	}

	@Override
	public void createSiteUserPermissionByRole(String appId, String userDomain, String roleEnum, List<String> users) {
		// 检查应用的角色是否存在
		SiteRole role = this.getSiteRoleByRoleEnum(appId, roleEnum);
		if (role == null) {
			throw new AWSClientException(ClientError.NoFoundData, ClientReason.NoFoundData);
		}

		for (String userId : users) {
			SiteUser user = userService.getSiteUserByAccountId(userId, appId);
			if (user != null) {
				if ((!StringUtils.isEmpty(userDomain) && !userDomain.equals(user.getUserDomain()))
						|| (!user.getAppId().equals(appId))) {// 传入信息与系统保存信息不一致
					throw new AWSClientException(ClientError.DataConsistent, ClientReason.DataConsistent);
				}
				SiteUserRole userrole = new SiteUserRole();
				userrole.setRoleEnum(roleEnum);
				userrole.setAppId(appId);
				userrole.setUserId(user.getId());
				userRoleRepository.save(userrole);
			}

		}

	}

	@Override
	@Transactional
	public void deleteSiteUserPermissionById(String id) {
		try {
			SiteUserRole sup = getSiteUserPermissionById(id);
			if (sup != null) {
				userRoleRepository.delete(sup);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void deleteSiteUserPermissionByUser(String userId) {
		try {
			userRoleRepository.deleteByUserId(userId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteSiteUserPermissionByRoleId(String roleId) {
		try {
			userRoleRepository.deleteByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public SiteUserRole getSiteUserPermissionById(String id) {
		SiteUserRole sup = null;
		try {
			sup = userRoleRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return sup;
	}

	@Override
	public List<SiteUserRole> findAllSiteUserPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<SiteUserRole> findSiteUserRoleByUserId(String userId) {
		Iterable<SiteUserRole> list = new ArrayList<SiteUserRole>();
		try {
			list = userRoleRepository.findByUserId(userId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<SiteUserRole> findSiteUserRoleByRoleId(String roleId) {
		Iterable<SiteUserRole> list = new ArrayList<SiteUserRole>();
		try {
			list = userRoleRepository.findByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	@Transactional
	public void createSiteMenuAcl(List<String> roleIds, List<String> menuIds) {
		try {
			for (String roleId : roleIds) {
				SiteRole siteRole = getSiteRoleById(roleId);
				deleteSiteMenuAclByRoleId(roleId);
				for (String menuId : menuIds) {
					SiteMenuAcl sma = new SiteMenuAcl();
					sma.setMenuId(menuId);
					sma.setRoleId(roleId);
					sma.setAppId(siteRole.getAppId());
					menuAclRepository.save(sma);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteSiteMenuAclById(String id) {
		try {
			SiteMenuAcl siteMenuAcl = getSiteMenuAclById(id);
			if (siteMenuAcl != null) {
				menuAclRepository.delete(siteMenuAcl);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public void deleteSiteMenuAclByMenuId(String menuId) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void deleteSiteMenuAclByRoleId(String roleId) {
		try {
			menuAclRepository.deleteByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteSiteMenuByMenuId(String menuId) {
		try {
			SiteMenu siteMenu = menuRepository.findById(menuId).get();
			if (null != siteMenu) {
				if (siteMenu.getBeClass()) {
					Iterable<SiteMenu> menus = menuRepository.findByParentId(menuId);
					if (null != menus) {
						for (SiteMenu menu : menus) {
							String id = menu.getId();
							deleteSiteMenuByMenuId(id);
							menuRepository.deleteById(menuId);
						}
					}
				}
				menuRepository.deleteById(menuId);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public SiteMenuAcl getSiteMenuAclById(String id) {
		SiteMenuAcl siteMenuAcl = null;
		try {
			siteMenuAcl = menuAclRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return siteMenuAcl;
	}

	@Override
	public List<SiteMenuAcl> findAllSiteMenuAcl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<SiteMenuAcl> findSiteMenuAclListByRoleId(String roleId) {
		Iterable<SiteMenuAcl> list = new ArrayList<SiteMenuAcl>();
		try {
			list = menuAclRepository.findByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	@Transactional
	public void createRoleAndDataAccess(List<String> roleIds, List<String> dataAccessIds) {
		try {
			for (String roleId : roleIds) {
				SiteRole siteRole = getSiteRoleById(roleId);
				deleteRoleAndDataAccessByRoleId(roleId);
				for (String dataAccessId : dataAccessIds) {
					RoleAndDataAccess rada = new RoleAndDataAccess();
					rada.setDataAccessId(dataAccessId);
					rada.setRoleId(roleId);
					rada.setSiteId(siteRole.getAppId());
					roleAndDataAccessRepository.save(rada);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void deleteRoleAndDataAccessById(String id) {
		try {
			RoleAndDataAccess rada = getRoleAndDataAccessById(id);
			if (rada != null) {
				roleAndDataAccessRepository.delete(rada);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public void deleteRoleAndDataAccessByDataAccessId(String menuId) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void deleteRoleAndDataAccessByRoleId(String roleId) {
		try {
			roleAndDataAccessRepository.deleteById(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public RoleAndDataAccess getRoleAndDataAccessById(String id) {
		RoleAndDataAccess rada = null;
		try {
			rada = roleAndDataAccessRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return rada;
	}

	@Override
	public List<RoleAndDataAccess> findAllRoleAndDataAccess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<RoleAndDataAccess> findRoleAndDataAccessListByRoleId(String roleId) {
		Iterable<RoleAndDataAccess> list = new ArrayList<RoleAndDataAccess>();
		try {
			list = roleAndDataAccessRepository.findByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	@Transactional
	public void createSiteUserRole(String appId, String userId, String RoleEnum, String roleId) {
		SiteUserRole user_role = new SiteUserRole();
		user_role.setRoleEnum(RoleEnum);
		user_role.setUserId(userId);
		user_role.setAppId(appId);
		user_role.setRoleId(roleId);
		userRoleRepository.save(user_role);
	}

	@Override
	@Transactional
	public void updateSiteUserRole(SiteUserRole siteUserRole) {
		try {
			userRoleRepository.save(siteUserRole);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Iterable<SiteMenu> findAllSiteMenuBeClass() {
		Iterable<SiteMenu> list = new ArrayList<SiteMenu>();
		try {
			list = menuRepository.findByBeClass(true);
			list = getAllSubMenu(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

	@Override
	public Iterable<SiteMenu> findAllSiteMenuByParentId(String parentId) {
		Iterable<SiteMenu> list = new ArrayList<SiteMenu>();
		try {
			list = menuRepository.findByParentId(parentId);
			list = getAllSubMenu(list);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return list;
	}

}
