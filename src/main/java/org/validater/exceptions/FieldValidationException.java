package org.validater.exceptions;

import java.lang.reflect.Field;

/**
 * Thrown when the field type not matches the validation type.
 */

public class FieldValidationException extends ValidationException{

    private final Class<?> cls;
    private final Field field;

    public FieldValidationException(Class<?> cls, Field field) {
        super("Invalid type of field: [" + field.getName() + "] Class: " + cls.getSimpleName());
        this.cls = cls;
        this.field = field;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Field getField() {
        return field;
    }
}
