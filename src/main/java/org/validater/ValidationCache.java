package org.validater;

import java.util.List;

interface ValidationCache {

    boolean isCached(Class<?> type);

    void cacheValidations(Class<?> type, List<FieldValidation> validations);

    List<FieldValidation> getValidations(Class<?> clss);

    void clear();
}
