package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.NotValidChildException;

public class OnlyOneChildIsAllowedException extends NotValidChildException {
	private static final long serialVersionUID = 8095970103333007972L;

	public OnlyOneChildIsAllowedException(String message) {
		super(message);
	}

}
