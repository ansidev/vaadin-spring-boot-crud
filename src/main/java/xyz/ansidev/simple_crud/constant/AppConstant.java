package xyz.ansidev.simple_crud.constant;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class AppConstant {
	public final static String APP_NAME = "Simple CRUD";
	public final static String LANG = "lang";

	public final static String LANG_VI = "vi";
	public final static Locale LOCALE_VIETNAMESE = new Locale(LANG_VI);
	
	public final static String DATE_FORMAT_STRING = "dd/MM/yyyy";
	public final static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_FORMAT_STRING);
	
	public final static String EMPTY = "";
	public final static String WHITE_SPACE = " ";

	// Must have columns for all tables
	// Column names
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_USERNAME = "username";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_FIRST_NAME = "firstName";
	public static final String COLUMN_LAST_NAME = "lastName";
	public static final String COLUMN_CREATED_AT = "createdAt";
	public static final String COLUMN_UPDATED_AT = "updatedAt";
	public static final String COLUMN_CREATED_DATE = "createdDate";
	public static final String COLUMN_UPDATED_DATE = "updatedDate";
	public static final String COLUMN_ACTIONS = "actions";
	
	public static final String CAPTION_OK_BUTTON = "OK";
	public static final String CAPTION_CANCEL_BUTTON = "Cancel";
	public static final String CAPTION_FILTER_BOX = "Filter";
	
	public static final float FULL_WIDTH = 100;
	
	
}
