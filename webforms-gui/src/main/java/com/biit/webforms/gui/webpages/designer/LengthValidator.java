package com.biit.webforms.gui.webpages.designer;

import com.vaadin.data.Validator;

public class LengthValidator implements Validator {
	private static final long serialVersionUID = 7781498467462964780L;
	private int length;
	
	public LengthValidator(int length) {
		this.length = length;
	}
	
	@Override
	public void validate(Object value) throws InvalidValueException {
		if(value!=null && ((String)value).length()>length){
			throw new InvalidValueException("Max number of characters "+length);
		}
	}

}
