package foo;

import foo.cache.hazelcast.KidEventIdentifiedDataSerializable;
import foo.model.KidEvent;
import foo.service.hazelcast.HazelcastCacheAsideKidRadarService;;

public class HazelcastKidEventProofOfConcept {
	private static final int REQUESTS_COUNT = 500000;
	private static final int MB = 1024 * 1024;

	public static void main(String[] args) throws InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		long startIdenifiedSerialization = System.currentTimeMillis();
		try (HazelcastCacheAsideKidRadarService hazelcastCacheAsideKidRadarService = new HazelcastCacheAsideKidRadarService()) {
			for (int i = 0; i < REQUESTS_COUNT; i++) {
				KidEvent kidEvent = new KidEventIdentifiedDataSerializable(i, 0, Integer.MAX_VALUE);
				if (hazelcastCacheAsideKidRadarService.isHandled(kidEvent)) {
					System.out.println("The event was already handled " + kidEvent);
				}
			}
			
		}
		long totalIdentifiedSerialization = System.currentTimeMillis() - startIdenifiedSerialization;
		System.out.println("totalIdentifiedSerialization:" + totalIdentifiedSerialization);

		long startDefaultSerialization = System.currentTimeMillis();
		try (HazelcastCacheAsideKidRadarService hazelcastCacheAsideKidRadarService = new HazelcastCacheAsideKidRadarService()) {
			for (int i = 0; i < REQUESTS_COUNT; i++) {
				KidEvent kidEvent = new KidEvent(i, 0, Integer.MAX_VALUE);
				if (hazelcastCacheAsideKidRadarService.isHandled(kidEvent)) {
					System.out.println("The event was already handled " + kidEvent);
				}
			}
			System.out.println("Used Memory: " + (runtime.totalMemory() - runtime.freeMemory()) / MB);
		}
		long totalDefaultSerialization = System.currentTimeMillis() - startDefaultSerialization;
		System.out.println("totalDefaultSerialization:" + totalDefaultSerialization);

	}

}
