package org.validater;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

class ValidationCache {

    private final Map<Class<?>, List<FieldValidation>> fieldValidations = new HashMap<>();

    private static class FieldValidation {
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
    }

    boolean isCached(Class<?> type) {
        return fieldValidations.containsKey(type);
    }

    void addValidation(Class<?> type, Field field, Annotation annotation, FieldValidator<Object, Annotation> validator) {
        List<FieldValidation> validations = fieldValidations.get(type);
        if(validations != null)
            validations.add(new FieldValidation(field, annotation, validator));
        else {
            validations = new ArrayList<>();
            validations.add(new FieldValidation(field, annotation, validator));
            fieldValidations.put(type, validations);
        }
    }

    ValidationResult validate(Object obj) {
        ValidationResult res = new ValidationResult();
        try {
            for (FieldValidation v : fieldValidations.get(obj.getClass())) {
                v.run(obj, res);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return res;
    }
}
