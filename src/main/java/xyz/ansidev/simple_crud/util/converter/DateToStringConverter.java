
/*
 * Copyright 2000-2014 Vaadin Ltd.
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
package xyz.ansidev.simple_crud.util.converter;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A converter that converts from {@link Date} to {@link String} and back. Uses the given locale and {@link DateFormat}
 * for formatting and parsing.
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
public class DateToStringConverter implements Converter<Date, String> {

	/**
	 * Returns the format used by {@link #convertToPresentation(Date, Class,Locale)} and
	 * {@link #convertToModel(String, Class, Locale)}.
	 * 
	 * @param locale
	 *            The locale to use
	 * @return A DateFormat instance
	 */
	protected DateFormat getFormat(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}

		DateFormat f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
		f.setLenient(false);
		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#convertToModel(java.lang.Object, java.lang.Class, java.util.Locale)
	 */
	@Override
	public String convertToModel(Date value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (targetType != getModelType()) {
			throw new ConversionException("Converter only supports " + getModelType().getName() + " (targetType was "
					+ targetType.getName() + ")");
		}

		if (value == null) {
			return null;
		}

		return getFormat(locale).format(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang .Object, java.lang.Class,
	 * java.util.Locale)
	 */
	@Override
	public Date convertToPresentation(String value, Class<? extends Date> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		// Remove leading and trailing white space
		value = value.trim();

		ParsePosition parsePosition = new ParsePosition(0);
		Date parsedValue = getFormat(locale).parse(value, parsePosition);
		if (parsePosition.getIndex() != value.length()) {
			throw new ConversionException("Could not convert '" + value + "' to " + getModelType().getName());
		}

		return parsedValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getModelType()
	 */
	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vaadin.data.util.converter.Converter#getPresentationType()
	 */
	@Override
	public Class<Date> getPresentationType() {
		return Date.class;
	}

}
