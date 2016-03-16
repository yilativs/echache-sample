package foo;

import java.util.Random;

import foo.model.User;
import foo.service.UserCacheAsideService;

public class CacheCopyStrategyComparison {
	private static final int DIFFERENT_ID_COUNT = 10;
	private static final int REQUESTS_COUNT = 10000;

	public static void main(String[] args) {
		compareInMemoryStrategies();
	}

	private static void compareInMemoryStrategies() {
		UserCacheAsideService userService = new UserCacheAsideService();
//		userService.clearCache();
		Integer[] randomIdArray = getRandomIdArray(REQUESTS_COUNT, DIFFERENT_ID_COUNT);

		long startReferenceTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = userService.getUserFromReferenceCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalReferenceTimeTaken = System.currentTimeMillis() - startReferenceTime;
		System.out.println("totalReferenceTimeTaken=" + totalReferenceTimeTaken);

		long startCopyTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = userService.getUserFromCopyBasedCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		double totalCopyTimeTaken = System.currentTimeMillis() - startCopyTime;

		System.out.println("totalCopyTimeTaken=" + totalCopyTimeTaken);

		long startSerializedTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			// long interationStartTime = System.currentTimeMillis();
			User user = userService.getUserFromSerializedCache(id);
			// long timeTaken = System.currentTimeMillis() - interationStartTime;
			// System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalSerializedTimeTaken = System.currentTimeMillis() - startSerializedTime;
		System.out.println("totalSerializedTimeTaken=" + totalSerializedTimeTaken);

		userService.close();
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
