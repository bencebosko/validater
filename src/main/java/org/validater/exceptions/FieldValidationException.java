package org.validater.exceptions;

import java.lang.reflect.Field;

/**
 * Thrown when the field type not matches the validation type.
 */

public class FieldValidationException extends ValidationException{

    private final Class<?> clss;
    private final Field field;

    public FieldValidationException(Class<?> clss, Field field) {
        super("Invalid field type for: " + field.getName() + " Class: " + clss.getSimpleName());
        this.clss = clss;
        this.field = field;
    }

    public Class<?> getClss() {
        return clss;
    }

    public Field getField() {
        return field;
    }
}
