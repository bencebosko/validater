package org.validater.exceptions;

import org.validater.Validator;

public class ObjectValidationException extends ValidationException{

    private final Object obj;
    private final Validator<Object> validator;

    public ObjectValidationException(Object obj, Validator<Object> validator) {
        super("Object type not matches validator: " + obj.getClass().getSimpleName());
        this.obj = obj;
        this.validator = validator;
    }

    public Object getObject() {
        return obj;
    }
    public Validator<Object> getValidator() {
        return validator;
    }
}
