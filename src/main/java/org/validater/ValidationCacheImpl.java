package org.validater;


import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Caches validations when an object of a given type is scanned.
 * */

class ValidationCacheImpl implements ValidationCache {

    private static final int INITIAL_SIZE = 32;

    private final ConcurrentMap<Class<?>, List<FieldValidation>> fieldValidations = new ConcurrentHashMap<>(INITIAL_SIZE);

    public boolean isCached(Class<?> type) {
        return fieldValidations.containsKey(type);
    }

    public void cacheValidations(Class<?> type, List<FieldValidation> validations) {
        fieldValidations.put(type, Collections.unmodifiableList(validations));
    }

    @Nullable
    public List<FieldValidation> getValidations(Class<?> clss) {
        return fieldValidations.get(clss);
    }

    public void clear() {
        fieldValidations.clear();
    }
}
