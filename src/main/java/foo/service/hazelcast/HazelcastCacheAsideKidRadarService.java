package foo.service.hazelcast;

import java.io.Closeable;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import foo.model.KidEvent;
import foo.service.UserService;

public class HazelcastCacheAsideKidRadarService extends UserService implements Closeable {
	public static final String KID_EVENT_MAP_NAME = "kid_event_map";
	// it returns a signleton instance or returns already created singleton instance (for multiple use newInstance)

	private final HazelcastInstance hazelcastInstance;

	public HazelcastCacheAsideKidRadarService() {
		Config config = new ClasspathXmlConfig("hazelcast4altarix.xml");
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
	}

	public boolean isHandled(KidEvent kidEvent) {
		return null != hazelcastInstance.getMap(KID_EVENT_MAP_NAME).putIfAbsent(kidEvent, Long.valueOf(System.currentTimeMillis()));
	}

	// don't forget to close service and cache properly
	@Override
	public void close() {
		hazelcastInstance.shutdown();
	}

	public void clearCache() {
		hazelcastInstance.getMap(KID_EVENT_MAP_NAME).clear();
	}

}
