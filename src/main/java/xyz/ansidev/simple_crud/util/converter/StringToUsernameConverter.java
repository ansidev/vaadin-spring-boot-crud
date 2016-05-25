package xyz.ansidev.simple_crud.util.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

@SuppressWarnings("serial")
public class StringToUsernameConverter implements Converter<String, String> {

	@Override
	public String convertToModel(String value, Class<? extends String> targetType, Locale locale)
			throws Converter.ConversionException {
		return "not implemented";
	}

	@Override
	public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale)
			throws Converter.ConversionException {
		return "<a href='http://en.wikipedia.org/wiki/" + value + "' target='_blank'>more info</a>";
	}

	@Override
	public Class<String> getModelType() {
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
