package com.biit.webforms.gui.common.components;

import com.vaadin.data.util.converter.Converter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

/**
 * String-timestamp conversor for vaadin containers. Uses default locale.
 *
 */
public class StringToTimestampConverter implements Converter<String, Timestamp> {
	private static final long serialVersionUID = 5276134636377495040L;

	/**
	 * Returns the format used by
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

	@Override
	public Timestamp convertToModel(String value, Class<? extends Timestamp> targetType, Locale locale)
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

		ParsePosition parsePosition = new ParsePosition(0);
		Timestamp parsedValue = new Timestamp(getFormat(locale).parse(value, parsePosition).getTime());
		if (parsePosition.getIndex() != value.length()) {
			throw new ConversionException("Could not convert '" + value + "' to " + getModelType().getName());
		}
		return parsedValue;
	}

	@Override
	public String convertToPresentation(Timestamp value, Class<? extends String> targetType, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		if (value == null) {
			return null;
		}

		return getFormat(locale).format(value);
	}

	@Override
	public Class<Timestamp> getModelType() {
		return Timestamp.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
