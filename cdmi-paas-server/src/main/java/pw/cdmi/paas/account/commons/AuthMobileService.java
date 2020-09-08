package pw.cdmi.paas.account.commons;

public interface AuthMobileService {
	/**
	 * 对指定电话号码发送验证短信
	 * @param mobile
	 */
	public void sendMessage(String mobile);
	/**
	 * 验证短信
	 * @param mobile
	 * @param AuthNumber
	 */
	public boolean AuthMobile(String mobile,String AuthNumber);
	
}
