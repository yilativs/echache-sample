package foo.service.hazelcast;

import java.io.Closeable;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import foo.model.KidEvent;
import foo.service.UserService;

public class HazelcastCacheAsideKidRadarService extends UserService implements Closeable {
	public static final String KID_EVENT_SET_NAME = "kid_event_set";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)

	private final HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

	public HazelcastCacheAsideKidRadarService() {
	}

	public boolean isHandled(KidEvent kidEvent) {
		boolean result = hazelcastInstance.getSet(KID_EVENT_SET_NAME).contains(kidEvent);
		if (!result) {
			hazelcastInstance.getSet(KID_EVENT_SET_NAME).add(kidEvent);
		}
		return result;
	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		hazelcastInstance.shutdown();
	}

	public void clearCache() {
		hazelcastInstance.getMap(KID_EVENT_SET_NAME).clear();
	}

}


