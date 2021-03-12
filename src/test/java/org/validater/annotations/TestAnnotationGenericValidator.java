package org.validater.annotations;

import org.validater.FieldValidator;
import org.validater.ValidationError;

import java.util.List;

public class TestAnnotationGenericValidator implements FieldValidator<List, TestAnnotationGeneric> {

    @Override
    public Class<List> fieldType() {
        return List.class;
    }

    @Override
    public void validate(List value, TestAnnotationGeneric annotation, List<ValidationError> errors) {
        for(String s : (List<String>)value) {
            s.toUpperCase();
        }
    }
}
