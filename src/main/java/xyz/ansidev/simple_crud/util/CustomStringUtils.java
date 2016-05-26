package xyz.ansidev.simple_crud.util;

public class CustomStringUtils {
	/**
	 * Convert camel case string to title case. <br>
	 * http://stackoverflow.com/questions/2559759/how-do-i-convert-camelcase-into-human-readable-names-in-java <br>
	 * 
	 * @param s
	 *            Camel case string
	 * @return Title case String
	 */
	public static String splitCamelCase(String s) {
		// First character to upper case
		s = s.substring(0,1).toUpperCase() + s.substring(1);
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}
}
