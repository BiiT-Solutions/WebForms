package com.biit.webforms.persistence.entity.exceptions;

import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;

public class ElementIsUsedInFlowException extends ElementCannotBePersistedException {
	private static final long serialVersionUID = -6452493673961564645L;

	public ElementIsUsedInFlowException(String message) {
		super(message);
	}
}
