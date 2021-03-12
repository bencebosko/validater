package org.validater.annotations;

import org.validater.FieldValidator;
import org.validater.ValidationError;
import java.util.List;

public class TestAnnotationExValidator implements FieldValidator<String, TestAnnotationEx> {

    private TestAnnotationExValidator() {}

    @Override
    public Class<String> fieldType() {
        return String.class;
    }

    @Override
    public void validate(String value, TestAnnotationEx annotation, List<ValidationError> errors) {

    }
}
