package foo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.junit.Assert;
import org.junit.Test;

public class TestBlockingUserCacheService {

	private static final int DIFFERENT_IDS_COUNT = 10;
	private static final int NUMBER_OF_SIMULTANEOS_THREADS = 8000;

	@Test
	public void getUserFromCacheSynhcronizedWithSynchronizeBlock() throws InterruptedException {
		final BlockingUserCacheService blockingUserCacheService = new BlockingUserCacheService();
		blockingUserCacheService.cacheManager.clearAll();
		long startTime = System.currentTimeMillis();

		final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_SIMULTANEOS_THREADS);

		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					barrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					System.out.println("failed");
				}
				// all threads try to get same ID ranging from 0 to 9, only one should take it not from cache
				blockingUserCacheService.getUserFromCacheSynhcronizedWithSynchronizeBlock((int) (System.currentTimeMillis() % DIFFERENT_IDS_COUNT));
			}
		};
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_SIMULTANEOS_THREADS; i++) {
			Thread thread = new Thread(r);
			threads.add(thread);
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();
		}
		long timeSpent = System.currentTimeMillis() - startTime;
		// if at least one element taken taken not form cache twice, test fails
		Assert.assertTrue(timeSpent < 2 * BlockingUserCacheService.MILLIS_TO_GET_LOGIN - 1);
		System.out.println("getUserFromCacheSynhcronizedWithSynchronizeBlock " + timeSpent);
		Assert.assertTrue(blockingUserCacheService.idMonitorMap.isEmpty());
		blockingUserCacheService.close();
	}

	@Test
	public void getUserFromCacheSynchronizedWithReentrantReadWriteUpdateLock() throws InterruptedException {
		final BlockingUserCacheService blockingUserCacheService = new BlockingUserCacheService();
		blockingUserCacheService.cacheManager.clearAll();
		long startTime = System.currentTimeMillis();
		final CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_SIMULTANEOS_THREADS);

		Runnable r = new Runnable() {

			@Override
			public void run() {
				try {
					barrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					System.out.println("failed");
				}
				// all threads try to get same ID ranging from 0 to 9, only one should take it not from cache
				blockingUserCacheService.getUserFromCacheSynchronizedWithReentrantReadWriteUpdateLock((int) (System.currentTimeMillis() % DIFFERENT_IDS_COUNT));
			}
		};
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_SIMULTANEOS_THREADS; i++) {
			Thread thread = new Thread(r);
			threads.add(thread);
			thread.start();
		}

		for (Thread thread : threads) {
			thread.join();
		}
		blockingUserCacheService.close();
		long timeSpent = System.currentTimeMillis() - startTime;
		// if at least one element taken taken not form cache twice, test fails
		Assert.assertTrue(timeSpent < 2 * BlockingUserCacheService.MILLIS_TO_GET_LOGIN - 1);
		System.out.println("getUserFromCacheSynchronizedWithReentrantReadWriteUpdateLock " + timeSpent);
		Assert.assertTrue(blockingUserCacheService.idMonitorMap.isEmpty());
	}

}
