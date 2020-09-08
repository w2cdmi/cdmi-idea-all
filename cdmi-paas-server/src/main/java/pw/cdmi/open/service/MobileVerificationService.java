package pw.cdmi.open.service;

/****************************************************
 * 接口类，提供手机操作检验码服务。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 28, 2014
 ***************************************************/
public interface MobileVerificationService {
	
	/**
	 * 生产一个手机操作校验码
	 * @param mobile 手机号码
	 * @param template 短信信息模版
	 * @return 生产的手机校验码
	 */
	public String makeMobileCode(String mobile, String template);
	
	/**
	 * 校验手机检验号码
	 * @param mobile 手机号码
	 * @param code 手机校验码
	 * @return
	 */
	public boolean validity(String mobile, String code);
}
