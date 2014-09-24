package com.biit.webforms.gui.webpages.designer;

import java.util.regex.Pattern;

import com.vaadin.data.Validator;

public class TreeObjectNameValidator implements Validator {
	private static final long serialVersionUID = 8591363723969902840L;

	private Pattern pattern;

	public TreeObjectNameValidator(Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!pattern.matcher((String) value).matches()) {
			throw new InvalidValueException("'" + value + "' doesn't comply XML tag restrictions");
		}
	}

}
