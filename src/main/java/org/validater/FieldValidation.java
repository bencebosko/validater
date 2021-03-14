package org.validater;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a cached field validation.
 *
 */

class FieldValidation {
    private final Field field;
    private final Annotation annotation;
    private final FieldValidator<Object, Annotation> validator;

    FieldValidation(Field field, Annotation annotation, FieldValidator<Object, Annotation> validator) {
        this.field = field;
        this.annotation = annotation;
        this.validator = validator;
    }

    void run(Object obj, ValidationResult res) throws IllegalArgumentException, IllegalAccessException {
        List<ValidationError> errors = new ArrayList<>();
        validator.validate(field.get(obj), annotation, errors);
        if(!errors.isEmpty())
            res.addErrorList(field.getName(), errors);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldValidation that = (FieldValidation) o;

        return this.field.equals(that.field) && this.annotation.equals(that.annotation);
    }

    @Override
    public int hashCode() {
        return field.hashCode();
    }
}