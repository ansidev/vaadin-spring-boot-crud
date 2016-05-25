package xyz.ansidev.simple_blog.constant;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppConstant {
	public final static String LANG = "lang";

	public final static String LANG_VI = "vi";
	public final static Locale LOCALE_VIETNAMESE = new Locale(LANG_VI);
	
	public final static String DATE_FORMAT_STRING = "dd/MM/yyyy";
	public final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STRING);
	
	public final static String EMPTY = "";
	public final static String WHITE_SPACE = " ";

	// Must have columns for all tables
	// Column names
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_CREATED_AT = "created_at";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_ACTIONS = "actions";
	
	public static final String CAPTION_OK_BUTTON = "OK";
	public static final String CAPTION_CANCEL_BUTTON = "Cancel";
	
	public static final float FULL_WIDTH = 100;
}
