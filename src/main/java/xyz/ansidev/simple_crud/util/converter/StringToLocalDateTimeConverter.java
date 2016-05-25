package xyz.ansidev.simple_crud.util.converter;

/*
 * Copyright 2016 Le Minh Tri
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A converter that converts from {@link LocalDateTime} to {@link String} and back. Uses the given locale and
 * {@link DateFormat} for formatting and parsing.
 * <p>
 * Leading and trailing white spaces are ignored when converting from a String.
 * </p>
 * <p>
 * Override and overwrite {@link #getFormat(Locale)} to use a different format.
 * </p>
 * 
 * @author Vaadin Ltd
 * @since 7.0
 */
@SuppressWarnings("serial")
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

	private String pattern;

	public StringToLocalDateTimeConverter(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Returns the format used by {@link #convertToPresentation(Date, Class,Locale)} and
	 * {@link #convertToModel(String, Class, Locale)}.
	 * 
	 * @param locale
	 *            The locale to use
	 * @return A DateFormat instance
	 */
	@SuppressWarnings("static-access")
	protected DateTimeFormatter getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		DateTimeFormatter f = DateTimeFormatter.ofPattern(pattern, locale).ofLocalizedDateTime(FormatStyle.MEDIUM,
				FormatStyle.MEDIUM);
		return f;
	}

	@Override
	public LocalDateTime convertToModel(String value, Class<? extends LocalDateTime> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (targetType != getModelType()) {
			throw new ConversionException("Converter only supports " + getModelType().getName() + " (targetType was "
					+ targetType.getName() + ")");
		}

		if (value == null) {
			return null;
		}

		// Remove leading and trailing white space
		value = value.trim();

		LocalDateTime parsedValue = LocalDateTime.parse(value, getFormat(locale));

		return parsedValue;
	}

	@Override
	public String convertToPresentation(LocalDateTime value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		return getFormat(locale).format(value);
	}

	@Override
	public Class<LocalDateTime> getModelType() {
		return LocalDateTime.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
