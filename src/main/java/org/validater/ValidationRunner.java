package org.validater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
 * Caching can be turned on and off by setting 'cache' true or false.
 *
 */

public class ValidationRunner {

    @Autowired
    private ApplicationContext context;
    private final ValidationLoader validationLoader;
    private ValidationCache validationCache;

    public ValidationRunner() {
        validationLoader = ValidationLoader.getInstance();
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
            List<FieldValidation> cached = new ArrayList<>();
            ValidationResult result = validateAllField(obj, cached);
            if(!validationCache.isCached(objType))
                validationCache.cacheForType(objType, cached);
            return result;
        }
    }

    public void cache(boolean cacheOn) {
        if(cacheOn) {
            validationCache = new ValidationCache();
        } else {
            validationCache = new ValidationCache() {
                @Override
                boolean isCached(Class<?> type) {
                    return false;
                }
            };
        }
    }

    private ValidationResult validate(Object obj, Validator<Object> validator) {
        if(obj.getClass() != validator.type())
            throw new ObjectValidationException(obj, validator);

        Map<String, List<ValidationError>> errors = new HashMap<>();
        validator.validate(obj, errors);

        return new ValidationResult(errors);
    }

    private ValidationResult validateAllField(Object obj, List<FieldValidation> cached) {
        ValidationResult res = new ValidationResult();

        for(Field field : obj.getClass().getDeclaredFields()) {
            for(Annotation annotation : field.getDeclaredAnnotations()) {
                Optional<Validation> validation =
                        Optional.ofNullable(AnnotationUtils.getAnnotation(annotation, Validation.class));

                if(validation.isPresent()) {
                    FieldValidator<Object, Annotation> validator =
                            validationLoader.getFieldValidator(validation.get().validator());
                    validateField(obj, field, validator, annotation, res, cached);
                }
            }
        }
        return res;
    }

    void validateField(Object obj, Field field, FieldValidator<Object, Annotation> validator,
                       Annotation annotation, ValidationResult result, List<FieldValidation> cached) {

        Class<?> fieldType = field.getType();
        if(fieldType.isPrimitive()) {
            fieldType = PrimitiveMapper.getWrapper(fieldType);
        }
        if(!subTypeOf(fieldType, validator.fieldType()))
            throw new FieldValidationException(obj, field);
        try {
            List<ValidationError> errors = new ArrayList<>();
            field.setAccessible(true);
            validator.validate(field.get(obj), annotation, errors);
            if(!errors.isEmpty())
                result.addErrorList(field.getName(), errors);
            cached.add(new FieldValidation(field, annotation, validator));

        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    private boolean subTypeOf(Class<?> a, Class<?> b) {
        return b.isAssignableFrom(a);
    }
}
