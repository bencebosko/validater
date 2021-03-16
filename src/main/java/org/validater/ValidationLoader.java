package org.validater;

import org.jetbrains.annotations.NotNull;
import org.validater.exceptions.ValidatorInstantiationException;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class which loads and stores validator instances.
 * */

class ValidationLoader {

    private final Map<Class<? extends Validator>, Validator<Object>> validators;
    private final Map<Class<? extends FieldValidator>, FieldValidator<Object, Annotation>> fieldValidators;

    ValidationLoader() {
        validators = new ConcurrentHashMap<>();
        fieldValidators = new ConcurrentHashMap<>();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    Validator<Object> getValidator(Class<? extends Validator> type) {
        Validator<Object> validator = validators.get(type);
        if(validator == null) {
            try {
                validator = (Validator<Object>)type.newInstance();
                validators.put(type, validator);

            } catch (InstantiationException | IllegalAccessException ex) {
                throw new ValidatorInstantiationException(type);
            }
        }
        return validator;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    FieldValidator<Object, Annotation> getFieldValidator(Class<? extends FieldValidator> type) {
        FieldValidator<Object, Annotation> validator = fieldValidators.get(type);
        if(validator == null) {
            try {
                validator = (FieldValidator<Object, Annotation>)type.newInstance();
                fieldValidators.put(type, validator);

            } catch (InstantiationException | IllegalAccessException ex) {
                throw new ValidatorInstantiationException(type);
            }
        }
        return validator;
    }
}
