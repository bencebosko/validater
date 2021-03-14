package org.validater.exceptions;

import java.lang.reflect.Field;

/**
 * Thrown when the field type not matches the validation type.
 */

public class FieldValidationException extends ValidationException{

    private final Object obj;
    private final Field field;

    public FieldValidationException(Object obj, Field field) {
        super("Invalid type of field: [" + field.getName() + "] Class: " + obj.getClass().getSimpleName());
        this.obj = obj;
        this.field = field;
    }

    public Object getObj() {
        return obj;
    }

    public Field getField() {
        return field;
    }
}
