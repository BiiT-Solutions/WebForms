package com.biit.webforms.persistence.entity.exceptions;

import com.biit.form.exceptions.DependencyExistException;

public class ElementIsUsedAsDefaultValueException extends DependencyExistException {
    private static final long serialVersionUID = -6452493673961564645L;

    public ElementIsUsedAsDefaultValueException(String message) {
        super(message);
    }
}
