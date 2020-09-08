package pw.cdmi.open.service;

import pw.cdmi.open.model.entities.AuditPrivilege;

/**
 * **********************************************************
 * 接口类，提供审计管理模块的操作方法
 * 
 * @author liujh
 * @version iSoc Service Platform, 2015-5-4
 ***********************************************************
 */
public interface AuditManageService {
	
	//系统用户权限授权审计信息
	/**
	 * 新增用户权限授权审计信息
	 * 
	 * @param auditPrivilege 用户权限变化对象 
	 */
	public void createAuditPrivailege(AuditPrivilege auditPrivilege);
	/**
	 * 删除用户权限授权审计信息
	 * 
	 * @param id 用户权限变化的id
	 */
	public void deleteAuditPrivilegeById(String id);
	/**
	 * 根据用户权限变化的id查询用户权限授权审计信息
	 * 
	 * @param id 用户权限变化的id
	 * @return 用户权限授权对象
	 */
	public AuditPrivilege getAuditPrivilegeById(String id);
	/**
	 * 查询所有用户权限授权审计信息
	 * 
	 * @return 用户权限授权审计信息列表
	 */
	public Iterable<AuditPrivilege> findAllAuditPrivilege();
	/**
	 * 用分页的方式查询所有
	 * 
	 * @param pageNo 待查询的索引号
	 * @param pageSize 每页最大的查询数量
	 * @param queryObject 条件参数封装对象
	 * @return 用户权限授权审计信息列表
	 */
	public Iterable<AuditPrivilege> findAuditPrivilegeByPage(int pageNo, int pageSize);
	/**
	 * 
	 * 按条件查询用户授权审计信息
	 * 
	 * @param auditPrivilege 用户权限授权对象
	 * @return 用户权限审计信息
	 */
	Iterable<AuditPrivilege> searchAuditPrivilegeList(AuditPrivilege auditPrivilege);
	/**
	 * 
	 * 按条件分页查询用户授权信息
	 * 
	 * @param 用户权限授权对象
	 * @param pageNo 待查询的索引号
	 * @param pageSize 每页最大的查询数量
	 * @return 用户权限审计信息
	 */
	Iterable<AuditPrivilege> searchAuditPrivilegeListPage(AuditPrivilege auditPrivilege, int pageNo, int pageSize);
	
	
}
