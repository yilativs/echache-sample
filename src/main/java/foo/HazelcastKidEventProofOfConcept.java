package foo;

import foo.model.KidEvent;
import foo.service.hazelcast.HazelcastCacheAsideKidRadarService;;

public class HazelcastKidEventProofOfConcept {
	private static final int REQUESTS_COUNT = 500;

	public static void main(String[] args) throws InterruptedException {
		try (HazelcastCacheAsideKidRadarService hazelcastCacheAsideKidRadarService = new HazelcastCacheAsideKidRadarService()) {
			for (int i = 0; i < REQUESTS_COUNT; i++) {
				KidEvent kidEvent = new KidEvent(i, 0, Integer.MAX_VALUE);
				if (hazelcastCacheAsideKidRadarService.isHandled(kidEvent)) {
					System.out.println("The event was already handled " + kidEvent  );
				}
			}
		}

	}

}
