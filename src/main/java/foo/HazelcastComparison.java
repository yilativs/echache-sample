package foo;

import java.util.Random;

import foo.model.User;
import foo.service.ehcache.EhCacheAsideUserService;
import foo.service.hazelcast.HazelcastCacheAsideUserService;;

public class HazelcastComparison {
	private static final int DIFFERENT_ID_COUNT = 20000;
	private static final int REQUESTS_COUNT = 100000;

	public static void main(String[] args) {
		compareInMemoryStrategies();
	}

	private static void compareInMemoryStrategies() {

		Integer[] randomIdArray = getRandomIdArray(REQUESTS_COUNT, DIFFERENT_ID_COUNT);
		EhCacheAsideUserService ehcacheUserService = new EhCacheAsideUserService();
		ehcacheUserService.clearCache();
		long startReferenceTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = ehcacheUserService.getUserFromReferenceCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalReferenceTimeTaken = System.currentTimeMillis() - startReferenceTime;
		System.out.println("totalReferenceTimeTaken=" + totalReferenceTimeTaken);
		ehcacheUserService.clearCache();

		long startCopyTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = ehcacheUserService.getUserFromCopyBasedCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		double totalCopyTimeTaken = System.currentTimeMillis() - startCopyTime;
		System.out.println("totalCopyTimeTaken=" + totalCopyTimeTaken);
		ehcacheUserService.clearCache();

		long startSerializedTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = ehcacheUserService.getUserFromSerializedCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalSerializedTimeTaken = System.currentTimeMillis() - startSerializedTime;
		System.out.println("totalSerializedTimeTaken=" + totalSerializedTimeTaken);
		ehcacheUserService.clearCache();
		ehcacheUserService.close();

		HazelcastCacheAsideUserService hazelcastUserService = new HazelcastCacheAsideUserService();

		long startObjectMemoryMapHazelcastTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = hazelcastUserService.getUserFromBinaryMemory(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalObjectHazelcastTimeTaken = System.currentTimeMillis() - startObjectMemoryMapHazelcastTime;
		System.out.println("totalObjectMemoryHazelcastTimeTaken=" + totalObjectHazelcastTimeTaken);
		hazelcastUserService.clearCache();

		long startIdentifiedDataSerializableTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = hazelcastUserService.getIdentifiedDataSerializableUserFromBinaryMemory(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalIdentifiedDataSerializableTimeTaken = System.currentTimeMillis() - startIdentifiedDataSerializableTime;
		System.out.println("totalIdentifiedDataSerializableTimeTaken=" + totalIdentifiedDataSerializableTimeTaken);
		hazelcastUserService.clearCache();

		long startBinaryMemoryMapHazelcastTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = hazelcastUserService.getUserFromBinaryMemory(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalSerializedHazelcastTimeTaken = System.currentTimeMillis() - startBinaryMemoryMapHazelcastTime;
		System.out.println("totalBinaryMemoryHazelcastTimeTaken=" + totalSerializedHazelcastTimeTaken);
		hazelcastUserService.clearCache();

		long startBinaryExternalizebleMemoryMapHazelcastTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = hazelcastUserService.getUserFromBinaryMemory(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalBinaryExternalizebleHazelcastTimeTaken = System.currentTimeMillis() - startBinaryExternalizebleMemoryMapHazelcastTime;
		System.out.println("totalBinaryExternalizebleHazelcastTimeTaken=" + totalBinaryExternalizebleHazelcastTimeTaken);
		hazelcastUserService.clearCache();

		hazelcastUserService.close();

	}

	public static Integer[] getRandomIdArray(int size, int bound) {
		Integer[] result = new Integer[size];
		Random random = new Random();
		for (int i = 0; i < result.length; i++) {
			result[i] = random.nextInt(bound);
		}
		return result;
	}

}
