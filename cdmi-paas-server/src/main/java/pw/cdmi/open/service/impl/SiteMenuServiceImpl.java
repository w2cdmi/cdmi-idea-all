package pw.cdmi.open.service.impl;

import java.util.ArrayList;
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

import pw.cdmi.core.http.exception.AWSServiceException;
import pw.cdmi.core.http.exception.SystemReason;
import pw.cdmi.open.model.entities.DataAccess;
import pw.cdmi.open.service.SiteMenuService;
import pw.cdmi.paas.app.model.entities.RoleAndDataAccess;
import pw.cdmi.paas.app.model.entities.SiteMenu;
import pw.cdmi.paas.app.model.entities.SiteMenuAcl;
import pw.cdmi.paas.app.model.entities.SiteRole;
import pw.cdmi.paas.app.model.entities.SiteUserRole;
import pw.cdmi.paas.app.repositories.DataAccessRepository;
import pw.cdmi.paas.app.repositories.RoleAndDataAccessRepository;
import pw.cdmi.paas.app.repositories.SiteMenuAclRepository;
import pw.cdmi.paas.app.repositories.SiteMenuRepository;
import pw.cdmi.paas.app.repositories.SiteRoleRepository;

/************************************************************
 * 实现类，提供权限管理的操作方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-7
 ************************************************************/
public class SiteMenuServiceImpl implements SiteMenuService {

	private static final Logger log = LoggerFactory.getLogger(SiteMenuServiceImpl.class);

	@Autowired
	private SiteMenuRepository repository;

	@Autowired
	private SiteRoleRepository roleRepository;

	@Autowired
	private SiteMenuAclRepository siteMenuAclRepository;

	@Autowired
	private DataAccessRepository dataAccessRepository;

	@Autowired
	private RoleAndDataAccessRepository roleAndDataAccessRepository;

	@Override
	@Transactional
	public void createSiteRole(SiteRole siteRole) {
		try {
			roleRepository.save(siteRole);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void updateSiteRole(SiteRole siteRole) {
		try {
			roleRepository.save(siteRole);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void deleteSiteRoleById(String id) {
		try {
			SiteRole siteRole = getSiteRoleById(id);
			if (siteRole != null) {
				roleRepository.delete(siteRole);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	public SiteRole getSiteRoleById(String id) {
		SiteRole siteRole = null;
		try {
			siteRole = roleRepository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return siteRole;
	}

	@Override
	public Iterable<SiteRole> findAllSiteRole() {
		try {
			return roleRepository.findAll();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	public Page<SiteRole> findSiteRoleByPage(int pageNo, int pageSize) {
		try {
			Pageable pageable = new PageRequest(pageNo, pageSize);
			return roleRepository.findAll(pageable);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void createSiteMenu(SiteMenu siteMenu) {
		try {
			repository.save(siteMenu);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}

	}

	@Override
	@Transactional
	public void updateSiteMenu(SiteMenu siteMenu) {
		try {
			repository.save(siteMenu);
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
				repository.delete(siteMenu);
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
			siteMenu = repository.findById(id).get();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
		return siteMenu;
	}

	@Override
	public Iterable<SiteMenu> findAllSiteMenu() {
		try {
			return repository.findAll();
		} catch (NoResultException e) {
			log.warn("没有找到相应的结果！");
			throw new AWSServiceException(SystemReason.SQLError);
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
			list = repository.findByParentIdIsNotNull();
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
			Iterable<SiteMenu> subList = repository.findByParentId(siteMenu.getId());
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
	// list = jpaImpl.findByNamedQuery("SiteMenu.findListByRoleId", new Object[] {
	// roleId, null });
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
	// jpaImpl.findByNamedQuery("SiteMenu.findListByRoleId", new Object[] { roleId,
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
		try {
			Iterable<DataAccess> list = dataAccessRepository.findByParentIdIsNotNull();
			return getAllSubDataAccess(list);
		} catch (NoResultException e) {
			log.warn("没有找到相应的结果！");
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
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
		try {
			Iterable<RoleAndDataAccess> dataAcess_list = roleAndDataAccessRepository.findByRoleId(roleId);
			return getSubDataAccesses(dataAcess_list, roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
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
	private List<DataAccess> getSubDataAccesses(Iterable<RoleAndDataAccess> list, String roleId) {

		List<DataAccess> data_access_list = new ArrayList<DataAccess>();
		for (RoleAndDataAccess role_access : list) {
			String accessId = role_access.getDataAccessId();
			Iterable<RoleAndDataAccess> subList = roleAndDataAccessRepository.findByDataAccessIdAndRoleId(accessId, roleId);
			Iterator<RoleAndDataAccess> iter = subList.iterator();
			if (iter.hasNext()) {
				DataAccess data_access = dataAccessRepository.findById(iter.next().getDataAccessId()).get();
				if (data_access != null) {
					data_access_list.addAll(getSubDataAccesses(subList, roleId));
					data_access.setSubDataAccess(data_access_list);
				}
			}
		}
		return data_access_list;
	}

	@Override
	public Iterable<SiteUserRole> findAllSiteUserPermission() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<SiteUserRole> findSiteUserPermissionListByUserId(String employeeId) {
		List<SiteUserRole> list = new ArrayList<SiteUserRole>();
		// try {
		// list = daoImpl.findByNamedQuery("SiteUserPermission.findListByUser", new
		// Object[] { employeeId });
		// } catch (Exception e) {
		// log.error(e.getMessage());
		// e.printStackTrace();
		// throw new AWSServiceException(SystemError.SQLError);
		// }
		return list;
	}

	@Override
	public Iterable<SiteUserRole> findSiteUserPermissionListByRoleId(String roleId) {
		List<SiteUserRole> list = new ArrayList<SiteUserRole>();

		// try {
		// list = daoImpl.findByNamedQuery("SiteUserPermission.findListByRole", new
		// Object[] { roleId });
		// } catch (Exception e) {
		// log.error(e.getMessage());
		// e.printStackTrace();
		// throw new AWSServiceException(SystemError.SQLError);
		// }
		return list;
	}

	@Override
	@Transactional
	public void createSiteMenuAcl(Iterable<String> roleIds, Iterable<String> menuIds) {
		try {
			for (String roleId : roleIds) {
				SiteRole siteRole = getSiteRoleById(roleId);
				deleteSiteMenuAclByRoleId(roleId);
				for (String menuId : menuIds) {
					SiteMenuAcl sma = new SiteMenuAcl();
					sma.setMenuId(menuId);
					sma.setRoleId(roleId);
					sma.setAppId(siteRole.getAppId());
					siteMenuAclRepository.save(sma);
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
				siteMenuAclRepository.delete(siteMenuAcl);
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
			siteMenuAclRepository.deleteByRoleId(roleId);
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
			siteMenuAcl = siteMenuAclRepository.findById(id).get();
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
		try {
			return siteMenuAclRepository.findByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

	@Override
	@Transactional
	public void createRoleAndDataAccess(Iterable<String> roleIds, Iterable<String> dataAccessIds) {
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
	public Iterable<RoleAndDataAccess> findAllRoleAndDataAccess() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<RoleAndDataAccess> findRoleAndDataAccessListByRoleId(String roleId) {
		try {
			return roleAndDataAccessRepository.findByRoleId(roleId);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new AWSServiceException(SystemReason.SQLError);
		}
	}

}
