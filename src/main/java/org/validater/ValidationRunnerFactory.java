package org.validater;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import java.util.List;

public class ValidationRunnerFactory {

    private String[] packagesToScan;

    public ValidationRunner getValidationRunner() {

        ValidationRunner validationRunner = new ValidationRunner();
        ValidationCache cache = validationRunner.getCache();

        if(packagesToScan != null) {
            for (String p : packagesToScan) {
                Reflections reflections = new Reflections(p, new SubTypesScanner(false));
                for (Class<?> cls : reflections.getSubTypesOf(Object.class)) {
                    List<FieldValidation> validations = validationRunner.scanClass(cls);
                    cache.cacheValidations(cls, validations);
                }
            }
        }

        return validationRunner;
    }

    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    public void setPackagesToScan(String... packagesToScan) {
        this.packagesToScan = packagesToScan;
    }
}
