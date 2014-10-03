package com.biit.webforms.gui.webpages.floweditor;

import java.util.regex.Pattern;

import com.biit.webforms.language.LanguageCodes;
import com.vaadin.data.Validator;

public class ValidatorPattern implements Validator {
	private static final long serialVersionUID = -3158239634237872250L;

	private Pattern[] patterns;
	
	public ValidatorPattern(Pattern ... patterns) {
		this.patterns = patterns;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if(value!=null){
			for(Pattern pattern: patterns){
				if(pattern.matcher((String)value).matches()){
					//Validated
					return;
				}
			}
		}
		String patternString = patterns[0].pattern();
		for(int i=1;i<patterns.length;i++){
			patternString+=" or "+patterns[i].pattern();
		}
		throw new InvalidValueException(LanguageCodes.VALIDATOR_ERROR_PATTERN.translation()+" "+patternString);
	}

}
