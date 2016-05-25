package xyz.ansidev.simple_blog.constant;

public class UserFormConstant {

	public final static String FORM_CAPTION_PATTERN = "%s User Form";
	public final static String FORM_CAPTION_ADD = String.format(FORM_CAPTION_PATTERN, "Add");
	public final static String FORM_CAPTION_EDIT = String.format(FORM_CAPTION_PATTERN, "Edit");

	public final static String ID = "ID";
	public final static String USERNAME = "Username";
	public final static String FIRST_NAME = "First name";
	public final static String LAST_NAME = "Last name";
	public final static String FULL_NAME = "Full name";
	public final static String EMAIL = "Email";
	public final static String PASSWORD = "Password";
	public final static String BIRTHDAY = "Birthday";
	public final static String GENDER = "Gender";
	public final static String NATIONALITY = "Nationality";
	public final static String ADDRESS = "Address";
	public final static String PHONE = "Phone";
	
	public final static String BUTTON_ADD = "Add %s";
	public final static String BUTTON_SAVE = "Save";
	public final static String BUTTON_DELETE = "Delete";
	public final static String BUTTON_RESET = "Reset";
	public final static String BUTTON_CANCEL = "Cancel";

	public final static String GENDER_MALE = "Male";
	public final static String GENDER_FEMALE = "Female";
	
	

	/**
	 * Explanation: <br>
	 * ^ 				# start-of-string <br>
	 * [a-zA-Z0-9] 		# only allow a-z, A-Z, 0-9 characters <br>
	 * .{5,10} 			# length between 5 and 10 characters <br>
	 * $				# end-of-string <br>
	 **/
	public final static String USERNAME_PATTERN = "^[a-zA-Z0-9]{5,10}$";
	public final static String USERNAME_DESCRIPTION = "Requirement for username: <br>"
			+ "- Only allow a-z, A-Z, 0-9 characters. <br>"
			+ "- Length between 5 and 10 characters. <br>";

	/**
	 * Explanation: <br>
	 * ^ 				# start-of-string <br>
	 * (?=.*[0-9]) 		# a digit must occur at least once <br>
	 * (?=.*[a-z]) 		# a lower case letter must occur at least once <br>
	 * (?=.*[A-Z]) 		# an upper case letter must occur at least once <br>
	 * (?=.*[@#$%^&+=]) # a special character must occur at least once <br>
	 * (?=\S+$) 		# no whitespace allowed in the entire string <br>
	 * .{8,} 			# anything, at least eight places though <br>
	 * $				# end-of-string <br>
	 **/
	public final static String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
	public final static String PASSWORD_DESCRIPTION = "Requirement for password: <br>"
			+ "- A digit must occur at least once. <br/>"
			+ "- A lower case letter must occur at least once. <br/>"
			+ "- An upper case letter must occur at least once. <br/>"
			+ "- A special character must occur at least once. <br/>"
			+ "- No whitespace allowed in the entire string. <br/>"
			+ "- At least 8 characters length. <br/>";

}
