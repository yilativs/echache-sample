package foo.service;

import java.io.Closeable;

import foo.model.User;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserCacheAsideService extends UserService implements Closeable {
	public static final String USER_BY_ID_COPY_STRATEGY_BASED_CACHE = "userByIdCopyStrategyBasedCache";
	public static final String USER_BY_ID_SERIALIZATION_BASED_CACHE = "userByIdSerializationBasedCache";
	public static final String USER_BY_ID_REFERENCE_BASED_CACHE = "userByIdRefenceBasedCache";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)
	private CacheManager cacheManager = CacheManager.create();

	public User getUserFromReferenceCache(int id) {
		return getUser(id, USER_BY_ID_REFERENCE_BASED_CACHE);
	}

	public User getUserFromSerializedCache(int id) {
		return getUser(id, USER_BY_ID_SERIALIZATION_BASED_CACHE);
	}

	public User getUserFromCopyBasedCache(int id) {
		return getUser(id, USER_BY_ID_COPY_STRATEGY_BASED_CACHE);
	}

	public User getUser(int id, String cacheName) {
		Element element = cacheManager.getCache(cacheName).get(id);
		if (element == null) {
			element = new Element(id, getUser(id));
			cacheManager.getCache(cacheName).put(element);
		}
		return (User) element.getObjectValue();
	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		cacheManager.shutdown();
	}

	public void clearCache() {
		cacheManager.clearAll();
	}

}