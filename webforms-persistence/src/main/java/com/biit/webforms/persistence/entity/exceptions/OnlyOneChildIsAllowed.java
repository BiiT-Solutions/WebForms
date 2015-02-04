package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.NotValidChildException;

public class OnlyOneChildIsAllowed extends NotValidChildException {
	private static final long serialVersionUID = 8095970103333007972L;

	public OnlyOneChildIsAllowed(String message) {
		super(message);
	}

}
