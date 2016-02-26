
import java.util.Random;

import foo.model.User;
import foo.service.UserService;

public class Main {
	private static final int DIFFERENT_ID_COUNT = 10;
	private static final int REQUESTS_COUNT = 100000;

	public static void main(String[] args) {
		UserService userService = new UserService();

		Integer[] randomIdArray = getRandomIdArray(REQUESTS_COUNT, DIFFERENT_ID_COUNT);
		long startSerializedTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			long interationStartTime = System.currentTimeMillis();
			User user = userService.getUserFromCopyBasedCache(id);
			long timeTaken = System.currentTimeMillis() - interationStartTime;
//			System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		long totalSerializedTimeTaken = System.currentTimeMillis() - startSerializedTime;
		System.out.println("totalSerializedTimeTaken=" + totalSerializedTimeTaken);
		
		long startCopyTime = System.currentTimeMillis();
		for (Integer id : randomIdArray) {
			long interationStartTime = System.currentTimeMillis();
			User user = userService.getUserFromCopyBasedCache(id);
			long timeTaken = System.currentTimeMillis() - interationStartTime;
//			System.out.println("id=" + user.getId() + " time=" + timeTaken);
		}
		double totalCopyTimeTaken = System.currentTimeMillis() - startCopyTime;

		System.out.println("totalCopyTimeTaken=" + totalCopyTimeTaken);

		System.out.println("totalSerializedTimeTaken/totalCopyTimeTaken = " + totalSerializedTimeTaken / totalCopyTimeTaken);

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
