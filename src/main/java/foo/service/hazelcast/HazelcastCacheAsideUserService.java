package foo.service.hazelcast;

import java.io.Closeable;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import foo.cache.hazelcast.UserExternalizable;
import foo.cache.hazelcast.UserIdentifiedDataSerializable;
import foo.model.User;
import foo.service.UserService;

public class HazelcastCacheAsideUserService extends UserService implements Closeable {
	public static final String USERS_BINARY_MEMORY_MAP_NAME = "user_binary_memory_map";
	public static final String USERS_OBJECT_MEMORY_MAP_NAME = "user_object_memory_map";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)

	private final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

	public HazelcastCacheAsideUserService() {
	}

	public User getUserFromBinaryMemory(int id) {
		return getUser(id, USERS_BINARY_MEMORY_MAP_NAME);
	}

	public User getExternlazizedUserFromBinaryMemory(int id) {
		User user = (User) hazelcastInstance.getMap(USERS_BINARY_MEMORY_MAP_NAME).get(id);;
		if (user == null) {
			user = getUser(id);
			user = new UserExternalizable(user.getUserId(), user.getLogin());
			hazelcastInstance.getMap(USERS_BINARY_MEMORY_MAP_NAME).put(id, user);
			hazelcastInstance.getSet("").add("");
		}
		return user;
	}
	
	public User getIdentifiedDataSerializableUserFromBinaryMemory(int id) {
		User user = (User) hazelcastInstance.getMap(USERS_BINARY_MEMORY_MAP_NAME).get(id);
		if (user == null) {
			user = getUser(id);
			user = new UserIdentifiedDataSerializable(user.getUserId(), user.getLogin());
			hazelcastInstance.getMap(USERS_BINARY_MEMORY_MAP_NAME).put(id, user);
		}
		return user;
	}
	

	public User getUserFromObjectMemoryMap(int id) {
		return getUser(id, USERS_OBJECT_MEMORY_MAP_NAME);
	}

	private User getUser(int id, String mapName) {
		User user = (User) hazelcastInstance.getMap(mapName).get(id);;
		if (user == null) {
			user = getUser(id);
			hazelcastInstance.getMap(mapName).put(id, user);
		}
		return user;
	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		hazelcastInstance.shutdown();
	}

	public void clearCache() {
		hazelcastInstance.getMap(USERS_BINARY_MEMORY_MAP_NAME).clear();
		hazelcastInstance.getMap(USERS_OBJECT_MEMORY_MAP_NAME).clear();
	}

}