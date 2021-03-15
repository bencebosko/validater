package org.validater.internal;

import org.springframework.lang.Nullable;
import org.validater.FieldValidator;
import org.validater.ValidationError;
import org.validater.annotations.Required;
import java.util.List;

public class RequiredValidator implements FieldValidator<Object, Required> {

    @Override
    public Class<Object> fieldType() {
        return Object.class;
    }

    @Override
    public void validate(@Nullable Object value, Required annotation, List<ValidationError> errors) {
        if(value == null)
            errors.add(new ValidationError(annotation.message()));
        else if(value instanceof String && value.equals("")) {
            errors.add(new ValidationError(annotation.message()));
        }
    }
}
