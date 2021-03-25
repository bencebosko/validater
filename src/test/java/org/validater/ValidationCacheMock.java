package org.validater;

import java.util.List;

public class ValidationCacheMock implements ValidationCache {

    private final ValidationCache cache = new ValidationCacheImpl();
    private int adds = 0;
    private int gets = 0;

    @Override
    public boolean isCached(Class<?> type) {
        return cache.isCached(type);
    }

    @Override
    public void cacheValidations(Class<?> type, List<FieldValidation> validations) {
        adds++;
        cache.cacheValidations(type, validations);
    }

    @Override
    public List<FieldValidation> getValidations(Class<?> clss) {
        gets++;
        return cache.getValidations(clss);
    }

    @Override
    public void clear() {
        cache.clear();
    }

    int getCacheAdds() {
        return adds;
    }

    int getCacheGets() {
        return gets;
    }
}
