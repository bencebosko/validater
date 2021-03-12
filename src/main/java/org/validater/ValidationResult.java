package org.validater;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {

    private final Map<String, List<ValidationError>> errors;

    ValidationResult() {
        errors = new HashMap<>();
    }

    public ValidationResult(Map<String, List<ValidationError>> errorMap) {
        errors = errorMap;
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public Map<String, List<ValidationError>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;

        ValidationResult that = (ValidationResult) o;

        return errors.equals(that.errors);
    }

    @Override
    public int hashCode() {
        return errors.hashCode();
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "errors=" + errors +
                '}';
    }

    void addErrorList(String field, List<ValidationError> errorList) {
        List<ValidationError> stored = errors.get(field);
        if(stored == null)
            errors.put(field, errorList);
        else {
            for (ValidationError e : errorList) {
                if (!stored.contains(e))
                    stored.add(e);
            }
        }
    }
}
