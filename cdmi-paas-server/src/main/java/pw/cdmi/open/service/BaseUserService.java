package pw.cdmi.open.service;

import java.util.List;

import pw.cdmi.open.model.ConfirmationEvent;

/****************************************************
 * 接口类，提供对O2O平台用户账号的操作方法。
 * 
 * @author 伍伟
 * @version O2O Service Platform, July 28, 2014
 ***************************************************/
public interface BaseUserService<T> {

	/**
	 * 获得手机验证码
	 * @param mobile 以手机号码
	 * @param event 操作事件
	 * @return 手机验证码
	 */
	public String getVerifycodeMobile(String mobile, ConfirmationEvent event);
	
	/**
	 * 向数据库中插入一条新的用户账号信息
	 * @param email 以邮件为登陆账号
	 * @param user 新用户账号信息
	 * @return 新创建的用户账号信息
	 */
	public T createUserByEmail(String email, T user);
	
	/**
	 * 向数据库中插入一条新的用户账号信息
	 * @param mobile 以手机为登陆账号
	 * @param user 新用户账号信息
	 * @param verifycode 手机验证码
	 * @return 新创建的用户账号信息
	 */
	public T createUserByMobile(String mobile, T user, String verifycode);
	
	/**
	 * 根据用户的OpenID删除一条用户账号信息
	 * @param openId 
	 */
	public void deleteUser(String openId);
	
	/**
	 * 根据用户的id key获得用户账号信息，账号信息不包含密码(不推荐使用)
	 * @param uid 用户表的主键
	 * @return 用户账号信息
	 */
	public T getUser(long id);
	
	/**
	 * 根据用户的openId获得用户账号信息，账号信息不包含密码
	 * @param openId 用户账号的OpenId
	 * @return 用户账号信息
	 */
	public T getUser(String openId);
	
	
	/**
	 * 根据邮件地址判断用户是否存在
	 * @param email 邮件地址
	 * @return 不存在，返回false。
	 */
	public boolean isExistUserByMail(String email);
	
	/**
	 * 根据邮件地址判断用户是否存在
	 * @param mobile 用户的手机号码
	 * @return 不存在，返回false。
	 */
	public boolean isExistUserByMobile(String mobile);
	
	/**
	 * 通过邮件重置用户的账号密码
	 * @param openId 用户账号的OpenId
	 * @return 用户的邮件地址
	 */
	public String resetPasswordByEmail(String openId);
	
	/**
	 * 通过手机重置用户的账号密码
	 * @param openId 用户账号的OpenId
	 * @return 用户的手机号码
	 */
	public String resetPasswordByMobile(String openId);
	/**
	 * 重新设置用户的手机号码
	 * @param openId 用户账号的OpenId
	 * @param mobile 用户账号的新手机号码
	 */
	public void updateMobile(String openId, String mobile);
	
	/**
	 * 重新设置用户的邮件地址
	 * @param openId 用户账号的OpenId
	 * @param email 重置用户账号的新邮件地址
	 */
	public void updateEmail(String openId, String email);
	
	/**
	 * 分页方式获取账号记录
	 * @param pageNo 当前页码
	 * @param pageSize 每页数据的最大记录条数
	 * @return 用户账号信息列表
	 */
	public List<T> listUsers(int pageNo, int pageSize);
	
	/**
	 * 用户帐号登录校验
	 * @param loginname 用户账号登陆名
	 * @param password 用户账号密码
	 * @return
	 */
	public T validity(String loginname, String password);
}
