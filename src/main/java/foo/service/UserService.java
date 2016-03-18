package foo.service;

import java.util.Arrays;

import foo.model.User;

public class UserService {

	public static final int MILLIS_TO_GET_LOGIN = 5 * 1000;

	public User getUser(Integer id) {
//		System.out.println("real service request for id= " + id);
//		try {
//			// we will sleep id seconds :-)
//			Thread.sleep(MILLIS_TO_GET_LOGIN);
//		} catch (InterruptedException e) {
//			Thread.interrupted();// who dared to interrupt me?
//		}
		// we want to store relatively large strings to check the speed of disk cache
		char[] chars = new char[100000];
		Arrays.fill(chars, 'a');
		return new User(id, id + new String(chars));
	}

}
