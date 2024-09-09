package com.biit.webforms.persistence.entity.exceptions;

public class InvalidValue extends Exception {
    private static final long serialVersionUID = 186709325473429038L;


    public InvalidValue() {
    }

    public InvalidValue(String message) {
        super(message);
    }
}
