package pw.cdmi.paas.account.commons;

public interface AuthMobileRepository {
	/**
	 * 添加到缓存
	 * @param key
	 * @param value
	 */
	public void save(String key,String value);
	/**
	 * 删除key键
	 * @param key
	 */
	public void deleteObject(String key);
	/**
	 * 查询key键的值
	 * @param key
	 * @return
	 */
	public Object getValue(String key ); 
}
