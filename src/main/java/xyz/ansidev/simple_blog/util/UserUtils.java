package xyz.ansidev.simple_blog.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.ansidev.simple_blog.SimpleBlogApplication;
import xyz.ansidev.simple_blog.constant.AppConstant;
import xyz.ansidev.simple_blog.entity.User;

public class UserUtils {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleBlogApplication.class);

	public static String getUserName(String firstName, String lastName) {
		LOG.info("getUserName: " + firstName + "-" + lastName);
		String username = AppConstant.EMPTY;
		String[] lastNameArr = lastName.trim().split(AppConstant.WHITE_SPACE);
		int lastNameLength = lastNameArr.length;
		username += lastNameArr[lastNameLength - 1] + getFirstCharacter(firstName);
		for (int i = 0; i < lastNameLength - 1; i++) {
			username += getFirstCharacter(lastNameArr[i]);
		}
		LOG.info(username);
		return VNCharacterUtils.removeAccent(username.trim().toLowerCase());
	}

	private static String getFirstCharacter(String s) {
		return s.trim().substring(0, 1);
	}

	public static User initUser(String fullname) {
		String[] fullnameArr = fullname.trim().split(AppConstant.WHITE_SPACE);
		String firstName = fullnameArr[0];
		String lastName = fullname.substring(firstName.length());
		String username = getUserName(firstName, lastName);

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);

		return user;
	}

}
