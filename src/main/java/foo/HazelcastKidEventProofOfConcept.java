package foo;

import foo.model.KidEvent;
import foo.service.hazelcast.HazelcastCacheAsideKidRadarService;;

public class HazelcastKidEventProofOfConcept {
	private static final int REQUESTS_COUNT = 5000000;

	public static void main(String[] args) {
		try (HazelcastCacheAsideKidRadarService hazelcastCacheAsideKidRadarService = new HazelcastCacheAsideKidRadarService()) {
			for (int i = 0; i < REQUESTS_COUNT; i++) {
				hazelcastCacheAsideKidRadarService.isHandled(new KidEvent(i, 0, Integer.MAX_VALUE));
			}
		}
	}

}
