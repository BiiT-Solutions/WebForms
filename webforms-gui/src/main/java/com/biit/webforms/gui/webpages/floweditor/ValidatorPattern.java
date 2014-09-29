package com.biit.webforms.gui.webpages.floweditor;

import java.util.regex.Pattern;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Validator;

public class ValidatorPattern implements Validator {
	private static final long serialVersionUID = -3158239634237872250L;

	private Pattern regex;
	
	public ValidatorPattern(Pattern regex) {
		this.regex = regex;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		
		System.out.println(value);
		System.out.println(regex.pattern());
		
		if(value!=null &&!regex.matcher((String)value).matches()){
			throw new InvalidValueException(LanguageCodes.VALIDATOR_ERROR_PATTERN.translation()+" "+regex.pattern());
		}
	}

}
