package xyz.ansidev.simple_blog.message;

import xyz.ansidev.simple_blog.constant.UserFormConstant;

public class UserRegistrationMessage {

	public final static String SAVE_SUCCESS = "Thanks %s for registering!";
	public final static String FILTER_PLACEHOLDER = "Filter by username";
	public final static String ERROR_USERNAME_INVALID_LENGTH = "The name must be "
			+ UserFormConstant.USERNAME_MIN_LENGTH + "-" + UserFormConstant.USERNAME_MAX_LENGTH + " letters (was {0})";
}
