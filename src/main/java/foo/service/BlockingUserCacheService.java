package foo.service;

import java.io.Closeable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.googlecode.concurentlocks.ReentrantReadWriteUpdateLock;

import foo.model.User;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class BlockingUserCacheService implements Closeable {
	public static final int MILLIS_TO_GET_LOGIN = 5 * 1000;
	private static final String USER_BY_ID_COPY_STRATEGY_BASED_CACHE = "userByIdCopyStrategyBasedCache";
	private static final String USER_BY_ID_SERIALIZATION_BASED_CACHE = "userByIdSerializationBasedCache";
	private static final String USER_LOGIN_CACHE_NAME = "loginById";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)
	CacheManager cacheManager = CacheManager.create();

	private String getLogin(int id) {
		try {
			// we will sleep id seconds :-)
			Thread.sleep(MILLIS_TO_GET_LOGIN);
		} catch (InterruptedException e) {
			Thread.interrupted();// who dared to interrupt me?
		}
		// we want to store relativly large strings to check the speed of disk cache
		char[] chars = new char[100000];
		Arrays.fill(chars, 'a');
		return Integer.toString(id) + new String(chars);
	}

	Map<Integer, ReentrantLock> idMonitorMap = new HashMap<>();
	private ReentrantReadWriteUpdateLock idMonitorMapReadWriteLock = new ReentrantReadWriteUpdateLock();

	public User getUserFromSerializedCache(int id) {
		Element element = cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).get(id);
		if (element == null) {
			ReentrantLock idLock = null;
			Integer idInteger = Integer.valueOf(id);
			try {
				boolean isFirstRequest = false;
				synchronized (idMonitorMap) {
					idLock = idMonitorMap.get(idInteger);
					if (idLock == null) {
						isFirstRequest = true;
						idLock = new ReentrantLock();
						idLock.lock();
						idMonitorMap.put(idInteger, idLock);
					}
				}
				if (isFirstRequest) {
					element = new Element(id, new User(id, getLogin(id)));
					cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).put(element);
					synchronized (idMonitorMap) {
						idMonitorMap.remove(idInteger);
					}
				} else {
					idLock.lock();
					element = cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).get(id);
				}
			} finally {
				if (idLock != null) {
					idLock.unlock();
				}
			}
		}
		return (User) element.getObjectValue();
	}

	public User getUserFromSerializedCacheReadWriteLocked(int id) {
		Element element = cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).get(id);
		if (element == null) {
			ReentrantLock idLock = null;
			try {
				boolean isFirstRequest = false;
				Integer idInteger = Integer.valueOf(id);
				try {
					idMonitorMapReadWriteLock.updateLock().lock();
					idLock = idMonitorMap.get(idInteger);
					if (idLock == null) {
						isFirstRequest = true;
						idLock = new ReentrantLock();
						idLock.lock();
						try {
							idMonitorMapReadWriteLock.writeLock().lock();
							idMonitorMap.put(idInteger, idLock);
						} finally {
							idMonitorMapReadWriteLock.writeLock().unlock();
						}
					}
				} finally {
					idMonitorMapReadWriteLock.updateLock().unlock();
				}
				if (isFirstRequest) {
					System.out.println("putting new " + id);
					element = new Element(id, new User(id, getLogin(id)));
					cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).put(element);
					try {
						idMonitorMapReadWriteLock.writeLock().lock();
						idMonitorMap.remove(idInteger);
					} finally {
						idMonitorMapReadWriteLock.writeLock().unlock();
					}
				} else {
					idLock.lock();
					element = cacheManager.getCache(USER_BY_ID_SERIALIZATION_BASED_CACHE).get(id);
				}
			} finally {
				if (idLock != null) {
					idLock.unlock();
				}
			}
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