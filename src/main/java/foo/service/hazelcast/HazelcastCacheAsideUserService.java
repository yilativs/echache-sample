package foo.service.hazelcast;

import java.io.Closeable;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import foo.model.User;
import foo.service.UserService;

public class HazelcastCacheAsideUserService extends UserService implements Closeable {
	public static final String USERS_MAP_NAME = "users";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)

	private final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

	public HazelcastCacheAsideUserService() {
	}

	public User getUserFromMap(int id) {
		User user = (User) hazelcastInstance.getMap(USERS_MAP_NAME).get(id);;
		if (user == null) {
			user = getUser(id);
			hazelcastInstance.getMap(USERS_MAP_NAME).put(id, user);
		}
		return user;
	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		hazelcastInstance.shutdown();
	}

	public void clearCache() {
		hazelcastInstance.getMap(USERS_MAP_NAME).clear();
	}

}