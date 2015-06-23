package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.DependencyExistException;

public class WebserviceDependencyExistException extends DependencyExistException  {
	private static final long serialVersionUID = 8968918098077359062L;

	public WebserviceDependencyExistException(String string) {
		super(string);
	}
}
