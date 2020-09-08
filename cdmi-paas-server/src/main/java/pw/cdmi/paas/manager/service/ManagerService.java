package pw.cdmi.paas.manager.service;

import pw.cdmi.paas.manager.model.entities.Manager;
import pw.cdmi.wishlist.model.entities.WxUser;

public interface ManagerService {
	
	/**
	 * 新增一个平台管理员
	 * @param manager
	 * @return
	 */
	public String createManager(Manager manager);
	
	/**
	 * 平台管理员自己将管理权限移交给其他微信用户
	 * @param oldmanager_id
	 * @param wxUser
	 * @return
	 */
	public void switchManager(String oldmanager_id, WxUser wxUser);
	
	/**
	 * 根据用户的平台账号获取平台管理员信息
	 * @param wxUnionId
	 * @return
	 */
	public Manager getManagerByUserId(String userId);
	
	/**
	 * 检查是否存在平台管理员账号
	 * @return
	 */
	public long countManager();
	
}
