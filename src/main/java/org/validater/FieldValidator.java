package org.validater;

import java.lang.annotation.Annotation;
import java.util.List;

public interface FieldValidator<T, A extends Annotation> {
    Class<T> fieldType();
    void validate(T value, A annotation, List<ValidationError> errors);
}
