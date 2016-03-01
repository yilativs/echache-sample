package foo.service;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.googlecode.concurentlocks.ReentrantReadWriteUpdateLock;

import foo.model.User;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserBlockingCacheAsideService extends UserService implements Closeable {
	private static final String USER_BY_ID_COPY_STRATEGY_BASED_CACHE = "userByIdCopyStrategyBasedCache";
	// it returns a singleton instance or returns already created singleton instance (for multiple use newInstance)
	CacheManager cacheManager = CacheManager.create();

	Map<Integer, ReentrantLock> idMonitorMap = new HashMap<>();
	private ReentrantReadWriteUpdateLock idMonitorMapReadWriteLock = new ReentrantReadWriteUpdateLock();

	public User getUserFromCacheSynhcronizedWithSynchronizeBlock(Integer id) {
		Element element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
		if (element == null) {
			ReentrantLock idLock = null;
			try {
				boolean isFirstRequest = false;
				synchronized (idMonitorMap) {
					// we need second check in case the cache was updated before we took the lock
					element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
					if (element != null) {
						return (User) element.getObjectValue();
					}
					idLock = idMonitorMap.get(id);
					if (idLock == null) {
						isFirstRequest = true;
						idLock = new ReentrantLock();
						idLock.lock();
						idMonitorMap.put(id, idLock);
					}
				}
				if (isFirstRequest) {
					element = new Element(id, getUser(id));
					cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).put(element);
					synchronized (idMonitorMap) {
						idMonitorMap.remove(id);
					}
				} else {
					idLock.lock();
					element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
				}
			} finally {
				if (idLock != null) {
					idLock.unlock();
				}
			}
		}
		return (User) element.getObjectValue();
	}

	public User getUserFromCacheSynchronizedWithReentrantReadWriteUpdateLock(Integer id) {
		Element element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
		if (element == null) {
			ReentrantLock idLock = null;
			try {
				boolean isFirstRequest = false;
				try {
					idMonitorMapReadWriteLock.updateLock().lock();
					element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
					if (element != null) {
						return (User) element.getObjectValue();
					}
					idLock = idMonitorMap.get(id);
					if (idLock == null) {
						isFirstRequest = true;
						idLock = new ReentrantLock();
						idLock.lock();
						try {
							idMonitorMapReadWriteLock.writeLock().lock();
							idMonitorMap.put(id, idLock);
						} finally {
							idMonitorMapReadWriteLock.writeLock().unlock();
						}
					}
				} finally {
					idMonitorMapReadWriteLock.updateLock().unlock();
				}
				if (isFirstRequest) {
					element = new Element(id, getUser(id));
					cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).put(element);
					try {
						idMonitorMapReadWriteLock.writeLock().lock();
						idMonitorMap.remove(id);
					} finally {
						idMonitorMapReadWriteLock.writeLock().unlock();
					}
				} else {
					idLock.lock();
					element = cacheManager.getCache(USER_BY_ID_COPY_STRATEGY_BASED_CACHE).get(id);
				}
			} finally {
				if (idLock != null) {
					idLock.unlock();
				}
			}
		}
		return (User) element.getObjectValue();

	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		cacheManager.shutdown();

	}

}