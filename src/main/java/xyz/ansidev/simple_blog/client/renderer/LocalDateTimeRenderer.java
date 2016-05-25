package xyz.ansidev.simple_blog.client.renderer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.ui.Grid.AbstractRenderer;

import elemental.json.JsonValue;

/**
 * A renderer for presenting LocalDateTime values.
 * 
 * @since 7.4
 * @author Le Minh Tri
 */
@SuppressWarnings("serial")
public class LocalDateTimeRenderer extends AbstractRenderer<LocalDateTime> {
	private final Locale locale;
	private final String formatString;
	private  DateTimeFormatter dateTimeFormatter;

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the {@link LocalDateTime#toString()} representation for the default
	 * locale.
	 */
	public LocalDateTimeRenderer() {
		this(Locale.getDefault(), "");
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the {@link LocalDateTime#toString()} representation for the given
	 * locale.
	 * 
	 * @param locale
	 *            the locale in which to present dates
	 * @throws IllegalArgumentException
	 *             if {@code locale} is {@code null}
	 */
	public LocalDateTimeRenderer(Locale locale) throws IllegalArgumentException {
		this("%s", locale, "");
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the {@link LocalDateTime#toString()} representation for the given
	 * locale.
	 *
	 * @param locale
	 *            the locale in which to present dates
	 * @param nullRepresentation
	 *            the textual representation of {@code null} value
	 * @throws IllegalArgumentException
	 *             if {@code locale} is {@code null}
	 */
	public LocalDateTimeRenderer(Locale locale, String nullRepresentation) throws IllegalArgumentException {
		this("%s", locale, nullRepresentation);
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the given string format, as displayed in the default locale.
	 * 
	 * @param formatString
	 *            the format string with which to format the LocalDateTime
	 * @throws IllegalArgumentException
	 *             if {@code formatString} is {@code null}
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">Format String Syntax</a>
	 */
	public LocalDateTimeRenderer(String formatString) throws IllegalArgumentException {
		this(formatString, "");
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the given string format, as displayed in the default locale.
	 *
	 * @param formatString
	 *            the format string with which to format the LocalDateTime
	 * @param nullRepresentation
	 *            the textual representation of {@code null} value
	 * @throws IllegalArgumentException
	 *             if {@code formatString} is {@code null}
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">Format String Syntax</a>
	 */
	public LocalDateTimeRenderer(String formatString, String nullRepresentation) throws IllegalArgumentException {
		this(formatString, Locale.getDefault(), nullRepresentation);
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the given string format, as displayed in the given locale.
	 * 
	 * @param formatString
	 *            the format string to format the LocalDateTime with
	 * @param locale
	 *            the locale to use
	 * @throws IllegalArgumentException
	 *             if either argument is {@code null}
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">Format String Syntax</a>
	 */
	public LocalDateTimeRenderer(String formatString, Locale locale) throws IllegalArgumentException {
		this(formatString, locale, "");
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with the given string format, as displayed in the given locale.
	 *
	 * @param formatString
	 *            the format string to format the LocalDateTime with
	 * @param locale
	 *            the locale to use
	 * @param nullRepresentation
	 *            the textual representation of {@code null} value
	 * @throws IllegalArgumentException
	 *             if either argument is {@code null}
	 * @see <a href="http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">Format String Syntax</a>
	 */
	public LocalDateTimeRenderer(String formatString, Locale locale, String nullRepresentation)
			throws IllegalArgumentException {
		super(LocalDateTime.class, nullRepresentation);

		if (formatString == null) {
			throw new IllegalArgumentException("format string may not be null");
		}

		if (locale == null) {
			throw new IllegalArgumentException("locale may not be null");
		}

		this.locale = locale;
		this.formatString = formatString;
		dateTimeFormatter = null;
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with he given LocalDateTime format.
	 * 
	 * @param dateFormat
	 *            the LocalDateTime format to use when rendering dates
	 * @throws IllegalArgumentException
	 *             if {@code dateFormat} is {@code null}
	 */
	public LocalDateTimeRenderer(DateTimeFormatter dateTimeFormatter) throws IllegalArgumentException {
		this(dateTimeFormatter, "");
	}

	/**
	 * Creates a new LocalDateTime renderer.
	 * <p>
	 * The renderer is configured to render with he given LocalDateTime format.
	 *
	 * @param dateFormat
	 *            the LocalDateTime format to use when rendering dates
	 * @throws IllegalArgumentException
	 *             if {@code dateFormat} is {@code null}
	 */
	public LocalDateTimeRenderer(DateTimeFormatter dateTimeFormatter, String nullRepresentation)
			throws IllegalArgumentException {
		super(LocalDateTime.class, nullRepresentation);
		if (dateTimeFormatter == null) {
			throw new IllegalArgumentException("date format may not be null");
		}

		locale = null;
		formatString = null;
		this.dateTimeFormatter = dateTimeFormatter;
	}

	@Override
	public String getNullRepresentation() {
		return super.getNullRepresentation();
	}

	@Override
	public JsonValue encode(LocalDateTime value) {
		String dateString;
		if (value == null) {
			dateString = getNullRepresentation();
		} else if (dateTimeFormatter != null) {
			dateString = dateTimeFormatter.format(value);
		} else {
			dateString = String.format(locale, formatString, value);
		}
		return encode(dateString, String.class);
	}

	@Override
	public String toString() {
		final String fieldInfo;
		if (dateTimeFormatter != null) {
			fieldInfo = "dateFormat: " + dateTimeFormatter.toString();
		} else {
			fieldInfo = "locale: " + locale + ", formatString: " + formatString;
		}

		return String.format("%s [%s]", getClass().getSimpleName(), fieldInfo);
	}
}
