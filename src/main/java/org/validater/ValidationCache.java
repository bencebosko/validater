package org.validater;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class ValidationCache {

    private final Map<Class<?>, List<FieldValidation>> fieldValidations = new ConcurrentHashMap<>();

    boolean isCached(Class<?> type) {
        return fieldValidations.containsKey(type);
    }

    void cacheForType(Class<?> type, List<FieldValidation> validations) {
        fieldValidations.put(type, Collections.unmodifiableList(validations));
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
