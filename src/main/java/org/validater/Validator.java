package org.validater;

import java.util.List;
import java.util.Map;

/**
 * Validators mapped to specific objects must implement this interface.
 * */

public interface Validator<T> {
    Class<T> type();
    void validate(T value, Map<String, List<ValidationError>> errorsByField);
}
