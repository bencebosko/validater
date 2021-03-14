package org.validater.exceptions;

/**
 * Super class of all validation exceptions.
 */

public class ValidationException extends RuntimeException {
    public ValidationException(String msg) {
        super(msg);
    }
}
