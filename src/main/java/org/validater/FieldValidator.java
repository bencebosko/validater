package org.validater;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Single field validations must implement this interface.
 * */

public interface FieldValidator<T, A extends Annotation> {
    Class<T> fieldType();
    void validate(T value, A annotation, List<ValidationError> errors);
}
