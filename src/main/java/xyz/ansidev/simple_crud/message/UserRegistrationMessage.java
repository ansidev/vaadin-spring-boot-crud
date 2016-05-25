package xyz.ansidev.simple_crud.message;

public class UserRegistrationMessage {

	public final static String SAVE_SUCCESS = "Saved user %s successfully!";
	public final static String SAVE_FAILED = "Saved user %s failed!";
	public final static String PASSWORD_CHANGED = "User password was changed!";

	public final static String FILTER_PLACEHOLDER = "Filter by username";
	
	public final static String MESSAGE_CONFIRM_DELETE = "Are you sure want to delete this user?";

	public final static String ERROR_NOT_MATCH_REQUIREMENTS = "%s not match requirements";
	public final static String ERROR_EMPTY_NOT_ALLOWED = "%s must not empty or null";
	public final static String ERROR_NULL_NOT_ALLOWED = "Null value is not allowed!";
	public final static String ERROR_REQUIRED_FIELD = "Please give %s";
	public final static String ERROR_INVALID_EMAIL = "Invalid email!";
}
