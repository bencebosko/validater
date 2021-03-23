package org.validater;

import org.jetbrains.annotations.NotNull;
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
        Class<?> type = obj.getClass();
        if(validationCache.isCached(type)) {
            List<FieldValidation> validations = validationCache.getValidations(type);
            return validate(obj, validations);
        }

        Optional<ValidatedBy> validation =
                Optional.ofNullable(type.getAnnotation(ValidatedBy.class));

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
        List<FieldValidation> validations = scanClass(cls);
        validationCache.cacheForType(cls, validations);
        return validate(obj, validations);
    }

    List<FieldValidation> scanClass(Class<?> cls) {
        return scanClassRec(cls, new ArrayList<>(), new HashSet<>());
    }

    private List<FieldValidation> scanClassRec(Class<?> cls, List<FieldValidation> validations, Set<String> scannedNames) {
        if(cls == Object.class)
            return validations;

        for(Field field : cls.getDeclaredFields()) {
            if(!isFieldHidden(field, scannedNames)) {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    Optional<Validation> validation =
                            Optional.ofNullable(AnnotationUtils.getAnnotation(annotation, Validation.class));

                    if (validation.isPresent()) {
                        FieldValidator<Object, Annotation> validator =
                                validationLoader.getFieldValidator(validation.get().validator());
                        if (!isCompatible(field, validator))
                            throw new FieldValidationException(cls, field);
                        else
                            validations.add(new FieldValidation(field, annotation, validator));
                    }
                }
                scannedNames.add(field.getName());
            }
        }
        return scanClassRec(cls.getSuperclass(), validations, scannedNames);
    }

    private ValidationResult validate(Object obj, List<FieldValidation> validations) {
        ValidationResult res = new ValidationResult();
        try {
            for (FieldValidation v : validations) {
                v.run(obj, res);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
        return res;
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

    private boolean isFieldHidden(Field field, Set<String> fieldNames) {
        return fieldNames.contains(field.getName());
    }
}
