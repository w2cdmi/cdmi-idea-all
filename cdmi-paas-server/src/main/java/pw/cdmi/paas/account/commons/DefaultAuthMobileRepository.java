package pw.cdmi.paas.account.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthMobileRepository  implements AuthMobileRepository{

	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public void save(String key, String value) {
		Cache cache = cacheManager.getCache("test");
		cache.put(key, value);		
	}

	@Override
	public void deleteObject(String key) {
		Cache cache = cacheManager.getCache("test");
		cache.evict(key);
		
	}

	@Override
	public Object getValue(String key) {
		Cache cache = cacheManager.getCache("test");
		return cache.get(key).get();
	}

	
}
