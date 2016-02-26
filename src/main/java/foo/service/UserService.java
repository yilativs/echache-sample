package foo.service;

import java.io.Closeable;
import java.util.Arrays;

import foo.model.User;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserService implements Closeable {
	private static final String USER_BY_ID_COPY_STRATEGY_BASED_CACHE = "userByIdCopyStrategyBasedCache";
	private static final String USER_BY_ID_SERIALIZATION_BASED_CACHE = "userByIdSerializationBasedCache";
	private static final String USER_BY_ID_REFERENCE_BASED_CACHE = "userByIdRefenceBasedCache";
	private static final String USER_LOGIN_CACHE_NAME = "loginById";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)
	private CacheManager cacheManager = CacheManager.create();

	public String getCachedLogin(Integer id) {
		Element element = cacheManager.getCache(USER_LOGIN_CACHE_NAME).get(id);
		if (element == null) {
			String login = getLogin(id);
			element = new Element(id, login);
			cacheManager.getCache(USER_LOGIN_CACHE_NAME).put(element);
		}
		return (String) element.getObjectValue();
	}

	private String getLogin(int id) {
//		try {
			// we will sleep id seconds :-)
//			Thread.sleep(3 * 1000);
//		} catch (InterruptedException e) {
//			Thread.interrupted();// who dared to interrupt me?
//		}
		// we want to store relativly large strings to check the speed of disk cache
		char[] chars = new char[1000];
		Arrays.fill(chars, 'a');
		return Integer.toString(id) + new String(chars);
	}
	
	public User getUserFromReferenceCache(int id) {
		Element element = cacheManager.getCache(USER_BY_ID_REFERENCE_BASED_CACHE).get(id);
		if (element == null) {
			element = new Element(id, new User(id, getLogin(id)));
			cacheManager.getCache(USER_BY_ID_REFERENCE_BASED_CACHE).put(element);
		}
		return (User) element.getObjectValue();
	}

	public User getUserFromSerializedCache(int id) {
		Element element = cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).get(id);
		if (element == null) {
			element = new Element(id, new User(id, getLogin(id)));
			cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).put(element);
		}
		return (User) element.getObjectValue();
	}
	
	public User getUserFromCopyBasedCache(int id) {
		Element element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
		if (element == null) {
			element = new Element(id, new User(id, getLogin(id)));
			cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).put(element);
		}
		return (User) element.getObjectValue();
	}
	

	// don't forget to close service and cache properly
	@Override
	public void close() {
		cacheManager.shutdown();

	}

}