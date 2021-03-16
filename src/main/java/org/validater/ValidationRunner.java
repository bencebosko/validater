package org.validater;

import org.springframework.core.annotation.AnnotationUtils;
import org.validater.annotations.ValidatedBy;
import org.validater.annotations.Validation;
import org.validater.exceptions.FieldValidationException;
import org.validater.exceptions.ObjectValidationException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Main class which runs all type of validations.
 * The annotated objects must be passed to the 'validate' method and the 'ValidationResult' is returned.
 *
 */

public class ValidationRunner {

    private final ValidationLoader validationLoader;
    private final ValidationCache validationCache;

    ValidationRunner() {
        validationLoader = new ValidationLoader();
        validationCache = new ValidationCache();
    }

    public ValidationResult validate(Object obj) {
        Class<?> objType = obj.getClass();
        if(validationCache.isCached(objType))
            return validationCache.validate(obj);

        Optional<ValidatedBy> validation =
                Optional.ofNullable(objType.getAnnotation(ValidatedBy.class));

        if(validation.isPresent()) {
            Validator<Object> validator = validationLoader.getValidator(validation.get().validator());
            return validate(obj, validator);
        } else {
            return validateAllField(obj);
        }
    }

    private ValidationResult validate(Object obj, Validator<Object> validator) {
        if(obj.getClass() != validator.type())
            throw new ObjectValidationException(obj, validator);

        Map<String, List<ValidationError>> errors = new HashMap<>();
        validator.validate(obj, errors);

        return new ValidationResult(errors);
    }

    private ValidationResult validateAllField(Object obj) {
        Class<?> cls = obj.getClass();
        List<FieldValidation> validations = scanClassFields(cls);
        validationCache.cacheForType(cls, validations);
        return validationCache.validate(obj);
    }

    List<FieldValidation> scanClassFields(Class<?> cls) {

        List<FieldValidation> validations = new ArrayList<>();

        for(Field field : cls.getDeclaredFields()) {
            for(Annotation annotation : field.getDeclaredAnnotations()) {
                Optional<Validation> validation =
                        Optional.ofNullable(AnnotationUtils.getAnnotation(annotation, Validation.class));

                if(validation.isPresent()) {
                    FieldValidator<Object, Annotation> validator =
                            validationLoader.getFieldValidator(validation.get().validator());
                    if(!isCompatible(field, validator))
                        throw new FieldValidationException(cls, field);
                    else
                        validations.add(new FieldValidation(field, annotation, validator));
                }
            }
        }
        return validations;
    }

    ValidationCache getCache() {
        return validationCache;
    }

    private boolean isCompatible(Field field, FieldValidator<Object, Annotation> validator) {
        Class<?> fieldType = field.getType();
        if(fieldType.isPrimitive()) {
            fieldType = PrimitiveMapper.getWrapper(fieldType);
        }
        return validator.fieldType().isAssignableFrom(fieldType);
    }
}
