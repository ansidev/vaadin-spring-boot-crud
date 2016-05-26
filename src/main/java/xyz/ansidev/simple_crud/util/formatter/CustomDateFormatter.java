package xyz.ansidev.simple_crud.util.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.ui.component.UserRegistrationForm;

public class CustomDateFormatter {
	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationForm.class);
	private static final String DEFAULT_RETURN_VALUE = AppConstant.EMPTY;

	/**
	 * Format LocalDateTime with specific format.
	 * 
	 * @param date
	 *            LocalDateTime
	 * @param formatString
	 *            Format of output
	 * @return
	 */
	public static String format(LocalDateTime date, String formatString) {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatString);
			return date.format(dtf);
		} catch (NullPointerException e) {
			LOG.error("NullPointerException: " + e.getMessage());
			return DEFAULT_RETURN_VALUE;
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
			return DEFAULT_RETURN_VALUE;
		}
	}

	/**
	 * Format date to pattern dd/MM/YYYY.
	 * 
	 * @param date
	 * @return Date as string, ex: 15/01/2016.
	 */
	public static String toDate(LocalDateTime date) {
		return format(date, AppConstant.DATE_FORMAT_STRING);
	}
}
