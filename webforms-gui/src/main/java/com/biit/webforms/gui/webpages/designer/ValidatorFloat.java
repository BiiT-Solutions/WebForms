package com.biit.webforms.gui.webpages.designer;

import com.vaadin.data.Validator;

import java.util.regex.Pattern;

public class ValidatorFloat implements Validator {
	private static final long serialVersionUID = 8591363723969902840L;
	public static final String ANSWER_TAG_ALLOWED_CHARS = "[-+]?[0-9]*\\.?[0-9]+";
	private static final Pattern pattern = Pattern.compile(ANSWER_TAG_ALLOWED_CHARS);

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!pattern.matcher((String) value).matches()) {
			throw new InvalidValueException("Text '" + value + "' is not valid.");
		}
	}

}
