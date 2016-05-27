package xyz.ansidev.simple_crud.util.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.ansidev.simple_crud.constant.AppConstant;
import xyz.ansidev.simple_crud.ui.component.UserRegistrationForm;

public class CustomDateFormatter implements IFormatter<LocalDateTime, String> {
	private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationForm.class);

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
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatString);
			return date.format(formatter);
		} catch (NullPointerException e) {
			LOG.error("NullPointerException: " + e.getMessage());
			return null;
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Parse string to LocalDateTime.
	 * 
	 * @param dateString
	 *            String of LocalDateTime
	 * @param formatter
	 *            DateTimeFormatter
	 * @return LocalDateTime
	 */
	public static LocalDateTime parse(String dateString, DateTimeFormatter formatter) {
		try {
			return LocalDateTime.parse(dateString, formatter);
		} catch (Exception e) {
			LOG.error("Exception: " + e.getMessage());
			return null;
		}
	}

	@Override
	public String format(LocalDateTime date) {
		return format(date, AppConstant.DATE_FORMAT_STRING);
	}

	@Override
	public LocalDateTime parse(String presentation) {
		return parse(presentation, AppConstant.DATE_FORMAT);
	}
}
