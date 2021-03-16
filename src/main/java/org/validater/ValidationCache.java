package org.validater;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches validations when an object of a given type is scanned.
 * */

class ValidationCache {

    private static final int INITIAL_SIZE = 32;
    private final Map<Class<?>, List<FieldValidation>> fieldValidations = new ConcurrentHashMap<>(INITIAL_SIZE);

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
